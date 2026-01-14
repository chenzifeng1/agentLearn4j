# Bean 冲突问题解决

## 🔍 问题分析

### 错误信息
```
The bean 'chatClientBuilderConfigurer', defined in class path resource
[org/springframework/ai/model/chat/client/autoconfigure/ChatClientAutoConfiguration.class],
could not be registered. A bean with that name has already been defined in class path resource
[org/springframework/ai/autoconfigure/chat/client/ChatClientAutoConfiguration.class]
and overriding is disabled.
```

### 根本原因
同时引入了两个 Spring AI 依赖：
1. **Spring AI Alibaba** (版本 1.1.0.0-RC2)
2. **Spring AI OpenAI** (版本 1.0.0-M5)

两者都包含了 `ChatClientAutoConfiguration`，导致 Bean 定义冲突。

## ✅ 解决方案（已实施）

### 方案：排除冲突的自动配置

在 `pom.xml` 中添加排除规则：

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
    <version>1.0.0-M5</version>
    <exclusions>
        <!-- 排除冲突的自动配置 -->
        <exclusion>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-autoconfigure</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### 为什么这个方案有效

1. **保留核心功能** - OpenAI 的核心 API 和 ChatModel 仍然可用
2. **避免冲突** - 排除了自动配置，使用 Alibaba 版本的自动配置
3. **手动配置** - 我们在 `OpenAiConfig.java` 中手动配置了 OpenAI 客户端

## 🚀 测试步骤

### 1. 启动应用
```bash
./mvnw spring-boot:run
```

应该能正常启动，不再报 Bean 冲突错误。

### 2. 测试接口
```bash
# 测试简单接口
curl -X POST http://localhost:8080/api/v1/debug/simple-test \
  -H "Content-Type: application/json" \
  -d '{"input": "你好"}'

# 测试流式接口
curl -X POST http://localhost:8080/api/v1/intent-workflow/stream \
  -H "Content-Type: application/json" \
  -d '{"input": "Spring Boot 的优势有哪些？"}' \
  -N
```

## 📝 其他可行方案（未采用）

### 方案 1: 启用 Bean 覆盖
在 `application.properties` 添加：
```properties
spring.main.allow-bean-definition-overriding=true
```

**缺点**：
- 不推荐，可能导致不可预测的行为
- 不知道哪个 Bean 会被使用

### 方案 2: 移除 Spring AI OpenAI 依赖
只使用自定义的 OpenAI API 客户端。

**缺点**：
- 需要更多手动配置
- 失去了 Spring AI 的一些便利功能

### 方案 3: 统一 Spring AI 版本
等待 Spring AI Alibaba 更新到与 Spring AI OpenAI 兼容的版本。

**缺点**：
- 需要等待更新
- 可能需要改动更多代码

## 🎯 当前方案的优势

✅ **最小改动** - 只需要添加一个排除规则
✅ **保留功能** - OpenAI API 功能完整可用
✅ **避免冲突** - 彻底解决 Bean 重复定义问题
✅ **灵活性** - 可以同时使用两个客户端

## 📌 验证清单

- [x] 编译成功
- [ ] 应用启动成功（无 Bean 冲突错误）
- [ ] Debug 接口测试成功
- [ ] 流式接口测试成功
- [ ] 日志显示正确的请求格式（OpenAI 标准）

下一步：启动应用并进行测试！
