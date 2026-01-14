# 最终测试步骤

## ✅ 问题已解决

添加了 `@Primary` 注解到 `CustomOpenAiCompatibleChatModel`，确保它作为主要的 ChatModel Bean。

```java
@Primary  // ← 标记为主要 Bean
@Component
public class CustomOpenAiCompatibleChatModel implements ChatModel {
    // ...
}
```

## 🚀 启动应用

```bash
./mvnw spring-boot:run
```

### 预期启动日志

```
初始化 CustomOpenAiCompatibleChatModel
DashScope configuration initialized
IntentBasedWorkflowAgent 初始化成功
```

应该**不再有 Bean 冲突错误**。

## 🧪 测试步骤

### 1. 测试简单接口

```bash
curl -X POST http://localhost:8080/api/v1/debug/simple-test \
  -H "Content-Type: application/json" \
  -d '{"input": "你好"}'
```

**预期输出**：
```json
{
  "success": true,
  "input": "你好",
  "output": "你好！有什么我可以帮助你的吗？",
  "metadata": "..."
}
```

**预期日志**：
```
========== HTTP Request ==========
URI: https://openapi-ait.ke.com/v1/chat/completions
Method: POST
Headers: [Authorization:"Bearer yIPi...", Content-Type:"application/json"]
Request Body: {
  "model": "gpt-5-chat",
  "messages": [
    {
      "role": "user",
      "content": "你好"
    }
  ],
  "stream": false,
  "temperature": 0.7,
  "max_tokens": 2000
}

========== HTTP Response ==========
Status Code: 200 OK
Response Body: {
  "choices": [
    {
      "message": {
        "role": "assistant",
        "content": "你好！有什么我可以帮助你的吗？"
      }
    }
  ]
}
```

### 2. 测试流式接口

```bash
curl -X POST http://localhost:8080/api/v1/intent-workflow/stream \
  -H "Content-Type: application/json" \
  -d '{"input": "Spring Boot 的优势有哪些？"}' \
  -N
```

**预期输出**（SSE 流）：
```
data: {"event":"start","message":"工作流开始执行"}

data: {"event":"intent_recognition","message":"正在识别意图..."}

data: {"event":"intent_recognized","intent":"QUERY"}

data: {"event":"routing","message":"正在路由请求..."}

data: {"event":"routed","route":"knowledge_query"}

data: {"event":"processing","message":"正在处理请求..."}

data: {"event":"result","output":"Spring Boot 的主要优势包括：\n1. 快速开发...\n2. 自动配置...\n3. ..."}

data: {"event":"complete","message":"工作流执行完成"}
```

### 3. 测试健康检查

```bash
curl http://localhost:8080/api/v1/intent-workflow/health
```

**预期输出**：
```json
{
  "success": true,
  "data": "Intent Workflow Agent is healthy",
  "message": "success"
}
```

## ✅ 成功标志

当你看到以下内容时，说明一切正常：

1. ✅ **应用启动成功** - 无 Bean 冲突错误
2. ✅ **请求格式正确** - 日志中显示 OpenAI 标准格式
3. ✅ **响应状态 200** - 代理接口返回成功
4. ✅ **流式接口工作** - SSE 事件正常推送
5. ✅ **意图识别准确** - 能正确识别 QUERY、CHAT 等意图

## 📊 架构总览

```
用户请求
   ↓
IntentWorkflowController
   ↓
IntentBasedWorkflowAgent
   ↓
IntentRecognitionNode (使用 CustomOpenAiCompatibleChatModel)
   ↓
IntentRoutingNode
   ↓
QueryHandlerNode / ChatHandlerNode / DefaultHandlerNode
   ↓
CustomOpenAiCompatibleChatModel
   ↓
RestClient (发送 OpenAI 格式请求)
   ↓
代理接口 (https://openapi-ait.ke.com/v1/chat/completions)
   ↓
响应返回
```

## 🔍 关键组件

### CustomOpenAiCompatibleChatModel
- **职责**: 实现 ChatModel 接口，发送 OpenAI 格式请求
- **标记**: `@Primary` - 作为主要的 ChatModel Bean
- **功能**:
  - 请求格式转换
  - HTTP 请求发送
  - 响应格式转换
  - 详细日志记录

### IntentBasedWorkflowAgent
- **职责**: 编排工作流执行
- **流程**: 意图识别 → 路由 → 处理 → 返回结果
- **支持**: 同步执行和流式执行

### IntentRecognitionNode
- **职责**: 识别用户输入的意图
- **支持的意图**: QUERY、CHAT、TASK、TOOL、PLANNING、UNKNOWN
- **返回**: 意图类型和置信度

## 🎯 核心特性

1. **纯 Spring AI Alibaba** - 不引入其他依赖
2. **OpenAI 兼容** - 完全符合 OpenAI API 标准
3. **流式响应** - 支持 SSE 实时推送
4. **意图识别** - 自动识别用户意图并路由
5. **详细日志** - 完整的请求/响应日志
6. **易于扩展** - 可添加更多意图类型和处理器

## ⚠️ 故障排查

### 如果还是报 Bean 冲突
1. 确认 `CustomOpenAiCompatibleChatModel` 有 `@Primary` 注解
2. 清理并重新编译：`./mvnw clean compile`
3. 重启应用

### 如果返回 500 错误
1. 检查日志中的实际请求格式
2. 使用 curl 直接测试代理接口
3. 确认 API Key 是否正确

### 如果流式接口中断
1. 检查客户端是否支持 SSE
2. 确认网络连接稳定
3. 查看服务器日志中的错误信息

## 📞 测试脚本

创建一个测试脚本 `test-all.sh`：

```bash
#!/bin/bash

echo "=== 测试简单接口 ==="
curl -X POST http://localhost:8080/api/v1/debug/simple-test \
  -H "Content-Type: application/json" \
  -d '{"input": "你好"}'

echo -e "\n\n=== 测试流式接口 ==="
curl -X POST http://localhost:8080/api/v1/intent-workflow/stream \
  -H "Content-Type: application/json" \
  -d '{"input": "Spring Boot 的优势有哪些？"}' \
  -N

echo -e "\n\n=== 测试健康检查 ==="
curl http://localhost:8080/api/v1/intent-workflow/health
```

运行：
```bash
chmod +x test-all.sh
./test-all.sh
```

---

**现在启动应用并开始测试！** 🚀

如果一切正常，你应该看到正确的 OpenAI 格式请求和成功的响应！
