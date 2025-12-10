import { ZsxqClientBuilder, ZsxqException } from 'zsxq-sdk';

/**
 * Checkins 模块单元测试
 *
 * 测试场景：
 * 1. list() - 获取打卡项目列表
 * 2. get() - 获取打卡项目详情
 * 3. getStatistics() - 获取打卡统计
 * 4. getRankingList() - 获取打卡排行榜
 */
describe('Checkins Module Unit Tests', () => {
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

  describe('list() - 获取打卡项目列表', () => {
    it('应该成功返回打卡项目列表', async () => {
      const checkins = await client.checkins.list(testGroupId);

      // 验证返回数据结构
      expect(checkins).toBeDefined();
      expect(Array.isArray(checkins)).toBe(true);
    });

    it('每个打卡项目对象应该包含必要字段', async () => {
      const checkins = await client.checkins.list(testGroupId);

      if (checkins.length > 0) {
        const firstCheckin = checkins[0];

        // 验证打卡项目对象结构
        expect(firstCheckin.checkin_id).toBeDefined();
        expect(typeof firstCheckin.checkin_id).toBe('number');
        expect(firstCheckin.name).toBeDefined();
        expect(typeof firstCheckin.name).toBe('string');
        expect(firstCheckin.status).toBeDefined();
        expect(typeof firstCheckin.status).toBe('string');
      }
    });

    it('应该支持 scope 参数筛选进行中的打卡', async () => {
      const ongoing = await client.checkins.list(testGroupId, {
        scope: 'ongoing',
      });

      // 验证返回数据
      expect(ongoing).toBeDefined();
      expect(Array.isArray(ongoing)).toBe(true);

      // 验证所有打卡项目都是进行中状态
      ongoing.forEach(checkin => {
        expect(['ongoing', 'active']).toContain(checkin.status);
      });
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.checkins.list(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('get() - 获取打卡项目详情', () => {
    let testCheckinId: number;

    beforeAll(async () => {
      // 获取一个有效的打卡项目 ID 用于测试
      const checkins = await client.checkins.list(testGroupId);
      if (checkins.length === 0) {
        testCheckinId = -1; // 标记没有打卡项目
      } else {
        testCheckinId = checkins[0].checkin_id;
      }
    });

    it('应该成功返回打卡项目详情', async () => {
      if (testCheckinId === -1) {
        // 跳过测试：该星球没有打卡项目
        return;
      }

      const checkin = await client.checkins.get(testGroupId, testCheckinId);

      // 验证返回数据结构
      expect(checkin).toBeDefined();
      expect(checkin.checkin_id).toBe(testCheckinId);
      expect(checkin.name).toBeDefined();
      expect(checkin.status).toBeDefined();
    });

    it('使用无效 checkinId 应该抛出异常', async () => {
      const invalidCheckinId = 999999999999999;

      await expect(
        client.checkins.get(testGroupId, invalidCheckinId)
      ).rejects.toThrow();
    });
  });

  describe('getStatistics() - 获取打卡统计', () => {
    let testCheckinId: number;

    beforeAll(async () => {
      const checkins = await client.checkins.list(testGroupId);
      if (checkins.length === 0) {
        testCheckinId = -1;
      } else {
        testCheckinId = checkins[0].checkin_id;
      }
    });

    it('应该成功返回打卡统计数据', async () => {
      if (testCheckinId === -1) {
        return;
      }

      const stats = await client.checkins.getStatistics(testGroupId, testCheckinId);

      // 验证返回数据结构
      expect(stats).toBeDefined();
      expect(stats.joined_count).toBeDefined();
      expect(typeof stats.joined_count).toBe('number');
      expect(stats.completed_count).toBeDefined();
      expect(typeof stats.completed_count).toBe('number');
    });

    it('统计数据应该合理', async () => {
      if (testCheckinId === -1) {
        return;
      }

      const stats = await client.checkins.getStatistics(testGroupId, testCheckinId);

      // 验证统计数据的合理性
      expect(stats.joined_count).toBeGreaterThanOrEqual(0);
      expect(stats.completed_count).toBeGreaterThanOrEqual(0);
      expect(stats.completed_count).toBeLessThanOrEqual(stats.joined_count);
    });
  });

  describe('getRankingList() - 获取打卡排行榜', () => {
    let testCheckinId: number;

    beforeAll(async () => {
      const checkins = await client.checkins.list(testGroupId);
      if (checkins.length === 0) {
        testCheckinId = -1;
      } else {
        testCheckinId = checkins[0].checkin_id;
      }
    });

    it('应该成功返回打卡排行榜', async () => {
      if (testCheckinId === -1) {
        return;
      }

      const ranking = await client.checkins.getRankingList(testGroupId, testCheckinId);

      // 验证返回数据结构
      expect(ranking).toBeDefined();
      expect(Array.isArray(ranking)).toBe(true);
    });

    it('应该支持 type 参数筛选连续打卡排行', async () => {
      if (testCheckinId === -1) {
        return;
      }

      const continuous = await client.checkins.getRankingList(
        testGroupId,
        testCheckinId,
        { type: 'continuous' }
      );

      // 验证返回数据
      expect(continuous).toBeDefined();
      expect(Array.isArray(continuous)).toBe(true);
    });
  });
});
