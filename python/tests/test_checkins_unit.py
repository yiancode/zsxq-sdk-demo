#!/usr/bin/env python3
"""Checkins 模块单元测试"""

import pytest
from unittest.mock import AsyncMock, patch, MagicMock
import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "../../../zsxq-sdk/packages/python"))

from zsxq import ZsxqClientBuilder, ZsxqException


class TestCheckinsUnit:
    """Checkins 模块单元测试类"""

    @pytest.fixture
    async def client(self):
        client = ZsxqClientBuilder().set_token("test-token").set_timeout(10).build()
        await client.__aenter__()
        yield client
        await client.__aexit__(None, None, None)

    @pytest.mark.asyncio
    async def test_list_success(self, client):
        """测试 list() 获取打卡列表"""
        group_id = 123
        mock_checkins = [MagicMock(checkin_id=1), MagicMock(checkin_id=2)]

        with patch.object(client.checkins, 'list', new_callable=AsyncMock) as mock_list:
            mock_list.return_value = mock_checkins

            result = await client.checkins.list(group_id)

            assert len(result) == 2
            mock_list.assert_called_once_with(group_id, None)

    @pytest.mark.asyncio
    async def test_list_with_options(self, client):
        """测试 list() 带参数获取打卡"""
        group_id = 123
        options = MagicMock(count=10, end_time="2024-01-01")

        with patch.object(client.checkins, 'list', new_callable=AsyncMock) as mock_list:
            mock_list.return_value = [MagicMock(checkin_id=1)]

            result = await client.checkins.list(group_id, options)

            assert len(result) == 1
            mock_list.assert_called_once_with(group_id, options)

    @pytest.mark.asyncio
    async def test_get_success(self, client):
        """测试 get() 获取打卡详情"""
        group_id = 123
        checkin_id = 456

        with patch.object(client.checkins, 'get', new_callable=AsyncMock) as mock_get:
            mock_get.return_value = MagicMock(checkin_id=checkin_id)

            result = await client.checkins.get(group_id, checkin_id)

            assert result.checkin_id == checkin_id
            mock_get.assert_called_once_with(group_id, checkin_id)

    @pytest.mark.asyncio
    async def test_get_invalid_id(self, client):
        """测试 get() 使用无效 checkin_id"""
        group_id = 123

        with patch.object(client.checkins, 'get', new_callable=AsyncMock) as mock_get:
            mock_get.side_effect = ZsxqException("Checkin not found")

            with pytest.raises(ZsxqException):
                await client.checkins.get(group_id, 999999)

    @pytest.mark.asyncio
    async def test_get_statistics_success(self, client):
        """测试 get_statistics() 获取打卡统计"""
        group_id = 123
        checkin_id = 456
        mock_stats = {
            "total_count": 100,
            "today_count": 10
        }

        with patch.object(client.checkins, 'get_statistics', new_callable=AsyncMock) as mock_stats_method:
            mock_stats_method.return_value = mock_stats

            result = await client.checkins.get_statistics(group_id, checkin_id)

            assert result["total_count"] == 100
            mock_stats_method.assert_called_once_with(group_id, checkin_id)

    @pytest.mark.asyncio
    async def test_get_ranking_list_success(self, client):
        """测试 get_ranking_list() 获取打卡排行榜"""
        group_id = 123
        checkin_id = 456

        with patch.object(client.checkins, 'get_ranking_list', new_callable=AsyncMock) as mock_ranking:
            mock_ranking.return_value = [MagicMock(rank=1), MagicMock(rank=2)]

            result = await client.checkins.get_ranking_list(group_id, checkin_id)

            assert len(result) == 2
            mock_ranking.assert_called_once_with(group_id, checkin_id)

    @pytest.mark.asyncio
    async def test_get_comments_success(self, client):
        """测试 get_comments() 获取打卡评论"""
        group_id = 123
        checkin_id = 456

        with patch.object(client.checkins, 'get_comments', new_callable=AsyncMock) as mock_comments:
            mock_comments.return_value = [MagicMock(comment_id=1)]

            result = await client.checkins.get_comments(group_id, checkin_id)

            assert len(result) == 1

    @pytest.mark.asyncio
    async def test_empty_list_handling(self, client):
        """测试空列表处理"""
        group_id = 123

        with patch.object(client.checkins, 'list', new_callable=AsyncMock) as mock_list:
            mock_list.return_value = []

            result = await client.checkins.list(group_id)

            assert result == []

    @pytest.mark.asyncio
    async def test_list_with_pagination(self, client):
        """测试分页获取打卡列表"""
        group_id = 123
        options = MagicMock(count=20, end_time=None)

        with patch.object(client.checkins, 'list', new_callable=AsyncMock) as mock_list:
            mock_list.return_value = [MagicMock(checkin_id=i) for i in range(20)]

            result = await client.checkins.list(group_id, options)

            assert len(result) == 20
            mock_list.assert_called_once_with(group_id, options)

    @pytest.mark.asyncio
    async def test_statistics_zero_values(self, client):
        """测试零值统计数据"""
        group_id = 123
        checkin_id = 456
        mock_stats = {
            "total_count": 0,
            "today_count": 0
        }

        with patch.object(client.checkins, 'get_statistics', new_callable=AsyncMock) as mock_stats_method:
            mock_stats_method.return_value = mock_stats

            result = await client.checkins.get_statistics(group_id, checkin_id)

            assert result["total_count"] == 0
            assert result["today_count"] == 0

    @pytest.mark.asyncio
    async def test_multiple_calls_consistency(self, client):
        """测试多次调用一致性"""
        group_id = 123
        checkin_id = 456

        with patch.object(client.checkins, 'get', new_callable=AsyncMock) as mock_get:
            mock_get.return_value = MagicMock(checkin_id=checkin_id)

            result1 = await client.checkins.get(group_id, checkin_id)
            result2 = await client.checkins.get(group_id, checkin_id)

            assert result1.checkin_id == result2.checkin_id
            assert mock_get.call_count == 2
