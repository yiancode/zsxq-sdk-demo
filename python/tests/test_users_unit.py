#!/usr/bin/env python3
"""Users 模块单元测试

使用 pytest + unittest.mock 测试 Users 模块功能
"""

import pytest
from unittest.mock import AsyncMock, patch, MagicMock
import sys
import os

# 添加 SDK 路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), "../../../zsxq-sdk/packages/python"))

from zsxq import ZsxqClientBuilder, ZsxqException
from zsxq.model import User


class TestUsersUnit:
    """Users 模块单元测试类"""

    @pytest.fixture
    async def client(self):
        """创建测试客户端 fixture"""
        client = ZsxqClientBuilder().set_token("test-token").set_timeout(10).build()
        await client.__aenter__()
        yield client
        await client.__aexit__(None, None, None)

    @pytest.mark.asyncio
    async def test_self_success(self, client):
        """测试 self_() 方法成功场景"""
        # Mock 用户数据
        mock_user = User(
            user_id=12345,
            name="Test User",
            avatar_url="https://example.com/avatar.jpg",
            description="Test Description"
        )

        # Mock SDK 响应
        with patch.object(client.users, 'self_', new_callable=AsyncMock) as mock_self:
            mock_self.return_value = mock_user

            # 执行测试
            result = await client.users.self_()

            # 验证结果
            assert result is not None
            assert result.user_id == 12345
            assert result.name == "Test User"
            assert result.avatar_url == "https://example.com/avatar.jpg"
            mock_self.assert_called_once()

    @pytest.mark.asyncio
    async def test_self_with_all_fields(self, client):
        """测试 self_() 返回完整字段"""
        mock_user = User(
            user_id=67890,
            name="Full User",
            avatar_url="https://example.com/avatar2.jpg",
            description="Full description",
            location="Beijing",
            gender=1
        )

        with patch.object(client.users, 'self_', new_callable=AsyncMock) as mock_self:
            mock_self.return_value = mock_user

            result = await client.users.self_()

            assert result.user_id == 67890
            assert result.name == "Full User"
            assert result.location == "Beijing"
            assert result.gender == 1

    @pytest.mark.asyncio
    async def test_self_invalid_token(self, client):
        """测试 self_() 使用无效 token"""
        with patch.object(client.users, 'self_', new_callable=AsyncMock) as mock_self:
            mock_self.side_effect = ZsxqException("Unauthorized")

            with pytest.raises(ZsxqException) as exc_info:
                await client.users.self_()

            assert "Unauthorized" in str(exc_info.value)

    @pytest.mark.asyncio
    async def test_get_statistics_success(self, client):
        """测试 get_statistics() 方法成功场景"""
        user_id = 12345
        mock_stats = {
            "topics_count": 100,
            "questions_count": 50,
            "answers_count": 80,
            "likes_count": 200
        }

        with patch.object(client.users, 'get_statistics', new_callable=AsyncMock) as mock_stats_method:
            mock_stats_method.return_value = mock_stats

            result = await client.users.get_statistics(user_id)

            assert result is not None
            assert result["topics_count"] == 100
            assert result["questions_count"] == 50
            mock_stats_method.assert_called_once_with(user_id)

    @pytest.mark.asyncio
    async def test_get_statistics_invalid_user_id(self, client):
        """测试 get_statistics() 使用无效 user_id"""
        invalid_user_id = 999999999

        with patch.object(client.users, 'get_statistics', new_callable=AsyncMock) as mock_stats:
            mock_stats.side_effect = ZsxqException("User not found")

            with pytest.raises(ZsxqException) as exc_info:
                await client.users.get_statistics(invalid_user_id)

            assert "not found" in str(exc_info.value).lower()

    @pytest.mark.asyncio
    async def test_get_avatar_url_success(self, client):
        """测试 get_avatar_url() 方法成功场景"""
        user_id = 12345
        expected_url = "https://example.com/avatar.jpg"

        with patch.object(client.users, 'get_avatar_url', new_callable=AsyncMock) as mock_avatar:
            mock_avatar.return_value = expected_url

            result = await client.users.get_avatar_url(user_id)

            assert result == expected_url
            mock_avatar.assert_called_once_with(user_id)

    @pytest.mark.asyncio
    async def test_get_footprints_success(self, client):
        """测试 get_footprints() 方法成功场景"""
        user_id = 12345
        mock_topics = [
            MagicMock(topic_id=1, title="Topic 1"),
            MagicMock(topic_id=2, title="Topic 2")
        ]

        with patch.object(client.users, 'get_footprints', new_callable=AsyncMock) as mock_footprints:
            mock_footprints.return_value = mock_topics

            result = await client.users.get_footprints(user_id)

            assert result is not None
            assert len(result) == 2
            mock_footprints.assert_called_once_with(user_id)

    @pytest.mark.asyncio
    async def test_get_footprints_empty_list(self, client):
        """测试 get_footprints() 返回空列表"""
        user_id = 12345

        with patch.object(client.users, 'get_footprints', new_callable=AsyncMock) as mock_footprints:
            mock_footprints.return_value = []

            result = await client.users.get_footprints(user_id)

            assert result == []
            assert isinstance(result, list)

    @pytest.mark.asyncio
    async def test_get_created_groups_success(self, client):
        """测试 get_created_groups() 方法成功场景"""
        user_id = 12345
        mock_groups = [
            MagicMock(group_id=1, name="Group 1"),
            MagicMock(group_id=2, name="Group 2")
        ]

        with patch.object(client.users, 'get_created_groups', new_callable=AsyncMock) as mock_groups_method:
            mock_groups_method.return_value = mock_groups

            result = await client.users.get_created_groups(user_id)

            assert result is not None
            assert len(result) == 2
            mock_groups_method.assert_called_once_with(user_id)

    @pytest.mark.asyncio
    async def test_get_blocked_users_success(self, client):
        """测试 get_blocked_users() 方法成功场景"""
        mock_blocked = [
            User(user_id=111, name="Blocked User 1", avatar_url="url1"),
            User(user_id=222, name="Blocked User 2", avatar_url="url2")
        ]

        with patch.object(client.users, 'get_blocked_users', new_callable=AsyncMock) as mock_blocked_method:
            mock_blocked_method.return_value = mock_blocked

            result = await client.users.get_blocked_users()

            assert result is not None
            assert len(result) == 2
            assert result[0].user_id == 111
            assert result[1].user_id == 222
            mock_blocked_method.assert_called_once()

    @pytest.mark.asyncio
    async def test_method_call_verification(self, client):
        """测试方法调用次数验证"""
        mock_user = User(user_id=123, name="Test", avatar_url="url")

        with patch.object(client.users, 'self_', new_callable=AsyncMock) as mock_self:
            mock_self.return_value = mock_user

            # 调用两次
            await client.users.self_()
            await client.users.self_()

            # 验证调用次数
            assert mock_self.call_count == 2

    @pytest.mark.asyncio
    async def test_network_error_handling(self, client):
        """测试网络错误处理"""
        with patch.object(client.users, 'self_', new_callable=AsyncMock) as mock_self:
            mock_self.side_effect = Exception("Network error")

            with pytest.raises(Exception) as exc_info:
                await client.users.self_()

            assert "Network error" in str(exc_info.value)
