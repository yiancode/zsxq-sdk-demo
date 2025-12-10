#!/bin/bash

# 知识星球 SDK Demo 启动脚本

echo "=== 知识星球 SDK Spring Boot Demo ==="
echo ""

# 检查 SDK jar 是否存在
SDK_JAR="../../zsxq-sdk/packages/java/target/zsxq-sdk-1.0.0.jar"
if [ ! -f "$SDK_JAR" ]; then
    echo "错误: SDK jar 不存在: $SDK_JAR"
    echo "请先构建 SDK:"
    echo "  cd ../../zsxq-sdk/packages/java && mvn clean package"
    exit 1
fi

echo "✓ SDK jar 已找到"
echo ""

# 构建项目
echo "正在构建项目..."
mvn clean package -DskipTests -q
if [ $? -ne 0 ]; then
    echo "构建失败！"
    exit 1
fi
echo "✓ 构建成功"
echo ""

# 启动应用
echo "启动 Spring Boot 应用..."
echo "访问地址: http://localhost:8080"
echo ""
echo "可用的 API 端点:"
echo "  - GET /api/zsxq/user/self           获取当前用户"
echo "  - GET /api/zsxq/groups              获取星球列表"
echo "  - GET /api/zsxq/groups/{id}         获取星球详情"
echo "  - GET /api/zsxq/groups/default      获取默认星球"
echo ""

mvn spring-boot:run
