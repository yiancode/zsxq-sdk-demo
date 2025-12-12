package com.zsxq.demo.service;

import com.zsxq.demo.config.ZsxqProperties;
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.model.*;
import com.zsxq.sdk.request.UsersRequest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * ZsxqService Users 模块单元测试
 * 使用 TestNG + Mockito 框架
 */
public class ZsxqServiceUsersTest {

    @Mock
    private ZsxqClient zsxqClient;

    @Mock
    private ZsxqProperties properties;

    @Mock
    private UsersRequest usersRequest;

    private ZsxqService zsxqService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        zsxqService = new ZsxqService(zsxqClient, properties);
        when(zsxqClient.users()).thenReturn(usersRequest);
    }

    @Test
    public void testGetCurrentUser() {
        User mockUser = new User();
        mockUser.setUserId("123");
        mockUser.setName("TestUser");

        when(usersRequest.self()).thenReturn(mockUser);

        User result = zsxqService.getCurrentUser();

        assertNotNull(result);
        assertEquals(result.getUserId(), "123");
        assertEquals(result.getName(), "TestUser");
        verify(usersRequest).self();
    }

    @Test
    public void testGetUser() {
        long userId = 456L;
        User mockUser = new User();
        mockUser.setUserId(String.valueOf(userId));
        mockUser.setName("SpecificUser");

        when(usersRequest.get(userId)).thenReturn(mockUser);

        User result = zsxqService.getUser(userId);

        assertNotNull(result);
        assertEquals(result.getUserId(), String.valueOf(userId));
        assertEquals(result.getName(), "SpecificUser");
        verify(usersRequest).get(userId);
    }

    @Test
    public void testGetUserStatistics() {
        long userId = 789L;
        Map<String, Object> mockStats = new HashMap<>();
        mockStats.put("topics_count", 100);
        mockStats.put("comments_count", 200);

        when(usersRequest.getStatistics(userId)).thenReturn(mockStats);

        Map<String, Object> result = zsxqService.getUserStatistics(userId);

        assertNotNull(result);
        assertEquals(result.get("topics_count"), 100);
        assertEquals(result.get("comments_count"), 200);
        verify(usersRequest).getStatistics(userId);
    }

    @Test
    public void testGetUserAvatarUrl() {
        long userId = 111L;
        String expectedUrl = "https://example.com/avatar.jpg";

        when(usersRequest.getAvatarUrl(userId)).thenReturn(expectedUrl);

        String result = zsxqService.getUserAvatarUrl(userId);

        assertNotNull(result);
        assertEquals(result, expectedUrl);
        verify(usersRequest).getAvatarUrl(userId);
    }

    @Test
    public void testGetUserFootprints() {
        long userId = 222L;
        Topic topic1 = new Topic();
        topic1.setTopicId(1L);
        Topic topic2 = new Topic();
        topic2.setTopicId(2L);
        List<Topic> mockTopics = Arrays.asList(topic1, topic2);

        when(usersRequest.getFootprints(userId)).thenReturn(mockTopics);

        List<Topic> result = zsxqService.getUserFootprints(userId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(usersRequest).getFootprints(userId);
    }

    @Test
    public void testGetUserCreatedGroups() {
        long userId = 333L;
        Group group1 = new Group();
        group1.setGroupId(1L);
        Group group2 = new Group();
        group2.setGroupId(2L);
        List<Group> mockGroups = Arrays.asList(group1, group2);

        when(usersRequest.getCreatedGroups(userId)).thenReturn(mockGroups);

        List<Group> result = zsxqService.getUserCreatedGroups(userId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(usersRequest).getCreatedGroups(userId);
    }

    @Test
    public void testGetBlockedUsers() {
        User user1 = new User();
        user1.setUserId("1");
        User user2 = new User();
        user2.setUserId("2");
        List<User> mockUsers = Arrays.asList(user1, user2);

        when(usersRequest.getBlockedUsers()).thenReturn(mockUsers);

        List<User> result = zsxqService.getBlockedUsers();

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(usersRequest).getBlockedUsers();
    }

    @Test
    public void testGetContributions() {
        Contribution c1 = new Contribution();
        Contribution c2 = new Contribution();
        List<Contribution> mockContributions = Arrays.asList(c1, c2);

        when(usersRequest.getContributions()).thenReturn(mockContributions);

        List<Contribution> result = zsxqService.getContributions();

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(usersRequest).getContributions();
    }

    @Test
    public void testGetContributionStats() {
        ContributionStatistics mockStats = new ContributionStatistics();

        when(usersRequest.getContributionStats()).thenReturn(mockStats);

        ContributionStatistics result = zsxqService.getContributionStats();

        assertNotNull(result);
        verify(usersRequest).getContributionStats();
    }

    @Test
    public void testGetAchievementsSummary() {
        AchievementSummary a1 = new AchievementSummary();
        AchievementSummary a2 = new AchievementSummary();
        List<AchievementSummary> mockSummaries = Arrays.asList(a1, a2);

        when(usersRequest.getAchievementsSummary()).thenReturn(mockSummaries);

        List<AchievementSummary> result = zsxqService.getAchievementsSummary();

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(usersRequest).getAchievementsSummary();
    }

    @Test
    public void testGetFollowerStats() {
        FollowerStatistics mockStats = new FollowerStatistics();

        when(usersRequest.getFollowerStats()).thenReturn(mockStats);

        FollowerStatistics result = zsxqService.getFollowerStats();

        assertNotNull(result);
        verify(usersRequest).getFollowerStats();
    }

    @Test
    public void testGetUserPreferences() {
        Map<String, Object> mockPrefs = new HashMap<>();
        mockPrefs.put("notification_enabled", true);
        mockPrefs.put("theme", "dark");

        when(usersRequest.getPreferences()).thenReturn(mockPrefs);

        Map<String, Object> result = zsxqService.getUserPreferences();

        assertNotNull(result);
        assertEquals(result.get("notification_enabled"), true);
        assertEquals(result.get("theme"), "dark");
        verify(usersRequest).getPreferences();
    }

    @Test
    public void testGetWeeklyRanking() {
        long groupId = 444L;
        WeeklyRanking mockRanking = new WeeklyRanking();

        when(usersRequest.getWeeklyRanking(groupId)).thenReturn(mockRanking);

        WeeklyRanking result = zsxqService.getWeeklyRanking(groupId);

        assertNotNull(result);
        verify(usersRequest).getWeeklyRanking(groupId);
    }
}
