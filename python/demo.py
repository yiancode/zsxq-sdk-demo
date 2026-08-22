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
    InvitationRankingOptions,
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
        await test_ranking(client, group_id)

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


def _fmt_zsxq_time(dt):
    return dt.strftime("%Y-%m-%dT%H:%M:%S.000+0800")


def _print_invitation_ranking(title, range_text, ranking):
    print(f"\n{title}")
    print(f"  区间: {range_text}")
    if not ranking:
        print("  (还没有人上榜)")
        return
    print("  排名  成员昵称            编号    邀请人数")
    for item in ranking:
        name = (item.member.name if item.member else "?")[:16]
        number = item.member.number if item.member and item.member.number is not None else "-"
        print(f"  {item.rankings:>2}    {name:<16}  {str(number):>6}    {item.invitees_count}")


async def test_ranking(client, group_id: int):
    """测试邀请排行榜：日榜 / 周榜 / 月榜 / 自定义。

    对齐 App：日周月只传 begin_time + count + with_extra；自定义再加 end_time。
    """
    from datetime import datetime, timedelta, timezone

    print("\n[Ranking] 邀请排行榜（日/周/月/自定义）")
    print("-" * 40)

    tz = timezone(timedelta(hours=8))
    now = datetime.now(tz)
    daily_begin = now.replace(hour=0, minute=0, second=0, microsecond=0)
    weekly_begin = daily_begin - timedelta(days=daily_begin.weekday())
    monthly_begin = daily_begin.replace(day=1)
    custom_end = daily_begin.replace(hour=23, minute=59, second=0, microsecond=0)

    try:
        daily = await client.ranking.get_invitation_ranking(
            group_id,
            InvitationRankingOptions(
                begin_time=_fmt_zsxq_time(daily_begin),
                count=10,
                with_extra=True,
            ),
        )
        _print_invitation_ranking("日榜", f"{_fmt_zsxq_time(daily_begin)} ~ 当日 23:59", daily)

        weekly = await client.ranking.get_invitation_ranking(
            group_id,
            InvitationRankingOptions(
                begin_time=_fmt_zsxq_time(weekly_begin),
                count=10,
                with_extra=True,
            ),
        )
        _print_invitation_ranking("周榜", f"{_fmt_zsxq_time(weekly_begin)} ~ 本周日 23:59", weekly)

        monthly = await client.ranking.get_invitation_ranking(
            group_id,
            InvitationRankingOptions(
                begin_time=_fmt_zsxq_time(monthly_begin),
                count=20,
                with_extra=True,
            ),
        )
        _print_invitation_ranking("月榜", f"{_fmt_zsxq_time(monthly_begin)} ~ 本月末 23:59", monthly)

        custom = await client.ranking.get_invitation_ranking(
            group_id,
            InvitationRankingOptions(
                begin_time=_fmt_zsxq_time(daily_begin),
                end_time=_fmt_zsxq_time(custom_end),
                count=10,
                with_extra=True,
            ),
        )
        _print_invitation_ranking(
            "自定义",
            f"{_fmt_zsxq_time(daily_begin)} ~ {_fmt_zsxq_time(custom_end)}（最大 31 天）",
            custom,
        )

    except ZsxqException as e:
        print(f"✗ 排行榜模块错误: {e}")


if __name__ == "__main__":
    asyncio.run(main())
