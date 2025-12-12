#!/usr/bin/env python3
"""Dashboard 模块单元测试"""

import pytest
from unittest.mock import AsyncMock, patch, MagicMock
import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "../../../zsxq-sdk/packages/python"))

from zsxq import ZsxqClientBuilder, ZsxqException


class TestDashboardUnit:
    """Dashboard 模块单元测试类"""

    @pytest.fixture
    async def client(self):
        client = ZsxqClientBuilder().set_token("test-token").set_timeout(10).build()
        await client.__aenter__()
        yield client
        await client.__aexit__(None, None, None)

    @pytest.mark.asyncio
    async def test_get_overview_success(self, client):
        """测试 get_overview() 获取数据面板概览"""
        group_id = 123
        mock_overview = {
            "members_count": 1000,
            "topics_count": 500,
            "revenue": 10000.0
        }

        with patch.object(client.dashboard, 'get_overview', new_callable=AsyncMock) as mock_overview_method:
            mock_overview_method.return_value = mock_overview

            result = await client.dashboard.get_overview(group_id)

            assert result["members_count"] == 1000
            assert result["topics_count"] == 500
            mock_overview_method.assert_called_once_with(group_id)

    @pytest.mark.asyncio
    async def test_get_overview_with_date_range(self, client):
        """测试 get_overview() 带日期范围"""
        group_id = 123
        start_date = "2024-01-01"
        end_date = "2024-12-31"

        with patch.object(client.dashboard, 'get_overview', new_callable=AsyncMock) as mock_overview:
            mock_overview.return_value = {"period": "2024"}

            result = await client.dashboard.get_overview(group_id, start_date, end_date)

            assert result["period"] == "2024"
            mock_overview.assert_called_once_with(group_id, start_date, end_date)

    @pytest.mark.asyncio
    async def test_get_incomes_success(self, client):
        """测试 get_incomes() 获取收入数据"""
        group_id = 123
        mock_incomes = [
            {"date": "2024-01-01", "amount": 1000},
            {"date": "2024-01-02", "amount": 1500}
        ]

        with patch.object(client.dashboard, 'get_incomes', new_callable=AsyncMock) as mock_incomes_method:
            mock_incomes_method.return_value = mock_incomes

            result = await client.dashboard.get_incomes(group_id)

            assert len(result) == 2
            assert result[0]["amount"] == 1000
            mock_incomes_method.assert_called_once_with(group_id)

    @pytest.mark.asyncio
    async def test_get_incomes_empty(self, client):
        """测试 get_incomes() 返回空列表"""
        group_id = 123

        with patch.object(client.dashboard, 'get_incomes', new_callable=AsyncMock) as mock_incomes:
            mock_incomes.return_value = []

            result = await client.dashboard.get_incomes(group_id)

            assert result == []
            assert isinstance(result, list)

    @pytest.mark.asyncio
    async def test_get_members_growth_success(self, client):
        """测试 get_members_growth() 获取成员增长数据"""
        group_id = 123
        mock_growth = [
            {"date": "2024-01-01", "count": 10},
            {"date": "2024-01-02", "count": 15}
        ]

        with patch.object(client.dashboard, 'get_members_growth', new_callable=AsyncMock) as mock_growth_method:
            mock_growth_method.return_value = mock_growth

            result = await client.dashboard.get_members_growth(group_id)

            assert len(result) == 2
            assert result[0]["count"] == 10

    @pytest.mark.asyncio
    async def test_get_topics_statistics_success(self, client):
        """测试 get_topics_statistics() 获取话题统计"""
        group_id = 123
        mock_stats = {
            "total": 500,
            "today": 10,
            "this_week": 50
        }

        with patch.object(client.dashboard, 'get_topics_statistics', new_callable=AsyncMock) as mock_stats:
            mock_stats.return_value = mock_stats

            result = await client.dashboard.get_topics_statistics(group_id)

            assert result["total"] == 500
            assert result["today"] == 10

    @pytest.mark.asyncio
    async def test_get_privileges_success(self, client):
        """测试 get_privileges() 获取权益数据"""
        group_id = 123
        mock_privileges = [
            MagicMock(privilege_id=1, name="Privilege 1"),
            MagicMock(privilege_id=2, name="Privilege 2")
        ]

        with patch.object(client.dashboard, 'get_privileges', new_callable=AsyncMock) as mock_privileges_method:
            mock_privileges_method.return_value = mock_privileges

            result = await client.dashboard.get_privileges(group_id)

            assert len(result) == 2

    @pytest.mark.asyncio
    async def test_invalid_group_id(self, client):
        """测试使用无效 group_id"""
        with patch.object(client.dashboard, 'get_overview', new_callable=AsyncMock) as mock_overview:
            mock_overview.side_effect = ZsxqException("Group not found")

            with pytest.raises(ZsxqException):
                await client.dashboard.get_overview(999999)

    @pytest.mark.asyncio
    async def test_overview_zero_values(self, client):
        """测试零值概览数据"""
        group_id = 123
        mock_overview = {
            "members_count": 0,
            "topics_count": 0,
            "revenue": 0.0
        }

        with patch.object(client.dashboard, 'get_overview', new_callable=AsyncMock) as mock_overview_method:
            mock_overview_method.return_value = mock_overview

            result = await client.dashboard.get_overview(group_id)

            assert result["members_count"] == 0
            assert result["revenue"] == 0.0

    @pytest.mark.asyncio
    async def test_date_range_validation(self, client):
        """测试日期范围验证"""
        group_id = 123
        invalid_start = "invalid-date"
        invalid_end = "invalid-date"

        with patch.object(client.dashboard, 'get_overview', new_callable=AsyncMock) as mock_overview:
            mock_overview.side_effect = ZsxqException("Invalid date format")

            with pytest.raises(ZsxqException):
                await client.dashboard.get_overview(group_id, invalid_start, invalid_end)
