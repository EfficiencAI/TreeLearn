package io.github.EfficiencAI.service.impl;

import io.github.EfficiencAI.DAO.ConversationDAO;
import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import io.github.EfficiencAI.pojo.DTO.NodeRequestDTO;
import io.github.EfficiencAI.pojo.DTO.UserDTO;
import io.github.EfficiencAI.pojo.Entites.node.ConversationNode;
import io.github.EfficiencAI.pojo.Entites.node.SessionNode;
import io.github.EfficiencAI.pojo.Entites.node.UserNode;
import io.github.EfficiencAI.pojo.VO.ConversationNodeRegisterResult;
import io.github.EfficiencAI.pojo.VO.NodeOperationResult;
import io.github.EfficiencAI.service.AiService;
import io.github.EfficiencAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AiService aiService;

    @Autowired
    private ConversationDAO conversationDAO;

    // 用户管理实现
    @Override
    public Mono<NodeOperationResult<UserNode>> createUser(UserDTO userDTO) {
        return Mono.fromCallable(() -> conversationDAO.newUser(userDTO));
    }

    @Override
    public Mono<NodeOperationResult<UserNode>> getUser(String userId) {
        return Mono.fromCallable(() -> conversationDAO.getUser(userId));
    }

    @Override
    public Mono<NodeOperationResult<UserNode>> updateUser(UserDTO userDTO) {
        return Mono.fromCallable(() -> conversationDAO.modifyUser(userDTO));
    }

    @Override
    public Mono<NodeOperationResult<UserNode>> deleteUser(String userId) {
        return Mono.fromCallable(() -> conversationDAO.deleteUser(userId));
    }

    @Override
    public Mono<NodeOperationResult<SessionNode>> createSession(String userId, String sessionName) {
        return Mono.fromCallable(() -> conversationDAO.newSession(userId, sessionName));
    }

    @Override
    public Mono<NodeOperationResult<SessionNode>> getSession(String userId, String sessionName) {
        return Mono.fromCallable(() -> conversationDAO.getSession(userId, sessionName));
    }

    @Override
    public Mono<NodeOperationResult<HashSet<String>>> getAllSessionsName(String userId){
        return Mono.fromCallable(() -> conversationDAO.getAllSessionsName(userId));
    }

    @Override
    public Mono<NodeOperationResult<SessionNode>> updateSession(String userId, String sessionName, String newSessionName) {
        return Mono.fromCallable(() -> conversationDAO.modifySession(userId, sessionName, newSessionName));
    }

    @Override
    public Mono<NodeOperationResult<SessionNode>> deleteSession(String userId, String sessionName) {
        return Mono.fromCallable(() -> conversationDAO.deleteSession(userId, sessionName));
    }
    @Override
    public Mono<NodeOperationResult<ConversationNodeRegisterResult>> registerForNewConversationNode(String userId, String sessionName, String parentId) {
        return Mono.fromCallable(() -> conversationDAO.registerForNewConversationNode(userId, sessionName, parentId));
    }

    @Override
    public Flux<String> addConversationNode(NodeRequestDTO nodeRequestDTO, ChatRequestDTO chatRequestDTO) {

        AtomicReference<StringBuilder> responseBuilder = new AtomicReference<>(new StringBuilder());
        
        return aiService.chat(buildContext(nodeRequestDTO, chatRequestDTO))
                .doOnNext(token -> {
                    // 直接追加token，保持原始格式包括换行符
                    responseBuilder.get().append(token);
                })
                .doOnComplete(() -> {
                    // 流完成后异步保存，保持完整的原始响应格式
                    String completeResponse = responseBuilder.get().toString();
                    Mono.fromRunnable(() -> {
                        NodeOperationResult<ConversationNode> result = conversationDAO.newConversationNode(
                                nodeRequestDTO.getUserId(),
                                nodeRequestDTO.getSessionName(),
                                nodeRequestDTO.getRegistrationCertificate(),
                                nodeRequestDTO.getParentId(),
                                nodeRequestDTO.getContextStartIdx(),
                                nodeRequestDTO.getContextEndIdx(),
                                nodeRequestDTO.getUserMessage(),
                                completeResponse // 保持原始格式，包括\n换行符
                        );
                        if (!result.ifSuccess) {
                            System.err.println("保存对话节点失败: " + result.note);
                        }
                    })
                    .subscribeOn(Schedulers.boundedElastic())
                    .doOnError(error -> System.err.println("保存对话节点时出错: " + error.getMessage()))
                    .onErrorResume(error -> Mono.empty())
                    .subscribe();
                })
                .doOnError(error -> System.err.println("处理对话时出错: " + error.getMessage()));
    }

    @Override
    public Flux<String> updateConversationNode(NodeRequestDTO nodeRequestDTO, ChatRequestDTO chatRequestDTO) {
        // 使用AtomicReference来收集完整响应，保持原始格式
        AtomicReference<StringBuilder> responseBuilder = new AtomicReference<>(new StringBuilder());
        
        return aiService.chat(buildContext(nodeRequestDTO, chatRequestDTO))
                .doOnNext(token -> {
                    // 直接追加token，保持原始格式包括换行符
                    responseBuilder.get().append(token);
                })
                .doOnComplete(() -> {
                    // 流完成后异步保存，保持完整的原始响应格式
                    String completeResponse = responseBuilder.get().toString();
                    Mono.fromRunnable(() -> {
                        NodeOperationResult<ConversationNode> result = conversationDAO.updateConversationNode(
                                nodeRequestDTO.getUserId(),
                                nodeRequestDTO.getSessionName(),
                                nodeRequestDTO.getConversationNodeId(),
                                nodeRequestDTO.getContextStartIdx(),
                                nodeRequestDTO.getContextEndIdx(),
                                nodeRequestDTO.getUserMessage(),
                                completeResponse // 保持原始格式，包括\n换行符
                        );
                        if (!result.ifSuccess) {
                            System.err.println("更新对话节点失败: " + result.note);
                        }
                    })
                    .subscribeOn(Schedulers.boundedElastic())
                    .doOnError(error -> System.err.println("保存对话节点时出错: " + error.getMessage()))
                    .onErrorResume(error -> Mono.empty())
                    .subscribe();
                })
                .doOnError(error -> System.err.println("处理对话时出错: " + error.getMessage()));
    }

    private ChatRequestDTO buildContext(NodeRequestDTO nodeRequestDTO,ChatRequestDTO chatRequestDTO) {
        // 如果parentId为-1，表示是根节点，无需上下文
        if ("-1".equals(nodeRequestDTO.getParentId())) {
            return ChatRequestDTO.builder()
                    .systemPrompt(chatRequestDTO.getSystemPrompt())
                    .apikey(chatRequestDTO.getApikey())
                    .modelName(chatRequestDTO.getModelName())
                    .baseurl(chatRequestDTO.getBaseurl())
                    .message(chatRequestDTO.getMessage())
                    .mcpUrls(chatRequestDTO.getMcpUrls())
                    .build();
        }
        try {
            // 获取父节点信息
            NodeOperationResult<ConversationNode> parentResult = conversationDAO.getConversationNode(
                    nodeRequestDTO.getUserId(),
                    nodeRequestDTO.getSessionName(),
                    nodeRequestDTO.getParentId()
            );
            
            if (!parentResult.ifSuccess || parentResult.returnValue == null) {
                System.err.println("获取父节点失败: " + parentResult.note);
                return null;
            }
            
            ConversationNode parentNode = parentResult.returnValue;
            String parentUserMessage = parentNode.getUserMessage();
            String parentAIMessage = parentNode.getAIMessage();

            String contextAIMessage = extractContextFromAIMessage(
                    parentAIMessage,
                    nodeRequestDTO.getContextStartIdx(),
                    nodeRequestDTO.getContextEndIdx()
            );

            return ChatRequestDTO.builder()
                    .systemPrompt(chatRequestDTO.getSystemPrompt())
                    .apikey(chatRequestDTO.getApikey())
                    .modelName(chatRequestDTO.getModelName())
                    .baseurl(chatRequestDTO.getBaseurl())
                    .message(chatRequestDTO.getMessage())
                    .mcpUrls(chatRequestDTO.getMcpUrls())
                    .contextAIMessage(contextAIMessage)
                    .contextUserMessage(parentUserMessage)
                    .build();


            
        } catch (Exception e) {
            System.err.println("构建上下文时出错: " + e.getMessage());
            return null;
        }
    }

    // 新增方法：从AI消息中提取上下文
    private String extractContextFromAIMessage(String aiMessage, String startIdx, String endIdx) {
        if (aiMessage == null || aiMessage.isEmpty()) {
            return "";
        }

        try {
            int start = 0;
            int end = aiMessage.length();

            // 解析起始索引
            if (startIdx != null && !startIdx.isEmpty()) {
                int startIndex = Integer.parseInt(startIdx);
                if (startIndex >= 0 && startIndex < aiMessage.length()) {
                    start = startIndex;
                }
            }

            // 解析结束索引
            if (endIdx != null && !endIdx.isEmpty()) {
                int endIndex = Integer.parseInt(endIdx);
                if (endIndex > start && endIndex <= aiMessage.length()) {
                    end = endIndex;
                }
            }

            return aiMessage.substring(start, end);

        } catch (NumberFormatException e) {
            System.err.println("解析上下文索引失败: " + e.getMessage());
            return aiMessage; // 如果解析失败，返回完整消息
        } catch (StringIndexOutOfBoundsException e) {
            System.err.println("上下文索引超出范围: " + e.getMessage());
            return aiMessage; // 如果索引超出范围，返回完整消息
        }
    }


    @Override
    public Mono<NodeOperationResult<ConversationNode>> deleteConversationNode(String userId, String sessionName, String conversationNodeId) {
        return Mono.fromCallable(() -> conversationDAO.deleteConversationNode(userId, sessionName, conversationNodeId));
    }

    @Override
    public Mono<NodeOperationResult<ConversationNode>> getConversationNode(String userId, String sessionName, String conversationNodeId) {
        return Mono.fromCallable(() -> conversationDAO.getConversationNode(userId, sessionName, conversationNodeId));
    }
    @Override
    public Mono<NodeOperationResult<HashSet<String>>> getAllConversationNodesID(String userId, String sessionName) {
        return Mono.fromCallable(() -> conversationDAO.getAllConversationNodesID(userId, sessionName));
    }
}