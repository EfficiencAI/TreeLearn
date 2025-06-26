package io.github.EfficiencAI.service;

import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import io.github.EfficiencAI.pojo.DTO.NodeRequestDTO;
import io.github.EfficiencAI.pojo.DTO.UserDTO;
import io.github.EfficiencAI.pojo.Entites.node.ConversationNode;
import io.github.EfficiencAI.pojo.Entites.node.SessionNode;
import io.github.EfficiencAI.pojo.Entites.node.UserNode;
import io.github.EfficiencAI.pojo.VO.NodeOperationResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;

public interface UserService {
    // 用户管理
    Mono<NodeOperationResult<UserNode>> createUser(UserDTO userDTO);
    Mono<NodeOperationResult<UserNode>> getUser(String userId);
    Mono<NodeOperationResult<UserNode>> updateUser(UserDTO userDTO);
    Mono<NodeOperationResult<UserNode>> deleteUser(String userId);
    
    // 会话管理
    Mono<NodeOperationResult<SessionNode>> createSession(String userId, String sessionName);
    Mono<NodeOperationResult<SessionNode>> getSession(String userId, String sessionName);
    Mono<NodeOperationResult<HashSet<String>>> getAllSessionsName(String userId);
    Mono<NodeOperationResult<SessionNode>> updateSession(String userId, String sessionName, String newSessionName);
    Mono<NodeOperationResult<SessionNode>> deleteSession(String userId, String sessionName);
    
    // 对话节点管理
    Flux<String> addConversationNode(NodeRequestDTO nodeRequestDTO, ChatRequestDTO chatRequestDTO);
    Flux<String> updateConversationNode(NodeRequestDTO nodeRequestDTO, ChatRequestDTO chatRequestDTO);
    Mono<NodeOperationResult<ConversationNode>> deleteConversationNode(String userId, String sessionName, String conversationNodeId);
    Mono<NodeOperationResult<ConversationNode>> getConversationNode(String userId, String sessionName, String conversationNodeId);
    Mono<NodeOperationResult<HashSet<String>>> getAllConversationNodesID(String userId, String sessionName);
}
