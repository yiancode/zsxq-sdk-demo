import { ZsxqClientBuilder, ZsxqException } from 'zsxq-sdk';

/**
 * 集成测试 - 完整的工作流程
 *
 * 测试场景：
 * 1. 客户端初始化和配置
 * 2. 跨模块数据一致性
 * 3. 完整的业务流程
 */
describe('Integration Tests', () => {
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

  describe('客户端初始化', () => {
    it('应该成功创建客户端实例', () => {
      const testClient = new ZsxqClientBuilder()
        .setToken(process.env.ZSXQ_TOKEN!)
        .setTimeout(5000)
        .setRetry(2)
        .build();

      expect(testClient).toBeDefined();
      expect(testClient.users).toBeDefined();
      expect(testClient.groups).toBeDefined();
      expect(testClient.topics).toBeDefined();
      expect(testClient.checkins).toBeDefined();
      expect(testClient.dashboard).toBeDefined();
    });

    it('应该使用默认配置创建客户端', () => {
      const defaultClient = new ZsxqClientBuilder()
        .setToken(process.env.ZSXQ_TOKEN!)
        .build();

      expect(defaultClient).toBeDefined();
    });

    it('缺少 token 应该抛出异常', () => {
      expect(() => {
        new ZsxqClientBuilder().build();
      }).toThrow();
    });
  });

  describe('跨模块数据一致性', () => {
    it('用户和星球数据应该保持一致', async () => {
      // 获取当前用户
      const user = await client.users.self();

      // 获取星球列表
      const groups = await client.groups.list();

      // 验证数据存在
      expect(user).toBeDefined();
      expect(groups).toBeDefined();
      expect(groups.length).toBeGreaterThan(0);
    });

    it('星球列表和星球详情应该一致', async () => {
      // 从列表获取星球
      const groups = await client.groups.list();
      const targetGroup = groups.find(g => g.group_id === testGroupId);

      // 获取星球详情
      const detail = await client.groups.get(testGroupId);

      // 验证数据一致性
      expect(detail.group_id).toBe(targetGroup?.group_id);
      expect(detail.name).toBe(targetGroup?.name);
      expect(detail.type).toBe(targetGroup?.type);
    });

    it('话题列表和话题详情应该一致', async () => {
      // 获取话题列表
      const topics = await client.topics.list(testGroupId);

      if (topics.length > 0) {
        const firstTopic = topics[0];

        // 获取话题详情
        const detail = await client.topics.get(firstTopic.topic_id);

        // 验证数据一致性
        expect(detail.topic_id).toBe(firstTopic.topic_id);
        expect(detail.type).toBe(firstTopic.type);
        expect(detail.likes_count).toBe(firstTopic.likes_count);
      }
    });
  });

  describe('完整的业务流程', () => {
    it('应该能够完整浏览星球内容', async () => {
      // 1. 获取当前用户
      const user = await client.users.self();
      expect(user.user_id).toBeDefined();

      // 2. 获取星球列表
      const groups = await client.groups.list();
      expect(groups.length).toBeGreaterThan(0);

      // 3. 查看星球详情
      const group = await client.groups.get(testGroupId);
      expect(group.group_id).toBe(testGroupId);

      // 4. 浏览话题列表
      const topics = await client.topics.list(testGroupId);
      expect(Array.isArray(topics)).toBe(true);

      // 5. 查看精华话题
      const digests = await client.topics.list(testGroupId, {
        scope: 'digests',
        count: 5,
      });
      expect(Array.isArray(digests)).toBe(true);
    });

    it('应该能够查看打卡相关功能', async () => {
      // 1. 获取打卡项目列表
      const checkins = await client.checkins.list(testGroupId);
      expect(Array.isArray(checkins)).toBe(true);

      if (checkins.length > 0) {
        const firstCheckin = checkins[0];

        // 2. 查看打卡项目详情
        const detail = await client.checkins.get(testGroupId, firstCheckin.checkin_id);
        expect(detail.checkin_id).toBe(firstCheckin.checkin_id);

        // 3. 查看打卡统计
        const stats = await client.checkins.getStatistics(
          testGroupId,
          firstCheckin.checkin_id
        );
        expect(stats.joined_count).toBeGreaterThanOrEqual(0);

        // 4. 查看打卡排行榜
        const ranking = await client.checkins.getRankingList(
          testGroupId,
          firstCheckin.checkin_id
        );
        expect(Array.isArray(ranking)).toBe(true);
      }
    });

    it('应该能够获取统计数据', async () => {
      // 1. 获取当前用户
      const user = await client.users.self();

      // 2. 获取用户统计
      const userStats = await client.users.getStatistics(user.user_id);
      expect(userStats).toBeDefined();

      // 3. 获取星球统计
      const groupStats = await client.groups.getStatistics(testGroupId);
      expect(groupStats).toBeDefined();
    });
  });

  describe('错误处理和恢复', () => {
    it('应该能够处理网络错误', async () => {
      const shortTimeoutClient = new ZsxqClientBuilder()
        .setToken(process.env.ZSXQ_TOKEN!)
        .setTimeout(1)
        .build();

      await expect(shortTimeoutClient.groups.list()).rejects.toThrow();
    });

    it('应该能够处理无效参数', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.groups.get(invalidGroupId)
      ).rejects.toThrow();

      await expect(
        client.topics.list(invalidGroupId)
      ).rejects.toThrow();
    });

    it('错误后客户端应该仍然可用', async () => {
      try {
        await client.groups.get(999999999999999);
      } catch (error) {
        // 预期抛出异常
      }

      // 客户端应该仍然可用
      const groups = await client.groups.list();
      expect(groups).toBeDefined();
    });
  });

  describe('并发请求', () => {
    it('应该能够并发执行多个请求', async () => {
      const promises = [
        client.users.self(),
        client.groups.list(),
        client.groups.get(testGroupId),
        client.topics.list(testGroupId),
      ];

      const results = await Promise.all(promises);

      // 验证所有请求都成功
      expect(results).toHaveLength(4);
      results.forEach(result => {
        expect(result).toBeDefined();
      });
    });

    it('应该能够并发获取多个星球的详情', async () => {
      const groups = await client.groups.list();
      const groupIds = groups.slice(0, 3).map(g => g.group_id);

      const promises = groupIds.map(id => client.groups.get(id));
      const details = await Promise.all(promises);

      // 验证所有请求都成功
      expect(details).toHaveLength(groupIds.length);
      details.forEach((detail, index) => {
        expect(detail.group_id).toBe(groupIds[index]);
      });
    });
  });
});
