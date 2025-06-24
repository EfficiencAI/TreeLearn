package io.github.EfficiencAI.service;

import io.github.EfficiencAI.pojo.DTO.NodeRequestDTO;
import io.github.EfficiencAI.pojo.Entites.node.ConversationNode;
import io.github.EfficiencAI.pojo.Entites.node.SessionNode;
import io.github.EfficiencAI.pojo.VO.NodeOperationResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    // 会话管理
    Mono<NodeOperationResult<SessionNode>> createSession(String userId, String sessionName);
    Mono<NodeOperationResult<SessionNode>> getSession(String userId, String sessionName);
    Mono<NodeOperationResult<SessionNode>> updateSession(String userId, String sessionName, String newSessionName);
    Mono<NodeOperationResult<SessionNode>> deleteSession(String userId, String sessionName);
    
    // 对话节点管理
    Flux<String> addConversationNode(String userId, String sessionName, NodeRequestDTO nodeRequestDTO);
    Flux<String> updateConversationNode(String userId, String sessionName, String conversationNodeId, NodeRequestDTO nodeRequestDTO);
    Mono<NodeOperationResult<ConversationNode>> deleteConversationNode(String userId, String sessionName, String conversationNodeId);
}
