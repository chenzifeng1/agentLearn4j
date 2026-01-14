# 详细调试步骤

## 已完成的配置

1. ✅ 启用了详细的日志级别
   - `org.springframework.ai=DEBUG`
   - `com.alibaba.cloud.ai=DEBUG`
   - `org.springframework.web.client=DEBUG`
   - `org.springframework.http.client=DEBUG`

2. ✅ 添加了 HTTP 请求/响应拦截器
   - 会记录完整的请求 URI、Method、Headers、Body
   - 会记录完整的响应 Status、Headers、Body

3. ✅ 创建了专用的 Debug Controller
   - 端点：`/api/v1/debug/simple-test`
   - 用于测试基本的 AI 模型调用

## 测试步骤

### 1. 重启应用

```bash
# 停止当前运行的应用（Ctrl+C）

# 重新启动
./mvnw spring-boot:run
```

### 2. 测试 Debug 接口

使用新的 debug 接口进行测试：

```bash
curl -X POST http://localhost:8080/api/v1/debug/simple-test \
  -H "Content-Type: application/json" \
  -d '{"input": "你好"}'
```

### 3. 查看控制台日志

应用重启后，你会在控制台看到类似这样的详细日志：

```
========== HTTP Request ==========
URI: https://openapi-ait.ke.com/v1/chat/completions
Method: POST
Headers: [Content-Type:"application/json", Authorization:"Bearer yIPi..."]
Request Body: {"messages":[{"role":"user","content":"你好"}],"model":"gpt-5-chat",...}

========== HTTP Response ==========
Status Code: 500 INTERNAL_SERVER_ERROR
Response Headers: [...]
Response Body: {"error":{"code":"500",...}}
===================================
```

### 4. 对比实际请求和预期请求

**预期的请求格式**（根据你的 curl 示例）：
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

**实际的请求格式**（Spring AI 发送的）：
查看日志中的 "Request Body" 部分，对比两者的差异。

## 可能的差异和解决方案

### 差异 1: 参数名称或格式不同

如果日志显示 Spring AI 发送的参数格式与你的代理接口要求不一致，需要：
- 自定义请求转换器
- 或者使用标准的 OpenAI 客户端

### 差异 2: 缺少某些必需参数

如果你的代理接口要求特定参数（如 `user: "null"`），需要在 `DashScopeChatOptions` 中添加。

### 差异 3: Authorization Header 格式问题

检查日志中的 Authorization header 是否正确：
- 应该是：`Authorization: Bearer yIPijvEE3lUpZ5jW45l4weYHDJpqpjBI`

## 获取完整日志

### 启动应用并保存日志到文件

```bash
./mvnw spring-boot:run > app.log 2>&1
```

### 或者在运行时查看实时日志

```bash
./mvnw spring-boot:run | tee app.log
```

### 测试后查看日志

```bash
# 查看最近的日志
tail -200 app.log

# 搜索 HTTP 请求
grep -A 20 "HTTP Request" app.log

# 搜索错误
grep -A 10 "ERROR" app.log
```

## 下一步行动

1. **重启应用**
2. **调用 debug 接口**：`POST /api/v1/debug/simple-test`
3. **复制完整的日志输出**，特别是：
   - `========== HTTP Request ==========` 部分
   - `========== HTTP Response ==========` 部分
   - 任何 ERROR 或 WARN 信息
4. **分享日志**，我会根据实际的请求/响应内容来调整代码

## 额外诊断命令

### 测试代理接口是否正常工作

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

如果这个直接调用也失败，说明问题在代理接口本身。

### 检查网络连接

```bash
# 测试是否能连接到代理服务器
curl -I https://openapi-ait.ke.com/v1/chat/completions

# 测试 DNS 解析
nslookup openapi-ait.ke.com
```

## 关键日志位置

查看这些日志来诊断问题：

1. **应用启动日志** - 查看 DashScopeApi 配置是否正确
2. **HTTP Request 日志** - 查看实际发送的请求
3. **HTTP Response 日志** - 查看服务器返回的错误详情
4. **异常堆栈** - 查看错误发生的位置

重启应用后，请分享完整的日志输出（特别是 HTTP Request 和 Response 部分）！
