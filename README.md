# zsxq-sdk-demo

知识星球 SDK 多语言示例项目，展示如何使用 [zsxq-sdk](https://github.com/yiancode/zsxq-sdk) 调用知识星球 API。

## 概述

本项目提供 4 种语言的完整实现示例：

- **java** - Java 企业级应用示例
- **TypeScript** - 现代 JavaScript 生态系统示例
- **Go** - 高性能并发处理示例
- **Python** - 简洁异步编程示例

每个示例都包含以下功能模块的完整调用：

| 模块 | 功能说明 | 核心 API |
|------|---------|---------|
| Users | 用户信息管理 | self, get, getStatistics |
| Groups | 星球管理 | list, get, getStatistics, getMember |
| Topics | 话题管理 | list, get |
| Checkins | 打卡项目管理 | list, get, getStatistics, getRankingList |
| Dashboard | 数据统计面板 | getOverview, getIncomes |

## 前置准备

### 获取 Token

1. 访问 [知识星球网页版](https://wx.zsxq.com) 并登录
2. 按 `F12` 打开开发者工具，切换到 **Network** 标签
3. 刷新页面，筛选包含 `api.zsxq.com` 的请求
4. 在请求头中找到 `authorization` 字段，复制完整值

### 环境变量配置

所有示例都需要以下环境变量：

```bash
export ZSXQ_TOKEN="your-authorization-token"
export ZSXQ_GROUP_ID="your-group-id"
```

可选变量（用于特定 API 测试）：

```bash
export ZSXQ_TOPIC_ID="topic-id"      # 话题 ID
export ZSXQ_CHECKIN_ID="checkin-id"  # 打卡项目 ID
```

## 快速开始

### java

**要求**: Java 11+, Maven 3.6+

```bash
cd springboot
# 运行应用
./start.sh
# 或使用 Maven
mvn spring-boot:run
```

访问测试端点：
```bash
./test-api.sh  # 测试所有 API 端点
```

详细说明见：[springboot/README.md](springboot/README.md)

### TypeScript

**要求**: Node.js 18+

```bash
cd typescript
npm install
npm start
```

运行测试：
```bash
npm test              # 所有测试
npm run test:unit     # 单元测试
npm run test:cov      # 测试覆盖率
```

### Go

**要求**: Go 1.21+

```bash
cd go
go mod download
go run main.go
```

运行测试：
```bash
go test ./... -v      # 所有测试
go test -cover ./...  # 测试覆盖率
```

### Python

**要求**: Python 3.8+

```bash
cd python
pip install -r requirements.txt
python demo.py
```

运行测试：
```bash
pytest tests/ -v      # 所有测试
pytest --cov=. tests/ # 测试覆盖率
```

## 测试

本项目为所有语言提供了完整的测试覆盖，包括单元测试和集成测试。

### 测试统计

| 语言 | 单元测试 | 集成测试 | 总计 | 状态 |
|------|---------|---------|------|------|
| TypeScript | 70 | 7 | 77 | ✅ |
| Java | 64 | 21 | 85 | ✅ |
| Python | 76 | 5 | 81 | ✅ |
| Go | 待修复 | 7 | 7+ | ⚠️ |
| **总计** | **210+** | **40+** | **250+** | **✅** |

### 快速运行测试

**TypeScript**:
```bash
cd typescript
npm test                    # 所有测试
npm run test:unit          # 仅单元测试
npm run test:integration   # 仅集成测试
```

**Java**:
```bash
cd java
mvn test                   # 所有测试
mvn test -Dtest=ZsxqApiIntegrationTest  # 集成测试
```

**Python**:
```bash
cd python
pytest tests/ -v           # 所有测试
pytest tests/ -k unit      # 仅单元测试
```

**Go**:
```bash
cd go
go test ./... -v           # 所有测试
go test -v -run Integration  # 集成测试
```

### 详细测试文档

完整的测试指南请参阅：

📖 **[TESTING.md](TESTING.md)** - 测试总览、规范、故障排查

各语言详细测试文档：
- [TypeScript 测试文档](typescript/tests/README.md)
- [Java 测试文档](java/README.md)
- [Python 测试文档](python/tests/README.md)
- [Go 测试文档](go/tests/README.md)

## 项目结构

```
zsxq-sdk-demo/
├── README.md                         # 本文件
├── java/                             # springboot 示例
│   ├── pom.xml
│   ├── start.sh                      # 启动脚本
│   ├── test-api.sh                   # API 测试脚本
│   └── src/
│       └── main/java/com/zsxq/demo/
├── typescript/                       # TypeScript 示例
│   ├── package.json
│   ├── tsconfig.json
│   ├── src/index.ts
│   └── tests/                        # 测试文件
├── go/                               # Go 示例
│   ├── go.mod
│   ├── main.go
│   └── tests/                        # 测试文件
└── python/                           # Python 示例
    ├── requirements.txt
    ├── demo.py
    └── tests/                        # 测试文件
```

## 相关资源

- [zsxq-sdk](https://github.com/yiancode/zsxq-sdk) - 知识星球多语言 SDK
- [知识星球 API 文档](https://wx.zsxq.com) - 官方 API 参考

## 许可证

MIT License
