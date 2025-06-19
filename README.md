# AIChat
一个基于 Spring Boot 和 Spring AI 的智能聊天应用，集成了 Ollama 本地大语言模型。
## 项目简介
AIChat 是一个现代化的 AI 聊天应用，使用 Spring Boot 3.4.6 框架构建，
集成了 Spring AI 来与本地部署的 Ollama 模型进行交互。项目支持流式响应，
提供了完整的 JWT 认证机制和 CORS 跨域配置。
## 技术栈
- **后端框架**: Spring Boot 3.4.6
- **AI 集成**: Spring AI 1.0.0
- **大语言模型**: Ollama (qwen3:14b)
- **数据库**: MySQL 8.2.0
- **认证**: JWT (JSON Web Token)
- **构建工具**: Maven
- **Java 版本**: 17
- **其他依赖**:
    - Lombok (简化代码)
    - Spring Boot Starter Web (Web 服务)
    - Spring Boot Starter Test (测试)
## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── site/arookieofc/aichat/
│   │       ├── AIChatApplication.java          # 主启动类
│   │       ├── config/
│   │       │   └── CorsConfig.java             # CORS 跨域配置
│   │       ├── configuration/
│   │       │   └── ai/
│   │       │       └── Client.java             # AI 客户端配置
│   │       ├── controller/
│   │       │   └── ClientController.java       # 聊天控制器
│   │       ├── pojo/
│   │       │   └── VO/
│   │       │       └── Result.java             # 统一响应结果
│   │       ├── service/
│   │       │   ├── AiService.java              # AI 服务接口
│   │       │   └── impl/
│   │       │       └── AiServiceImpl.java      # AI 服务实现
│   │       └── utils/
│   │           └── JWTUtil.java                # JWT 工具类
│   └── resources/
│       └── application.yml                     # 应用配置文件
└── test/
    └── java/
        └── site/arookieofc/aichat/
            └── AiChatApplicationTests.java     # 测试类
```

## 功能特性

- 🤖 **AI 聊天**: 集成 Ollama 本地大语言模型，支持智能对话
- 🔄 **流式响应**: 支持实时流式输出，提升用户体验
- 🔐 **JWT 认证**: 完整的 JWT 令牌认证机制
- 🌐 **CORS 支持**: 配置了跨域资源共享，支持前端调用
- 📊 **统一响应**: 标准化的 API 响应格式
- 🏗️ **模块化设计**: 清晰地分层架构，易于维护和扩展

## 环境要求

- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Ollama (需要本地安装并运行)

## 快速开始

### 1. 安装 Ollama

首先需要在本地安装并启动 Ollama：

```bash
# 安装 Ollama (根据你的操作系统)
# 访问 https://ollama.ai 下载安装

# 拉取 qwen3:14b 模型
ollama pull qwen3:14b

# 启动 Ollama 服务 (默认端口 11434)
ollama serve
```

### 2. 配置数据库

创建 MySQL 数据库并配置连接信息（如需要）。

### 3. 克隆并运行项目

```bash
# 克隆项目
git clone https://github.com/a-rookie-of-C-language/StructuredAiChat.git
cd AIChat

# 编译项目
mvnw clean compile

# 运行项目
mvnw spring-boot:run
```

### 4. 测试 API

项目启动后，可以通过以下方式测试聊天功能：

```bash
# 发送聊天请求
curl "http://localhost:8080/ai/chat?message=你好"
```

## API 接口

### 聊天接口

- **URL**: `/ai/chat`
- **方法**: GET
- **参数**:
    - `message` (String): 用户输入的消息
- **响应**: 流式文本响应

## 配置说明

### application.yml 配置

```yaml
spring:
  application:
    name: AIChat
  ai:
    ollama:
      chat:
        model: qwen3:14b          # 使用的模型
      base-url: http://localhost:11434  # Ollama 服务地址
    system:
      prompt: ""                # 系统提示词

jwt:
  secretKey: arookieofc         # JWT 密钥
  issue: 不会C的菜鸟            # JWT 签发者
  expiration: 3000              # JWT 过期时间(秒)
```

## 开发说明

- 项目使用 UTF-8 编码
- 换行符统一使用 LF
- 代码缩进规则参考 `.editorconfig` 文件
- 使用 Lombok 简化代码，需要 IDE 安装 Lombok 插件

## 贡献

欢迎提交 Issue 和 Pull Request 来改进项目。

---
**作者**: 不会C的菜鸟
        