#!/usr/bin/env python3
"""知识星球 SDK Python 示例

演示 zsxq-sdk 的所有功能模块
"""

import asyncio
import os
import sys

# 添加 SDK 路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), "../../zsxq-sdk/packages/python"))

from zsxq import ZsxqClientBuilder, ZsxqException
from zsxq.request import (
    ListTopicsOptions,
    ListCheckinsOptions,
    ListRankingOptions,
)

SEPARATOR = "=" * 60


async def main():
    # 从环境变量获取配置
    token = os.environ.get("ZSXQ_TOKEN")
    group_id_str = os.environ.get("ZSXQ_GROUP_ID")

    if not token or not group_id_str:
        print("请设置环境变量:", file=sys.stderr)
        print("  ZSXQ_TOKEN=your-token", file=sys.stderr)
        print("  ZSXQ_GROUP_ID=your-group-id", file=sys.stderr)
        sys.exit(1)

    group_id = int(group_id_str)

    print(SEPARATOR)
    print("知识星球 SDK Python Demo")
    print(SEPARATOR)

    # 创建客户端
    client = (
        ZsxqClientBuilder()
        .set_token(token)
        .set_timeout(10)
        .set_retry(3)
        .build()
    )

    async with client:
        # 运行所有测试
        await test_users(client)
        await test_groups(client, group_id)
        await test_topics(client, group_id)
        await test_checkins(client, group_id)
        await test_dashboard(client, group_id)

    print(SEPARATOR)
    print("所有测试完成!")
    print(SEPARATOR)


async def test_users(client):
    """测试用户模块"""
    print("\n[Users] 用户模块测试")
    print("-" * 40)

    try:
        # 获取当前用户
        self_user = await client.users.self_()
        print(f"✓ self_() - 当前用户: {self_user.name}")
        print(f"  用户ID: {self_user.user_id}")
        print(f"  头像: {self_user.avatar_url}")

        # 获取用户统计
        stats = await client.users.get_statistics(self_user.user_id)
        print(f"✓ get_statistics() - 用户统计: {stats}")

    except ZsxqException as e:
        print(f"✗ 用户模块错误: {e}")


async def test_groups(client, group_id: int):
    """测试星球模块"""
    print("\n[Groups] 星球模块测试")
    print("-" * 40)

    try:
        # 获取星球列表
        groups = await client.groups.list()
        print(f"✓ list() - 我的星球数量: {len(groups)}")
        for g in groups:
            print(f"  - {g.name} (ID: {g.group_id})")

        # 获取星球详情
        group = await client.groups.get(group_id)
        print(f"✓ get() - 星球详情: {group.name}")
        print(f"  成员数: {group.member_count}")
        print(f"  类型: {group.type}")

        # 获取星球统计
        stats = await client.groups.get_statistics(group_id)
        print(f"✓ get_statistics() - 星球统计: {stats}")

    except ZsxqException as e:
        print(f"✗ 星球模块错误: {e}")


async def test_topics(client, group_id: int):
    """测试话题模块"""
    print("\n[Topics] 话题模块测试")
    print("-" * 40)

    try:
        # 获取话题列表
        topics = await client.topics.list(group_id)
        print(f"✓ list() - 话题数量: {len(topics)}")

        if topics:
            first = topics[0]
            print(f"  最新话题ID: {first.topic_id}")
            print(f"  类型: {first.type}")
            print(f"  点赞数: {first.likes_count}")

            # 获取话题详情
            detail = await client.topics.get(first.topic_id)
            print("✓ get() - 话题详情获取成功")

        # 测试带参数的列表查询
        digests = await client.topics.list(
            group_id, ListTopicsOptions(scope="digests", count=5)
        )
        print(f"✓ list(options) - 精华话题数量: {len(digests)}")

    except ZsxqException as e:
        print(f"✗ 话题模块错误: {e}")


async def test_checkins(client, group_id: int):
    """测试打卡模块"""
    print("\n[Checkins] 打卡模块测试")
    print("-" * 40)

    try:
        # 获取打卡项目列表
        checkins = await client.checkins.list(group_id)
        print(f"✓ list() - 打卡项目数量: {len(checkins)}")

        if checkins:
            first = checkins[0]
            print(f"  项目名称: {first.name}")
            print(f"  状态: {first.status}")

            checkin_id = first.checkin_id

            # 获取打卡项目详情
            detail = await client.checkins.get(group_id, checkin_id)
            print("✓ get() - 打卡项目详情获取成功")

            # 获取打卡统计
            stats = await client.checkins.get_statistics(group_id, checkin_id)
            print("✓ get_statistics() - 打卡统计:")
            print(f"  参与人数: {stats.joined_count}")
            print(f"  完成人数: {stats.completed_count}")

            # 获取打卡排行榜
            ranking = await client.checkins.get_ranking_list(group_id, checkin_id)
            print(f"✓ get_ranking_list() - 排行榜人数: {len(ranking)}")

            # 测试带参数的排行榜查询
            continuous = await client.checkins.get_ranking_list(
                group_id, checkin_id, ListRankingOptions(type="continuous")
            )
            print(f"✓ get_ranking_list(options) - 连续打卡排行: {len(continuous)}")
        else:
            print("  (该星球没有打卡项目)")

        # 测试带参数的列表查询
        ongoing = await client.checkins.list(
            group_id, ListCheckinsOptions(scope="ongoing")
        )
        print(f"✓ list(options) - 进行中的打卡: {len(ongoing)}")

    except ZsxqException as e:
        print(f"✗ 打卡模块错误: {e}")


async def test_dashboard(client, group_id: int):
    """测试数据面板模块"""
    print("\n[Dashboard] 数据面板模块测试")
    print("-" * 40)

    try:
        # 获取星球概览
        overview = await client.dashboard.get_overview(group_id)
        print(f"✓ get_overview() - 星球概览: {overview}")

        # 获取收入概览
        incomes = await client.dashboard.get_incomes(group_id)
        print(f"✓ get_incomes() - 收入概览: {incomes}")

    except ZsxqException as e:
        print(f"✗ 数据面板模块错误: {e}")
        print("  (可能需要星主权限)")


if __name__ == "__main__":
    asyncio.run(main())
