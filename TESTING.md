# zsxq-sdk-demo 测试文档

知识星球 SDK 多语言示例项目的集成测试套件。

## 概述

本项目为所有语言版本的 SDK Demo 提供了全面的集成测试，覆盖以下模块：

- **Users** - 用户管理
- **Groups** - 星球管理
- **Topics** - 话题管理
- **Checkins** - 打卡管理
- **Dashboard** - 数据面板

## 测试结构

```
zsxq-sdk-demo/
├── typescript/
│   ├── tests/
│   │   ├── users.unit.test.ts
│   │   ├── groups.unit.test.ts
│   │   ├── topics.unit.test.ts
│   │   ├── checkins.unit.test.ts
│   │   ├── dashboard.unit.test.ts
│   │   ├── integration.test.ts
│   │   └── README.md
│   └── package.json
├── java/
│   ├── src/test/java/com/zsxq/demo/
│   │   └── IntegrationTest.java
│   ├── tests/
│   │   └── README.md
│   └── pom.xml
├── python/
│   ├── tests/
│   │   ├── test_integration.py
│   │   └── README.md
│   └── requirements.txt
├── go/
│   ├── integration_test.go
│   ├── tests/
│   │   └── README.md
│   └── go.mod
└── README.md (本文件)
```

## 快速开始

### 1. 环境变量配置

所有语言版本的测试都需要设置以下环境变量：

**Linux/macOS:**

```bash
export ZSXQ_TOKEN="your-token-here"
export ZSXQ_GROUP_ID="your-group-id-here"
```

**Windows PowerShell:**

```powershell
$env:ZSXQ_TOKEN="your-token-here"
$env:ZSXQ_GROUP_ID="your-group-id-here"
```

**Windows CMD:**

```cmd
set ZSXQ_TOKEN=your-token-here
set ZSXQ_GROUP_ID=your-group-id-here
```

### 2. 运行测试

每个语言版本的详细测试说明请参考对应目录下的 README：

#### TypeScript

```bash
cd typescript
npm install
npm test
```

详细文档：[typescript/tests/README.md](typescript/tests/README.md)

#### Java

```bash
cd java
mvn test
```

详细文档：[java/tests/README.md](java/tests/README.md)

#### Python

```bash
cd python
python -m unittest tests.test_integration -v
```

详细文档：[python/tests/README.md](python/tests/README.md)

#### Go

```bash
cd go
go test -v
```

详细文档：[go/tests/README.md](go/tests/README.md)

## 测试覆盖范围

### Users 模块

| 功能 | TypeScript | Java | Python | Go |
|------|-----------|------|--------|-----|
| self() | ✓ | ✓ | ✓ | ✓ |
| getStatistics() | ✓ | ✓ | ✓ | ✓ |
| 异常处理 | ✓ | ✓ | ✓ | ✓ |

### Groups 模块

| 功能 | TypeScript | Java | Python | Go |
|------|-----------|------|--------|-----|
| list() | ✓ | ✓ | ✓ | ✓ |
| get() | ✓ | ✓ | ✓ | ✓ |
| getStatistics() | ✓ | ✓ | ✓ | ✓ |
| 数据一致性 | ✓ | ✓ | ✓ | ✓ |
| 异常处理 | ✓ | ✓ | ✓ | ✓ |

### Topics 模块

| 功能 | TypeScript | Java | Python | Go |
|------|-----------|------|--------|-----|
| list() | ✓ | ✓ | ✓ | ✓ |
| list() with options | ✓ | ✓ | ✓ | ✓ |
| get() | ✓ | ✓ | ✓ | ✓ |

### Checkins 模块

| 功能 | TypeScript | Java | Python | Go |
|------|-----------|------|--------|-----|
| list() | ✓ | ✓ | ✓ | ✓ |
| get() | ✓ | ✓ | - | - |
| getStatistics() | ✓ | ✓ | ✓ | ✓ |
| getRankingList() | ✓ | ✓ | ✓ | ✓ |

### Dashboard 模块

| 功能 | TypeScript | Java | Python | Go |
|------|-----------|------|--------|-----|
| getOverview() | ✓ | ✓ | ✓ | ✓ |
| getIncomes() | ✓ | ✓ | ✓ | ✓ |

### 集成测试

| 场景 | TypeScript | Java | Python | Go |
|------|-----------|------|--------|-----|
| 完整业务流程 | ✓ | ✓ | ✓ | ✓ |
| 错误处理与恢复 | ✓ | ✓ | ✓ | ✓ |
| 并发请求 | ✓ | - | - | - |

## 测试框架

| 语言 | 测试框架 | 断言库 | 其他依赖 |
|------|----------|--------|----------|
| TypeScript | Jest | Jest | ts-jest |
| Java | JUnit 5 | JUnit 5 Assertions | - |
| Python | unittest | unittest | - |
| Go | testing | testing | - |

## 注意事项

### 通用注意事项

1. **环境变量** - 所有测试都需要设置 `ZSXQ_TOKEN` 和 `ZSXQ_GROUP_ID`
2. **真实 API** - 测试使用真实的知识星球 API，建议使用测试账号
3. **权限要求** - Dashboard 模块需要星主权限，非星主用户会收到权限错误
4. **网络依赖** - 测试需要稳定的网络连接
5. **测试隔离** - 所有测试都是只读操作，不会修改数据
6. **条件跳过** - 对于没有数据的场景（如打卡项目），测试会自动跳过

### 语言特定注意事项

#### TypeScript
- 需要 Node.js 18+
- npm 缓存可能需要清理权限
- 测试文件命名约定：`*.test.ts`

#### Java
- 需要 JDK 11+
- SDK 需要先构建：`cd ../../zsxq-sdk/packages/java && mvn clean package`
- 测试输出在 `target/surefire-reports/`

#### Python
- 需要 Python 3.8+（支持 `IsolatedAsyncioTestCase`）
- 需要安装 SDK：`pip install -e "../../zsxq-sdk/packages/python"`
- 可选使用 pytest：`pip install pytest pytest-asyncio`

#### Go
- 需要 Go 1.21+
- 使用 `go mod replace` 引用本地 SDK
- 测试文件必须以 `_test.go` 结尾

## 持续集成

### GitHub Actions 示例

```yaml
name: Multi-Language Tests

on: [push, pull_request]

jobs:
  test-typescript:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
      - run: cd typescript && npm install && npm test
        env:
          ZSXQ_TOKEN: ${{ secrets.ZSXQ_TOKEN }}
          ZSXQ_GROUP_ID: ${{ secrets.ZSXQ_GROUP_ID }}

  test-java:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'
      - run: cd ../../zsxq-sdk/packages/java && mvn clean package -DskipTests
      - run: cd java && mvn test
        env:
          ZSXQ_TOKEN: ${{ secrets.ZSXQ_TOKEN }}
          ZSXQ_GROUP_ID: ${{ secrets.ZSXQ_GROUP_ID }}

  test-python:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-python@v4
        with:
          python-version: '3.8'
      - run: pip install -e "../../zsxq-sdk/packages/python"
      - run: cd python && python -m unittest tests.test_integration -v
        env:
          ZSXQ_TOKEN: ${{ secrets.ZSXQ_TOKEN }}
          ZSXQ_GROUP_ID: ${{ secrets.ZSXQ_GROUP_ID }}

  test-go:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-go@v4
        with:
          go-version: '1.21'
      - run: cd go && go mod tidy && go test -v
        env:
          ZSXQ_TOKEN: ${{ secrets.ZSXQ_TOKEN }}
          ZSXQ_GROUP_ID: ${{ secrets.ZSXQ_GROUP_ID }}
```

## 故障排查

### 环境变量未设置

**现象:**
- TypeScript: `请设置环境变量`
- Java: `IllegalStateException: 环境变量未设置`
- Python: `ValueError: 请设置环境变量`
- Go: `panic: 请设置环境变量`

**解决方案:** 确保正确设置了 `ZSXQ_TOKEN` 和 `ZSXQ_GROUP_ID`

### 网络超时

**现象:**
- TypeScript: `timeout of 10000ms exceeded`
- Java: `SocketTimeoutException`
- Python: `asyncio.TimeoutError`
- Go: `context deadline exceeded`

**解决方案:**
1. 检查网络连接
2. 检查知识星球 API 是否可用
3. 增加超时时间配置

### 权限错误

**现象:**
- Dashboard 测试报错：`权限不足` / `permission denied` / `forbidden`

**说明:** Dashboard 模块需要星主权限，这是正常现象。测试会捕获这些错误并记录警告，不会导致测试失败。

### SDK 依赖问题

**TypeScript:**
```bash
cd ../../zsxq-sdk/packages/typescript
npm install
npm run build
```

**Java:**
```bash
cd ../../zsxq-sdk/packages/java
mvn clean package
```

**Python:**
```bash
pip install -e "../../zsxq-sdk/packages/python"
```

**Go:**
```bash
go mod tidy
```

## 性能指标

基于真实 API 测试的平均响应时间（仅供参考）：

| 操作 | 平均响应时间 |
|------|--------------|
| users.self() | ~500ms |
| groups.list() | ~600ms |
| groups.get() | ~550ms |
| topics.list() | ~650ms |
| topics.get() | ~850ms |
| checkins.list() | ~550ms |
| checkins.getStatistics() | ~700ms |

注：实际响应时间取决于网络状况和 API 服务器负载。

## 贡献指南

### 添加新测试

1. 参考现有测试代码的结构和风格
2. 确保测试覆盖正常场景和异常场景
3. 添加适当的断言和日志输出
4. 更新对应的 README 文档

### 测试命名规范

- **TypeScript**: `describe('Module Name', () => { it('should ...', () => {}) })`
- **Java**: `@DisplayName("模块名: 功能描述")` + `void testXxx()`
- **Python**: `async def test_module_function(self)`
- **Go**: `func TestModuleFunction(t *testing.T)`

### 测试代码风格

- 使用清晰的测试名称描述测试意图
- 添加必要的注释说明测试场景
- 保持测试独立性，不依赖其他测试的执行结果
- 使用合适的断言，提供清晰的错误信息

## 许可证

MIT License - 与主项目相同

## 联系方式

如有问题或建议，请提交 Issue 或 Pull Request。
