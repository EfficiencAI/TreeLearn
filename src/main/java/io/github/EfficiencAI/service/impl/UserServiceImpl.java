package io.github.EfficiencAI.service.impl;

import io.github.EfficiencAI.DAO.ConversationDAO;
import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import io.github.EfficiencAI.pojo.DTO.NodeRequestDTO;
import io.github.EfficiencAI.pojo.Entites.node.ConversationNode;
import io.github.EfficiencAI.pojo.Entites.node.SessionNode;
import io.github.EfficiencAI.pojo.VO.NodeOperationResult;
import io.github.EfficiencAI.service.AiService;
import io.github.EfficiencAI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AiService aiService;
    
    @Autowired
    private ConversationDAO conversationDAO;

    @Override
    public Mono<NodeOperationResult<SessionNode>> createSession(String userId, String sessionName) {
        return Mono.fromCallable(() -> conversationDAO.newSession(userId, sessionName));
    }

    @Override
    public Mono<NodeOperationResult<SessionNode>> getSession(String userId, String sessionName) {
        return Mono.fromCallable(() -> conversationDAO.getSession(userId, sessionName));
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
    public Flux<String> addConversationNode(String userId, String sessionName, NodeRequestDTO nodeRequestDTO) {
        // 首先调用AI服务获取AI回复
        ChatRequestDTO chatRequest = ChatRequestDTO.builder()
                .message(nodeRequestDTO.getUserMessage())
                .build();
        
        return aiService.chat(chatRequest)
                .collectList()
                .flatMapMany(aiResponseList -> {
                    String aiResponse = String.join("", aiResponseList);
                    
                    // 保存对话节点
                    NodeOperationResult<ConversationNode> result = conversationDAO.addNewConversationNode(
                            userId, 
                            sessionName, 
                            nodeRequestDTO.getParentId(),
                            nodeRequestDTO.getContextStartIdx(),
                            nodeRequestDTO.getContextEndIdx(),
                            nodeRequestDTO.getUserMessage(),
                            aiResponse
                    );
                    
                    if (result.ifSuccess) {
                        return Flux.fromIterable(aiResponseList);
                    } else {
                        return Flux.error(new RuntimeException(result.note));
                    }
                });
    }

    @Override
    public Flux<String> updateConversationNode(String userId, String sessionName, String conversationNodeId, NodeRequestDTO nodeRequestDTO) {
        // 重新调用AI服务获取新回答
        ChatRequestDTO chatRequest = ChatRequestDTO.builder()
                .message(nodeRequestDTO.getUserMessage())
                .build();
        
        return aiService.chat(chatRequest)
                .collectList()
                .flatMapMany(aiResponseList -> {
                    String aiResponse = String.join("", aiResponseList);
                    
                    // 更新对话节点，包含新的AI回答
                    NodeOperationResult<ConversationNode> result = conversationDAO.updateConversationNode(
                            userId,
                            sessionName,
                            conversationNodeId,
                            nodeRequestDTO.getContextStartIdx(),
                            nodeRequestDTO.getContextEndIdx(),
                            nodeRequestDTO.getUserMessage(),
                            aiResponse
                    );
                    
                    if (result.ifSuccess) {
                        return Flux.fromIterable(aiResponseList);
                    } else {
                        return Flux.error(new RuntimeException(result.note));
                    }
                });
    }

    @Override
    public Mono<NodeOperationResult<ConversationNode>> deleteConversationNode(String userId, String sessionName, String conversationNodeId) {
        return Mono.fromCallable(() -> conversationDAO.deleteConversationNode(userId, sessionName, conversationNodeId));
    }
}
