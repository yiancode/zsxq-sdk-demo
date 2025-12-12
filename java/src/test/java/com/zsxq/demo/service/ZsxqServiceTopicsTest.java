package com.zsxq.demo.service;

import com.zsxq.demo.config.ZsxqProperties;
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.model.*;
import com.zsxq.sdk.request.TopicsRequest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * ZsxqService Topics 模块单元测试
 * 使用 TestNG + Mockito 框架
 */
public class ZsxqServiceTopicsTest {

    @Mock
    private ZsxqClient zsxqClient;

    @Mock
    private ZsxqProperties properties;

    @Mock
    private TopicsRequest topicsRequest;

    private ZsxqService zsxqService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        zsxqService = new ZsxqService(zsxqClient, properties);
        when(zsxqClient.topics()).thenReturn(topicsRequest);
    }

    @Test
    public void testGetTopics() {
        long groupId = 100L;
        Topic t1 = new Topic();
        t1.setTopicId(1L);
        Topic t2 = new Topic();
        t2.setTopicId(2L);
        List<Topic> mockTopics = Arrays.asList(t1, t2);

        when(topicsRequest.list(groupId)).thenReturn(mockTopics);

        List<Topic> result = zsxqService.getTopics(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(topicsRequest).list(groupId);
    }

    @Test
    public void testGetTopicsWithOptions() {
        long groupId = 200L;
        String scope = "digests";
        Integer count = 10;
        Topic t1 = new Topic();
        t1.setTopicId(1L);
        List<Topic> mockTopics = Arrays.asList(t1);

        when(topicsRequest.list(eq(groupId), any(TopicsRequest.ListTopicsOptions.class))).thenReturn(mockTopics);

        List<Topic> result = zsxqService.getTopics(groupId, scope, count);

        assertNotNull(result);
        assertEquals(result.size(), 1);
        verify(topicsRequest).list(eq(groupId), any(TopicsRequest.ListTopicsOptions.class));
    }

    @Test
    public void testGetTopic() {
        long topicId = 300L;
        Topic mockTopic = new Topic();
        mockTopic.setTopicId(topicId);

        when(topicsRequest.get(topicId)).thenReturn(mockTopic);

        Topic result = zsxqService.getTopic(topicId);

        assertNotNull(result);
        assertEquals(result.getTopicId().longValue(), topicId);
        verify(topicsRequest).get(topicId);
    }

    @Test
    public void testGetTopicComments() {
        long topicId = 400L;
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        List<Comment> mockComments = Arrays.asList(c1, c2);

        when(topicsRequest.getComments(topicId)).thenReturn(mockComments);

        List<Comment> result = zsxqService.getTopicComments(topicId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(topicsRequest).getComments(topicId);
    }

    @Test
    public void testGetTopicRewards() {
        long topicId = 500L;
        Reward r1 = new Reward();
        Reward r2 = new Reward();
        List<Reward> mockRewards = Arrays.asList(r1, r2);

        when(topicsRequest.getRewards(topicId)).thenReturn(mockRewards);

        List<Reward> result = zsxqService.getTopicRewards(topicId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(topicsRequest).getRewards(topicId);
    }

    @Test
    public void testGetStickyTopics() {
        long groupId = 600L;
        Topic t1 = new Topic();
        t1.setTopicId(1L);
        Topic t2 = new Topic();
        t2.setTopicId(2L);
        List<Topic> mockTopics = Arrays.asList(t1, t2);

        when(topicsRequest.listSticky(groupId)).thenReturn(mockTopics);

        List<Topic> result = zsxqService.getStickyTopics(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(topicsRequest).listSticky(groupId);
    }

    @Test
    public void testGetTopicsByHashtag() {
        long hashtagId = 700L;
        Topic t1 = new Topic();
        t1.setTopicId(1L);
        List<Topic> mockTopics = Arrays.asList(t1);

        when(topicsRequest.listByHashtag(hashtagId)).thenReturn(mockTopics);

        List<Topic> result = zsxqService.getTopicsByHashtag(hashtagId);

        assertNotNull(result);
        assertEquals(result.size(), 1);
        verify(topicsRequest).listByHashtag(hashtagId);
    }

    @Test
    public void testGetTopicsByColumn() {
        long groupId = 800L;
        long columnId = 900L;
        Topic t1 = new Topic();
        t1.setTopicId(1L);
        Topic t2 = new Topic();
        t2.setTopicId(2L);
        List<Topic> mockTopics = Arrays.asList(t1, t2);

        when(topicsRequest.listByColumn(groupId, columnId)).thenReturn(mockTopics);

        List<Topic> result = zsxqService.getTopicsByColumn(groupId, columnId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(topicsRequest).listByColumn(groupId, columnId);
    }
}
