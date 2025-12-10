import { ZsxqClientBuilder, ZsxqException } from 'zsxq-sdk';

const SEPARATOR = '='.repeat(60);

async function main() {
  // 从环境变量获取配置
  const token = process.env.ZSXQ_TOKEN;
  const groupId = process.env.ZSXQ_GROUP_ID;

  if (!token || !groupId) {
    console.error('请设置环境变量:');
    console.error('  ZSXQ_TOKEN=your-token');
    console.error('  ZSXQ_GROUP_ID=your-group-id');
    process.exit(1);
  }

  const groupIdNum = parseInt(groupId, 10);

  console.log(SEPARATOR);
  console.log('知识星球 SDK TypeScript Demo');
  console.log(SEPARATOR);

  // 创建客户端
  const client = new ZsxqClientBuilder()
    .setToken(token)
    .setTimeout(10000)
    .setRetry(3)
    .build();

  // 运行所有测试
  await testUsers(client);
  await testGroups(client, groupIdNum);
  await testTopics(client, groupIdNum);
  await testCheckins(client, groupIdNum);
  await testDashboard(client, groupIdNum);

  console.log(SEPARATOR);
  console.log('所有测试完成!');
  console.log(SEPARATOR);
}

/**
 * 测试用户模块
 */
async function testUsers(client: ReturnType<typeof ZsxqClientBuilder.prototype.build>) {
  console.log('\n[Users] 用户模块测试');
  console.log('-'.repeat(40));

  try {
    // 获取当前用户
    const self = await client.users.self();
    console.log('✓ self() - 当前用户:', self.name);
    console.log('  用户ID:', self.user_id);
    console.log('  头像:', self.avatar_url);

    // 获取用户统计
    const stats = await client.users.getStatistics(self.user_id);
    console.log('✓ getStatistics() - 用户统计:', stats);
  } catch (error) {
    if (error instanceof ZsxqException) {
      console.error('✗ 用户模块错误:', error.message);
    } else {
      throw error;
    }
  }
}

/**
 * 测试星球模块
 */
async function testGroups(client: ReturnType<typeof ZsxqClientBuilder.prototype.build>, groupId: number) {
  console.log('\n[Groups] 星球模块测试');
  console.log('-'.repeat(40));

  try {
    // 获取星球列表
    const groups = await client.groups.list();
    console.log('✓ list() - 我的星球数量:', groups.length);
    for (const g of groups) {
      console.log(`  - ${g.name} (ID: ${g.group_id})`);
    }

    // 获取星球详情
    const group = await client.groups.get(groupId);
    console.log('✓ get() - 星球详情:', group.name);
    console.log('  成员数:', group.member_count);
    console.log('  类型:', group.type);

    // 获取星球统计
    const stats = await client.groups.getStatistics(groupId);
    console.log('✓ getStatistics() - 星球统计:', stats);
  } catch (error) {
    if (error instanceof ZsxqException) {
      console.error('✗ 星球模块错误:', error.message);
    } else {
      throw error;
    }
  }
}

/**
 * 测试话题模块
 */
async function testTopics(client: ReturnType<typeof ZsxqClientBuilder.prototype.build>, groupId: number) {
  console.log('\n[Topics] 话题模块测试');
  console.log('-'.repeat(40));

  try {
    // 获取话题列表
    const topics = await client.topics.list(groupId);
    console.log('✓ list() - 话题数量:', topics.length);

    if (topics.length > 0) {
      const first = topics[0];
      console.log('  最新话题ID:', first.topic_id);
      console.log('  类型:', first.type);
      console.log('  点赞数:', first.likes_count);

      // 获取话题详情
      const detail = await client.topics.get(first.topic_id);
      console.log('✓ get() - 话题详情获取成功');
    }

    // 测试带参数的列表查询
    const digests = await client.topics.list(groupId, { scope: 'digests', count: 5 });
    console.log('✓ list(options) - 精华话题数量:', digests.length);
  } catch (error) {
    if (error instanceof ZsxqException) {
      console.error('✗ 话题模块错误:', error.message);
    } else {
      throw error;
    }
  }
}

/**
 * 测试打卡模块
 */
async function testCheckins(client: ReturnType<typeof ZsxqClientBuilder.prototype.build>, groupId: number) {
  console.log('\n[Checkins] 打卡模块测试');
  console.log('-'.repeat(40));

  try {
    // 获取打卡项目列表
    const checkins = await client.checkins.list(groupId);
    console.log('✓ list() - 打卡项目数量:', checkins.length);

    if (checkins.length > 0) {
      const first = checkins[0];
      console.log('  项目名称:', first.name);
      console.log('  状态:', first.status);

      const checkinId = first.checkin_id;

      // 获取打卡项目详情
      const detail = await client.checkins.get(groupId, checkinId);
      console.log('✓ get() - 打卡项目详情获取成功');

      // 获取打卡统计
      const stats = await client.checkins.getStatistics(groupId, checkinId);
      console.log('✓ getStatistics() - 打卡统计:');
      console.log('  参与人数:', stats.joined_count);
      console.log('  完成人数:', stats.completed_count);

      // 获取打卡排行榜
      const ranking = await client.checkins.getRankingList(groupId, checkinId);
      console.log('✓ getRankingList() - 排行榜人数:', ranking.length);

      // 测试带参数的排行榜查询
      const continuous = await client.checkins.getRankingList(groupId, checkinId, { type: 'continuous' });
      console.log('✓ getRankingList(options) - 连续打卡排行:', continuous.length);
    } else {
      console.log('  (该星球没有打卡项目)');
    }

    // 测试带参数的列表查询
    const ongoing = await client.checkins.list(groupId, { scope: 'ongoing' });
    console.log('✓ list(options) - 进行中的打卡:', ongoing.length);
  } catch (error) {
    if (error instanceof ZsxqException) {
      console.error('✗ 打卡模块错误:', error.message);
    } else {
      throw error;
    }
  }
}

/**
 * 测试数据面板模块
 */
async function testDashboard(client: ReturnType<typeof ZsxqClientBuilder.prototype.build>, groupId: number) {
  console.log('\n[Dashboard] 数据面板模块测试');
  console.log('-'.repeat(40));

  try {
    // 获取星球概览
    const overview = await client.dashboard.getOverview(groupId);
    console.log('✓ getOverview() - 星球概览:', overview);

    // 获取收入概览
    const incomes = await client.dashboard.getIncomes(groupId);
    console.log('✓ getIncomes() - 收入概览:', incomes);
  } catch (error) {
    if (error instanceof ZsxqException) {
      console.error('✗ 数据面板模块错误:', error.message);
      console.log('  (可能需要星主权限)');
    } else {
      throw error;
    }
  }
}

main().catch(console.error);
