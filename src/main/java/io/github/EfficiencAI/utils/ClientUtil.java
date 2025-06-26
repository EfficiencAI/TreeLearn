package io.github.EfficiencAI.utils;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.internal.chat.Message;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import reactor.core.publisher.Flux;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


public class ClientUtil {
    public interface ChatClient {
        Flux<String> chat(ChatMessage... messages);
    }

    private static StreamingChatModel openAiChatModel(ChatRequestDTO chatRequestDTO) {
        return OpenAiStreamingChatModel
                .builder()
                .apiKey(chatRequestDTO.getApikey())
                .baseUrl(chatRequestDTO.getBaseurl())
                .modelName(chatRequestDTO.getModelName())
                .build();
    }

    public static ChatClient client(ChatRequestDTO chatRequestDTO) {
        return AiServices.builder(ChatClient.class)
                .streamingChatModel(openAiChatModel(chatRequestDTO))
                .toolProvider(toolProvider(chatRequestDTO))
                .build();
    }

    private static CompletableFuture<Boolean> hostIsAccessAsync(String url) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("开始检查服务器: " + url);
            try {
                HttpClient client = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(5))  // 增加超时时间
                        .build();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(5))  // 增加超时时间
                        .header("User-Agent", "MCP-Health-Check")
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());

                System.out.println("服务器 " + url + " 响应状态码: " + response.statusCode());

                // 放宽状态码判断，SSE端点可能返回404但仍然可用
                boolean available = response.statusCode() >= 200 && response.statusCode() < 500;

                if (available) {
                    System.out.println("服务器可用: " + url);
                } else {
                    System.err.println("服务器不可用，状态码: " + response.statusCode() + ", URL: " + url);
                }

                return available;

            } catch (java.net.ConnectException e) {
                System.err.println("无法连接到服务器: " + url + " (连接被拒绝)");
                return false;
            } catch (java.net.SocketTimeoutException e) {
                System.err.println("连接服务器超时: " + url);
                return false;
            } catch (java.net.UnknownHostException e) {
                System.err.println("无法解析服务器地址: " + url);
                return false;
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                System.err.println("检查服务器可用性失败: " + url + ", 错误类型: " + e.getClass().getSimpleName() + ", 错误: " + errorMsg);
                e.printStackTrace(); // 打印完整堆栈跟踪以便调试
                return false;
            }
        });
    }

    private static ToolProvider toolProvider(ChatRequestDTO chatRequestDTO) {
        if (chatRequestDTO == null || chatRequestDTO.getMcpUrls() == null) {
            return null;
        }

        List<McpClient> mcpClients = new ArrayList<>();

        // 并发检查所有URL的可用性
        List<CompletableFuture<String>> healthChecks = new ArrayList<>();

        for (String url : chatRequestDTO.getMcpUrls()) {
            CompletableFuture<String> healthCheck = hostIsAccessAsync(url)
                    .thenApply(available -> available ? url : null);
            healthChecks.add(healthCheck);
        }

        try {
            // 等待所有健康检查完成，最多等待10秒
            CompletableFuture<Void> allChecks = CompletableFuture.allOf(
                    healthChecks.toArray(new CompletableFuture[0])
            );

            allChecks.get(10, TimeUnit.SECONDS);

            // 收集可用的URL
            List<String> availableUrls = new ArrayList<>();
            for (CompletableFuture<String> check : healthChecks) {
                try {
                    String url = check.get();
                    if (url != null) {
                        availableUrls.add(url);
                    }
                } catch (Exception e) {
                    System.err.println("获取健康检查结果失败: " + e.getMessage());
                }
            }

            System.out.println("健康检查完成，可用服务器数量: " + availableUrls.size());

            // 只连接可用的服务器
            for (String url : availableUrls) {
                try {
                    System.out.println("尝试连接MCP服务器: " + url);
                    transport(mcpClients, url);
                } catch (Exception e) {
                    String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                    System.err.println("连接MCP服务器失败: " + url + ", 错误: " + errorMsg);
                }
            }

        } catch (Exception e) {
            System.err.println("健康检查超时或失败: " + e.getMessage());
            // 如果健康检查失败，直接尝试连接所有URL（回退策略）
            return fallbackToolProvider(chatRequestDTO);
        }

        if (mcpClients.isEmpty()) {
            System.out.println("没有可用的MCP服务器，将不使用MCP工具");
            return null;
        }

        System.out.println("成功连接 " + mcpClients.size() + " 个MCP服务器");
        return McpToolProvider.builder()
                .mcpClients(mcpClients)
                .failIfOneServerFails(false)
                .build();
    }

    // 回退策略：直接尝试连接（原有逻辑）
    private static ToolProvider fallbackToolProvider(ChatRequestDTO chatRequestDTO) {
        List<McpClient> mcpClients = new ArrayList<>();

        for (String url : chatRequestDTO.getMcpUrls()) {
            try {
                System.out.println("回退策略：尝试连接MCP服务器: " + url);
                transport(mcpClients, url);
            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
                System.err.println("连接MCP服务器失败: " + url + ", 错误: " + errorMsg);
            }
        }

        if (mcpClients.isEmpty()) {
            return null;
        }

        return McpToolProvider.builder()
                .mcpClients(mcpClients)
                .failIfOneServerFails(false)
                .build();
    }

    private static void transport(List<McpClient> mcpClients, String url) {
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl(url + "/sse")
                .timeout(Duration.ofSeconds(300))
                .logRequests(true)
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .toolExecutionTimeout(Duration.ofSeconds(30))
                .build();

        mcpClient.listTools();
        mcpClients.add(mcpClient);
        System.out.println("MCP服务器连接成功: " + url);
    }
}
