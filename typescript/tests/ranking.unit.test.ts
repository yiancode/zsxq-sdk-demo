import { ZsxqClientBuilder, ZsxqException } from 'zsxq-sdk';

/**
 * Ranking 模块单元测试
 *
 * 测试场景：
 * 1. getGroupRanking() - 获取星球排行榜
 * 2. getGroupRankingStats() - 获取星球排行统计
 * 3. getScoreRanking() - 获取积分排行榜
 * 4. getMyScoreStats() - 获取我的积分统计
 * 5. getScoreboardSettings() - 获取积分榜设置
 * 6. getInvitationRanking() - 获取邀请排行榜
 * 7. getContributionRanking() - 获取贡献排行榜
 * 8. 参数验证和异常处理
 */
describe('Ranking Module Unit Tests', () => {
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

  describe('getGroupRanking() - 获取星球排行榜', () => {
    it('应该成功返回星球排行榜数据', async () => {
      const ranking = await client.ranking.getGroupRanking(testGroupId);

      // 验证返回数据结构
      expect(ranking).toBeDefined();
      expect(Array.isArray(ranking)).toBe(true);
    });

    it('带参数应该成功返回排行榜数据', async () => {
      const ranking = await client.ranking.getGroupRanking(testGroupId, {
        type: 'continuous',
        index: 0
      });

      expect(ranking).toBeDefined();
      expect(Array.isArray(ranking)).toBe(true);
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.ranking.getGroupRanking(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('getGroupRankingStats() - 获取星球排行统计', () => {
    it('应该成功返回排行统计数据', async () => {
      const stats = await client.ranking.getGroupRankingStats(testGroupId);

      // 验证返回数据结构
      expect(stats).toBeDefined();
      expect(typeof stats).toBe('object');
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.ranking.getGroupRankingStats(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('getScoreRanking() - 获取积分排行榜', () => {
    it('应该成功返回积分排行榜数据', async () => {
      const ranking = await client.ranking.getScoreRanking(testGroupId);

      // 验证返回数据结构
      expect(ranking).toBeDefined();
      expect(Array.isArray(ranking)).toBe(true);
    });

    it('带参数应该成功返回积分排行榜', async () => {
      const ranking = await client.ranking.getScoreRanking(testGroupId, {
        type: 'accumulated',
        index: 0
      });

      expect(ranking).toBeDefined();
      expect(Array.isArray(ranking)).toBe(true);
    });
  });

  describe('getMyScoreStats() - 获取我的积分统计', () => {
    it('应该成功返回我的积分统计', async () => {
      const stats = await client.ranking.getMyScoreStats(testGroupId);

      // 验证返回数据结构
      expect(stats).toBeDefined();
      expect(typeof stats).toBe('object');
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.ranking.getMyScoreStats(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('getScoreboardSettings() - 获取积分榜设置', () => {
    it('应该成功返回积分榜设置', async () => {
      const settings = await client.ranking.getScoreboardSettings(testGroupId);

      // 验证返回数据结构
      expect(settings).toBeDefined();
      expect(typeof settings).toBe('object');
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.ranking.getScoreboardSettings(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('getInvitationRanking() - 获取邀请排行榜', () => {
    it('应该成功返回邀请排行榜', async () => {
      const ranking = await client.ranking.getInvitationRanking(testGroupId);

      // 验证返回数据结构
      expect(ranking).toBeDefined();
      expect(Array.isArray(ranking)).toBe(true);
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.ranking.getInvitationRanking(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('getContributionRanking() - 获取贡献排行榜', () => {
    it('应该成功返回贡献排行榜', async () => {
      const ranking = await client.ranking.getContributionRanking(testGroupId);

      // 验证返回数据结构
      expect(ranking).toBeDefined();
      expect(Array.isArray(ranking)).toBe(true);
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.ranking.getContributionRanking(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('数据一致性验证', () => {
    it('多次调用应该返回一致的排行榜数据', async () => {
      const ranking1 = await client.ranking.getGroupRanking(testGroupId);
      const ranking2 = await client.ranking.getGroupRanking(testGroupId);

      // 排行榜数据应该一致（短时间内不会变化）
      expect(ranking1.length).toBe(ranking2.length);
    });
  });

  describe('权限验证', () => {
    it('使用无效 token 应该抛出异常', async () => {
      const invalidClient = new ZsxqClientBuilder()
        .setToken('invalid-token')
        .setTimeout(5000)
        .build();

      await expect(
        invalidClient.ranking.getGroupRanking(testGroupId)
      ).rejects.toThrow(ZsxqException);
    });
  });
});
