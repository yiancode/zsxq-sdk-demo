# 知识星球 SDK Spring Boot Demo

基于知识星球 SDK 的 Spring Boot 示例项目，提供 RESTful API 接口。

## 项目结构

```
springboot/
├── pom.xml                                 # Maven 配置
├── src/main/
│   ├── java/com/zsxq/demo/
│   │   ├── ZsxqDemoApplication.java       # 启动类
│   │   ├── controller/
│   │   │   └── ZsxqController.java        # REST 控制器
│   │   ├── service/
│   │   │   └── ZsxqService.java           # 业务服务层
│   │   ├── config/
│   │   │   ├── ZsxqConfig.java            # SDK Bean 配置
│   │   │   └── ZsxqProperties.java        # 配置属性
│   │   └── model/
│   │       └── ApiResponse.java           # 统一响应模型
│   └── resources/
│       └── application.yml                # 应用配置
```

## 快速开始

### 1. 配置

编辑 `src/main/resources/application.yml`：

```yaml
zsxq:
  token: YOUR_ZSXQ_TOKEN
  group-id: YOUR_GROUP_ID
  timeout: 10000
  retry-count: 3
```

### 2. 构建

```bash
cd springboot
mvn clean package
```

### 3. 运行

```bash
mvn spring-boot:run
```

或运行打包后的 jar：

```bash
java -jar target/zsxq-sdk-demo-springboot-1.0.0.jar
```

应用将在 http://localhost:8080 启动。

## API 接口

### 用户相关

#### 获取当前用户信息
```http
GET /api/zsxq/user/self
```

响应示例：
```json
{
  "success": true,
  "message": "操作成功",
  "data": {
    "uid": "184444848828412",
    "name": "易安",
    "avatarUrl": "https://...",
    "location": "广东"
  }
}
```

#### 获取用户统计
```http
GET /api/zsxq/user/{userId}/statistics
```

### 星球相关

#### 获取我的星球列表
```http
GET /api/zsxq/groups
```

#### 获取星球详情
```http
GET /api/zsxq/groups/{groupId}
```

#### 获取默认星球详情
```http
GET /api/zsxq/groups/default
```

#### 获取星球统计
```http
GET /api/zsxq/groups/{groupId}/statistics
```

## 配置说明

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `zsxq.token` | 知识星球 Token | - |
| `zsxq.group-id` | 默认星球 ID | - |
| `zsxq.timeout` | 请求超时时间（毫秒） | 10000 |
| `zsxq.retry-count` | 重试次数 | 3 |
| `zsxq.retry-delay` | 重试延迟（毫秒） | 1000 |

## 技术栈

- Spring Boot 2.7.18
- Java 11
- Lombok
- ZSXQ SDK 1.0.0

## 开发说明

### 添加新接口

1. 在 `ZsxqService` 中添加业务方法
2. 在 `ZsxqController` 中添加 API 端点
3. 使用 `ApiResponse` 包装响应

示例：

```java
// Service
public List<Topic> getTopics(long groupId) {
    return zsxqClient.topics().list(groupId);
}

// Controller
@GetMapping("/groups/{groupId}/topics")
public ApiResponse<List<Topic>> getTopics(@PathVariable Long groupId) {
    try {
        List<Topic> topics = zsxqService.getTopics(groupId);
        return ApiResponse.success(topics);
    } catch (Exception e) {
        return ApiResponse.error(e.getMessage());
    }
}
```

## 测试

使用 curl 测试：

```bash
# 获取当前用户
curl http://localhost:8080/api/zsxq/user/self

# 获取星球列表
curl http://localhost:8080/api/zsxq/groups

# 获取星球详情
curl http://localhost:8080/api/zsxq/groups/88885121521552
```

## 注意事项

1. **Token 安全**：生产环境不要在配置文件中明文存储 Token，建议使用环境变量或配置中心
2. **异常处理**：已实现基本异常捕获，可根据需要扩展全局异常处理器
3. **日志**：使用 slf4j，可通过配置文件调整日志级别
4. **系统依赖**：SDK jar 使用 system scope，需确保路径正确

## 已知问题

1. **Topics API**：部分接口返回"无效的 count"错误，可能需要特定参数
2. **Checkins API**：返回"参数错误"，需进一步调试
3. **权限限制**：某些接口需要特定权限（如 Dashboard 需要星主权限）

## License

MIT
