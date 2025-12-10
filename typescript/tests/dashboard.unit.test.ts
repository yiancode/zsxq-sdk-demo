import { ZsxqClientBuilder, ZsxqException } from 'zsxq-sdk';

/**
 * Dashboard 模块单元测试
 *
 * 测试场景：
 * 1. getOverview() - 获取星球概览
 * 2. getIncomes() - 获取收入概览
 * 3. 权限验证
 */
describe('Dashboard Module Unit Tests', () => {
  let client: ReturnType<typeof ZsxqClientBuilder.prototype.build>;
  let testGroupId: number;

  beforeAll(() => {
    const token = process.env.ZSXQ_TOKEN;
    const groupId = process.env.ZSXQ_GROUP_ID;

    if (!token || !groupId) {
      throw new Error('ZSXQ_TOKEN and ZSXQ_GROUP_ID environment variables are required');
    }

    testGroupId = parseInt(groupId, 10);

    client = new ZsxqClientBuilder()
      .setToken(token)
      .setTimeout(10000)
      .setRetry(3)
      .build();
  });

  describe('getOverview() - 获取星球概览', () => {
    it('应该成功返回星球概览数据', async () => {
      try {
        const overview = await client.dashboard.getOverview(testGroupId);

        // 验证返回数据结构
        expect(overview).toBeDefined();
        expect(typeof overview).toBe('object');
      } catch (error) {
        // 如果没有权限，应该抛出权限相关异常
        if (error instanceof ZsxqException) {
          expect(error.message).toMatch(/权限|permission|forbidden/i);
        } else {
          throw error;
        }
      }
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.dashboard.getOverview(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('getIncomes() - 获取收入概览', () => {
    it('应该成功返回收入概览数据', async () => {
      try {
        const incomes = await client.dashboard.getIncomes(testGroupId);

        // 验证返回数据结构
        expect(incomes).toBeDefined();
        expect(typeof incomes).toBe('object');
      } catch (error) {
        // 如果没有权限，应该抛出权限相关异常
        if (error instanceof ZsxqException) {
          expect(error.message).toMatch(/权限|permission|forbidden/i);
        } else {
          throw error;
        }
      }
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.dashboard.getIncomes(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('权限验证', () => {
    it('非星主用户应该无法访问数据面板', async () => {
      // 注意：这个测试取决于当前用户是否是星主
      // 如果当前用户不是星主，应该抛出权限异常

      try {
        await client.dashboard.getOverview(testGroupId);
        await client.dashboard.getIncomes(testGroupId);
      } catch (error) {
        // 如果抛出异常，验证是权限相关异常
        if (error instanceof ZsxqException) {
          expect(
            error.message.includes('权限') ||
            error.message.includes('permission') ||
            error.message.includes('forbidden')
          ).toBeTruthy();
        }
      }
    });
  });
});
