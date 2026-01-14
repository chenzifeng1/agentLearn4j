# 使用 Spring AI Alibaba 方式解决 OpenAI 兼容问题

## 🎯 解决方案概述

**不引入 Spring AI OpenAI 依赖**，而是创建一个自定义的 `ChatModel` 实现，使用 `RestClient` 直接发送 OpenAI 标准格式的请求。

## 📝 实现方式

### 1. 移除了 OpenAI 依赖

`pom.xml` 中只保留：
```xml
<dependency>
    <groupId>com.alibaba.cloud.ai</groupId>
    <artifactId>spring-ai-alibaba-starter-dashscope</artifactId>
    <version>1.1.0.0-RC2</version>
</dependency>
```

### 2. 创建自定义 ChatModel

**文件**: `CustomOpenAiCompatibleChatModel.java`

**核心功能**:
- 实现 Spring AI 的 `ChatModel` 接口
- 使用 `RestClient` 直接调用代理接口
- 发送 **OpenAI 标准格式** 的请求
- 包含详细的请求/响应日志

**请求格式**（OpenAI 标准）:
```json
{
  "model": "gpt-5-chat",
  "messages": [
    {
      "role": "user",
      "content": "用户输入"
    }
  ],
  "stream": false,
  "temperature": 0.7,
  "max_tokens": 2000
}
```

### 3. 自动注入

因为 `CustomOpenAiCompatibleChatModel` 实现了 `ChatModel` 接口并标记为 `@Component`，Spring 会自动将其注入到所有需要 `ChatModel` 的地方：

- `IntentRecognitionNode`
- `QueryHandlerNode`
- `ChatHandlerNode`
- `DefaultHandlerNode`
- `DebugController`

## 🔧 核心代码

### ChatModel 实现

```java
@Component
public class CustomOpenAiCompatibleChatModel implements ChatModel {

    private static final String API_URL = "https://openapi-ait.ke.com/v1/chat/completions";
    private static final String API_KEY = "yIPijvEE3lUpZ5jW45l4weYHDJpqpjBI";
    private static final String MODEL = "gpt-5-chat";

    @Override
    public ChatResponse call(Prompt prompt) {
        // 1. 构建 OpenAI 格式的请求
        OpenAiRequest request = buildRequest(prompt);

        // 2. 使用 RestClient 发送请求
        OpenAiResponse response = restClient.post()
                .uri(API_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(OpenAiResponse.class);

        // 3. 转换为 Spring AI 的 ChatResponse
        return convertToChatResponse(response);
    }
}
```

### 请求格式转换

```java
private OpenAiRequest buildRequest(Prompt prompt) {
    OpenAiRequest request = new OpenAiRequest();
    request.setModel(MODEL);
    request.setStream(false);
    request.setTemperature(0.7);
    request.setMaxTokens(2000);

    // 转换消息格式
    List<OpenAiMessage> messages = prompt.getInstructions().stream()
            .map(this::convertToOpenAiMessage)
            .collect(Collectors.toList());
    request.setMessages(messages);

    return request;
}
```

## ✅ 优势

1. **纯 Spring AI Alibaba 方案** - 不引入其他依赖
2. **完全控制请求格式** - 直接构建 OpenAI 标准格式
3. **详细日志** - 记录完整的请求和响应
4. **无 Bean 冲突** - 只有一个 ChatModel 实现
5. **易于扩展** - 可以轻松添加更多参数或功能

## 🚀 测试步骤

### 1. 启动应用
```bash
./mvnw spring-boot:run
```

应该看到日志：
```
CustomOpenAiCompatibleChatModel 已初始化
```

### 2. 测试简单接口
```bash
curl -X POST http://localhost:8080/api/v1/debug/simple-test \
  -H "Content-Type: application/json" \
  -d '{"input": "你好"}'
```

### 3. 测试流式接口
```bash
curl -X POST http://localhost:8080/api/v1/intent-workflow/stream \
  -H "Content-Type: application/json" \
  -d '{"input": "Spring Boot 的优势有哪些？"}' \
  -N
```

## 📊 预期日志

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
Response Headers: [...]
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

## 🎯 与之前方案的对比

| 特性 | 引入 OpenAI Starter | 自定义 ChatModel ✅ |
|------|-------------------|-------------------|
| 依赖数量 | 2 个 Spring AI | 1 个 Spring AI Alibaba |
| Bean 冲突 | 需要覆盖 | 无冲突 |
| 请求格式 | OpenAI 标准 | OpenAI 标准 |
| 可控性 | 依赖库实现 | 完全自定义 |
| 日志详细度 | 需要配置 | 内置详细日志 |
| 扩展性 | 受限于库 | 完全可控 |

## 📝 关键文件

### 新增文件
- `src/main/java/.../config/CustomOpenAiCompatibleChatModel.java` - 自定义 ChatModel

### 修改文件
- `pom.xml` - 移除 OpenAI 依赖
- `CustomChatModel.java` - 注释掉 DashScope Bean

### 删除文件
- `OpenAiConfig.java` - 不再需要

## 🔍 工作原理

1. **Spring 容器启动**
   - 检测到 `CustomOpenAiCompatibleChatModel` 实现了 `ChatModel`
   - 将其注册为 Bean

2. **依赖注入**
   - 所有需要 `ChatModel` 的组件
   - 自动注入 `CustomOpenAiCompatibleChatModel`

3. **请求处理**
   - 接收 Spring AI 的 `Prompt`
   - 转换为 OpenAI 格式的 JSON
   - 使用 `RestClient` 发送 HTTP 请求
   - 解析响应并转换回 Spring AI 格式

## 💡 扩展建议

### 1. 添加配置化
```java
@ConfigurationProperties(prefix = "custom.openai")
@Data
public class OpenAiProperties {
    private String apiUrl;
    private String apiKey;
    private String model;
    private Double temperature = 0.7;
    private Integer maxTokens = 2000;
}
```

### 2. 添加重试机制
```java
@Retryable(
    value = {RestClientException.class},
    maxAttempts = 3,
    backoff = @Backoff(delay = 1000)
)
public ChatResponse call(Prompt prompt) {
    // ...
}
```

### 3. 添加超时配置
```java
this.restClient = RestClient.builder()
    .requestFactory(clientHttpRequestFactory())
    .build();

private ClientHttpRequestFactory clientHttpRequestFactory() {
    HttpComponentsClientHttpRequestFactory factory =
        new HttpComponentsClientHttpRequestFactory();
    factory.setConnectTimeout(5000);
    factory.setReadTimeout(30000);
    return factory;
}
```

## ✅ 总结

这个方案：
- ✅ **纯 Spring AI Alibaba** - 不引入额外依赖
- ✅ **OpenAI 兼容** - 完全符合 OpenAI API 标准
- ✅ **简单清晰** - 代码易于理解和维护
- ✅ **灵活可控** - 可以随时调整请求格式
- ✅ **详细日志** - 便于调试和问题排查

现在就启动应用测试吧！🚀
