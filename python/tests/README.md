# Python SDK Demo 测试文档

## 测试结构

```
tests/
└── test_integration.py    # 集成测试（真实 API 调用）
```

## 环境准备

### 1. 安装依赖

```bash
cd python
pip install -e "../../zsxq-sdk/packages/python[dev]"
```

或者安装 pytest:

```bash
pip install pytest pytest-asyncio
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

## 运行测试

### 使用 unittest (推荐)

```bash
cd python
python -m unittest tests.test_integration
```

### 使用 pytest

```bash
cd python
pytest tests/test_integration.py -v
```

### 运行特定测试

```bash
# 运行单个测试方法
python -m unittest tests.test_integration.IntegrationTests.test_users_self

# 使用 pytest
pytest tests/test_integration.py::IntegrationTests::test_users_self -v
```

### 显示详细输出

```bash
python -m unittest tests.test_integration -v
```

## 测试覆盖的场景

### Users 模块
- ✓ self_() - 获取当前用户信息
- ✓ get_statistics() - 获取用户统计数据
- ✓ 异常处理（无效 token）

### Groups 模块
- ✓ list() - 获取星球列表
- ✓ get() - 获取星球详情
- ✓ get_statistics() - 获取星球统计
- ✓ 数据一致性验证（列表 vs 详情）
- ✓ 异常处理（无效 groupId）

### Topics 模块
- ✓ list() - 获取话题列表
- ✓ list() with options - 获取精华话题（带参数）
- ✓ get() - 获取话题详情

### Checkins 模块
- ✓ list() - 获取打卡项目列表
- ✓ get_statistics() - 获取打卡统计
- ✓ get_ranking_list() - 获取打卡排行榜
- ✓ 统计数据合理性验证

### Dashboard 模块
- ✓ get_overview() - 获取星球概览
- ✓ get_incomes() - 获取收入概览
- ⚠ 需要星主权限（测试会捕获权限异常）

### 集成测试
- ✓ 完整业务流程（从用户登录到浏览内容）
- ✓ 错误处理和恢复

## 测试特点

### unittest.IsolatedAsyncioTestCase

Python 3.8+ 提供的异步测试基类，支持：

- `asyncSetUp()` - 每个测试前初始化
- `asyncTearDown()` - 每个测试后清理
- `async def test_xxx()` - 异步测试方法
- `self.skipTest()` - 条件跳过测试

### 条件跳过

对于可能没有数据的场景：

```python
if len(checkins) == 0:
    self.skipTest("该星球没有打卡项目")
```

## 注意事项

1. **Python 版本** - 需要 Python 3.8+（支持 `IsolatedAsyncioTestCase`）
2. **环境变量** - 必须设置 `ZSXQ_TOKEN` 和 `ZSXQ_GROUP_ID`
3. **真实 API** - 测试使用真实 API，请使用测试账号
4. **权限要求** - Dashboard 模块需要星主权限
5. **测试隔离** - 每个测试都会创建新的客户端实例
6. **网络依赖** - 测试需要网络连接到知识星球 API

## 故障排查

### 环境变量未设置

```
ValueError: 请设置环境变量 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID
```

**解决方案:** 设置环境变量后重新运行

### Python 版本过低

```
AttributeError: module 'unittest' has no attribute 'IsolatedAsyncioTestCase'
```

**解决方案:** 升级到 Python 3.8+

### SDK 未安装

```
ModuleNotFoundError: No module named 'zsxq'
```

**解决方案:**

```bash
pip install -e "../../zsxq-sdk/packages/python"
```

### 网络超时

```
asyncio.TimeoutError
```

**解决方案:**
1. 检查网络连接
2. 检查知识星球 API 是否可用
3. 增加超时时间（修改测试代码中的 `.set_timeout()` 参数）

## 使用 pytest (可选)

如果使用 pytest 运行测试：

### 安装 pytest

```bash
pip install pytest pytest-asyncio
```

### 配置 pytest

创建 `pytest.ini`:

```ini
[pytest]
testpaths = tests
python_files = test_*.py
python_classes = Test* *Tests
python_functions = test_*
asyncio_mode = auto
```

### 运行测试

```bash
# 运行所有测试
pytest tests/ -v

# 运行特定测试
pytest tests/test_integration.py::IntegrationTests::test_users_self -v

# 显示打印输出
pytest tests/ -v -s

# 生成覆盖率报告
pytest tests/ --cov=. --cov-report=html
```

## 持续集成

### GitHub Actions 示例

```yaml
name: Python Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        python-version: ['3.8', '3.9', '3.10', '3.11']

    steps:
      - uses: actions/checkout@v3
      - name: Set up Python ${{ matrix.python-version }}
        uses: actions/setup-python@v4
        with:
          python-version: ${{ matrix.python-version }}

      - name: Install dependencies
        run: |
          cd zsxq-sdk/packages/python
          pip install -e ".[dev]"

      - name: Run tests
        run: |
          cd zsxq-sdk-demo/python
          python -m unittest tests.test_integration -v
        env:
          ZSXQ_TOKEN: ${{ secrets.ZSXQ_TOKEN }}
          ZSXQ_GROUP_ID: ${{ secrets.ZSXQ_GROUP_ID }}
```

## 测试输出示例

```
test_checkins_get_ranking_list (__main__.IntegrationTests) ... ✓ 获取打卡排行榜成功: 50 个用户
ok
test_checkins_get_statistics (__main__.IntegrationTests) ... ✓ 打卡统计: 参与=100, 完成=80
ok
test_checkins_list (__main__.IntegrationTests) ... ✓ 获取打卡项目列表成功: 2 个项目
ok
test_complete_workflow (__main__.IntegrationTests) ... ✓ 完整业务流程测试通过
ok
test_dashboard_get_incomes (__main__.IntegrationTests) ... ⚠ Dashboard 需要星主权限: 权限不足
ok
test_dashboard_get_overview (__main__.IntegrationTests) ... ⚠ Dashboard 需要星主权限: 权限不足
ok
test_error_recovery (__main__.IntegrationTests) ... ✓ 错误恢复测试通过
ok
test_groups_data_consistency (__main__.IntegrationTests) ... ✓ 星球列表和详情数据一致
ok
test_groups_get (__main__.IntegrationTests) ... ✓ 星球详情: 测试星球 (成员数: 500)
ok
test_groups_get_statistics (__main__.IntegrationTests) ... ✓ 星球统计数据: {...}
ok
test_groups_invalid_id (__main__.IntegrationTests) ... ✓ 无效 groupId 正确抛出异常
ok
test_groups_list (__main__.IntegrationTests) ... ✓ 获取星球列表成功: 3 个星球
ok
test_topics_get (__main__.IntegrationTests) ... ✓ 获取话题详情成功: ID=123456
ok
test_topics_list (__main__.IntegrationTests) ... ✓ 获取话题列表成功: 20 个话题
ok
test_topics_list_with_options (__main__.IntegrationTests) ... ✓ 获取精华话题成功: 5 个话题
ok
test_users_get_statistics (__main__.IntegrationTests) ... ✓ 用户统计数据: {...}
ok
test_users_invalid_token (__main__.IntegrationTests) ... ✓ 无效 token 正确抛出异常
ok
test_users_self (__main__.IntegrationTests) ... ✓ 当前用户: 测试用户 (ID: 123456)
ok

----------------------------------------------------------------------
Ran 18 tests in 12.345s

OK
```

## 扩展测试

添加更多测试的建议：

1. 创建单独的测试文件（如 `test_users.py`, `test_groups.py`）
2. 使用 Mock 对象进行单元测试
3. 添加性能测试
4. 添加错误场景测试

示例：

```python
class UsersModuleTests(unittest.IsolatedAsyncioTestCase):
    """Users 模块专项测试"""

    async def test_self_returns_valid_user(self):
        """self_() 应该返回有效的用户信息"""
        # 测试代码
```
