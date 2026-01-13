# CustomBaseAgent 使用指南

CustomBaseAgent 提供了三种构造方法，满足不同的使用场景。

## 三种构造方式

### 1. 完整构造方法（Full Constructor）
适用于需要精确控制所有参数的场景。

```java
public class AdvancedAgent extends CustomBaseAgent {

    public AdvancedAgent() {
        super(
            "AdvancedAgent",                    // name: Agent名称
            "Advanced reasoning agent",         // description: Agent描述
            true,                               // includeContents: 是否包含内容
            true,                               // returnReasoningContents: 是否返回推理内容
            "customOutput",                     // outputKey: 输出键名
            KeyStrategy.MULTIPLE                // outputKeyStrategy: 键策略
        );
    }

    @Override
    public String getAgentType() {
        return "advanced";
    }

    @Override
    public String execute(String input) {
        return "Advanced processing: " + input;
    }
}
```

### 2. 简化构造方法（Simplified Constructor）⭐ 推荐
只需提供name和description，其他参数使用合理的默认值。

**默认值：**
- `includeContents = true`
- `returnReasoningContents = false`
- `outputKey = "output"`
- `outputKeyStrategy = KeyStrategy.ONE`

```java
@Component
public class ConversationalAgentCustom extends CustomBaseAgent {

    private final ConversationService conversationService;

    public ConversationalAgentCustom(ConversationService conversationService) {
        super("ConversationalAgent", "Conversational AI for question-answer interactions");
        this.conversationService = conversationService;
    }

    @Override
    public String getAgentType() {
        return AgentConstants.AGENT_TYPE_CONVERSATION;
    }

    @Override
    public String execute(String input) {
        return conversationService.processMessage(input);
    }
}
```

### 3. 最简构造方法（Minimal Constructor）
只需要提供name，description会自动生成为 "{name} Agent"。

```java
@Component
public class SimpleToolAgent extends CustomBaseAgent {

    private final ToolExecutor toolExecutor;

    public SimpleToolAgent(ToolExecutor toolExecutor) {
        super("SimpleTool");  // 描述自动为 "SimpleTool Agent"
        this.toolExecutor = toolExecutor;
    }

    @Override
    public String getAgentType() {
        return "simple-tool";
    }

    @Override
    public String execute(String input) {
        return toolExecutor.executeTool("default", input);
    }
}
```

## 使用建议

### 推荐场景

1. **简化构造方法（2种参数）**：适合大多数场景
   - ✅ 清晰明确的name和description
   - ✅ 使用标准的输出配置
   - ✅ 代码简洁易读

2. **完整构造方法（6个参数）**：适合特殊需求
   - 需要自定义outputKey
   - 需要返回推理内容（returnReasoningContents=true）
   - 需要使用MULTIPLE键策略

3. **最简构造方法（1个参数）**：适合快速原型
   - 快速测试
   - 简单的工具类Agent
   - name能够充分说明Agent功能

## 完整示例对比

### 原来的写法（冗长）
```java
public ConversationalAgentCustom(ConversationService conversationService) {
    super("ConversationalAgentCustom", "description", true, true, "outputKey", outputKeyStrategy);
    this.conversationService = conversationService;
}
```

### 优化后的写法（简洁）
```java
public ConversationalAgentCustom(ConversationService conversationService) {
    super("ConversationalAgent", "Conversational AI for question-answer interactions");
    this.conversationService = conversationService;
}
```

### 极简写法（最简）
```java
public SimpleAgent(SomeService service) {
    super("SimpleAgent");  // description自动为 "SimpleAgent Agent"
    this.service = service;
}
```

## 默认参数说明

| 参数 | 默认值 | 说明 |
|------|--------|------|
| includeContents | true | 包含执行内容 |
| returnReasoningContents | false | 不返回推理过程（大多数场景不需要） |
| outputKey | "output" | 标准输出键名 |
| outputKeyStrategy | KeyStrategy.ONE | 单一键策略 |

## 注意事项

1. **Spring依赖注入**：如果使用`@Component`注解，构造函数中的依赖会自动注入
2. **不要忘记调用super()**：子类构造函数必须调用父类构造函数
3. **name要有意义**：name会用于日志和调试，应该清晰表达Agent的功能
4. **description要详细**：description用于文档和展示，应该说明Agent的具体作用

## 迁移指南

如果你有现有的Agent使用完整构造方法，可以按照以下步骤迁移：

### 步骤1：确认当前参数
```java
super("MyAgent", "My Description", true, false, "output", KeyStrategy.ONE);
```

### 步骤2：检查是否使用默认值
如果后4个参数是 `true, false, "output", KeyStrategy.ONE`，可以简化。

### 步骤3：使用简化构造
```java
super("MyAgent", "My Description");
```

### 步骤4：测试验证
运行测试确保行为一致。
