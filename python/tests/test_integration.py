#!/usr/bin/env python3
"""知识星球 SDK Python Demo 集成测试

测试所有模块的真实 API 调用功能
"""

import asyncio
import os
import sys
import unittest

# 添加 SDK 路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), "../../../zsxq-sdk/packages/python"))

from zsxq import ZsxqClientBuilder, ZsxqException
from zsxq.request import (
    ListTopicsOptions,
    ListCheckinsOptions,
    ListRankingOptions,
)


class IntegrationTests(unittest.IsolatedAsyncioTestCase):
    """集成测试类"""

    @classmethod
    def setUpClass(cls):
        """测试前初始化"""
        cls.token = os.environ.get("ZSXQ_TOKEN")
        cls.group_id_str = os.environ.get("ZSXQ_GROUP_ID")

        if not cls.token or not cls.group_id_str:
            raise ValueError("请设置环境变量 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID")

        cls.group_id = int(cls.group_id_str)

    async def asyncSetUp(self):
        """每个测试前初始化客户端"""
        self.client = (
            ZsxqClientBuilder()
            .set_token(self.token)
            .set_timeout(10)
            .set_retry(3)
            .build()
        )
        await self.client.__aenter__()

    async def asyncTearDown(self):
        """每个测试后关闭客户端"""
        await self.client.__aexit__(None, None, None)

    # ============================================================
    # Users 模块测试
    # ============================================================

    async def test_users_self(self):
        """Users: self_() - 获取当前用户信息"""
        user = await self.client.users.self_()

        # 验证返回数据
        self.assertIsNotNone(user)
        self.assertIsNotNone(user.user_id)
        self.assertIsNotNone(user.name)
        self.assertIsNotNone(user.avatar_url)

        print(f"✓ 当前用户: {user.name} (ID: {user.user_id})")

    async def test_users_get_statistics(self):
        """Users: get_statistics() - 获取用户统计数据"""
        user = await self.client.users.self_()
        stats = await self.client.users.get_statistics(user.user_id)

        # 验证返回数据
        self.assertIsNotNone(stats)

        print(f"✓ 用户统计数据: {stats}")

    async def test_users_invalid_token(self):
        """Users: 使用无效 token 应该抛出异常"""
        invalid_client = ZsxqClientBuilder().set_token("invalid-token").set_timeout(5).build()

        async with invalid_client:
            with self.assertRaises(ZsxqException):
                await invalid_client.users.self_()

        print("✓ 无效 token 正确抛出异常")

    # ============================================================
    # Groups 模块测试
    # ============================================================

    async def test_groups_list(self):
        """Groups: list() - 获取星球列表"""
        groups = await self.client.groups.list()

        # 验证返回数据
        self.assertIsNotNone(groups)
        self.assertGreater(len(groups), 0)

        for group in groups:
            self.assertIsNotNone(group.group_id)
            self.assertIsNotNone(group.name)
            self.assertGreaterEqual(group.member_count, 0)

        print(f"✓ 获取星球列表成功: {len(groups)} 个星球")

    async def test_groups_get(self):
        """Groups: get() - 获取星球详情"""
        group = await self.client.groups.get(self.group_id)

        # 验证返回数据
        self.assertIsNotNone(group)
        self.assertEqual(self.group_id, group.group_id)
        self.assertIsNotNone(group.name)
        self.assertIsNotNone(group.type)

        print(f"✓ 星球详情: {group.name} (成员数: {group.member_count})")

    async def test_groups_get_statistics(self):
        """Groups: get_statistics() - 获取星球统计"""
        stats = await self.client.groups.get_statistics(self.group_id)

        # 验证返回数据
        self.assertIsNotNone(stats)

        print(f"✓ 星球统计数据: {stats}")

    async def test_groups_data_consistency(self):
        """Groups: 星球列表和详情数据应该一致"""
        groups = await self.client.groups.list()
        from_list = next((g for g in groups if g.group_id == self.group_id), None)
        self.assertIsNotNone(from_list, "测试星球应该在列表中")

        from_detail = await self.client.groups.get(self.group_id)

        # 验证数据一致性
        self.assertEqual(from_list.group_id, from_detail.group_id)
        self.assertEqual(from_list.name, from_detail.name)
        self.assertEqual(from_list.type, from_detail.type)

        print("✓ 星球列表和详情数据一致")

    async def test_groups_invalid_id(self):
        """Groups: 使用无效 groupId 应该抛出异常"""
        invalid_group_id = 999999999999999

        with self.assertRaises(ZsxqException):
            await self.client.groups.get(invalid_group_id)

        print("✓ 无效 groupId 正确抛出异常")

    # ============================================================
    # Topics 模块测试
    # ============================================================

    async def test_topics_list(self):
        """Topics: list() - 获取话题列表"""
        topics = await self.client.topics.list(self.group_id)

        # 验证返回数据
        self.assertIsNotNone(topics)

        if len(topics) > 0:
            first = topics[0]
            self.assertIsNotNone(first.topic_id)
            self.assertIsNotNone(first.type)
            self.assertGreaterEqual(first.likes_count, 0)

        print(f"✓ 获取话题列表成功: {len(topics)} 个话题")

    async def test_topics_list_with_options(self):
        """Topics: list() with options - 获取精华话题"""
        digests = await self.client.topics.list(
            self.group_id, ListTopicsOptions(scope="digests", count=5)
        )

        # 验证返回数据
        self.assertIsNotNone(digests)
        self.assertLessEqual(len(digests), 5)

        print(f"✓ 获取精华话题成功: {len(digests)} 个话题")

    async def test_topics_get(self):
        """Topics: get() - 获取话题详情"""
        topics = await self.client.topics.list(self.group_id)
        if len(topics) == 0:
            self.skipTest("该星球没有话题")

        topic_id = topics[0].topic_id
        topic = await self.client.topics.get(topic_id)

        # 验证返回数据
        self.assertIsNotNone(topic)
        self.assertEqual(topic_id, topic.topic_id)

        print(f"✓ 获取话题详情成功: ID={topic_id}")

    # ============================================================
    # Checkins 模块测试
    # ============================================================

    async def test_checkins_list(self):
        """Checkins: list() - 获取打卡项目列表"""
        checkins = await self.client.checkins.list(self.group_id)

        # 验证返回数据
        self.assertIsNotNone(checkins)

        if len(checkins) > 0:
            first = checkins[0]
            self.assertIsNotNone(first.checkin_id)
            self.assertIsNotNone(first.name)
            self.assertIsNotNone(first.status)

        print(f"✓ 获取打卡项目列表成功: {len(checkins)} 个项目")

    async def test_checkins_get_statistics(self):
        """Checkins: get_statistics() - 获取打卡统计"""
        checkins = await self.client.checkins.list(self.group_id)
        if len(checkins) == 0:
            self.skipTest("该星球没有打卡项目")

        checkin_id = checkins[0].checkin_id
        stats = await self.client.checkins.get_statistics(self.group_id, checkin_id)

        # 验证返回数据
        self.assertIsNotNone(stats)
        self.assertGreaterEqual(stats.joined_count, 0)
        self.assertGreaterEqual(stats.completed_count, 0)
        self.assertLessEqual(stats.completed_count, stats.joined_count)

        print(f"✓ 打卡统计: 参与={stats.joined_count}, 完成={stats.completed_count}")

    async def test_checkins_get_ranking_list(self):
        """Checkins: get_ranking_list() - 获取打卡排行榜"""
        checkins = await self.client.checkins.list(self.group_id)
        if len(checkins) == 0:
            self.skipTest("该星球没有打卡项目")

        checkin_id = checkins[0].checkin_id
        ranking = await self.client.checkins.get_ranking_list(self.group_id, checkin_id)

        # 验证返回数据
        self.assertIsNotNone(ranking)

        print(f"✓ 获取打卡排行榜成功: {len(ranking)} 个用户")

    # ============================================================
    # Dashboard 模块测试
    # ============================================================

    async def test_dashboard_get_overview(self):
        """Dashboard: get_overview() - 获取星球概览"""
        try:
            overview = await self.client.dashboard.get_overview(self.group_id)
            self.assertIsNotNone(overview)
            print(f"✓ 获取星球概览成功: {overview}")
        except ZsxqException as e:
            print(f"⚠ Dashboard 需要星主权限: {e}")

    async def test_dashboard_get_incomes(self):
        """Dashboard: get_incomes() - 获取收入概览"""
        try:
            incomes = await self.client.dashboard.get_incomes(self.group_id)
            self.assertIsNotNone(incomes)
            print(f"✓ 获取收入概览成功: {incomes}")
        except ZsxqException as e:
            print(f"⚠ Dashboard 需要星主权限: {e}")

    # ============================================================
    # 完整业务流程测试
    # ============================================================

    async def test_complete_workflow(self):
        """完整业务流程: 浏览星球内容"""
        # 1. 获取当前用户
        user = await self.client.users.self_()
        self.assertIsNotNone(user)

        # 2. 获取星球列表
        groups = await self.client.groups.list()
        self.assertGreater(len(groups), 0)

        # 3. 查看星球详情
        group = await self.client.groups.get(self.group_id)
        self.assertEqual(self.group_id, group.group_id)

        # 4. 浏览话题列表
        topics = await self.client.topics.list(self.group_id)
        self.assertIsNotNone(topics)

        # 5. 查看精华话题
        digests = await self.client.topics.list(
            self.group_id, ListTopicsOptions(scope="digests", count=5)
        )
        self.assertIsNotNone(digests)

        print("✓ 完整业务流程测试通过")

    async def test_error_recovery(self):
        """错误处理: 错误后客户端应该仍然可用"""
        # 触发一个错误
        with self.assertRaises(ZsxqException):
            await self.client.groups.get(999999999999999)

        # 客户端应该仍然可用
        groups = await self.client.groups.list()
        self.assertIsNotNone(groups)

        print("✓ 错误恢复测试通过")


if __name__ == "__main__":
    unittest.main(verbosity=2)
