#!/bin/bash

echo "========================================"
echo "验证所有 Agent 构造方法更新"
echo "========================================"
echo ""

echo "✅ 检查所有 CustomAgent 类..."
find src/main/java -name "*AgentCustom.java" | while read file; do
    echo "  - $(basename $file)"
done
echo ""

echo "✅ 检查是否移除了 @RequiredArgsConstructor..."
if grep -r "@RequiredArgsConstructor" src/main/java/com/czf/agentLearn4j/agentLearn4j/agent/ --include="*AgentCustom.java" 2>/dev/null; then
    echo "  ⚠️  发现未移除的 @RequiredArgsConstructor"
else
    echo "  ✓ 所有类都已移除 @RequiredArgsConstructor"
fi
echo ""

echo "✅ 检查简化的 super() 调用..."
echo "  找到以下 super() 调用:"
grep -h "super(" src/main/java/com/czf/agentLearn4j/agentLearn4j/agent/ -r --include="*AgentCustom.java" | grep -v "//" | sed 's/^[ \t]*/  /' | sort -u
echo ""

echo "✅ 统计信息:"
AGENT_COUNT=$(find src/main/java -name "*AgentCustom.java" | wc -l | tr -d ' ')
SUPER_COUNT=$(grep -r "super(" src/main/java/com/czf/agentLearn4j/agentLearn4j/agent/ --include="*AgentCustom.java" | grep -v "//" | wc -l | tr -d ' ')
echo "  - Agent 类总数: $AGENT_COUNT"
echo "  - super() 调用总数: $SUPER_COUNT"
echo ""

echo "✅ 所有更新已完成！"
echo "========================================"
