# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

知识星球 SDK 多语言示例项目，展示 [zsxq-sdk](https://github.com/yiancode/zsxq-sdk) 的使用方式。包含 Java、TypeScript、Go、Python 四种语言实现。

### SDK 依赖方式

| 语言 | 依赖方式 |
|------|---------|
| TypeScript | 本地路径 `../../zsxq-sdk/packages/typescript` |
| Go | 本地路径 replace `../../zsxq-sdk/packages/go` |
| Java | Maven Central `io.github.yiancode:zsxq-sdk:1.0.0` |
| Python | 需手动安装本地 SDK |

**注意**: TypeScript 和 Go 需确保 zsxq-sdk 仓库已克隆到同级目录。

## 常用命令

### TypeScript
```bash
cd typescript
npm install
npm start                    # 运行 demo
npm test                     # 所有测试
npm run test:unit           # 仅单元测试
npm run test:integration    # 仅集成测试
npm run test:cov            # 测试覆盖率
# 运行单个测试文件
npx jest tests/users.unit.test.ts
```

### Go
```bash
cd go
go mod download
go run main.go              # 运行 demo
go test ./... -v            # 所有测试
go test -cover ./...        # 测试覆盖率
# 运行单个测试
go test -v -run TestIntegration
```

### Python
```bash
cd python
pip install -r requirements.txt
python demo.py              # 运行 demo
pytest tests/ -v            # 所有测试
pytest --cov=. tests/       # 测试覆盖率
# 运行单个测试
pytest tests/test_integration.py -v
```

### Java (Spring Boot)
```bash
cd java
mvn clean compile           # 编译项目
mvn test                    # 运行单元测试
./start.sh                  # 启动应用 (端口 8080)
mvn spring-boot:run         # 或使用 Maven
./test-api.sh               # 测试 API 端点
# 运行单个测试类
mvn test -Dtest=ZsxqApiIntegrationTest
```

## 环境变量

```bash
export ZSXQ_TOKEN="your-authorization-token"   # 必需
export ZSXQ_GROUP_ID="your-group-id"           # 必需
export ZSXQ_TOPIC_ID="topic-id"                # 可选
export ZSXQ_CHECKIN_ID="checkin-id"            # 可选
```

Token 获取: 访问 https://wx.zsxq.com → F12 → Network → 筛选 api.zsxq.com → 复制 authorization 请求头

## 架构说明

每种语言实现相同的 5 个 API 模块: Users、Groups、Topics、Checkins、Dashboard。

### 客户端构建模式 (Builder Pattern)

```typescript
// TypeScript
new ZsxqClientBuilder().setToken(token).setTimeout(10000).setRetry(3).build()

// Go
zsxq.NewClientBuilder().SetToken(token).MustBuild()

// Python (异步上下文管理器)
ZsxqClientBuilder().set_token(token).build()

// Java (Spring Bean 注入)
new ZsxqClientBuilder().token(token).timeout(timeout).retry(count, delay).build()
```

### Java 特有架构

Spring Boot 分层结构:
- `config/ZsxqConfig.java` - ZsxqClient Bean 配置
- `config/ZsxqProperties.java` - 配置属性映射 (`zsxq.*`)
- `controller/` - REST API 端点 (ZsxqController, DashboardController, TopicsController, CheckinsController)
- `service/` - 业务逻辑封装 (ZsxqService, DashboardService, TopicsService, CheckinsService)
- `model/ApiResponse.java` - 统一响应模型

### 测试命名约定

TypeScript 测试分为:
- `*.unit.test.ts` - 各模块独立测试
- `*.integration.test.ts` - 跨模块工作流测试

## 测试统计

| 语言 | 单元测试 | 集成测试 | 总计 | 状态 |
|------|---------|---------|------|------|
| TypeScript | 70 | 7 | 77 | ✅ |
| Java | 64 | 21 | 85 | ✅ |
| Python | 76 | 5 | 81 | ✅ |
| Go | 待修复 | 7 | 7+ | ⚠️ |

详细测试文档:
- [TESTING.md](TESTING.md) - 测试总览、规范、故障排查
- [typescript/tests/README.md](typescript/tests/README.md)
- [java/README.md](java/README.md)
- [python/tests/README.md](python/tests/README.md)
- [go/tests/README.md](go/tests/README.md)
