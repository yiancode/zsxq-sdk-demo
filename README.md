# zsxq-sdk-demo

知识星球 SDK 多语言示例项目，演示如何使用 [zsxq-sdk](https://github.com/yiancode/zsxq-sdk) 调用知识星球 API。

## 功能演示

每个语言的 Demo 都包含以下功能模块的完整测试：

| 模块 | 功能 | API |
|------|------|-----|
| Groups | 星球管理 | list, get, getStatistics, getMember |
| Topics | 话题管理 | list, get |
| Users | 用户管理 | self, get, getStatistics |
| Checkins | 打卡管理 | list, get, getStatistics, getRankingList |
| Dashboard | 数据面板 | getOverview, getIncomes |

## 获取 Token

1. 打开 [知识星球网页版](https://wx.zsxq.com) 并登录
2. 按 `F12` 打开开发者工具 → Network
3. 刷新页面，找到 `api.zsxq.com` 请求
4. 在请求头中复制 `authorization` 值

## 快速开始

### Java Demo

```bash
cd java
# 设置环境变量
export ZSXQ_TOKEN=your-token
export ZSXQ_GROUP_ID=your-group-id

# 运行
mvn compile exec:java
```

### TypeScript Demo

```bash
cd typescript
npm install
# 设置环境变量
export ZSXQ_TOKEN=your-token
export ZSXQ_GROUP_ID=your-group-id

# 运行
npm start
```

### Go Demo

```bash
cd go
# 设置环境变量
export ZSXQ_TOKEN=your-token
export ZSXQ_GROUP_ID=your-group-id

# 运行
go run main.go
```

### Python Demo

```bash
cd python
pip install -r requirements.txt
# 设置环境变量
export ZSXQ_TOKEN=your-token
export ZSXQ_GROUP_ID=your-group-id

# 运行
python demo.py
```

## 项目结构

```
zsxq-sdk-demo/
├── README.md
├── java/                    # Java Demo
│   ├── pom.xml
│   └── src/main/java/
│       └── com/zsxq/demo/
│           └── ZsxqDemo.java
├── typescript/              # TypeScript Demo
│   ├── package.json
│   ├── tsconfig.json
│   └── src/
│       └── index.ts
├── go/                      # Go Demo
│   ├── go.mod
│   └── main.go
└── python/                  # Python Demo
    ├── requirements.txt
    └── demo.py
```

## 环境变量

| 变量 | 说明 | 必填 |
|------|------|------|
| `ZSXQ_TOKEN` | 知识星球 Token | 是 |
| `ZSXQ_GROUP_ID` | 测试用星球 ID | 是 |
| `ZSXQ_TOPIC_ID` | 测试用话题 ID | 否 |
| `ZSXQ_CHECKIN_ID` | 测试用打卡项目 ID | 否 |

## 相关项目

- [zsxq-sdk](https://github.com/yiancode/zsxq-sdk) - 知识星球多语言 SDK

## 许可证

MIT License
