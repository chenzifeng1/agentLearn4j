# 解决 500 错误问题

## 问题描述

调用 `/api/v1/intent-workflow/stream` 接口时，出现以下错误：
```
org.springframework.ai.retry.TransientAiException: 500 - {"error":{"code":"500","httpCode":500,"message":null,"type":"Internal Exception","param":null}}
```

## 可能的原因

### 1. 模型名称配置错误 ✅ 已修复

**问题**: 配置文件中使用的是 `qwen-plus`，但代理接口需要 `gpt-5-chat`

**解决方案**:
- 已更新 `application.properties` 中的模型名称为 `gpt-5-chat`
- 已更新 `CustomChatModel.java` 中的模型名称为 `gpt-5-chat`

### 2. 请求格式不匹配

你的代理接口期望的请求格式：
```json
{
    "messages": [{
        "content": "你好",
        "role": "user"
    }],
    "model": "gpt-5-chat",
    "stream": false,
    "user": "null"
}
```

Spring AI DashScope 客户端发送的格式可能略有不同。

## 诊断步骤

### 1. 测试基本连接

创建一个简单的测试接口来验证 API 连接：

```java
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final DashScopeChatModel normalChatModel;

    @PostMapping("/simple")
    public String testSimple(@RequestBody Map<String, String> request) {
        try {
            String input = request.get("input");
            ChatResponse response = normalChatModel.call(new Prompt(input));
            return response.getResult().getOutput().getText();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
```

测试调用：
```bash
curl -X POST http://localhost:8080/api/v1/test/simple \
  -H "Content-Type: application/json" \
  -d '{"input": "你好"}'
```

### 2. 启用详细日志

在 `application.properties` 中添加：
```properties
# 启用 Spring AI 详细日志
logging.level.org.springframework.ai=DEBUG
logging.level.com.alibaba.cloud.ai=DEBUG
logging.level.org.springframework.web.client=DEBUG

# 启用 HTTP 请求日志
logging.level.org.springframework.web.client.RestTemplate=DEBUG
```

### 3. 检查请求参数

可能需要调整 `DashScopeChatOptions` 的参数：

```java
@Bean
public DashScopeChatModel normalChatModel() {
    return DashScopeChatModel.builder()
            .dashScopeApi(getDashScopeApi())
            .defaultOptions(DashScopeChatOptions.builder()
                    .withModel("gpt-5-chat")
                    .withTemperature(0.7)
                    .withMaxToken(2000)
                    .withTopP(0.9)
                    // 尝试添加或移除某些参数
                    .build())
            .build();
}
```

### 4. 验证代理接口直接调用

使用 curl 直接测试代理接口：

```bash
curl --location 'https://openapi-ait.ke.com/v1/chat/completions' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer yIPijvEE3lUpZ5jW45l4weYHDJpqpjBI' \
--data '{
    "messages": [{
        "content": "你好",
        "role": "user"
    }],
    "model": "gpt-5-chat",
    "stream": false
}'
```

如果直接调用失败，说明问题在于代理接口本身。

## 可能的解决方案

### 方案 1: 自定义 RestClient（推荐）

如果代理接口的格式与标准 OpenAI/DashScope 格式不完全兼容，可以自定义 RestClient：

```java
@Bean
public DashScopeChatModel normalChatModel() {
    // 创建自定义 RestClient.Builder
    RestClient.Builder restClientBuilder = RestClient.builder()
            .requestInterceptor((request, body, execution) -> {
                // 添加自定义 headers
                request.getHeaders().add("Authorization", "Bearer yIPijvEE3lUpZ5jW45l4weYHDJpqpjBI");
                // 如果需要，可以添加 Cookie
                // request.getHeaders().add("Cookie", "...");
                return execution.execute(request, body);
            });

    DashScopeApi dashScopeApi = DashScopeApi.builder()
            .baseUrl("https://openapi-ait.ke.com")
            .completionsPath("/v1/chat/completions")
            .apiKey("yIPijvEE3lUpZ5jW45l4weYHDJpqpjBI")
            .restClientBuilder(restClientBuilder)
            .build();

    return DashScopeChatModel.builder()
            .dashScopeApi(dashScopeApi)
            .defaultOptions(DashScopeChatOptions.builder()
                    .withModel("gpt-5-chat")
                    .withTemperature(0.7)
                    .withMaxToken(2000)
                    .build())
            .build();
}
```

### 方案 2: 使用 OpenAI 兼容客户端

如果你的代理接口完全兼容 OpenAI API 格式，可以考虑使用 Spring AI 的 OpenAI 客户端：

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
</dependency>
```

配置：
```properties
spring.ai.openai.api-key=yIPijvEE3lUpZ5jW45l4weYHDJpqpjBI
spring.ai.openai.base-url=https://openapi-ait.ke.com/v1
spring.ai.openai.chat.options.model=gpt-5-chat
```

### 方案 3: 检查 user 参数

你的 curl 示例中有 `"user": "null"`，这可能导致问题。尝试移除或设置为合法值：

```java
DashScopeChatOptions.builder()
        .withModel("gpt-5-chat")
        .withTemperature(0.7)
        .withMaxToken(2000)
        .withUser("system")  // 或者不设置 user 参数
        .build()
```

## 下一步

1. **重启应用**，使新的配置生效
2. **先测试简单接口** (`/api/v1/test/simple`)，确保基本连接正常
3. **查看详细日志**，了解具体的请求和响应内容
4. **对比请求格式**，看 Spring AI 发送的请求与你的 curl 示例有什么差异
5. **根据日志调整配置**，可能需要自定义 RestClient 或调整参数

## 常见错误码含义

- **500 Internal Server Error**: 服务器内部错误
  - 检查模型名称是否正确
  - 检查 API Key 是否有效
  - 检查请求格式是否符合要求
  - 查看服务器日志（如果有访问权限）

- **401 Unauthorized**: 认证失败
  - 检查 API Key 是否正确
  - 检查 Authorization header 是否正确设置

- **400 Bad Request**: 请求参数错误
  - 检查请求体格式是否正确
  - 检查必需参数是否都提供了

## 联系支持

如果问题持续存在，请联系代理接口提供方，提供以下信息：
1. 完整的请求 payload
2. 返回的错误信息
3. API Key（脱敏后的）
4. 使用的模型名称
