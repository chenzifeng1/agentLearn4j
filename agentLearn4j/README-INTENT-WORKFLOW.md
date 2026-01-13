# Intent-Based Workflow 实现总结

## 项目概述

本项目基于 Spring AI Alibaba Agent Framework 实现了一个智能意图工作流系统，能够自动识别用户输入的意图，并根据不同的意图路由到相应的处理器。系统支持流式响应（SSE），可以实时推送工作流执行进度和结果。

## 核心组件

### 1. 意图识别节点 (IntentRecognitionNode)

**位置**: `src/main/java/com/czf/agentLearn4j/agentLearn4j/agent/workflow/node/IntentRecognitionNode.java`

**功能**:
- 使用 AI 模型自动识别用户输入的意图类型
- 支持的意图类型：QUERY、CHAT、TASK、TOOL、PLANNING、UNKNOWN
- 提供意图识别的置信度评分

**关键实现**:
```java
@Component
public class IntentRecognitionNode implements Function<Map<String, Object>, Map<String, Object>> {
    // 使用 DashScopeChatModel 进行意图识别
    // 解析 AI 响应，提取意图和置信度
}
```

### 2. 意图路由节点 (IntentRoutingNode)

**位置**: `src/main/java/com/czf/agentLearn4j/agentLearn4j/agent/workflow/node/IntentRoutingNode.java`

**功能**:
- 根据识别的意图类型决定路由目标
- 置信度检查：低于 0.6 的置信度会路由到默认处理器
- 支持的路由：knowledge_query、conversational、task_execution、tool_calling、planning、default

**路由规则**:
```
QUERY -> knowledge_query
CHAT -> conversational
TASK -> task_execution
TOOL -> tool_calling
PLANNING -> planning
其他 -> default
```

### 3. 处理器节点

**位置**: `src/main/java/com/czf/agentLearn4j/agentLearn4j/agent/workflow/node/handler/`

#### QueryHandlerNode (查询处理器)
- 处理知识查询类型的请求
- 提供详细且准确的回答

#### ChatHandlerNode (对话处理器)
- 处理闲聊对话类型的请求
- 以自然、轻松的方式与用户交流

#### DefaultHandlerNode (默认处理器)
- 处理无法明确分类的请求
- 使用通用的处理方式

### 4. 主工作流 Agent (IntentBasedWorkflowAgent)

**位置**: `src/main/java/com/czf/agentLearn4j/agentLearn4j/agent/workflow/IntentBasedWorkflowAgent.java`

**功能**:
- 编排整个工作流：意图识别 → 意图路由 → 处理器执行
- 提供同步执行方法 `execute(String input)`
- 提供流式执行方法 `streamExecute(String input)`

**工作流程**:
```
用户输入
   ↓
意图识别节点 (识别意图和置信度)
   ↓
意图路由节点 (决定路由目标)
   ↓
处理器节点 (执行具体处理)
   ↓
返回结果
```

### 5. REST Controller (IntentWorkflowController)

**位置**: `src/main/java/com/czf/agentLearn4j/agentLearn4j/controller/IntentWorkflowController.java`

**API 端点**:

1. **同步执行接口**
   - `POST /api/v1/intent-workflow/execute`
   - 返回 JSON 格式的响应

2. **流式执行接口**
   - `POST /api/v1/intent-workflow/stream`
   - 返回 SSE 格式的实时事件流
   - 事件类型：start、intent_recognition、intent_recognized、routing、routed、processing、result、complete、error

3. **健康检查接口**
   - `GET /api/v1/intent-workflow/health`
   - 检查服务状态

## 配置说明

### application.properties

```properties
# DashScope Configuration
spring.ai.dashscope.api-key=${MODEL_API_KEY:your-api-key}
spring.ai.dashscope.agent.base-url=https://dashscope.aliyuncs.com/api/v1
spring.ai.dashscope.chat.options.model=qwen-plus
spring.ai.dashscope.chat.options.temperature=0.7
spring.ai.dashscope.chat.options.max-tokens=2000
```

**注意**:
- `DashScopeChatModel` 由 Spring AI Alibaba starter 自动配置
- 需要配置有效的 API Key

## 使用示例

### 1. 同步调用

```bash
curl -X POST http://localhost:8080/api/v1/intent-workflow/execute \
  -H "Content-Type: application/json" \
  -d '{
    "input": "什么是 Spring AI Alibaba？"
  }'
```

### 2. 流式调用

```bash
curl -X POST http://localhost:8080/api/v1/intent-workflow/stream \
  -H "Content-Type: application/json" \
  -d '{
    "input": "Spring Boot 的优势有哪些？"
  }' \
  -N
```

### 3. 测试脚本

项目根目录下提供了测试脚本 `test-workflow.sh`：

```bash
chmod +x test-workflow.sh
./test-workflow.sh
```

## 测试

### 单元测试

**位置**: `src/test/java/com/czf/agentLearn4j/agentLearn4j/agent/workflow/IntentBasedWorkflowAgentTest.java`

包含以下测试用例：
- 查询意图测试
- 对话意图测试
- 未知意图测试
- 流式执行测试

### 运行测试

```bash
# 运行所有测试
./mvnw test

# 运行特定测试类
./mvnw test -Dtest=IntentBasedWorkflowAgentTest
```

## 构建和运行

### 构建项目

```bash
./mvnw clean install
```

### 运行应用

```bash
./mvnw spring-boot:run
```

### 打包应用

```bash
./mvnw package
```

## 技术栈

- **Spring Boot 3.5.9** - 应用框架
- **Spring AI Alibaba 1.1.0.0-RC2** - AI Agent Framework
- **DashScope** - Alibaba Cloud AI 服务
- **Reactor** - 响应式编程支持（流式响应）
- **Lombok** - 减少样板代码

## 架构特点

### 1. 工作流编排
- 采用简单的链式调用方式
- 每个节点实现 `Function<Map<String, Object>, Map<String, Object>>`
- 状态在节点间传递，便于扩展

### 2. 流式响应
- 使用 Reactor 的 `Flux` 实现流式处理
- SSE (Server-Sent Events) 协议
- 实时推送工作流执行进度

### 3. 模块化设计
- 节点独立，易于添加新的处理器
- 路由逻辑集中在 `IntentRoutingNode`
- 支持灵活的意图扩展

## 扩展开发

### 添加新的意图类型

1. 在 `IntentRecognitionNode` 中更新意图列表
2. 在 `IntentRoutingNode` 的 `routeByIntent` 方法中添加路由规则
3. 创建对应的处理器节点
4. 在 `IntentBasedWorkflowAgent` 中注册新节点

### 添加自定义处理器示例

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomHandlerNode implements Function<Map<String, Object>, Map<String, Object>> {

    private final DashScopeChatModel chatModel;

    @Override
    public Map<String, Object> apply(Map<String, Object> state) {
        // 实现自定义处理逻辑
        String input = (String) state.get("input");
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

## 文档

- **使用指南**: `docs/intent-workflow-usage.md`
- **测试脚本**: `test-workflow.sh`
- **本文档**: `README-INTENT-WORKFLOW.md`

## 注意事项

1. 确保配置正确的 DashScope API Key
2. 网络环境需要能够访问 DashScope API
3. 流式接口需要客户端支持 SSE
4. 意图识别的准确性依赖于 AI 模型的质量
5. 建议在生产环境中添加请求限流和熔断机制

## 常见问题

### 1. 编译错误
- 确保使用 Java 17
- 检查依赖是否正确下载

### 2. API Key 错误
- 检查 `application.properties` 中的配置
- 确保 API Key 有效且有足够的额度

### 3. 流式响应中断
- 检查客户端是否支持 SSE
- 检查网络稳定性
- 增加超时时间配置

## 性能优化建议

1. 使用缓存减少重复的意图识别
2. 异步处理长时间运行的任务
3. 实现请求限流和熔断机制
4. 监控 API 调用次数和响应时间
5. 考虑使用连接池优化 HTTP 连接

## 版本历史

- **v1.0.0** (2026-01-13) - 初始版本
  - 实现意图识别和路由
  - 支持流式响应
  - 提供 REST API 接口

## 作者

- 姓名: zifengchen
- 日期: 2026-01-13

## 许可证

本项目采用 [项目许可证]
