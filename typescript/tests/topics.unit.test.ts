import { ZsxqClientBuilder, ZsxqException } from 'zsxq-sdk';

/**
 * Topics 模块单元测试
 *
 * 测试场景：
 * 1. list() - 获取话题列表（基本查询和带参数查询）
 * 2. get() - 获取话题详情
 * 3. 参数验证
 */
describe('Topics Module Unit Tests', () => {
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

  describe('list() - 获取话题列表', () => {
    it('应该成功返回话题列表', async () => {
      const topics = await client.topics.list(testGroupId);

      // 验证返回数据结构
      expect(topics).toBeDefined();
      expect(Array.isArray(topics)).toBe(true);
    });

    it('每个话题对象应该包含必要字段', async () => {
      const topics = await client.topics.list(testGroupId);

      if (topics.length > 0) {
        const firstTopic = topics[0];

        // 验证话题对象结构
        expect(firstTopic.topic_id).toBeDefined();
        expect(typeof firstTopic.topic_id).toBe('number');
        expect(firstTopic.type).toBeDefined();
        expect(typeof firstTopic.type).toBe('string');
        expect(firstTopic.likes_count).toBeDefined();
        expect(typeof firstTopic.likes_count).toBe('number');
      }
    });

    it('应该支持 scope 参数筛选精华话题', async () => {
      const digests = await client.topics.list(testGroupId, {
        scope: 'digests',
        count: 5,
      });

      // 验证返回数据
      expect(digests).toBeDefined();
      expect(Array.isArray(digests)).toBe(true);
      expect(digests.length).toBeLessThanOrEqual(5);
    });

    it('应该支持 count 参数限制返回数量', async () => {
      const count = 3;
      const topics = await client.topics.list(testGroupId, { count });

      // 验证返回数量
      expect(topics.length).toBeLessThanOrEqual(count);
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.topics.list(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('get() - 获取话题详情', () => {
    let testTopicId: number;

    beforeAll(async () => {
      // 获取一个有效的话题 ID 用于测试
      const topics = await client.topics.list(testGroupId);
      if (topics.length === 0) {
        throw new Error('No topics available for testing');
      }
      testTopicId = topics[0].topic_id;
    });

    it('应该成功返回话题详情', async () => {
      const topic = await client.topics.get(testTopicId);

      // 验证返回数据结构
      expect(topic).toBeDefined();
      expect(topic.topic_id).toBe(testTopicId);
      expect(topic.type).toBeDefined();
    });

    it('话题详情应与列表中的数据一致', async () => {
      const topics = await client.topics.list(testGroupId);
      const fromList = topics.find(t => t.topic_id === testTopicId);
      const fromDetail = await client.topics.get(testTopicId);

      // 验证数据一致性
      expect(fromDetail.topic_id).toBe(fromList?.topic_id);
      expect(fromDetail.type).toBe(fromList?.type);
      expect(fromDetail.likes_count).toBe(fromList?.likes_count);
    });

    it('使用无效 topicId 应该抛出异常', async () => {
      const invalidTopicId = 999999999999999;

      await expect(
        client.topics.get(invalidTopicId)
      ).rejects.toThrow();
    });
  });

  describe('点赞数量验证', () => {
    it('点赞数量应该是非负整数', async () => {
      const topics = await client.topics.list(testGroupId);

      topics.forEach(topic => {
        expect(topic.likes_count).toBeGreaterThanOrEqual(0);
        expect(Number.isInteger(topic.likes_count)).toBe(true);
      });
    });
  });
});
