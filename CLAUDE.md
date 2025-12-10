# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

知识星球 SDK 多语言示例项目，展示 [zsxq-sdk](https://github.com/yiancode/zsxq-sdk) 的使用方式。包含 Java、TypeScript、Go、Python 四种语言实现。

**重要**: SDK 依赖通过本地路径引用 (`../../zsxq-sdk/packages/`)，需确保 zsxq-sdk 仓库已克隆到同级目录。

## 常用命令

### TypeScript (主要开发语言)
```bash
cd typescript
npm install
npm start                    # 运行 demo
npm test                     # 所有测试
npm run test:unit           # 仅单元测试 (*.unit.test.ts)
npm run test:integration    # 仅集成测试 (*.integration.test.ts)
npm run test:cov            # 测试覆盖率
```

### Go
```bash
cd go
go mod download
go run main.go              # 运行 demo
go test ./... -v            # 所有测试
go test -cover ./...        # 测试覆盖率
```

### Python
```bash
cd python
pip install -r requirements.txt
python demo.py              # 运行 demo
pytest tests/ -v            # 所有测试
pytest --cov=. tests/       # 测试覆盖率
```

### Java (Spring Boot)
```bash
cd java
./start.sh                  # 启动应用
mvn spring-boot:run         # 或使用 Maven
./test-api.sh               # 测试 API 端点
```

## 环境变量

所有语言实现都需要:
```bash
export ZSXQ_TOKEN="your-authorization-token"   # 必需
export ZSXQ_GROUP_ID="your-group-id"           # 必需
export ZSXQ_TOPIC_ID="topic-id"                # 可选
export ZSXQ_CHECKIN_ID="checkin-id"            # 可选
```

Token 获取: 访问 https://wx.zsxq.com → F12 → Network → 筛选 api.zsxq.com → 复制 authorization 请求头

## 架构说明

每种语言实现相同的 5 个 API 模块:

| 模块 | 功能 | 核心方法 |
|------|------|----------|
| Users | 用户信息 | self, getStatistics |
| Groups | 星球管理 | list, get, getStatistics |
| Topics | 话题管理 | list, get |
| Checkins | 打卡项目 | list, get, getStatistics, getRankingList |
| Dashboard | 数据面板 | getOverview, getIncomes |

### 客户端构建模式

所有语言使用 Builder 模式创建客户端:
- TypeScript: `new ZsxqClientBuilder().setToken(token).build()`
- Go: `zsxq.NewClientBuilder().SetToken(token).MustBuild()`
- Python: `ZsxqClientBuilder().set_token(token).build()` (异步上下文管理器)
- Java: 通过 Spring Bean 配置

### 测试结构

TypeScript 测试分为:
- 单元测试 (`*.unit.test.ts`): 各模块独立测试
- 集成测试 (`*.integration.test.ts`): 跨模块工作流测试

运行测试需要有效的环境变量配置。
