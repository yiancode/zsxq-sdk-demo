#!/bin/bash

# API 测试脚本

BASE_URL="http://localhost:8080/api/zsxq"

echo "=== 知识星球 SDK API 测试 ==="
echo ""

# 检查服务是否运行
if ! curl -s -o /dev/null -w "%{http_code}" $BASE_URL/user/self | grep -q "200\|500"; then
    echo "错误: 服务未运行，请先启动应用"
    echo "运行: ./start.sh"
    exit 1
fi

echo "✓ 服务已运行"
echo ""

# 测试 1: 获取当前用户
echo "1. 获取当前用户信息"
echo "   GET $BASE_URL/user/self"
curl -s $BASE_URL/user/self | python3 -m json.tool
echo ""
echo ""

# 测试 2: 获取星球列表
echo "2. 获取我的星球列表"
echo "   GET $BASE_URL/groups"
curl -s $BASE_URL/groups | python3 -m json.tool | head -30
echo ""
echo ""

# 测试 3: 获取默认星球
echo "3. 获取默认星球详情"
echo "   GET $BASE_URL/groups/default"
curl -s $BASE_URL/groups/default | python3 -m json.tool
echo ""
echo ""

echo "=== 测试完成 ==="
