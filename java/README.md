# 知识星球 SDK Demo - Java Spring Boot 版

这是一个基于 Spring Boot 的知识星球 SDK 示例项目,展示了如何在 Spring Boot 应用中使用 zsxq-sdk。

## 功能特性

- ✅ Spring Boot 集成
- ✅ RESTful API 封装
- ✅ 统一错误处理
- ✅ 配置文件管理
- ✅ 集成测试

## 项目结构

```
src/main/java/com/zsxq/demo/
├── ZsxqDemoApplication.java       # Spring Boot 主类
├── config/
│   ├── ZsxqConfig.java            # SDK 配置
│   └── ZsxqProperties.java        # 配置属性
├── controller/
│   ├── ZsxqController.java        # 星球和用户 API
│   ├── TopicsController.java      # 话题 API
│   ├── CheckinsController.java    # 打卡 API
│   └── DashboardController.java   # 数据面板 API
├── service/
│   ├── ZsxqService.java           # 星球和用户服务
│   ├── TopicsService.java         # 话题服务
│   ├── CheckinsService.java       # 打卡服务
│   └── DashboardService.java      # 数据面板服务
└── model/
    └── ApiResponse.java           # 统一响应模型

src/test/java/com/zsxq/demo/
└── integration/
    └── ZsxqApiIntegrationTest.java # 集成测试
```

## 快速开始

### 1. 配置环境变量

```bash
export ZSXQ_TOKEN="your-zsxq-token"
export ZSXQ_GROUP_ID="your-group-id"
```

### 2. 启动应用

```bash
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动。

### 3. 测试 API

```bash
# 获取当前用户信息
curl http://localhost:8080/api/zsxq/user/self

# 获取星球列表
curl http://localhost:8080/api/zsxq/groups

# 获取话题列表
curl "http://localhost:8080/api/zsxq/topics/groups/15555411412112?count=5"

# 获取数据概览
curl http://localhost:8080/api/zsxq/dashboard/groups/15555411412112/overview
```

## API 接口

### 用户系统

- `GET /api/zsxq/user/self` - 获取当前用户信息
- `GET /api/zsxq/user/{userId}/statistics` - 获取用户统计

### 星球管理

- `GET /api/zsxq/groups` - 获取星球列表
- `GET /api/zsxq/groups/{groupId}` - 获取星球详情
- `GET /api/zsxq/groups/default` - 获取默认星球
- `GET /api/zsxq/groups/{groupId}/statistics` - 获取星球统计

### 话题管理

- `GET /api/zsxq/topics/groups/{groupId}` - 获取话题列表
  - 查询参数: `count` (数量), `scope` (范围: all/digests/questions)

### 打卡系统

- `GET /api/zsxq/checkins/groups/{groupId}` - 获取打卡项目列表
- `GET /api/zsxq/checkins/groups/{groupId}/{checkinId}` - 获取打卡详情
- `GET /api/zsxq/checkins/groups/{groupId}/{checkinId}/statistics` - 获取打卡统计
- `GET /api/zsxq/checkins/groups/{groupId}/{checkinId}/ranking` - 获取打卡排行榜

### 数据面板

- `GET /api/zsxq/dashboard/groups/{groupId}/overview` - 获取数据概览
- `GET /api/zsxq/dashboard/groups/{groupId}/incomes` - 获取收入概览

## 配置说明

在 `application.yml` 中配置:

```yaml
zsxq:
  token: ${ZSXQ_TOKEN:YOUR_ZSXQ_TOKEN}
  group-id: ${ZSXQ_GROUP_ID:YOUR_GROUP_ID}
  timeout: 10000
  retry-count: 3
  retry-delay: 1000
```

## 运行测试

### 单元测试

```bash
mvn test
```

### 集成测试

```bash
export ZSXQ_TOKEN="your-token"
export ZSXQ_GROUP_ID="your-group-id"
mvn test -Dtest=ZsxqApiIntegrationTest
```

## 测试报告

查看 [TEST_REPORT.md](TEST_REPORT.md) 了解详细的测试结果和覆盖率。

## 依赖

- Spring Boot 2.7.18
- zsxq-sdk 1.0.0
- OkHttp 4.12.0
- Gson 2.10.1
- Lombok 1.18.34

## 开发

### 添加新的 API

1. 在对应的 Service 中添加方法
2. 在对应的 Controller 中添加端点
3. 添加集成测试
4. 更新文档

### 错误处理

所有 API 返回统一的响应格式:

```json
{
  "success": true,
  "message": "操作成功",
  "data": {...}
}
```

失败时:

```json
{
  "success": false,
  "message": "错误信息",
  "data": null
}
```

## 许可证

MIT License

## 相关链接

- [zsxq-sdk GitHub](https://github.com/yiancode/zsxq-sdk)
- [知识星球官网](https://www.zsxq.com)
