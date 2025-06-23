package io.github.EfficiencAI.DAO;

import io.github.EfficiencAI.pojo.Entites.node.ConversationNode;
import io.github.EfficiencAI.pojo.Entites.node.SessionNode;
import io.github.EfficiencAI.pojo.Entites.node.UserNode;
import io.github.EfficiencAI.pojo.VO.NodeOperationResult;
import io.github.EfficiencAI.utils.IDElementComposition;

public class ConversationDAO {
    private final static String USER_NODE_STORAGE_PATH = "./Saves/Users/";
    private final static String DEFAULT_SESSION_NODE_STORAGE_PATH = "./Saves/Sessions/";

    /**
     * 添加新的会话
     * @param userID 用户ID（唯一标识符）
     * @param sessionName 会话名称（唯一标识符）
     * @return 会话操作结果
     */
    public NodeOperationResult<SessionNode> newSession(String userID, String sessionName){
        // 1. 根据前端传入的用户ID查询已有会话的会话名称并生成可用且唯一的会话名称
        if(userID == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "用户ID传输出错"
            );
        }
        UserNode userNode = UserNode.loadFromFile(USER_NODE_STORAGE_PATH + userID + ".json", UserNode.class);
        if(userNode == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "用户信息获取失败"
            );
        }
        if(sessionName == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "会话名称传输出错"
            );
        }
        String newSessionName = sessionName;
        if(newSessionName.isEmpty()){
            // 未填写会话名称，自动生成会话名称
            newSessionName = "未命名会话";
            for(int i = 1;i < 257;i++){
                if(!userNode.getSessionNames().contains(newSessionName + i)){
                    newSessionName = newSessionName + i;
                    break;
                } else if (i == 256) {
                    return new NodeOperationResult<>(
                            NodeOperationResult.OperationType.CREATE,
                            null,
                            false,
                            "未命名会话过多，无法创建新的会话"
                    );
                }
            }
        }else{
            // 填写了会话名称，检查会话名称是否重复
            if(userNode.getSessionNames().contains(newSessionName)){
                return new NodeOperationResult<>(
                        NodeOperationResult.OperationType.CREATE,
                        null,
                        false,
                        "会话名称重复"
                );
            }
        }

        //2. 组装会话节点
        SessionNode newSessionNode = new SessionNode(
                newSessionName,
                DEFAULT_SESSION_NODE_STORAGE_PATH + userID + "/" + newSessionName + "/"
        );

        //3. 持久化会话节点
        if(!newSessionNode.saveSelfToFile(userNode.getSessionsStorageFolderPath())){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "会话保存失败"
            );
        }

        //4. 返回会话创建结果
        return new NodeOperationResult<>(
                NodeOperationResult.OperationType.CREATE,
                newSessionNode,
                true,
                "会话创建成功"
        );
    }

    /**
     * 获取会话信息
     * @param userID 用户ID（唯一标识符）
     * @param sessionName 会话名称（唯一标识符）
     * @return 会话操作结果
     */
    public NodeOperationResult<SessionNode> getSession(String userID, String sessionName) {
        if(userID == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.GET,
                    null,
                    false,
                    "用户ID传输出错"
            );
        }
        UserNode userNode = UserNode.loadFromFile(USER_NODE_STORAGE_PATH + userID + ".json", UserNode.class);
        if(userNode == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.GET,
                    null,
                    false,
                    "用户信息获取失败"
            );
        }
        if(sessionName == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.GET,
                    null,
                    false,
                    "会话名称传输出错"
            );
        }
        if(!userNode.getSessionNames().contains(sessionName)){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.GET,
                    null,
                    false,
                    "会话不存在"
            );
        }
        SessionNode sessionNode = SessionNode.loadFromFile(
                userNode.getSessionsStorageFolderPath() + sessionName + ".json",
                SessionNode.class
        );
        if(sessionNode == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.GET,
                    null,
                    false,
                    "会话信息获取失败"
            );
        }
        return new NodeOperationResult<>(
                NodeOperationResult.OperationType.GET,
                sessionNode,
                true,
                "会话信息获取成功"
        );
    }

    /**
     * 修改会话信息
     * @param userID 用户ID（唯一标识符）
     * @param sessionName 会话名称（唯一标识符）
     * @param newSessionName 新的会话名称（唯一标识符）
     * @return 会话操作结果
     */
    public NodeOperationResult<SessionNode> modifySession(String userID, String sessionName, String newSessionName) {
        if(userID == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "用户ID传输出错"
            );
        }
        UserNode userNode = UserNode.loadFromFile(USER_NODE_STORAGE_PATH + userID + ".json", UserNode.class);
        if(userNode == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "用户信息获取失败"
            );
        }
        if(sessionName == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "会话名称传输出错"
            );
        }
        if(!userNode.getSessionNames().contains(sessionName)){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "会话不存在"
            );
        }
        if(newSessionName == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "新会话名称传输出错"
            );
        }
        if(newSessionName.isEmpty()){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "新会话名称不能为空"
            );
        }
        if(userNode.getSessionNames().contains(newSessionName)){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "新会话名称重复"
            );
        }
        SessionNode sessionNode = SessionNode.loadFromFile(
                userNode.getSessionsStorageFolderPath() + sessionName + ".json",
                SessionNode.class
        );
        if(sessionNode == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "会话信息获取失败"
            );
        }
        sessionNode.setSessionName(newSessionName);
        if(!sessionNode.saveSelfToFile(userNode.getSessionsStorageFolderPath() + newSessionName + ".json")){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "新会话信息保存失败"
            );
        }
        if(!deleteSession(userID, sessionName).ifSuccess){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    sessionNode,
                    true,
                    "会话信息更新成功，但旧会话信息删除失败"
            );
        }
        return new NodeOperationResult<>(
                NodeOperationResult.OperationType.MODIFY,
                sessionNode,
                true,
                "会话信息更新成功"
        );
    }

    /*
        向会话中添加新的对话节点：
            需要传入的参数：
                1. 用户ID
                2. 会话ID
                3. 父节点ID（-1代表会话节点）
                4. 上下文起始索引
                5. 上下文结束索引
                6. 用户消息
                7. 模型响应
            操作：
                1. 根据前端传入的用户ID与会话ID查询会话
                2. 根据父节点ID查询父节点下所有子节点，并根据上下文起始索引和上下文结束索引进行排序，生成可用且唯一的对话ID
                3. 组装对话节点
                4. 持久化对话节点
                5. 向前端返回对话信息
     */
    public NodeOperationResult<ConversationNode> addNewConversationNode(String userID, String sessionName, String parentId, String contextStartIdx, String contextEndIdx, String userMessage, String AIMessage) {
        if (userID == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "用户ID传输出错"
            );
        }
        UserNode userNode = UserNode.loadFromFile(USER_NODE_STORAGE_PATH + userID + ".json", UserNode.class);
        if (userNode == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "用户信息获取失败"
            );
        }
        if (sessionName == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "会话名称传输出错"
            );
        }
        if (!userNode.getSessionNames().contains(sessionName)) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "会话不存在"
            );
        }
        SessionNode sessionNode = SessionNode.loadFromFile(
                userNode.getSessionsStorageFolderPath() + sessionName + ".json",
                SessionNode.class
        );
        if (sessionNode == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "会话信息获取失败"
            );
        }

        //查询并生成可用且唯一的对话ID
        if(parentId == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "父节点ID传输出错"
            );
        }
        String newConversationNodeID = parentId;
        if(parentId.equals("-1")){
            boolean hasFoundAvailableID = false;
            for(IDElementComposition idElementComposition : IDElementComposition.values()){
                if(!sessionNode.getLinkedConversationNodesID().contains(idElementComposition.toString())){
                    hasFoundAvailableID = true;
                    newConversationNodeID = idElementComposition.toString();
                    break;
                }
            }
            if(!hasFoundAvailableID){
                return new NodeOperationResult<>(
                        NodeOperationResult.OperationType.CREATE,
                        null,
                        false,
                        "子节点数超过上限，无法创建新的对话节点"
                );
            }
        }else{
            if(!sessionNode.getAllConversationNodes().containsKey(parentId)){
                return new NodeOperationResult<>(
                        NodeOperationResult.OperationType.CREATE,
                        null,
                        false,
                        "父节点不存在"
                );
            }
            ConversationNode parentConversationNode = sessionNode.getAllConversationNodes().get(parentId);
            if(parentConversationNode == null){
                return new NodeOperationResult<>(
                        NodeOperationResult.OperationType.CREATE,
                        null,
                        false,
                        "父节点信息获取失败"
                );
            }
            boolean hasFoundAvailableID = false;
            for(IDElementComposition idElementComposition : IDElementComposition.values()){
                if(!sessionNode.getLinkedConversationNodesID().contains(parentId + idElementComposition)){
                    hasFoundAvailableID = true;
                    newConversationNodeID = parentId + idElementComposition;
                    break;
                }
            }
            if(!hasFoundAvailableID){
                return new NodeOperationResult<>(
                        NodeOperationResult.OperationType.CREATE,
                        null,
                        false,
                        "子节点数超过上限，无法创建新的对话节点"
                );
            }
        }

        //检查是否存在信息缺失
        if (contextStartIdx == null || contextEndIdx == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "上下文信息传输失败"
            );
        }
        if (userMessage == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "用户对话信息传输失败"
            );
        }
        if (AIMessage == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "AI对话信息传输失败"
            );
        }
        //组装对话节点
        ConversationNode newConversationNode = new ConversationNode(
                newConversationNodeID,
                contextStartIdx,
                contextEndIdx,
                userMessage,
                AIMessage
        );
        //持久化对话节点
        if(!newConversationNode.saveSelfToFile(sessionNode.getNodesStorageFolderPath() + newConversationNodeID + ".json")){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.CREATE,
                    null,
                    false,
                    "对话节点保存失败"
            );
        }
        //返回对话信息
        return new NodeOperationResult<>(
                NodeOperationResult.OperationType.CREATE,
                newConversationNode,
                true,
                "对话节点创建成功"
        );
    }

    /**
     * 更新对话节点信息
     * @param userID 用户ID（唯一标识符）
     * @param sessionName 会话名称（唯一标识符）
     * @param conversationNodeID 对话节点ID（唯一标识符）
     * @param contextStartIdx 上下文起始索引
     * @param contextEndIdx 上下文结束索引
     * @param userMessage 用户消息
     * @param AIMessage AI消息
     * @return 对话操作结果
     */
    public NodeOperationResult<ConversationNode> updateConversationNode(String userID, String sessionName, String conversationNodeID, String contextStartIdx, String contextEndIdx, String userMessage, String AIMessage) {
        if (userID == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "用户ID传输出错"
            );
        }
        UserNode userNode = UserNode.loadFromFile(USER_NODE_STORAGE_PATH + userID + ".json", UserNode.class);
        if (userNode == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "用户信息获取失败"
            );
        }
        if (sessionName == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "会话名称传输出错"
            );
        }
        if (!userNode.getSessionNames().contains(sessionName)) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "会话不存在"
            );
        }
        SessionNode sessionNode = SessionNode.loadFromFile(
                userNode.getSessionsStorageFolderPath() + sessionName + ".json",
                SessionNode.class
        );
        if (sessionNode == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "会话信息获取失败"
            );
        }
        if (conversationNodeID == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "对话ID传输出错"
            );
        }
        if (!sessionNode.getAllConversationNodes().containsKey(conversationNodeID)) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "对话节点不存在"
            );
        }
        ConversationNode conversationNode = sessionNode.getAllConversationNodes().get(conversationNodeID);
        if (conversationNode == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "对话节点信息获取失败"
            );
        }
        //检查是否存在信息缺失
        if (contextStartIdx == null || contextEndIdx == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "上下文信息传输失败"
            );
        }
        if (userMessage == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "用户对话信息传输失败"
            );
        }
        if (AIMessage == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "AI对话信息传输失败"
            );
        }
        //更新对话节点信息
        conversationNode.setContextStartIdx(contextStartIdx);
        conversationNode.setContextEndIdx(contextEndIdx);
        conversationNode.setUserMessage(userMessage);
        conversationNode.setAIMessage(AIMessage);
        //持久化更新对话节点信息
        if(!conversationNode.saveSelfToFile(sessionNode.getNodesStorageFolderPath() + conversationNodeID + ".json")){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.MODIFY,
                    null,
                    false,
                    "对话节点保存失败"
            );
        }
        //返回对话信息
        return new NodeOperationResult<>(
                NodeOperationResult.OperationType.MODIFY,
                conversationNode,
                true,
                "对话节点更新成功"
        );
    }

    /*
        通过对话ID删除对话节点：
            需要传入的参数：
                1. 用户ID
                2. 会话ID
                3. 对话ID
            操作：
                1. 根据前端传入的用户ID与会话ID查询会话
                2. 根据对话ID查询对话节点
                3. 持久化删除对话节点下所有子节点
                4. 持久化删除对话节点
                5. 向前端返回对话信息
     */
    public NodeOperationResult<ConversationNode> deleteConversationNode(String userID, String sessionName, String conversationNodeID) {
        if (userID == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "用户ID传输出错"
            );
        }
        UserNode userNode = UserNode.loadFromFile(USER_NODE_STORAGE_PATH + userID + ".json", UserNode.class);
        if (userNode == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "用户信息获取失败"
            );
        }
        if (sessionName == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "会话名称传输出错"
            );
        }
        if (!userNode.getSessionNames().contains(sessionName)) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "会话不存在"
            );
        }
        SessionNode sessionNode = SessionNode.loadFromFile(
                userNode.getSessionsStorageFolderPath() + sessionName + ".json",
                SessionNode.class
        );
        if (sessionNode == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "会话信息获取失败"
            );
        }
        ConversationNode conversationNode = sessionNode.getAllConversationNodes().get(conversationNodeID);
        if (conversationNode == null) {
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "对话节点不存在"
            );
        }
        //持久化删除对话节点下所有子节点
        boolean ifAllDeleteOperationExecuteSucceed = true;
        for (String childConversationNodeID : conversationNode.getLinkedConversationNodesID()) {
            ConversationNode childConversationNode = sessionNode.getAllConversationNodes().get(childConversationNodeID);
            if (childConversationNode == null) {
                ifAllDeleteOperationExecuteSucceed = false;
                continue;
            }
            if(!childConversationNode.getLinkedConversationNodesID().isEmpty()){
                if(!deleteConversationNode(userID, sessionName, childConversationNodeID).ifSuccess){
                    ifAllDeleteOperationExecuteSucceed = false;
                }
            }
            if(!childConversationNode.deleteSelfFromFile(sessionNode.getNodesStorageFolderPath())){
                ifAllDeleteOperationExecuteSucceed = false;
            }
            sessionNode.getAllConversationNodes().remove(childConversationNodeID);
        }
        //持久化删除对话节点
        if(!conversationNode.deleteSelfFromFile(sessionNode.getNodesStorageFolderPath())){
            ifAllDeleteOperationExecuteSucceed = false;
        }
        sessionNode.getAllConversationNodes().remove(conversationNodeID);
        //返回操作结果
        return new NodeOperationResult<>(
                NodeOperationResult.OperationType.DELETE,
                null,
                ifAllDeleteOperationExecuteSucceed,
                ifAllDeleteOperationExecuteSucceed ? "对话节点删除成功" : "对话节点删除成功，但有子节点删除失败"
        );
    }

    /**
     * 删除会话
     * @param userID 用户ID（唯一标识符）
     * @param sessionName 会话名称（唯一标识符）
     * @return 会话操作结果
     */
    public NodeOperationResult<SessionNode> deleteSession(String userID, String sessionName) {
        if(userID == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "用户ID传输出错"
            );
        }
        UserNode userNode = UserNode.loadFromFile(USER_NODE_STORAGE_PATH + userID + ".json", UserNode.class);
        if(userNode == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "用户信息获取失败"
            );
        }
        if(sessionName == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "会话名称传输出错"
            );
        }
        if(!userNode.getSessionNames().contains(sessionName)){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "会话不存在"
            );
        }
        SessionNode sessionNode = SessionNode.loadFromFile(
                userNode.getSessionsStorageFolderPath() + sessionName + ".json",
                SessionNode.class
        );
        if(sessionNode == null){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "会话信息获取失败"
            );
        }
        if(!sessionNode.deleteSelfFromFile(userNode.getSessionsStorageFolderPath())){
            return new NodeOperationResult<>(
                    NodeOperationResult.OperationType.DELETE,
                    null,
                    false,
                    "会话信息删除失败"
            );
        }
        return new NodeOperationResult<>(
                NodeOperationResult.OperationType.DELETE,
                null,
                true,
                "会话删除成功"
        );
    }

}
