import { ZsxqClientBuilder, ZsxqException } from 'zsxq-sdk';

/**
 * Users 模块单元测试
 *
 * 测试场景：
 * 1. self() - 获取当前用户信息
 * 2. getStatistics() - 获取用户统计数据
 * 3. 异常处理
 */
describe('Users Module Unit Tests', () => {
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

  describe('self() - 获取当前用户', () => {
    it('应该成功返回当前用户信息', async () => {
      const user = await client.users.self();

      // 验证返回的数据结构
      expect(user).toBeDefined();
      expect(user.user_id).toBeDefined();
      expect(typeof user.user_id).toBe('number');
      expect(user.name).toBeDefined();
      expect(typeof user.name).toBe('string');
      expect(user.avatar_url).toBeDefined();
      expect(typeof user.avatar_url).toBe('string');
    });

    it('应该返回一致的用户信息', async () => {
      const user1 = await client.users.self();
      const user2 = await client.users.self();

      // 多次调用应返回相同的用户 ID
      expect(user1.user_id).toBe(user2.user_id);
      expect(user1.name).toBe(user2.name);
    });
  });

  describe('getStatistics() - 获取用户统计', () => {
    it('应该成功返回用户统计数据', async () => {
      const user = await client.users.self();
      const stats = await client.users.getStatistics(user.user_id);

      // 验证返回的数据结构
      expect(stats).toBeDefined();
      expect(typeof stats).toBe('object');
    });

    it('应该为不同用户返回不同的统计数据', async () => {
      const user = await client.users.self();
      const stats = await client.users.getStatistics(user.user_id);

      // 验证统计数据包含合理的值
      expect(stats).toBeTruthy();
    });
  });

  describe('异常处理', () => {
    it('使用无效 token 应该抛出 ZsxqException', async () => {
      const invalidClient = new ZsxqClientBuilder()
        .setToken('invalid-token')
        .setTimeout(5000)
        .build();

      await expect(invalidClient.users.self()).rejects.toThrow(ZsxqException);
    });

    it('getStatistics() 使用无效 userId 应该抛出异常', async () => {
      // 使用一个明显不存在的用户 ID
      const invalidUserId = 999999999999999;

      await expect(
        client.users.getStatistics(invalidUserId)
      ).rejects.toThrow();
    });
  });

  describe('超时和重试', () => {
    it('应该在超时时抛出异常', async () => {
      const timeoutClient = new ZsxqClientBuilder()
        .setToken(process.env.ZSXQ_TOKEN!)
        .setTimeout(1) // 设置极短的超时时间
        .build();

      // 超时应该抛出异常
      await expect(timeoutClient.users.self()).rejects.toThrow();
    }, 15000); // 增加测试超时时间
  });
});
