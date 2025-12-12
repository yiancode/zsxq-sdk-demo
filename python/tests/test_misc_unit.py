#!/usr/bin/env python3
"""Misc 模块单元测试"""

import pytest
from unittest.mock import AsyncMock, patch, MagicMock
import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "../../../zsxq-sdk/packages/python"))

from zsxq import ZsxqClientBuilder, ZsxqException


class TestMiscUnit:
    """Misc 模块单元测试类"""

    @pytest.fixture
    async def client(self):
        client = ZsxqClientBuilder().set_token("test-token").set_timeout(10).build()
        await client.__aenter__()
        yield client
        await client.__aexit__(None, None, None)

    @pytest.mark.asyncio
    async def test_get_global_config_success(self, client):
        """测试 get_global_config() 获取全局配置"""
        mock_config = {
            "app_version": "1.0.0",
            "api_version": "v2",
            "features": ["feature1", "feature2"]
        }

        with patch.object(client.misc, 'get_global_config', new_callable=AsyncMock) as mock_config_method:
            mock_config_method.return_value = mock_config

            result = await client.misc.get_global_config()

            assert result["app_version"] == "1.0.0"
            assert len(result["features"]) == 2
            mock_config_method.assert_called_once()

    @pytest.mark.asyncio
    async def test_get_activities_success(self, client):
        """测试 get_activities() 获取活动列表"""
        mock_activities = [
            MagicMock(activity_id=1, name="Activity 1"),
            MagicMock(activity_id=2, name="Activity 2")
        ]

        with patch.object(client.misc, 'get_activities', new_callable=AsyncMock) as mock_activities_method:
            mock_activities_method.return_value = mock_activities

            result = await client.misc.get_activities()

            assert len(result) == 2
            mock_activities_method.assert_called_once()

    @pytest.mark.asyncio
    async def test_get_activities_with_filter(self, client):
        """测试 get_activities() 带筛选条件"""
        filter_options = {"status": "active", "type": "event"}

        with patch.object(client.misc, 'get_activities', new_callable=AsyncMock) as mock_activities:
            mock_activities.return_value = [MagicMock(activity_id=1)]

            result = await client.misc.get_activities(filter_options)

            assert len(result) == 1
            mock_activities.assert_called_once_with(filter_options)

    @pytest.mark.asyncio
    async def test_get_pk_group_success(self, client):
        """测试 get_pk_group() 获取 PK 群组信息"""
        pk_group_id = 123
        mock_pk_group = {
            "pk_group_id": pk_group_id,
            "name": "Test PK Group",
            "participants": 10
        }

        with patch.object(client.misc, 'get_pk_group', new_callable=AsyncMock) as mock_pk:
            mock_pk.return_value = mock_pk_group

            result = await client.misc.get_pk_group(pk_group_id)

            assert result["pk_group_id"] == pk_group_id
            assert result["participants"] == 10
            mock_pk.assert_called_once_with(pk_group_id)

    @pytest.mark.asyncio
    async def test_get_pk_group_invalid_id(self, client):
        """测试 get_pk_group() 使用无效 ID"""
        with patch.object(client.misc, 'get_pk_group', new_callable=AsyncMock) as mock_pk:
            mock_pk.side_effect = ZsxqException("PK group not found")

            with pytest.raises(ZsxqException):
                await client.misc.get_pk_group(999999)

    @pytest.mark.asyncio
    async def test_get_announcements_success(self, client):
        """测试 get_announcements() 获取公告列表"""
        mock_announcements = [
            MagicMock(id=1, title="Announcement 1"),
            MagicMock(id=2, title="Announcement 2")
        ]

        with patch.object(client.misc, 'get_announcements', new_callable=AsyncMock) as mock_announcements_method:
            mock_announcements_method.return_value = mock_announcements

            result = await client.misc.get_announcements()

            assert len(result) == 2

    @pytest.mark.asyncio
    async def test_get_notifications_success(self, client):
        """测试 get_notifications() 获取通知列表"""
        mock_notifications = [MagicMock(notification_id=1)]

        with patch.object(client.misc, 'get_notifications', new_callable=AsyncMock) as mock_notifications:
            mock_notifications.return_value = mock_notifications

            result = await client.misc.get_notifications()

            assert len(result) == 1

    @pytest.mark.asyncio
    async def test_empty_activities_list(self, client):
        """测试空活动列表"""
        with patch.object(client.misc, 'get_activities', new_callable=AsyncMock) as mock_activities:
            mock_activities.return_value = []

            result = await client.misc.get_activities()

            assert result == []
            assert isinstance(result, list)

    @pytest.mark.asyncio
    async def test_config_with_missing_fields(self, client):
        """测试配置缺少字段"""
        mock_config = {"app_version": "1.0.0"}  # 缺少其他字段

        with patch.object(client.misc, 'get_global_config', new_callable=AsyncMock) as mock_config_method:
            mock_config_method.return_value = mock_config

            result = await client.misc.get_global_config()

            assert "app_version" in result
            assert result["app_version"] == "1.0.0"

    @pytest.mark.asyncio
    async def test_multiple_config_calls_consistency(self, client):
        """测试多次配置调用一致性"""
        mock_config = {"app_version": "1.0.0", "api_version": "v2"}

        with patch.object(client.misc, 'get_global_config', new_callable=AsyncMock) as mock_config_method:
            mock_config_method.return_value = mock_config

            result1 = await client.misc.get_global_config()
            result2 = await client.misc.get_global_config()

            assert result1 == result2
            assert mock_config_method.call_count == 2
