# UserController API 文档

## 基础信息

- **Base URL**: `/user`
- **Content-Type**: `application/json`
- **响应格式**: JSON

## 会话管理接口

### 1. 创建会话

**接口地址**: `POST /user/session/create`

**请求参数**:
- `userId` (String, 必填): 用户ID
- `sessionName` (String, 必填): 会话名称

**响应格式**:
```json
{
  "code": 200,
  "msg": "success",
  "obj": {
    
  }
}
```

**示例请求**:
```bash
POST /user/session/create?userId=user123&sessionName=我的对话
```

### 2. 获取会话

**接口地址**: `GET /user/session/get`

**请求参数**:
- `userId` (String, 必填): 用户ID
- `sessionName` (String, 必填): 会话名称

**响应格式**:
```json
{
  "code": 200,
  "msg": "success",
  "obj": {
    
  }
}
```

### 3. 更新会话

**接口地址**: `PUT /user/session/update`

**请求参数**:
- `userId` (String, 必填): 用户ID
- `sessionName` (String, 必填): 原会话名称
- `newSessionName` (String, 必填): 新会话名称

**响应格式**:
```json
{
  "code": 200,
  "msg": "success",
  "obj": {
    
  }
}
```

### 4. 删除会话

**接口地址**: `DELETE /user/session/delete`

**请求参数**:
- `userId` (String, 必填): 用户ID
- `sessionName` (String, 必填): 会话名称

**响应格式**:
```json
{
  "code": 200,
  "msg": "success",
  "obj": "会话删除成功"
}
```

## 对话节点管理接口

### 1. 添加对话节点

**接口地址**: `POST /user/conversation/add`

**请求参数**:
- `userId` (String, 必填): 用户ID
- `sessionName` (String, 必填): 会话名称
- **请求体** (JSON):

```json
{
  "parentId": "string",
  "userMessage": "string",
  "contextStartIdx": "string",
  "contextEndIdx": "string"
}
```

**响应格式**: 流式响应 (`text/plain; charset=utf-8`)
- 返回AI回答的流式数据，每个数据块为字符串

**示例请求**:
```bash
POST /user/conversation/add?userId=user123&sessionName=我的对话
Content-Type: application/json

{
  "parentId": "node_001",
  "userMessage": "你好，请介绍一下人工智能",
  "contextStartIdx": "0",
  "contextEndIdx": "10"
}
```

### 2. 更新对话节点

**接口地址**: `PUT /user/conversation/update/{conversationNodeId}`

**路径参数**:
- `conversationNodeId` (String, 必填): 对话节点ID

**请求参数**:
- `userId` (String, 必填): 用户ID
- `sessionName` (String, 必填): 会话名称
- **请求体** (JSON):

```json
{
  "parentId": "string",
  "userMessage": "string",
  "contextStartIdx": "string",
  "contextEndIdx": "string"
}
```

**响应格式**: 流式响应 (`text/plain; charset=utf-8`)
- 返回AI重新生成回答的流式数据，每个数据块为字符串

**示例请求**:
```bash
PUT /user/conversation/update/node_123?userId=user123&sessionName=我的对话
Content-Type: application/json

{
  "parentId": "node_001",
  "userMessage": "请详细介绍一下机器学习",
  "contextStartIdx": "0",
  "contextEndIdx": "15"
}
```

### 3. 删除对话节点

**接口地址**: `DELETE /user/conversation/delete/{conversationNodeId}`

**路径参数**:
- `conversationNodeId` (String, 必填): 对话节点ID

**请求参数**:
- `userId` (String, 必填): 用户ID
- `sessionName` (String, 必填): 会话名称

**响应格式**:
```json
{
  "code": 200,
  "msg": "success",
  "obj": "对话节点删除成功"
}
```

## 数据模型

### NodeRequestVO

```json
{
  "parentId": "string",       
  "userMessage": "string",     
  "contextStartIdx": "string", 
  "contextEndIdx": "string"    
}
```

### Result

```json
{
  "code": 200,           
  "msg": "success",    
  "obj": {}            
}
```

## 错误处理

### 错误响应格式

```json
{
  "code": 500,
  "msg": "错误信息",
  "obj": null
}
```

### 常见错误码

- `200`: 成功
- `500`: 服务器内部错误
- 其他错误码根据具体业务逻辑返回

## 注意事项

1. **流式响应**: `addConversationNode` 和 `updateConversationNode` 接口返回流式数据，前端需要使用 Server-Sent Events 或类似技术接收
2. **参数验证**: 所有必填参数都需要提供，否则可能返回错误
3. **会话管理**: 在操作对话节点前，需要确保对应的会话已存在
4. **编码格式**: 所有请求和响应都使用 UTF-8 编码
        