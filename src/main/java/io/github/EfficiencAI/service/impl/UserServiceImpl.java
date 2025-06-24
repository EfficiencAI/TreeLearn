package io.github.EfficiencAI.service.impl;

import io.github.EfficiencAI.DAO.ConversationDAO;
import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import io.github.EfficiencAI.pojo.DTO.NodeRequestDTO;
import io.github.EfficiencAI.pojo.Entites.node.ConversationNode;
import io.github.EfficiencAI.pojo.Entites.node.SessionNode;
import io.github.EfficiencAI.pojo.Entites.node.UserNode;
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

    // 用户管理实现
    @Override
    public Mono<NodeOperationResult<UserNode>> createUser(String userId, String userName) {
        return Mono.fromCallable(() -> conversationDAO.newUser(userId, userName));
    }

    @Override
    public Mono<NodeOperationResult<UserNode>> getUser(String userId) {
        return Mono.fromCallable(() -> conversationDAO.getUser(userId));
    }

    @Override
    public Mono<NodeOperationResult<UserNode>> updateUser(String userId, String newUserName) {
        return Mono.fromCallable(() -> conversationDAO.modifyUser(userId, newUserName));
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
    public Mono<NodeOperationResult<SessionNode>> updateSession(String userId, String sessionName, String newSessionName) {
        return Mono.fromCallable(() -> conversationDAO.modifySession(userId, sessionName, newSessionName));
    }

    @Override
    public Mono<NodeOperationResult<SessionNode>> deleteSession(String userId, String sessionName) {
        return Mono.fromCallable(() -> conversationDAO.deleteSession(userId, sessionName));
    }

    @Override
    public Flux<String> addConversationNode(NodeRequestDTO nodeRequestDTO,ChatRequestDTO chatRequestDTO) {
        
        return aiService.chat(chatRequestDTO)
                .collectList()
                .flatMapMany(aiResponseList -> {
                    String aiResponse = String.join("", aiResponseList);
                    // 保存对话节点
                    NodeOperationResult<ConversationNode> result = conversationDAO.addNewConversationNode(
                            nodeRequestDTO.getUserId(), 
                            nodeRequestDTO.getSessionName(), 
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
    public Flux<String> updateConversationNode(NodeRequestDTO nodeRequestDTO,ChatRequestDTO chatRequestDTO) {
        return aiService.chat(chatRequestDTO)
                .collectList()
                .flatMapMany(aiResponseList -> {
                    String aiResponse = String.join("", aiResponseList);
                    // 更新对话节点，包含新的AI回答
                    NodeOperationResult<ConversationNode> result = conversationDAO.updateConversationNode(
                            nodeRequestDTO.getUserId(),
                            nodeRequestDTO.getSessionName(),
                            nodeRequestDTO.getConversationNodeId(),
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
    
    @Override
    public Mono<NodeOperationResult<ConversationNode>> getConversationNode(String userId, String sessionName, String conversationNodeId) {
        return Mono.fromCallable(() -> conversationDAO.getConversationNode(userId, sessionName, conversationNodeId));
    }
}
