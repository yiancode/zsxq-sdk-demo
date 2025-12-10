# TypeScript SDK Demo 测试文档

## 测试结构

```
tests/
├── users.unit.test.ts          # 用户模块单元测试
├── groups.unit.test.ts         # 星球模块单元测试
├── topics.unit.test.ts         # 话题模块单元测试
├── checkins.unit.test.ts       # 打卡模块单元测试
├── dashboard.unit.test.ts      # 数据面板模块单元测试
└── integration.test.ts         # 集成测试
```

## 环境准备

### 1. 安装依赖

```bash
cd typescript
npm install
```

### 2. 配置环境变量

```bash
export ZSXQ_TOKEN="your-token"
export ZSXQ_GROUP_ID="your-group-id"
```

或创建 `.env` 文件：

```bash
ZSXQ_TOKEN=your-token
ZSXQ_GROUP_ID=your-group-id
```

## 运行测试

### 运行所有测试

```bash
npm test
```

### 运行单元测试

```bash
npm run test:unit
```

### 运行集成测试

```bash
npm run test:integration
```

### 运行特定模块的测试

```bash
# 只运行 Users 模块测试
npm test -- --testPathPattern="users"

# 只运行 Groups 模块测试
npm test -- --testPathPattern="groups"
```

### 生成测试覆盖率报告

```bash
npm run test:cov
```

覆盖率报告将生成在 `coverage/` 目录。

## 测试覆盖的场景

### Users 模块
- ✓ self() - 获取当前用户信息
- ✓ getStatistics() - 获取用户统计数据
- ✓ 异常处理（无效 token、无效 userId）
- ✓ 超时和重试机制

### Groups 模块
- ✓ list() - 获取星球列表
- ✓ get() - 获取星球详情
- ✓ getStatistics() - 获取星球统计
- ✓ 数据一致性验证
- ✓ 成员数量验证

### Topics 模块
- ✓ list() - 获取话题列表（基本查询和带参数查询）
- ✓ get() - 获取话题详情
- ✓ 参数验证（scope, count）
- ✓ 点赞数量验证

### Checkins 模块
- ✓ list() - 获取打卡项目列表
- ✓ get() - 获取打卡项目详情
- ✓ getStatistics() - 获取打卡统计
- ✓ getRankingList() - 获取打卡排行榜
- ✓ 参数验证（scope, type）
- ✓ 统计数据合理性验证

### Dashboard 模块
- ✓ getOverview() - 获取星球概览
- ✓ getIncomes() - 获取收入概览
- ✓ 权限验证（星主权限）

### 集成测试
- ✓ 客户端初始化和配置
- ✓ 跨模块数据一致性
- ✓ 完整的业务流程
- ✓ 错误处理和恢复
- ✓ 并发请求

## 测试要点

### 单元测试

单元测试关注单个模块的功能，验证：

1. **数据结构验证** - 确保返回的数据包含所有必要字段
2. **数据类型验证** - 确保字段类型正确
3. **参数验证** - 测试各种参数组合
4. **异常处理** - 测试错误场景
5. **边界条件** - 测试边界值和特殊情况

### 集成测试

集成测试关注多个模块的协作，验证：

1. **完整业务流程** - 模拟真实用户操作
2. **跨模块数据一致性** - 确保不同接口返回的数据一致
3. **错误恢复** - 确保错误后系统仍可用
4. **并发处理** - 测试并发请求的正确性

## 注意事项

1. **测试数据** - 测试使用真实 API，请确保使用测试账号
2. **权限要求** - Dashboard 模块需要星主权限，测试可能会失败
3. **网络依赖** - 测试需要网络连接到知识星球 API
4. **测试隔离** - 测试之间相互独立，可以单独运行
5. **幂等性** - 所有测试都是只读操作，不会修改数据

## 故障排查

### Token 无效

```
Error: ZSXQ_TOKEN environment variable is required
```

解决方案：设置正确的环境变量 `ZSXQ_TOKEN`

### 网络超时

```
Error: timeout of 10000ms exceeded
```

解决方案：
1. 检查网络连接
2. 增加超时时间配置
3. 检查 API 服务是否可用

### 权限错误

```
Error: 权限不足
```

解决方案：
1. 确保 token 有效
2. Dashboard 测试需要星主权限，可以跳过该测试

## 持续集成

可以将测试集成到 CI/CD 流程：

```yaml
# GitHub Actions 示例
- name: Run tests
  run: |
    cd typescript
    npm install
    npm test
  env:
    ZSXQ_TOKEN: ${{ secrets.ZSXQ_TOKEN }}
    ZSXQ_GROUP_ID: ${{ secrets.ZSXQ_GROUP_ID }}
```
