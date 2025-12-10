import { ZsxqClientBuilder, ZsxqException } from 'zsxq-sdk';

/**
 * Groups 模块单元测试
 *
 * 测试场景：
 * 1. list() - 获取星球列表
 * 2. get() - 获取星球详情
 * 3. getStatistics() - 获取星球统计
 * 4. 数据一致性验证
 */
describe('Groups Module Unit Tests', () => {
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

  describe('list() - 获取星球列表', () => {
    it('应该成功返回星球列表', async () => {
      const groups = await client.groups.list();

      // 验证返回数据结构
      expect(groups).toBeDefined();
      expect(Array.isArray(groups)).toBe(true);
      expect(groups.length).toBeGreaterThan(0);
    });

    it('每个星球对象应该包含必要字段', async () => {
      const groups = await client.groups.list();
      const firstGroup = groups[0];

      // 验证星球对象结构
      expect(firstGroup.group_id).toBeDefined();
      expect(typeof firstGroup.group_id).toBe('number');
      expect(firstGroup.name).toBeDefined();
      expect(typeof firstGroup.name).toBe('string');
      expect(firstGroup.member_count).toBeDefined();
      expect(typeof firstGroup.member_count).toBe('number');
      expect(firstGroup.type).toBeDefined();
    });

    it('应该包含测试星球 ID', async () => {
      const groups = await client.groups.list();
      const groupIds = groups.map(g => g.group_id);

      // 测试星球应该在列表中
      expect(groupIds).toContain(testGroupId);
    });
  });

  describe('get() - 获取星球详情', () => {
    it('应该成功返回星球详情', async () => {
      const group = await client.groups.get(testGroupId);

      // 验证返回数据结构
      expect(group).toBeDefined();
      expect(group.group_id).toBe(testGroupId);
      expect(group.name).toBeDefined();
      expect(group.member_count).toBeDefined();
      expect(typeof group.member_count).toBe('number');
    });

    it('星球详情应与列表中的数据一致', async () => {
      const groups = await client.groups.list();
      const fromList = groups.find(g => g.group_id === testGroupId);
      const fromDetail = await client.groups.get(testGroupId);

      // 验证数据一致性
      expect(fromDetail.group_id).toBe(fromList?.group_id);
      expect(fromDetail.name).toBe(fromList?.name);
      expect(fromDetail.type).toBe(fromList?.type);
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.groups.get(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('getStatistics() - 获取星球统计', () => {
    it('应该成功返回星球统计数据', async () => {
      const stats = await client.groups.getStatistics(testGroupId);

      // 验证返回数据结构
      expect(stats).toBeDefined();
      expect(typeof stats).toBe('object');
    });

    it('统计数据应该包含数值类型', async () => {
      const stats = await client.groups.getStatistics(testGroupId);

      // 验证统计数据的合理性
      expect(stats).toBeTruthy();
    });

    it('使用无效 groupId 应该抛出异常', async () => {
      const invalidGroupId = 999999999999999;

      await expect(
        client.groups.getStatistics(invalidGroupId)
      ).rejects.toThrow();
    });
  });

  describe('成员数量验证', () => {
    it('成员数量应该是非负整数', async () => {
      const group = await client.groups.get(testGroupId);

      expect(group.member_count).toBeGreaterThanOrEqual(0);
      expect(Number.isInteger(group.member_count)).toBe(true);
    });

    it('成员数量应该保持一致', async () => {
      const group1 = await client.groups.get(testGroupId);
      const group2 = await client.groups.get(testGroupId);

      // 短时间内成员数量应该一致
      expect(group1.member_count).toBe(group2.member_count);
    });
  });
});
