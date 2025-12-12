package com.zsxq.demo.service;

import com.zsxq.demo.config.ZsxqProperties;
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.model.InvoiceStats;
import com.zsxq.sdk.model.RankingItem;
import com.zsxq.sdk.request.DashboardRequest;
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
 * ZsxqService Dashboard 模块单元测试
 * 使用 TestNG + Mockito 框架
 */
public class ZsxqServiceDashboardTest {

    @Mock
    private ZsxqClient zsxqClient;

    @Mock
    private ZsxqProperties properties;

    @Mock
    private DashboardRequest dashboardRequest;

    private ZsxqService zsxqService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        zsxqService = new ZsxqService(zsxqClient, properties);
        when(zsxqClient.dashboard()).thenReturn(dashboardRequest);
    }

    @Test
    public void testGetDashboardOverview() {
        long groupId = 100L;
        Map<String, Object> mockOverview = new HashMap<>();
        mockOverview.put("members_count", 1000);
        mockOverview.put("topics_count", 500);
        mockOverview.put("income", 10000.0);

        when(dashboardRequest.getOverview(groupId)).thenReturn(mockOverview);

        Map<String, Object> result = zsxqService.getDashboardOverview(groupId);

        assertNotNull(result);
        assertEquals(result.get("members_count"), 1000);
        assertEquals(result.get("topics_count"), 500);
        verify(dashboardRequest).getOverview(groupId);
    }

    @Test
    public void testGetDashboardIncomes() {
        long groupId = 200L;
        Map<String, Object> mockIncomes = new HashMap<>();
        mockIncomes.put("total_income", 50000.0);
        mockIncomes.put("monthly_income", 5000.0);

        when(dashboardRequest.getIncomes(groupId)).thenReturn(mockIncomes);

        Map<String, Object> result = zsxqService.getDashboardIncomes(groupId);

        assertNotNull(result);
        assertEquals(result.get("total_income"), 50000.0);
        assertEquals(result.get("monthly_income"), 5000.0);
        verify(dashboardRequest).getIncomes(groupId);
    }

    @Test
    public void testGetDashboardPrivileges() {
        long groupId = 300L;
        Map<String, Object> mockPrivileges = new HashMap<>();
        mockPrivileges.put("can_post", true);
        mockPrivileges.put("can_comment", true);
        mockPrivileges.put("can_delete", false);

        when(dashboardRequest.getPrivileges(groupId)).thenReturn(mockPrivileges);

        Map<String, Object> result = zsxqService.getDashboardPrivileges(groupId);

        assertNotNull(result);
        assertEquals(result.get("can_post"), true);
        assertEquals(result.get("can_delete"), false);
        verify(dashboardRequest).getPrivileges(groupId);
    }

    @Test
    public void testGetInvoiceStats() {
        InvoiceStats mockStats = new InvoiceStats();

        when(dashboardRequest.getInvoiceStats()).thenReturn(mockStats);

        InvoiceStats result = zsxqService.getInvoiceStats();

        assertNotNull(result);
        verify(dashboardRequest).getInvoiceStats();
    }

    @Test
    public void testGetScoreboardRanking() {
        long groupId = 400L;
        RankingItem r1 = new RankingItem();
        RankingItem r2 = new RankingItem();
        List<RankingItem> mockRankings = Arrays.asList(r1, r2);

        when(dashboardRequest.getScoreboardRanking(groupId)).thenReturn(mockRankings);

        List<RankingItem> result = zsxqService.getScoreboardRanking(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(dashboardRequest).getScoreboardRanking(groupId);
    }
}
