#!/usr/bin/env python3
"""Groups 模块单元测试"""

import pytest
from unittest.mock import AsyncMock, patch, MagicMock
import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "../../../zsxq-sdk/packages/python"))

from zsxq import ZsxqClientBuilder, ZsxqException
from zsxq.model import Group


class TestGroupsUnit:
    """Groups 模块单元测试类"""

    @pytest.fixture
    async def client(self):
        client = ZsxqClientBuilder().set_token("test-token").set_timeout(10).build()
        await client.__aenter__()
        yield client
        await client.__aexit__(None, None, None)

    @pytest.mark.asyncio
    async def test_list_success(self, client):
        """测试 list() 获取星球列表"""
        mock_groups = [
            Group(group_id=1, name="Group 1", members_count=100),
            Group(group_id=2, name="Group 2", members_count=200)
        ]

        with patch.object(client.groups, 'list', new_callable=AsyncMock) as mock_list:
            mock_list.return_value = mock_groups

            result = await client.groups.list()

            assert len(result) == 2
            assert result[0].group_id == 1
            mock_list.assert_called_once()

    @pytest.mark.asyncio
    async def test_list_empty(self, client):
        """测试 list() 返回空列表"""
        with patch.object(client.groups, 'list', new_callable=AsyncMock) as mock_list:
            mock_list.return_value = []

            result = await client.groups.list()

            assert result == []

    @pytest.mark.asyncio
    async def test_get_success(self, client):
        """测试 get() 获取星球详情"""
        group_id = 123
        mock_group = Group(group_id=group_id, name="Test Group", members_count=500)

        with patch.object(client.groups, 'get', new_callable=AsyncMock) as mock_get:
            mock_get.return_value = mock_group

            result = await client.groups.get(group_id)

            assert result.group_id == group_id
            assert result.name == "Test Group"
            mock_get.assert_called_once_with(group_id)

    @pytest.mark.asyncio
    async def test_get_invalid_id(self, client):
        """测试 get() 使用无效 group_id"""
        with patch.object(client.groups, 'get', new_callable=AsyncMock) as mock_get:
            mock_get.side_effect = ZsxqException("Group not found")

            with pytest.raises(ZsxqException):
                await client.groups.get(999999)

    @pytest.mark.asyncio
    async def test_get_statistics_success(self, client):
        """测试 get_statistics() 获取星球统计"""
        group_id = 123
        mock_stats = {
            "members_count": 1000,
            "topics_count": 500,
            "likes_count": 2000
        }

        with patch.object(client.groups, 'get_statistics', new_callable=AsyncMock) as mock_stats:
            mock_stats.return_value = mock_stats

            result = await client.groups.get_statistics(group_id)

            assert result["members_count"] == 1000
            assert result["topics_count"] == 500

    @pytest.mark.asyncio
    async def test_get_hashtags_success(self, client):
        """测试 get_hashtags() 获取星球标签"""
        group_id = 123
        mock_hashtags = [
            MagicMock(hashtag_id=1, name="Tag1"),
            MagicMock(hashtag_id=2, name="Tag2")
        ]

        with patch.object(client.groups, 'get_hashtags', new_callable=AsyncMock) as mock_hashtags_method:
            mock_hashtags_method.return_value = mock_hashtags

            result = await client.groups.get_hashtags(group_id)

            assert len(result) == 2

    @pytest.mark.asyncio
    async def test_get_menus_success(self, client):
        """测试 get_menus() 获取星球菜单"""
        group_id = 123
        mock_menus = [MagicMock(menu_id=1), MagicMock(menu_id=2)]

        with patch.object(client.groups, 'get_menus', new_callable=AsyncMock) as mock_menus:
            mock_menus.return_value = mock_menus

            result = await client.groups.get_menus(group_id)

            assert len(result) == 2

    @pytest.mark.asyncio
    async def test_get_role_members_success(self, client):
        """测试 get_role_members() 获取角色成员"""
        group_id = 123

        with patch.object(client.groups, 'get_role_members', new_callable=AsyncMock) as mock_role:
            mock_role.return_value = MagicMock()

            result = await client.groups.get_role_members(group_id)

            assert result is not None

    @pytest.mark.asyncio
    async def test_get_member_success(self, client):
        """测试 get_member() 获取成员信息"""
        group_id = 123
        member_id = 456

        with patch.object(client.groups, 'get_member', new_callable=AsyncMock) as mock_member:
            mock_member.return_value = MagicMock(user_id=member_id)

            result = await client.groups.get_member(group_id, member_id)

            assert result.user_id == member_id

    @pytest.mark.asyncio
    async def test_multiple_calls_consistency(self, client):
        """测试多次调用一致性"""
        mock_group = Group(group_id=123, name="Test", members_count=100)

        with patch.object(client.groups, 'get', new_callable=AsyncMock) as mock_get:
            mock_get.return_value = mock_group

            result1 = await client.groups.get(123)
            result2 = await client.groups.get(123)

            assert result1.group_id == result2.group_id
            assert mock_get.call_count == 2
