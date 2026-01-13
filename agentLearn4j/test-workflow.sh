#!/bin/bash

# 意图工作流测试脚本

BASE_URL="http://localhost:8080/api/v1/intent-workflow"

echo "=========================================="
echo "意图工作流测试脚本"
echo "=========================================="
echo ""

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 测试健康检查
echo -e "${BLUE}1. 测试健康检查${NC}"
echo "GET ${BASE_URL}/health"
curl -s "${BASE_URL}/health" | jq .
echo -e "\n"

# 测试查询意图（同步）
echo -e "${BLUE}2. 测试查询意图（同步）${NC}"
echo "POST ${BASE_URL}/execute"
curl -s -X POST "${BASE_URL}/execute" \
  -H "Content-Type: application/json" \
  -d '{
    "input": "什么是 Spring AI Alibaba？"
  }' | jq .
echo -e "\n"

# 测试对话意图（同步）
echo -e "${BLUE}3. 测试对话意图（同步）${NC}"
echo "POST ${BASE_URL}/execute"
curl -s -X POST "${BASE_URL}/execute" \
  -H "Content-Type: application/json" \
  -d '{
    "input": "你好，今天天气怎么样？"
  }' | jq .
echo -e "\n"

# 测试任务意图（同步）
echo -e "${BLUE}4. 测试任务意图（同步）${NC}"
echo "POST ${BASE_URL}/execute"
curl -s -X POST "${BASE_URL}/execute" \
  -H "Content-Type: application/json" \
  -d '{
    "input": "帮我创建一个待办事项"
  }' | jq .
echo -e "\n"

# 测试流式执行
echo -e "${BLUE}5. 测试流式执行（SSE）${NC}"
echo "POST ${BASE_URL}/stream"
echo -e "${YELLOW}观察实时事件流...${NC}"
curl -X POST "${BASE_URL}/stream" \
  -H "Content-Type: application/json" \
  -d '{
    "input": "Spring Boot 的优势有哪些？"
  }' \
  -N
echo -e "\n"

echo -e "${GREEN}=========================================="
echo "测试完成！"
echo "==========================================${NC}"
