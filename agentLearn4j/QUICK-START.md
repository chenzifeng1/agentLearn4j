# 快速启动指南

## 🚀 启动步骤

### 1. 启动应用
```bash
./mvnw spring-boot:run
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

## 📝 配置说明

### Bean 覆盖配置
在 `application.properties` 中添加了：
```properties
spring.main.allow-bean-definition-overriding=true
```

**原因**：Spring AI Alibaba 和 Spring AI OpenAI 都定义了相同的 Bean，需要允许覆盖。

**影响**：后加载的 Bean 会覆盖先加载的。由于 `OpenAiConfig` 中的 Bean 标记了 `@Primary`，所以会优先使用 OpenAI 客户端。

## 🔍 验证清单

### 应用启动检查
- [ ] 应用成功启动（无 Bean 冲突错误）
- [ ] 日志中看到 "初始化 OpenAI ChatModel（使用代理接口）"
- [ ] 无其他错误信息

### 功能测试
- [ ] Debug 接口返回成功响应
- [ ] 日志显示正确的请求格式（OpenAI 标准）
- [ ] 响应状态码为 200 OK
- [ ] 流式接口正常推送事件

## 📊 预期的日志输出

### 启动日志
```
初始化 OpenAI ChatModel（使用代理接口）
DashScope configuration initialized
```

### 请求日志（Debug 接口）
```
========== HTTP Request ==========
URI: https://openapi-ait.ke.com/v1/chat/completions
Method: POST
Headers: [Authorization:"Bearer yIPi...", Content-Type:"application/json"]
Request Body: {
  "messages": [
    {
      "role": "user",
      "content": "你好"
    }
  ],
  "model": "gpt-5-chat",
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
===================================
```

### 流式接口日志
```
执行意图识别节点
识别的意图: QUERY, 置信度: 0.85
执行意图路由节点
意图 QUERY 路由到: knowledge_query
执行查询处理节点
查询处理完成
```

## ⚠️ 常见问题

### 1. Bean 冲突错误
如果还是报 Bean 冲突，检查：
- `application.properties` 中是否添加了 `spring.main.allow-bean-definition-overriding=true`
- 是否重启了应用

### 2. 500 错误
如果还是返回 500，检查日志中的：
- 实际请求格式是否正确（应该是 OpenAI 标准格式）
- 代理接口是否正常工作（用 curl 直接测试）
- API Key 是否正确

### 3. 找不到 ChatModel Bean
如果报找不到 ChatModel，检查：
- `OpenAiConfig.java` 是否有 `@Configuration` 和 `@Primary` 注解
- 包扫描路径是否正确

## 🎯 成功标志

当你看到以下内容时，说明配置成功：

1. ✅ 应用启动无错误
2. ✅ 日志中请求格式为 OpenAI 标准（messages 在顶层）
3. ✅ 响应状态码为 200 OK
4. ✅ 能够正常返回 AI 模型的响应

## 📞 调试技巧

### 查看完整日志
```bash
./mvnw spring-boot:run 2>&1 | tee app.log
```

### 搜索关键信息
```bash
# 查看 Bean 初始化
grep "初始化" app.log

# 查看 HTTP 请求
grep "HTTP Request" app.log -A 10

# 查看错误
grep "ERROR" app.log -A 5
```

### 直接测试代理接口
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

如果这个直接调用失败，说明问题在代理接口本身。

---

**现在就启动应用测试吧！** 🚀
