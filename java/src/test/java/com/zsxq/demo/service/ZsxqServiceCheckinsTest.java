package com.zsxq.demo.service;

import com.zsxq.demo.config.ZsxqProperties;
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.model.*;
import com.zsxq.sdk.request.CheckinsRequest;
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
 * ZsxqService Checkins 模块单元测试
 * 使用 TestNG + Mockito 框架
 */
public class ZsxqServiceCheckinsTest {

    @Mock
    private ZsxqClient zsxqClient;

    @Mock
    private ZsxqProperties properties;

    @Mock
    private CheckinsRequest checkinsRequest;

    private ZsxqService zsxqService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        zsxqService = new ZsxqService(zsxqClient, properties);
        when(zsxqClient.checkins()).thenReturn(checkinsRequest);
    }

    @Test
    public void testGetCheckins() {
        long groupId = 100L;
        Checkin c1 = new Checkin();
        c1.setCheckinId(1L);
        Checkin c2 = new Checkin();
        c2.setCheckinId(2L);
        List<Checkin> mockCheckins = Arrays.asList(c1, c2);

        when(checkinsRequest.list(groupId)).thenReturn(mockCheckins);

        List<Checkin> result = zsxqService.getCheckins(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(checkinsRequest).list(groupId);
    }

    @Test
    public void testGetCheckinsWithScope() {
        long groupId = 200L;
        String scope = "ongoing";
        Checkin c1 = new Checkin();
        c1.setCheckinId(1L);
        List<Checkin> mockCheckins = Arrays.asList(c1);

        when(checkinsRequest.list(eq(groupId), any(CheckinsRequest.ListCheckinsOptions.class))).thenReturn(mockCheckins);

        List<Checkin> result = zsxqService.getCheckins(groupId, scope);

        assertNotNull(result);
        assertEquals(result.size(), 1);
        verify(checkinsRequest).list(eq(groupId), any(CheckinsRequest.ListCheckinsOptions.class));
    }

    @Test
    public void testGetCheckin() {
        long groupId = 300L;
        long checkinId = 400L;
        Checkin mockCheckin = new Checkin();
        mockCheckin.setCheckinId(checkinId);
        mockCheckin.setName("TestCheckin");

        when(checkinsRequest.get(groupId, checkinId)).thenReturn(mockCheckin);

        Checkin result = zsxqService.getCheckin(groupId, checkinId);

        assertNotNull(result);
        assertEquals(result.getCheckinId().longValue(), checkinId);
        assertEquals(result.getName(), "TestCheckin");
        verify(checkinsRequest).get(groupId, checkinId);
    }

    @Test
    public void testGetCheckinStatistics() {
        long groupId = 500L;
        long checkinId = 600L;
        CheckinStatistics mockStats = new CheckinStatistics();

        when(checkinsRequest.getStatistics(groupId, checkinId)).thenReturn(mockStats);

        CheckinStatistics result = zsxqService.getCheckinStatistics(groupId, checkinId);

        assertNotNull(result);
        verify(checkinsRequest).getStatistics(groupId, checkinId);
    }

    @Test
    public void testGetCheckinRankingList() {
        long groupId = 700L;
        long checkinId = 800L;
        RankingItem r1 = new RankingItem();
        RankingItem r2 = new RankingItem();
        List<RankingItem> mockRankings = Arrays.asList(r1, r2);

        when(checkinsRequest.getRankingList(groupId, checkinId)).thenReturn(mockRankings);

        List<RankingItem> result = zsxqService.getCheckinRankingList(groupId, checkinId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(checkinsRequest).getRankingList(groupId, checkinId);
    }

    @Test
    public void testGetCheckinTopics() {
        long groupId = 900L;
        long checkinId = 1000L;
        Topic t1 = new Topic();
        t1.setTopicId(1L);
        Topic t2 = new Topic();
        t2.setTopicId(2L);
        List<Topic> mockTopics = Arrays.asList(t1, t2);

        when(checkinsRequest.getTopics(groupId, checkinId)).thenReturn(mockTopics);

        List<Topic> result = zsxqService.getCheckinTopics(groupId, checkinId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(checkinsRequest).getTopics(groupId, checkinId);
    }

    @Test
    public void testGetCheckinDailyStatistics() {
        long groupId = 1100L;
        long checkinId = 1200L;
        DailyStatistics d1 = new DailyStatistics();
        DailyStatistics d2 = new DailyStatistics();
        List<DailyStatistics> mockDailyStats = Arrays.asList(d1, d2);

        when(checkinsRequest.getDailyStatistics(groupId, checkinId)).thenReturn(mockDailyStats);

        List<DailyStatistics> result = zsxqService.getCheckinDailyStatistics(groupId, checkinId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(checkinsRequest).getDailyStatistics(groupId, checkinId);
    }

    @Test
    public void testGetCheckinJoinedUsers() {
        long groupId = 1300L;
        long checkinId = 1400L;
        User u1 = new User();
        u1.setUserId("1");
        User u2 = new User();
        u2.setUserId("2");
        List<User> mockUsers = Arrays.asList(u1, u2);

        when(checkinsRequest.getJoinedUsers(groupId, checkinId)).thenReturn(mockUsers);

        List<User> result = zsxqService.getCheckinJoinedUsers(groupId, checkinId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(checkinsRequest).getJoinedUsers(groupId, checkinId);
    }

    @Test
    public void testGetMyCheckins() {
        long groupId = 1500L;
        long checkinId = 1600L;
        Topic t1 = new Topic();
        t1.setTopicId(1L);
        List<Topic> mockTopics = Arrays.asList(t1);

        when(checkinsRequest.getMyCheckins(groupId, checkinId)).thenReturn(mockTopics);

        List<Topic> result = zsxqService.getMyCheckins(groupId, checkinId);

        assertNotNull(result);
        assertEquals(result.size(), 1);
        verify(checkinsRequest).getMyCheckins(groupId, checkinId);
    }

    @Test
    public void testGetMyCheckinDays() {
        long groupId = 1700L;
        long checkinId = 1800L;
        List<String> mockDays = Arrays.asList("2024-01-01", "2024-01-02", "2024-01-03");

        when(checkinsRequest.getMyCheckinDays(groupId, checkinId)).thenReturn(mockDays);

        List<String> result = zsxqService.getMyCheckinDays(groupId, checkinId);

        assertNotNull(result);
        assertEquals(result.size(), 3);
        assertTrue(result.contains("2024-01-01"));
        verify(checkinsRequest).getMyCheckinDays(groupId, checkinId);
    }

    @Test
    public void testGetMyCheckinStatistics() {
        long groupId = 1900L;
        long checkinId = 2000L;
        MyCheckinStatistics mockStats = new MyCheckinStatistics();

        when(checkinsRequest.getMyStatistics(groupId, checkinId)).thenReturn(mockStats);

        MyCheckinStatistics result = zsxqService.getMyCheckinStatistics(groupId, checkinId);

        assertNotNull(result);
        verify(checkinsRequest).getMyStatistics(groupId, checkinId);
    }

    @Test
    public void testCreateCheckin() {
        long groupId = 100L;
        CheckinsRequest.CreateCheckinParams params = new CheckinsRequest.CreateCheckinParams()
                .title("测试训练营")
                .text("完成7天打卡")
                .checkinDays(7)
                .type("accumulated")
                .showTopicsOnTimeline(false);

        Checkin mockCheckin = new Checkin();
        mockCheckin.setCheckinId(2001L);
        mockCheckin.setName("测试训练营");

        when(checkinsRequest.create(eq(groupId), any(CheckinsRequest.CreateCheckinParams.class)))
                .thenReturn(mockCheckin);

        Checkin result = zsxqService.createCheckin(groupId, params);

        assertNotNull(result);
        assertEquals(result.getCheckinId().longValue(), 2001L);
        assertEquals(result.getName(), "测试训练营");
        verify(checkinsRequest).create(eq(groupId), any(CheckinsRequest.CreateCheckinParams.class));
    }

    @Test
    public void testCreateCheckinWithLongPeriod() {
        long groupId = 200L;
        CheckinsRequest.CreateCheckinParams params = new CheckinsRequest.CreateCheckinParams()
                .title("长期训练营")
                .text("持续打卡30天")
                .checkinDays(30)
                .type("continuous")
                .showTopicsOnTimeline(true);

        Checkin mockCheckin = new Checkin();
        mockCheckin.setCheckinId(2002L);
        mockCheckin.setName("长期训练营");

        when(checkinsRequest.create(eq(groupId), any(CheckinsRequest.CreateCheckinParams.class)))
                .thenReturn(mockCheckin);

        Checkin result = zsxqService.createCheckin(groupId, params);

        assertNotNull(result);
        assertEquals(result.getCheckinId().longValue(), 2002L);
        assertEquals(result.getName(), "长期训练营");
        verify(checkinsRequest).create(eq(groupId), any(CheckinsRequest.CreateCheckinParams.class));
    }
}
