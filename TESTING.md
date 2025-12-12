# 测试文档

本文档提供 zsxq-sdk-demo 多语言测试的完整指南。

## 📋 测试概览

本项目为 TypeScript、Java、Python、Go 四种语言实现提供了完整的测试覆盖。测试分为两类：

- **单元测试 (Unit Tests)**: 使用 Mock 快速验证各模块功能，不依赖真实 API
- **集成测试 (Integration Tests)**: 使用真实 API 调用，验证端到端功能

## 📊 测试矩阵

| 语言 | 单元测试 | 集成测试 | 测试框架 | 状态 |
|------|---------|---------|---------|------|
| **TypeScript** | 70 个 | 7 个 | Jest | ✅ 完成 |
| **Java** | 64 个 | 21 个 | TestNG + Mockito / JUnit 5 + Spring Boot Test | ✅ 完成 |
| **Python** | 76 个 | 5 个 | pytest + AsyncMock | ✅ 完成 |
| **Go** | 待修复 | 7 个 | testing + testify | ⚠️ 部分完成 |
| **总计** | **210+** | **40+** | - | **250+ 测试** |

## 🚀 快速开始

### TypeScript

```bash
cd typescript
npm install
npm test                    # 所有测试
npm run test:unit          # 仅单元测试
npm run test:integration   # 仅集成测试
npm run test:cov           # 测试覆盖率
```

**单元测试模块**:
- users.unit.test.ts (10 个测试)
- groups.unit.test.ts (10 个测试)
- topics.unit.test.ts (10 个测试)
- checkins.unit.test.ts (10 个测试)
- dashboard.unit.test.ts (10 个测试)
- ranking.unit.test.ts (17 个测试)
- misc.unit.test.ts (15 个测试)

### Java

```bash
cd java
mvn clean compile          # 编译项目
mvn test                   # 运行所有测试
mvn test -Dtest=ZsxqApiIntegrationTest  # 运行集成测试
```

**测试结构**:
- **单元测试**: `src/test/java/com/zsxq/demo/service/` (7 个模块，64 个测试)
- **集成测试**: `src/test/java/com/zsxq/demo/integration/` (21 个测试)

### Python

```bash
cd python
pip install -r requirements.txt
pytest tests/ -v                    # 所有测试
pytest tests/ -v -k unit           # 仅单元测试
pytest tests/test_integration.py   # 仅集成测试
pytest --cov=. tests/              # 测试覆盖率
```

**单元测试模块**:
- test_users_unit.py (13 个测试)
- test_groups_unit.py (11 个测试)
- test_topics_unit.py (11 个测试)
- test_checkins_unit.py (11 个测试)
- test_dashboard_unit.py (10 个测试)
- test_ranking_unit.py (10 个测试)
- test_misc_unit.py (10 个测试)

### Go

```bash
cd go
go mod download
go test ./... -v              # 所有测试
go test -v -run Integration   # 集成测试
go test -v -run Unit          # 单元测试（待修复）
go test -cover ./...          # 测试覆盖率
```

**注意**: Go 单元测试存在编译错误，需要手动修复 `.Build()` vs `.MustBuild()` 问题。

## 🔧 环境配置

### 集成测试环境变量

集成测试需要配置以下环境变量：

```bash
# 必需
export ZSXQ_TOKEN="your-authorization-token"
export ZSXQ_GROUP_ID="your-group-id"

# 可选（用于特定测试）
export ZSXQ_TOPIC_ID="topic-id"
export ZSXQ_CHECKIN_ID="checkin-id"
```

### 获取 Token

1. 访问 https://wx.zsxq.com
2. 打开浏览器开发者工具 (F12)
3. 切换到 Network 标签
4. 筛选 api.zsxq.com 请求
5. 查看请求头中的 `authorization` 字段
6. 复制该值作为 `ZSXQ_TOKEN`

## 📝 测试规范

### 命名约定

#### TypeScript (Jest)
```typescript
describe('Module Name', () => {
  it('should do something', () => {
    // 测试代码
  });
});
```

#### Java (TestNG)
```java
@Test
@DisplayName("描述测试场景")
public void testMethodName() {
    // 测试代码
}
```

#### Python (pytest)
```python
def test_method_name_scenario():
    """测试描述"""
    # 测试代码
```

#### Go (testing)
```go
func TestModuleScenario(t *testing.T) {
    // 测试代码
}
```

### 测试模式

#### 单元测试 (Mock)

**TypeScript (Jest)**:
```typescript
jest.mock('../src/services/zsxq');
// 测试使用 mock 数据
```

**Java (Mockito)**:
```java
@Mock
private ZsxqClient mockClient;

when(mockClient.users().self()).thenReturn(mockUser);
```

**Python (AsyncMock)**:
```python
with patch.object(client.users, 'self_', new_callable=AsyncMock) as mock_self:
    mock_self.return_value = mock_user
    result = await client.users.self_()
```

**Go (testify)**:
```go
// 主要测试客户端配置和上下文处理
```

#### 集成测试 (真实 API)

所有语言的集成测试都使用真实 API 调用：
- 需要配置有效的 Token 和 Group ID
- 测试会调用真实的知识星球 API
- 请使用测试账号避免影响生产数据

## 📈 测试覆盖范围

### 核心模块

所有语言均覆盖以下 7 个核心模块：

1. **Users** - 用户信息、统计、足迹
2. **Groups** - 星球列表、详情、统计、成员
3. **Topics** - 话题列表、详情、评论、打赏
4. **Checkins** - 打卡列表、详情、统计、排行
5. **Dashboard** - 数据面板、收入、成员增长
6. **Ranking** - 各类排行榜（星球、积分、邀请等）
7. **Misc** - 全局配置、活动、PK 群组

### 测试场景

每个模块包含以下测试场景：

- ✅ **主要功能测试** - 验证核心 API 功能
- ✅ **参数验证测试** - 测试各种参数组合
- ✅ **异常处理测试** - 验证错误场景处理
- ✅ **边界条件测试** - 测试边界值和特殊情况
- ✅ **数据一致性测试** - 验证跨模块数据一致性（集成测试）

## 🎯 测试覆盖率目标

- **单元测试**: > 80%
- **总体覆盖率**: > 75%
- **关键路径**: 100%

## ⚠️ 注意事项

### 单元测试
1. **不需要环境变量** - 使用 Mock 数据
2. **快速执行** - 所有测试在几秒内完成
3. **隔离运行** - 测试之间互不影响

### 集成测试
1. **需要有效 Token** - 必须配置环境变量
2. **使用测试账号** - 避免影响生产数据
3. **注意频率限制** - API 可能有调用频率限制
4. **网络依赖** - 需要稳定的网络连接
5. **权限要求** - 某些功能需要特定权限（如 Dashboard）

## 🐛 故障排查

### TypeScript

**问题**: `npm test` 失败
```bash
# 清理并重新安装
rm -rf node_modules package-lock.json
npm install
npm test
```

**问题**: Cache 权限错误
```bash
sudo chown -R $(id -u):$(id -g) "$HOME/.npm"
```

### Java

**问题**: 测试编译失败
```bash
mvn clean compile
mvn test-compile
```

**问题**: Surefire 配置冲突
- 单元测试使用 TestNG
- 集成测试使用 JUnit 5
- 确保 pom.xml 中 Surefire 配置正确

### Python

**问题**: 模块导入错误
```bash
# 确保 SDK 路径正确
export PYTHONPATH="${PYTHONPATH}:../../zsxq-sdk/packages/python"
```

**问题**: pytest 未安装
```bash
pip install -r requirements.txt
```

### Go

**问题**: 单元测试编译错误
```bash
# 当前已知问题：需要手动修复 .Build() 为 .MustBuild()
sed -i 's/\.Build()/\.MustBuild()/g' *_unit_test.go
```

**问题**: 依赖下载失败
```bash
go mod tidy
go mod download
```

## 📚 详细文档

各语言详细测试文档：

- [TypeScript 测试文档](typescript/tests/README.md)
- [Java 测试文档](java/README.md)
- [Python 测试文档](python/tests/README.md)
- [Go 测试文档](go/tests/README.md)

## 🤝 贡献指南

### 添加新测试

1. **单元测试**：
   - 遵循现有命名规范
   - 使用 Mock 不依赖 API
   - 添加清晰的测试描述
   - 确保测试快速执行

2. **集成测试**：
   - 使用真实 API 调用
   - 添加适当的错误处理
   - 考虑测试数据清理
   - 验证跨模块数据一致性

### 测试检查清单

- [ ] 测试命名清晰描述场景
- [ ] 包含正常和异常场景
- [ ] 添加测试文档字符串
- [ ] 测试通过本地验证
- [ ] 代码覆盖率达标
- [ ] 更新相关文档

## 📄 许可证

本项目测试代码遵循项目主许可证。

## 🔗 相关链接

- [zsxq-sdk](https://github.com/yiancode/zsxq-sdk) - 知识星球 SDK
- [知识星球 API 文档](https://wx.zsxq.com/docs)
- [项目主页](https://github.com/yiancode/zsxq-sdk-demo)

---

**最后更新**: 2024-12-12
**维护者**: Claude Code Assistant
