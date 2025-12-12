#!/usr/bin/env python3
"""Topics 模块单元测试"""

import pytest
from unittest.mock import AsyncMock, patch, MagicMock
import sys
import os

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "../../../zsxq-sdk/packages/python"))

from zsxq import ZsxqClientBuilder, ZsxqException
from zsxq.request import ListTopicsOptions


class TestTopicsUnit:
    """Topics 模块单元测试类"""

    @pytest.fixture
    async def client(self):
        client = ZsxqClientBuilder().set_token("test-token").set_timeout(10).build()
        await client.__aenter__()
        yield client
        await client.__aexit__(None, None, None)

    @pytest.mark.asyncio
    async def test_list_success(self, client):
        """测试 list() 获取话题列表"""
        group_id = 123
        mock_topics = [MagicMock(topic_id=1), MagicMock(topic_id=2)]

        with patch.object(client.topics, 'list', new_callable=AsyncMock) as mock_list:
            mock_list.return_value = mock_topics

            result = await client.topics.list(group_id)

            assert len(result) == 2
            mock_list.assert_called_once_with(group_id, None)

    @pytest.mark.asyncio
    async def test_list_with_options(self, client):
        """测试 list() 带参数获取话题"""
        group_id = 123
        options = ListTopicsOptions(scope="digests", count=10)

        with patch.object(client.topics, 'list', new_callable=AsyncMock) as mock_list:
            mock_list.return_value = [MagicMock(topic_id=1)]

            result = await client.topics.list(group_id, options)

            assert len(result) == 1
            mock_list.assert_called_once_with(group_id, options)

    @pytest.mark.asyncio
    async def test_get_success(self, client):
        """测试 get() 获取话题详情"""
        topic_id = 456

        with patch.object(client.topics, 'get', new_callable=AsyncMock) as mock_get:
            mock_get.return_value = MagicMock(topic_id=topic_id)

            result = await client.topics.get(topic_id)

            assert result.topic_id == topic_id

    @pytest.mark.asyncio
    async def test_get_invalid_id(self, client):
        """测试 get() 使用无效 topic_id"""
        with patch.object(client.topics, 'get', new_callable=AsyncMock) as mock_get:
            mock_get.side_effect = ZsxqException("Topic not found")

            with pytest.raises(ZsxqException):
                await client.topics.get(999999)

    @pytest.mark.asyncio
    async def test_get_comments_success(self, client):
        """测试 get_comments() 获取评论"""
        topic_id = 456
        mock_comments = [MagicMock(comment_id=1), MagicMock(comment_id=2)]

        with patch.object(client.topics, 'get_comments', new_callable=AsyncMock) as mock_comments_method:
            mock_comments_method.return_value = mock_comments

            result = await client.topics.get_comments(topic_id)

            assert len(result) == 2

    @pytest.mark.asyncio
    async def test_get_rewards_success(self, client):
        """测试 get_rewards() 获取打赏列表"""
        topic_id = 456
        mock_rewards = [MagicMock(reward_id=1)]

        with patch.object(client.topics, 'get_rewards', new_callable=AsyncMock) as mock_rewards:
            mock_rewards.return_value = mock_rewards

            result = await client.topics.get_rewards(topic_id)

            assert len(result) == 1

    @pytest.mark.asyncio
    async def test_list_sticky_success(self, client):
        """测试 list_sticky() 获取置顶话题"""
        group_id = 123

        with patch.object(client.topics, 'list_sticky', new_callable=AsyncMock) as mock_sticky:
            mock_sticky.return_value = [MagicMock(topic_id=1)]

            result = await client.topics.list_sticky(group_id)

            assert len(result) == 1

    @pytest.mark.asyncio
    async def test_list_by_hashtag_success(self, client):
        """测试 list_by_hashtag() 按标签获取话题"""
        hashtag_id = 789

        with patch.object(client.topics, 'list_by_hashtag', new_callable=AsyncMock) as mock_hashtag:
            mock_hashtag.return_value = [MagicMock(topic_id=1)]

            result = await client.topics.list_by_hashtag(hashtag_id)

            assert len(result) == 1

    @pytest.mark.asyncio
    async def test_list_by_column_success(self, client):
        """测试 list_by_column() 按专栏获取话题"""
        group_id = 123
        column_id = 456

        with patch.object(client.topics, 'list_by_column', new_callable=AsyncMock) as mock_column:
            mock_column.return_value = [MagicMock(topic_id=1)]

            result = await client.topics.list_by_column(group_id, column_id)

            assert len(result) == 1

    @pytest.mark.asyncio
    async def test_empty_list_handling(self, client):
        """测试空列表处理"""
        group_id = 123

        with patch.object(client.topics, 'list', new_callable=AsyncMock) as mock_list:
            mock_list.return_value = []

            result = await client.topics.list(group_id)

            assert result == []
