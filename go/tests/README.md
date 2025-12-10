# Go SDK Demo 测试文档

## 测试结构

```
go/
└── integration_test.go    # 集成测试（真实 API 调用）
```

## 环境准备

### 1. 安装依赖

```bash
cd go
go mod tidy
```

### 2. 配置环境变量

**Linux/macOS:**

```bash
export ZSXQ_TOKEN="your-token"
export ZSXQ_GROUP_ID="your-group-id"
```

**Windows PowerShell:**

```powershell
$env:ZSXQ_TOKEN="your-token"
$env:ZSXQ_GROUP_ID="your-group-id"
```

**Windows CMD:**

```cmd
set ZSXQ_TOKEN=your-token
set ZSXQ_GROUP_ID=your-group-id
```

## 运行测试

### 运行所有测试

```bash
cd go
go test -v
```

### 运行特定测试

```bash
# 运行单个测试函数
go test -v -run TestUsersSelf

# 运行匹配模式的测试
go test -v -run "TestUsers.*"
go test -v -run "TestGroups.*"
```

### 显示详细输出

```bash
go test -v
```

### 运行测试并生成覆盖率报告

```bash
go test -v -cover
go test -v -coverprofile=coverage.out
go tool cover -html=coverage.out -o coverage.html
```

### 运行基准测试

```bash
go test -v -bench=.
```

## 测试覆盖的场景

### Users 模块
- ✓ Self() - 获取当前用户信息
- ✓ GetStatistics() - 获取用户统计数据
- ✓ 异常处理（无效 token）

### Groups 模块
- ✓ List() - 获取星球列表
- ✓ Get() - 获取星球详情
- ✓ GetStatistics() - 获取星球统计
- ✓ 数据一致性验证（列表 vs 详情）
- ✓ 异常处理（无效 groupId）

### Topics 模块
- ✓ List() - 获取话题列表
- ✓ List() with options - 获取精华话题（带参数）
- ✓ Get() - 获取话题详情

### Checkins 模块
- ✓ List() - 获取打卡项目列表
- ✓ GetStatistics() - 获取打卡统计
- ✓ GetRankingList() - 获取打卡排行榜
- ✓ 统计数据合理性验证

### Dashboard 模块
- ✓ GetOverview() - 获取星球概览
- ✓ GetIncomes() - 获取收入概览
- ⚠ 需要星主权限（测试会记录警告）

### 集成测试
- ✓ 完整业务流程（从用户登录到浏览内容）
- ✓ 错误处理和恢复

## 测试特点

### Go Testing 包

- `TestMain(m *testing.M)` - 测试入口，初始化测试环境
- `t.Fatalf()` - 致命错误，停止当前测试
- `t.Errorf()` - 非致命错误，记录错误但继续执行
- `t.Logf()` - 输出日志（需要 `-v` 参数才显示）
- `t.Skip()` - 跳过测试

### 条件跳过

对于可能没有数据的场景：

```go
if len(checkins) == 0 {
    t.Skip("该星球没有打卡项目")
}
```

### 错误处理

Dashboard 模块测试不会失败，只会记录警告：

```go
if err != nil {
    t.Logf("⚠ Dashboard 需要星主权限: %v", err)
    return
}
```

## 注意事项

1. **Go 版本** - 需要 Go 1.21+
2. **环境变量** - 必须设置 `ZSXQ_TOKEN` 和 `ZSXQ_GROUP_ID`
3. **真实 API** - 测试使用真实 API，请使用测试账号
4. **权限要求** - Dashboard 模块需要星主权限
5. **Context** - 所有测试使用共享的 `context.Background()`
6. **网络依赖** - 测试需要网络连接到知识星球 API

## 故障排查

### 环境变量未设置

```
panic: 请设置环境变量 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID
```

**解决方案:** 设置环境变量后重新运行

### Go 模块依赖问题

```
go: module github.com/zsxq-sdk/zsxq-sdk-go: no matching versions for query ...
```

**解决方案:**

```bash
go mod tidy
```

### SDK 未构建

确保 SDK 代码存在于正确位置（通过 `go.mod` 的 `replace` 指令引用）

### 网络超时

```
context deadline exceeded
```

**解决方案:**
1. 检查网络连接
2. 检查知识星球 API 是否可用
3. 增加超时时间（修改 `TestMain` 中的 `.SetTimeout()` 参数）

### 权限错误

```
⚠ Dashboard 需要星主权限: ...
```

这是正常的警告信息，Dashboard 测试需要星主权限。

## 并发测试

Go 支持并发测试，使用 `-parallel` 参数：

```bash
# 并发运行测试（默认 GOMAXPROCS）
go test -v -parallel 4

# 不并发运行
go test -v -parallel 1
```

**注意:** 由于测试使用真实 API，不建议高并发测试。

## 性能基准测试

添加基准测试（可选）：

```go
func BenchmarkGroupsList(b *testing.B) {
    for i := 0; i < b.N; i++ {
        _, _ = testClient.Groups().List(ctx)
    }
}

func BenchmarkTopicsList(b *testing.B) {
    for i := 0; i < b.N; i++ {
        _, _ = testClient.Topics().List(ctx, testGroupID, nil)
    }
}
```

运行基准测试：

```bash
go test -v -bench=. -benchmem
```

## 与 IDE 集成

### VS Code

1. 安装 Go 扩展
2. 点击测试函数上方的 `run test` 或 `debug test`
3. 在 `.vscode/settings.json` 中配置环境变量：

```json
{
  "go.testEnvVars": {
    "ZSXQ_TOKEN": "your-token",
    "ZSXQ_GROUP_ID": "your-group-id"
  }
}
```

### GoLand

1. 右键点击测试文件或测试函数 → Run 'go test ...'
2. Run → Edit Configurations → Environment
3. 添加环境变量 `ZSXQ_TOKEN` 和 `ZSXQ_GROUP_ID`

## 持续集成

### GitHub Actions 示例

```yaml
name: Go Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        go-version: ['1.21', '1.22']

    steps:
      - uses: actions/checkout@v3
      - name: Set up Go ${{ matrix.go-version }}
        uses: actions/setup-go@v4
        with:
          go-version: ${{ matrix.go-version }}

      - name: Install dependencies
        run: |
          cd zsxq-sdk-demo/go
          go mod tidy

      - name: Run tests
        run: |
          cd zsxq-sdk-demo/go
          go test -v -cover
        env:
          ZSXQ_TOKEN: ${{ secrets.ZSXQ_TOKEN }}
          ZSXQ_GROUP_ID: ${{ secrets.ZSXQ_GROUP_ID }}
```

## 测试输出示例

```
=== RUN   TestUsersSelf
    integration_test.go:60: ✓ 当前用户: 测试用户 (ID: 123456)
--- PASS: TestUsersSelf (0.50s)
=== RUN   TestUsersGetStatistics
    integration_test.go:76: ✓ 用户统计数据: map[followers:100 following:50]
--- PASS: TestUsersGetStatistics (0.45s)
=== RUN   TestUsersInvalidToken
    integration_test.go:86: ✓ 无效 token 正确返回错误
--- PASS: TestUsersInvalidToken (0.20s)
=== RUN   TestGroupsList
    integration_test.go:112: ✓ 获取星球列表成功: 3 个星球
--- PASS: TestGroupsList (0.60s)
=== RUN   TestGroupsGet
    integration_test.go:135: ✓ 星球详情: 测试星球 (成员数: 500)
--- PASS: TestGroupsGet (0.55s)
=== RUN   TestGroupsGetStatistics
    integration_test.go:146: ✓ 星球统计数据: map[members:500 topics:1000]
--- PASS: TestGroupsGetStatistics (0.50s)
=== RUN   TestGroupsDataConsistency
    integration_test.go:178: ✓ 星球列表和详情数据一致
--- PASS: TestGroupsDataConsistency (1.10s)
=== RUN   TestGroupsInvalidID
    integration_test.go:186: ✓ 无效 groupId 正确返回错误
--- PASS: TestGroupsInvalidID (0.30s)
=== RUN   TestTopicsList
    integration_test.go:207: ✓ 获取话题列表成功: 20 个话题
--- PASS: TestTopicsList (0.65s)
=== RUN   TestTopicsListWithOptions
    integration_test.go:223: ✓ 获取精华话题成功: 5 个话题
--- PASS: TestTopicsListWithOptions (0.60s)
=== RUN   TestTopicsGet
    integration_test.go:246: ✓ 获取话题详情成功: ID=123456
--- PASS: TestTopicsGet (0.85s)
=== RUN   TestCheckinsList
    integration_test.go:268: ✓ 获取打卡项目列表成功: 2 个项目
--- PASS: TestCheckinsList (0.55s)
=== RUN   TestCheckinsGetStatistics
    integration_test.go:300: ✓ 打卡统计: 参与=100, 完成=80
--- PASS: TestCheckinsGetStatistics (0.70s)
=== RUN   TestCheckinsGetRankingList
    integration_test.go:320: ✓ 获取打卡排行榜成功: 50 个用户
--- PASS: TestCheckinsGetRankingList (0.65s)
=== RUN   TestDashboardGetOverview
    integration_test.go:335: ⚠ Dashboard 需要星主权限: permission denied
--- PASS: TestDashboardGetOverview (0.25s)
=== RUN   TestDashboardGetIncomes
    integration_test.go:348: ⚠ Dashboard 需要星主权限: permission denied
--- PASS: TestDashboardGetIncomes (0.25s)
=== RUN   TestCompleteWorkflow
    integration_test.go:390: ✓ 完整业务流程测试通过
--- PASS: TestCompleteWorkflow (2.50s)
=== RUN   TestErrorRecovery
    integration_test.go:408: ✓ 错误恢复测试通过
--- PASS: TestErrorRecovery (0.85s)
PASS
coverage: 85.5% of statements
ok      github.com/zsxq-sdk/zsxq-sdk-demo      15.320s
```

## 测试最佳实践

1. **使用表驱动测试** - 对于类似的测试场景，使用表驱动测试减少重复代码

```go
func TestGroupsValidation(t *testing.T) {
    tests := []struct {
        name    string
        groupID int64
        wantErr bool
    }{
        {"valid group", testGroupID, false},
        {"invalid group", 999999999999999, true},
    }

    for _, tt := range tests {
        t.Run(tt.name, func(t *testing.T) {
            _, err := testClient.Groups().Get(ctx, tt.groupID)
            if (err != nil) != tt.wantErr {
                t.Errorf("wantErr = %v, got err = %v", tt.wantErr, err)
            }
        })
    }
}
```

2. **使用子测试** - 组织相关测试

```go
func TestGroups(t *testing.T) {
    t.Run("List", func(t *testing.T) { /* test code */ })
    t.Run("Get", func(t *testing.T) { /* test code */ })
    t.Run("GetStatistics", func(t *testing.T) { /* test code */ })
}
```

3. **使用 t.Cleanup** - 清理测试资源

```go
func TestWithCleanup(t *testing.T) {
    resource := setup()
    t.Cleanup(func() {
        cleanup(resource)
    })
    // test code
}
```

## 扩展测试

添加更多测试的建议：

1. 创建单独的测试文件（如 `users_test.go`, `groups_test.go`）
2. 添加单元测试（使用 Mock）
3. 添加性能基准测试
4. 添加模糊测试（Go 1.18+）

示例：

```go
// users_test.go
func TestUsersModule(t *testing.T) {
    t.Run("Self", testUsersSelf)
    t.Run("GetStatistics", testUsersGetStatistics)
    // more tests...
}
```
