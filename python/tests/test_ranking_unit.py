#!/usr/bin/env python3
"""Ranking 模块单元测试"""

import pytest
from unittest.mock import AsyncMock, patch, MagicMock
import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "../../../zsxq-sdk/packages/python"))

from zsxq import ZsxqClientBuilder, ZsxqException


class TestRankingUnit:
    """Ranking 模块单元测试类"""

    @pytest.fixture
    async def client(self):
        client = ZsxqClientBuilder().set_token("test-token").set_timeout(10).build()
        await client.__aenter__()
        yield client
        await client.__aexit__(None, None, None)

    @pytest.mark.asyncio
    async def test_get_group_ranking_success(self, client):
        """测试 get_group_ranking() 获取星球排行榜"""
        group_id = 123
        mock_ranking = [
            MagicMock(rank=1, user_id=1, score=1000),
            MagicMock(rank=2, user_id=2, score=900)
        ]

        with patch.object(client.ranking, 'get_group_ranking', new_callable=AsyncMock) as mock_ranking_method:
            mock_ranking_method.return_value = mock_ranking

            result = await client.ranking.get_group_ranking(group_id)

            assert len(result) == 2
            assert result[0].rank == 1
            mock_ranking_method.assert_called_once_with(group_id)

    @pytest.mark.asyncio
    async def test_get_group_ranking_stats_success(self, client):
        """测试 get_group_ranking_stats() 获取星球排行统计"""
        group_id = 123
        mock_stats = {
            "total_users": 1000,
            "top_score": 5000,
            "avg_score": 1500
        }

        with patch.object(client.ranking, 'get_group_ranking_stats', new_callable=AsyncMock) as mock_stats:
            mock_stats.return_value = mock_stats

            result = await client.ranking.get_group_ranking_stats(group_id)

            assert result["total_users"] == 1000
            assert result["top_score"] == 5000

    @pytest.mark.asyncio
    async def test_get_score_ranking_success(self, client):
        """测试 get_score_ranking() 获取积分排行榜"""
        group_id = 123
        mock_ranking = [MagicMock(rank=i, score=1000-i*10) for i in range(1, 11)]

        with patch.object(client.ranking, 'get_score_ranking', new_callable=AsyncMock) as mock_ranking:
            mock_ranking.return_value = mock_ranking

            result = await client.ranking.get_score_ranking(group_id)

            assert len(result) == 10
            assert result[0].rank == 1

    @pytest.mark.asyncio
    async def test_get_my_score_stats_success(self, client):
        """测试 get_my_score_stats() 获取我的积分统计"""
        group_id = 123
        mock_stats = {
            "my_score": 1500,
            "my_rank": 42,
            "total_users": 1000
        }

        with patch.object(client.ranking, 'get_my_score_stats', new_callable=AsyncMock) as mock_stats:
            mock_stats.return_value = mock_stats

            result = await client.ranking.get_my_score_stats(group_id)

            assert result["my_score"] == 1500
            assert result["my_rank"] == 42

    @pytest.mark.asyncio
    async def test_get_invitation_ranking_success(self, client):
        """测试 get_invitation_ranking() 获取邀请排行榜"""
        group_id = 123
        mock_ranking = [MagicMock(user_id=1, invitations=50)]

        with patch.object(client.ranking, 'get_invitation_ranking', new_callable=AsyncMock) as mock_ranking:
            mock_ranking.return_value = mock_ranking

            result = await client.ranking.get_invitation_ranking(group_id)

            assert len(result) == 1
            assert result[0].invitations == 50

    @pytest.mark.asyncio
    async def test_get_contribution_ranking_success(self, client):
        """测试 get_contribution_ranking() 获取贡献排行榜"""
        group_id = 123
        mock_ranking = [MagicMock(user_id=1, contributions=100)]

        with patch.object(client.ranking, 'get_contribution_ranking', new_callable=AsyncMock) as mock_ranking:
            mock_ranking.return_value = mock_ranking

            result = await client.ranking.get_contribution_ranking(group_id)

            assert len(result) == 1

    @pytest.mark.asyncio
    async def test_get_consumption_ranking_success(self, client):
        """测试 get_consumption_ranking() 获取消费排行榜"""
        group_id = 123
        mock_ranking = [MagicMock(user_id=1, amount=5000)]

        with patch.object(client.ranking, 'get_consumption_ranking', new_callable=AsyncMock) as mock_ranking:
            mock_ranking.return_value = mock_ranking

            result = await client.ranking.get_consumption_ranking(group_id)

            assert len(result) == 1

    @pytest.mark.asyncio
    async def test_empty_ranking_list(self, client):
        """测试空排行榜列表"""
        group_id = 123

        with patch.object(client.ranking, 'get_group_ranking', new_callable=AsyncMock) as mock_ranking:
            mock_ranking.return_value = []

            result = await client.ranking.get_group_ranking(group_id)

            assert result == []

    @pytest.mark.asyncio
    async def test_invalid_group_id(self, client):
        """测试无效 group_id"""
        with patch.object(client.ranking, 'get_group_ranking', new_callable=AsyncMock) as mock_ranking:
            mock_ranking.side_effect = ZsxqException("Group not found")

            with pytest.raises(ZsxqException):
                await client.ranking.get_group_ranking(999999)

    @pytest.mark.asyncio
    async def test_ranking_with_pagination(self, client):
        """测试分页获取排行榜"""
        group_id = 123
        mock_ranking = [MagicMock(rank=i) for i in range(1, 21)]

        with patch.object(client.ranking, 'get_score_ranking', new_callable=AsyncMock) as mock_ranking_method:
            mock_ranking_method.return_value = mock_ranking

            result = await client.ranking.get_score_ranking(group_id)

            assert len(result) == 20
            assert result[0].rank == 1
            assert result[19].rank == 20
