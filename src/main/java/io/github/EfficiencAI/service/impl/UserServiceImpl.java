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

import java.util.HashSet;

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
    public Flux<String> addConversationNode(NodeRequestDTO nodeRequestDTO, ChatRequestDTO chatRequestDTO) {
        return aiService.chat(chatRequestDTO)
                .publish(sharedFlux -> {
                    // 保存操作
                    Mono<Void> saveOperation = sharedFlux
                            .collectList()
                            .map(list -> String.join("", list))
                            .flatMap(completeResponse -> {
                                NodeOperationResult<ConversationNode> result = conversationDAO.newConversationNode(
                                        nodeRequestDTO.getUserId(),
                                        nodeRequestDTO.getSessionName(),
                                        nodeRequestDTO.getParentId(),
                                        nodeRequestDTO.getContextStartIdx(),
                                        nodeRequestDTO.getContextEndIdx(),
                                        nodeRequestDTO.getUserMessage(),
                                        completeResponse
                                );
                                if (!result.ifSuccess) {
                                    return Mono.error(new RuntimeException("保存对话节点失败: " + result.note));
                                }
                                return Mono.empty();
                            })
                            .then();
                    
                    // 返回流式数据，同时确保保存操作执行
                    return sharedFlux.mergeWith(saveOperation.then(Mono.empty()));
                });
    }

    @Override
    public Flux<String> updateConversationNode(NodeRequestDTO nodeRequestDTO, ChatRequestDTO chatRequestDTO) {
        return aiService.chat(chatRequestDTO)
                .publish(sharedFlux -> {
                    // 保存操作
                    Mono<Void> saveOperation = sharedFlux
                            .collectList()
                            .map(list -> String.join("", list))
                            .flatMap(completeResponse -> {
                                NodeOperationResult<ConversationNode> result = conversationDAO.updateConversationNode(
                                        nodeRequestDTO.getUserId(),
                                        nodeRequestDTO.getSessionName(),
                                        nodeRequestDTO.getConversationNodeId(),
                                        nodeRequestDTO.getContextStartIdx(),
                                        nodeRequestDTO.getContextEndIdx(),
                                        nodeRequestDTO.getUserMessage(),
                                        completeResponse
                                );
                                if (!result.ifSuccess) {
                                    return Mono.error(new RuntimeException("更新对话节点失败: " + result.note));
                                }
                                return Mono.empty();
                            })
                            .then();
                    
                    // 返回流式数据，同时确保保存操作执行
                    return sharedFlux.mergeWith(saveOperation.then(Mono.empty()));
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
    @Override
    public Mono<NodeOperationResult<HashSet<String>>> getAllConversationNodesID(String userId, String sessionName) {
        return Mono.fromCallable(() -> conversationDAO.getAllConversationNodesID(userId, sessionName));
    }
}