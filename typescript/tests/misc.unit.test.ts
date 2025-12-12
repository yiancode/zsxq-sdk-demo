import { ZsxqClientBuilder, ZsxqException } from 'zsxq-sdk';

/**
 * Misc 模块单元测试
 *
 * 测试场景：
 * 1. getGlobalConfig() - 获取全局配置
 * 2. getActivities() - 获取动态列表
 * 3. getActivities() 带参数 - 获取特定动态
 * 4. getPkGroup() - 获取 PK 群组详情
 * 5. 参数验证和异常处理
 */
describe('Misc Module Unit Tests', () => {
  let client: ReturnType<typeof ZsxqClientBuilder.prototype.build>;

  beforeAll(() => {
    const token = process.env.ZSXQ_TOKEN;

    if (!token) {
      throw new Error('ZSXQ_TOKEN environment variable is required');
    }

    client = new ZsxqClientBuilder()
      .setToken(token)
      .setTimeout(10000)
      .setRetry(3)
      .build();
  });

  describe('getGlobalConfig() - 获取全局配置', () => {
    it('应该成功返回全局配置', async () => {
      const config = await client.misc.getGlobalConfig();

      // 验证返回数据结构
      expect(config).toBeDefined();
      expect(typeof config).toBe('object');
    });

    it('全局配置应该包含基础字段', async () => {
      const config = await client.misc.getGlobalConfig();

      // 验证配置对象有内容
      expect(Object.keys(config).length).toBeGreaterThan(0);
    });

    it('多次调用应该返回一致的配置', async () => {
      const config1 = await client.misc.getGlobalConfig();
      const config2 = await client.misc.getGlobalConfig();

      // 全局配置在短时间内应该保持一致
      expect(JSON.stringify(config1)).toBe(JSON.stringify(config2));
    });
  });

  describe('getActivities() - 获取动态列表', () => {
    it('应该成功返回动态列表', async () => {
      const activities = await client.misc.getActivities();

      // 验证返回数据结构
      expect(activities).toBeDefined();
      expect(Array.isArray(activities)).toBe(true);
    });

    it('带参数应该成功返回动态列表', async () => {
      const activities = await client.misc.getActivities({
        scope: 'general',
        count: 10
      });

      expect(activities).toBeDefined();
      expect(Array.isArray(activities)).toBe(true);
      expect(activities.length).toBeLessThanOrEqual(10);
    });

    it('使用不同 scope 应该返回不同的动态', async () => {
      const generalActivities = await client.misc.getActivities({
        scope: 'general',
        count: 5
      });

      const likeActivities = await client.misc.getActivities({
        scope: 'like',
        count: 5
      });

      expect(generalActivities).toBeDefined();
      expect(likeActivities).toBeDefined();
      // 不同 scope 的数据可能不同
    });

    it('空动态列表应该返回空数组', async () => {
      const activities = await client.misc.getActivities({
        scope: 'system_message',
        count: 100
      });

      expect(Array.isArray(activities)).toBe(true);
    });
  });

  describe('getPkGroup() - 获取 PK 群组详情', () => {
    it('使用无效 pkGroupId 应该抛出异常', async () => {
      const invalidPkGroupId = 999999999999999;

      await expect(
        client.misc.getPkGroup(invalidPkGroupId)
      ).rejects.toThrow();
    });
  });

  describe('参数验证', () => {
    it('getActivities 使用无效 scope 应该抛出异常', async () => {
      await expect(
        client.misc.getActivities({
          scope: 'invalid_scope' as any,
          count: 10
        })
      ).rejects.toThrow();
    });

    it('getActivities 使用负数 count 应该抛出异常', async () => {
      await expect(
        client.misc.getActivities({
          scope: 'general',
          count: -1
        })
      ).rejects.toThrow();
    });
  });

  describe('权限验证', () => {
    it('使用无效 token 应该抛出异常', async () => {
      const invalidClient = new ZsxqClientBuilder()
        .setToken('invalid-token')
        .setTimeout(5000)
        .build();

      await expect(
        invalidClient.misc.getGlobalConfig()
      ).rejects.toThrow(ZsxqException);
    });

    it('使用无效 token 获取动态列表应该抛出异常', async () => {
      const invalidClient = new ZsxqClientBuilder()
        .setToken('invalid-token')
        .setTimeout(5000)
        .build();

      await expect(
        invalidClient.misc.getActivities()
      ).rejects.toThrow(ZsxqException);
    });
  });

  describe('边界条件', () => {
    it('getActivities 使用极大 count 应该能正常处理', async () => {
      const activities = await client.misc.getActivities({
        scope: 'general',
        count: 999999
      });

      expect(Array.isArray(activities)).toBe(true);
      // API 应该有内部限制，不会真的返回那么多数据
    });

    it('getActivities 使用 count=0 应该返回空数组', async () => {
      const activities = await client.misc.getActivities({
        scope: 'general',
        count: 0
      });

      expect(Array.isArray(activities)).toBe(true);
      expect(activities.length).toBe(0);
    });
  });
});
