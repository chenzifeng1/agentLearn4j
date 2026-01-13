# 意图工作流使用指南

## 概述

意图工作流 (Intent-Based Workflow) 是一个基于 Spring AI Alibaba 的智能工作流系统，能够自动识别用户输入的意图，并根据不同的意图路由到相应的处理器。

## 核心特性

### 1. 意图识别
系统使用 AI 模型自动识别用户输入的意图类型：
- **QUERY** - 查询问题，需要知识检索
- **CHAT** - 闲聊对话，日常交流
- **TASK** - 任务执行，需要执行具体操作
- **TOOL** - 工具调用，需要调用特定工具
- **PLANNING** - 规划类任务，需要制定计划
- **UNKNOWN** - 无法识别的意图

### 2. 智能路由
根据识别的意图，系统自动路由到对应的处理器：
- `knowledge_query` - 知识查询处理器
- `conversational` - 对话处理器
- `task_execution` - 任务执行处理器
- `tool_calling` - 工具调用处理器
- `planning` - 规划处理器
- `default` - 默认处理器

### 3. 流式响应
支持 Server-Sent Events (SSE)，实时推送工作流执行进度和结果。

## API 接口

### 1. 同步执行接口

**端点**: `POST /api/v1/intent-workflow/execute`

**请求示例**:
```bash
curl -X POST http://localhost:8080/api/v1/intent-workflow/execute \
  -H "Content-Type: application/json" \
  -d '{
    "input": "什么是 Spring AI？"
  }'
```

**响应示例**:
```json
{
  "success": true,
  "data": "Spring AI 是一个为 Java 开发者提供的 AI 应用框架...",
  "message": "success"
}
```

### 2. 流式执行接口

**端点**: `POST /api/v1/intent-workflow/stream`

**请求示例**:
```bash
curl -X POST http://localhost:8080/api/v1/intent-workflow/stream \
  -H "Content-Type: application/json" \
  -d '{
    "input": "Spring Boot 的优势有哪些？"
  }' \
  -N
```

**响应示例** (SSE 流):
```
data: {"event":"start","message":"工作流开始执行"}

data: {"event":"intent_recognition","message":"正在识别意图..."}

data: {"event":"intent_recognized","intent":"QUERY"}

data: {"event":"routing","message":"正在路由请求..."}

data: {"event":"routed","route":"knowledge_query"}

data: {"event":"processing","message":"正在处理请求..."}

data: {"event":"result","output":"Spring Boot 的主要优势包括..."}

data: {"event":"complete","message":"工作流执行完成"}
```

### 3. 健康检查接口

**端点**: `GET /api/v1/intent-workflow/health`

**请求示例**:
```bash
curl http://localhost:8080/api/v1/intent-workflow/health
```

## 工作流架构

```
用户输入
   ↓
意图识别节点 (IntentRecognitionNode)
   ↓
意图路由节点 (IntentRoutingNode)
   ↓
   ├─→ 知识查询处理器 (QueryHandlerNode)
   ├─→ 对话处理器 (ChatHandlerNode)
   ├─→ 任务执行处理器 (TaskHandlerNode)
   ├─→ 工具调用处理器 (ToolHandlerNode)
   ├─→ 规划处理器 (PlanningHandlerNode)
   └─→ 默认处理器 (DefaultHandlerNode)
   ↓
返回结果
```

## 配置说明

在 `application.properties` 中配置 DashScope:

```properties
# DashScope Configuration
spring.ai.dashscope.api-key=${MODEL_API_KEY:your-api-key}
spring.ai.dashscope.agent.base-url=https://dashscope.aliyuncs.com/api/v1
spring.ai.dashscope.chat.options.model=qwen-plus
spring.ai.dashscope.chat.options.temperature=0.7
spring.ai.dashscope.chat.options.max-tokens=2000
```

## 测试示例

### 1. 查询意图测试
```bash
curl -X POST http://localhost:8080/api/v1/intent-workflow/execute \
  -H "Content-Type: application/json" \
  -d '{
    "input": "什么是 Spring AI Alibaba？"
  }'
```

### 2. 对话意图测试
```bash
curl -X POST http://localhost:8080/api/v1/intent-workflow/execute \
  -H "Content-Type: application/json" \
  -d '{
    "input": "你好，今天天气怎么样？"
  }'
```

### 3. 流式执行测试
```bash
curl -X POST http://localhost:8080/api/v1/intent-workflow/stream \
  -H "Content-Type: application/json" \
  -d '{
    "input": "请介绍一下 Java 17 的新特性"
  }' \
  -N
```

## 前端集成示例

### JavaScript (使用 EventSource)

```javascript
// 创建请求
fetch('http://localhost:8080/api/v1/intent-workflow/stream', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    input: '什么是 Spring AI？'
  })
})
.then(response => response.body)
.then(body => {
  const reader = body.getReader();
  const decoder = new TextDecoder();

  function read() {
    reader.read().then(({ done, value }) => {
      if (done) {
        console.log('Stream complete');
        return;
      }

      const text = decoder.decode(value);
      const lines = text.split('\n\n');

      lines.forEach(line => {
        if (line.startsWith('data: ')) {
          const data = JSON.parse(line.substring(6));
          console.log('Event:', data.event, 'Data:', data);

          // 根据不同的事件类型处理
          switch(data.event) {
            case 'start':
              console.log('工作流开始');
              break;
            case 'intent_recognized':
              console.log('识别意图:', data.intent);
              break;
            case 'result':
              console.log('最终结果:', data.output);
              break;
            case 'complete':
              console.log('工作流完成');
              break;
          }
        }
      });

      read();
    });
  }

  read();
});
```

## 扩展开发

### 添加新的意图类型

1. 在 `IntentRecognitionNode` 中更新意图类型列表
2. 在 `IntentRoutingNode` 中添加新的路由规则
3. 创建对应的处理器节点
4. 在 `IntentBasedWorkflowAgent` 中注册新节点

### 添加自定义处理器

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomHandlerNode implements Node<Map<String, Object>> {

    private final DashScopeChatModel chatModel;

    @Override
    public Map<String, Object> apply(Map<String, Object> state) {
        log.info("执行自定义处理节点");

        String input = (String) state.get("input");

        // 实现自定义处理逻辑
        String result = processCustomLogic(input);

        state.put("output", result);
        state.put("processingNode", "custom_handler");

        return state;
    }

    private String processCustomLogic(String input) {
        // 自定义处理逻辑
        return "处理结果";
    }
}
```

## 注意事项

1. 确保配置正确的 API Key
2. 网络环境需要能够访问 DashScope API
3. 流式接口需要客户端支持 SSE
4. 意图识别的准确性依赖于 AI 模型的质量

## 故障排查

### 常见问题

1. **工作流初始化失败**
   - 检查 DashScope API Key 是否正确
   - 检查网络连接是否正常
   - 查看日志中的详细错误信息

2. **意图识别不准确**
   - 调整提示词模板
   - 更换更强大的模型
   - 增加更多的意图类型示例

3. **流式响应中断**
   - 检查客户端是否支持 SSE
   - 检查网络稳定性
   - 增加超时时间配置

## 性能优化建议

1. 使用缓存减少重复的意图识别
2. 异步处理长时间运行的任务
3. 实现请求限流和熔断机制
4. 监控 API 调用次数和响应时间
