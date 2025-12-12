package com.zsxq.demo.service;

import com.zsxq.demo.config.ZsxqProperties;
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.model.*;
import com.zsxq.sdk.request.GroupsRequest;
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
 * ZsxqService Groups 模块单元测试
 * 使用 TestNG + Mockito 框架
 */
public class ZsxqServiceGroupsTest {

    @Mock
    private ZsxqClient zsxqClient;

    @Mock
    private ZsxqProperties properties;

    @Mock
    private GroupsRequest groupsRequest;

    private ZsxqService zsxqService;

    private static final long DEFAULT_GROUP_ID = 123456L;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        zsxqService = new ZsxqService(zsxqClient, properties);
        when(zsxqClient.groups()).thenReturn(groupsRequest);
        when(properties.getGroupId()).thenReturn(DEFAULT_GROUP_ID);
    }

    @Test
    public void testGetMyGroups() {
        Group group1 = new Group();
        group1.setGroupId(1L);
        group1.setName("Group1");
        Group group2 = new Group();
        group2.setGroupId(2L);
        group2.setName("Group2");
        List<Group> mockGroups = Arrays.asList(group1, group2);

        when(groupsRequest.list()).thenReturn(mockGroups);

        List<Group> result = zsxqService.getMyGroups();

        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getName(), "Group1");
        verify(groupsRequest).list();
    }

    @Test
    public void testGetGroup() {
        long groupId = 100L;
        Group mockGroup = new Group();
        mockGroup.setGroupId(groupId);
        mockGroup.setName("TestGroup");

        when(groupsRequest.get(groupId)).thenReturn(mockGroup);

        Group result = zsxqService.getGroup(groupId);

        assertNotNull(result);
        assertEquals(result.getGroupId().longValue(), groupId);
        assertEquals(result.getName(), "TestGroup");
        verify(groupsRequest).get(groupId);
    }

    @Test
    public void testGetDefaultGroup() {
        Group mockGroup = new Group();
        mockGroup.setGroupId(DEFAULT_GROUP_ID);
        mockGroup.setName("DefaultGroup");

        when(groupsRequest.get(DEFAULT_GROUP_ID)).thenReturn(mockGroup);

        Group result = zsxqService.getDefaultGroup();

        assertNotNull(result);
        assertEquals(result.getGroupId().longValue(), DEFAULT_GROUP_ID);
        verify(groupsRequest).get(DEFAULT_GROUP_ID);
    }

    @Test
    public void testGetGroupStatistics() {
        long groupId = 200L;
        Map<String, Object> mockStats = new HashMap<>();
        mockStats.put("topics_count", 500);
        mockStats.put("members_count", 1000);

        when(groupsRequest.getStatistics(groupId)).thenReturn(mockStats);

        Map<String, Object> result = zsxqService.getGroupStatistics(groupId);

        assertNotNull(result);
        assertEquals(result.get("topics_count"), 500);
        assertEquals(result.get("members_count"), 1000);
        verify(groupsRequest).getStatistics(groupId);
    }

    @Test
    public void testGetGroupHashtags() {
        long groupId = 300L;
        Hashtag h1 = new Hashtag();
        h1.setHashtagId(1L);
        h1.setName("Tag1");
        Hashtag h2 = new Hashtag();
        h2.setHashtagId(2L);
        h2.setName("Tag2");
        List<Hashtag> mockHashtags = Arrays.asList(h1, h2);

        when(groupsRequest.getHashtags(groupId)).thenReturn(mockHashtags);

        List<Hashtag> result = zsxqService.getGroupHashtags(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getName(), "Tag1");
        verify(groupsRequest).getHashtags(groupId);
    }

    @Test
    public void testGetGroupMenus() {
        long groupId = 400L;
        Menu m1 = new Menu();
        Menu m2 = new Menu();
        List<Menu> mockMenus = Arrays.asList(m1, m2);

        when(groupsRequest.getMenus(groupId)).thenReturn(mockMenus);

        List<Menu> result = zsxqService.getGroupMenus(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(groupsRequest).getMenus(groupId);
    }

    @Test
    public void testGetGroupRoleMembers() {
        long groupId = 500L;
        RoleMembers mockRoleMembers = new RoleMembers();

        when(groupsRequest.getRoleMembers(groupId)).thenReturn(mockRoleMembers);

        RoleMembers result = zsxqService.getGroupRoleMembers(groupId);

        assertNotNull(result);
        verify(groupsRequest).getRoleMembers(groupId);
    }

    @Test
    public void testGetGroupMember() {
        long groupId = 600L;
        long memberId = 700L;
        User mockMember = new User();
        mockMember.setUserId(String.valueOf(memberId));
        mockMember.setName("MemberUser");

        when(groupsRequest.getMember(groupId, memberId)).thenReturn(mockMember);

        User result = zsxqService.getGroupMember(groupId, memberId);

        assertNotNull(result);
        assertEquals(result.getUserId(), String.valueOf(memberId));
        assertEquals(result.getName(), "MemberUser");
        verify(groupsRequest).getMember(groupId, memberId);
    }

    @Test
    public void testGetMemberActivitySummary() {
        long groupId = 800L;
        long memberId = 900L;
        ActivitySummary mockSummary = new ActivitySummary();

        when(groupsRequest.getMemberActivitySummary(groupId, memberId)).thenReturn(mockSummary);

        ActivitySummary result = zsxqService.getMemberActivitySummary(groupId, memberId);

        assertNotNull(result);
        verify(groupsRequest).getMemberActivitySummary(groupId, memberId);
    }

    @Test
    public void testGetGroupColumns() {
        long groupId = 1000L;
        Column c1 = new Column();
        Column c2 = new Column();
        List<Column> mockColumns = Arrays.asList(c1, c2);

        when(groupsRequest.getColumns(groupId)).thenReturn(mockColumns);

        List<Column> result = zsxqService.getGroupColumns(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(groupsRequest).getColumns(groupId);
    }

    @Test
    public void testGetGroupColumnsSummary() {
        long groupId = 1100L;
        Map<String, Object> mockSummary = new HashMap<>();
        mockSummary.put("total_columns", 5);
        mockSummary.put("total_topics", 100);

        when(groupsRequest.getColumnsSummary(groupId)).thenReturn(mockSummary);

        Map<String, Object> result = zsxqService.getGroupColumnsSummary(groupId);

        assertNotNull(result);
        assertEquals(result.get("total_columns"), 5);
        verify(groupsRequest).getColumnsSummary(groupId);
    }

    @Test
    public void testGetGroupCustomTags() {
        long groupId = 1200L;
        CustomTag t1 = new CustomTag();
        CustomTag t2 = new CustomTag();
        List<CustomTag> mockTags = Arrays.asList(t1, t2);

        when(groupsRequest.getCustomTags(groupId)).thenReturn(mockTags);

        List<CustomTag> result = zsxqService.getGroupCustomTags(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(groupsRequest).getCustomTags(groupId);
    }

    @Test
    public void testGetGroupScheduledTasks() {
        long groupId = 1300L;
        ScheduledJob j1 = new ScheduledJob();
        ScheduledJob j2 = new ScheduledJob();
        List<ScheduledJob> mockJobs = Arrays.asList(j1, j2);

        when(groupsRequest.getScheduledTasks(groupId)).thenReturn(mockJobs);

        List<ScheduledJob> result = zsxqService.getGroupScheduledTasks(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(groupsRequest).getScheduledTasks(groupId);
    }

    @Test
    public void testGetGroupRiskWarnings() {
        long groupId = 1400L;
        GroupWarning mockWarning = new GroupWarning();

        when(groupsRequest.getRiskWarnings(groupId)).thenReturn(mockWarning);

        GroupWarning result = zsxqService.getGroupRiskWarnings(groupId);

        assertNotNull(result);
        verify(groupsRequest).getRiskWarnings(groupId);
    }

    @Test
    public void testGetUnreadCount() {
        Map<String, Integer> mockCounts = new HashMap<>();
        mockCounts.put("group1", 10);
        mockCounts.put("group2", 20);

        when(groupsRequest.getUnreadCount()).thenReturn(mockCounts);

        Map<String, Integer> result = zsxqService.getUnreadCount();

        assertNotNull(result);
        assertEquals(result.get("group1").intValue(), 10);
        assertEquals(result.get("group2").intValue(), 20);
        verify(groupsRequest).getUnreadCount();
    }
}
