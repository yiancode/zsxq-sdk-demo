package com.zsxq.demo.service;

import com.zsxq.demo.config.ZsxqProperties;
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.model.RankingItem;
import com.zsxq.sdk.model.RankingStatistics;
import com.zsxq.sdk.model.ScoreboardSettings;
import com.zsxq.sdk.request.RankingRequest;
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
 * ZsxqService Ranking 模块单元测试
 * 使用 TestNG + Mockito 框架
 */
public class ZsxqServiceRankingTest {

    @Mock
    private ZsxqClient zsxqClient;

    @Mock
    private ZsxqProperties properties;

    @Mock
    private RankingRequest rankingRequest;

    private ZsxqService zsxqService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        zsxqService = new ZsxqService(zsxqClient, properties);
        when(zsxqClient.ranking()).thenReturn(rankingRequest);
    }

    @Test
    public void testGetGroupRanking() {
        long groupId = 100L;
        RankingItem r1 = new RankingItem();
        RankingItem r2 = new RankingItem();
        RankingItem r3 = new RankingItem();
        List<RankingItem> mockRankings = Arrays.asList(r1, r2, r3);

        when(rankingRequest.getGroupRanking(groupId)).thenReturn(mockRankings);

        List<RankingItem> result = zsxqService.getGroupRanking(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 3);
        verify(rankingRequest).getGroupRanking(groupId);
    }

    @Test
    public void testGetGroupRankingStats() {
        long groupId = 200L;
        RankingStatistics mockStats = new RankingStatistics();

        when(rankingRequest.getGroupRankingStats(groupId)).thenReturn(mockStats);

        RankingStatistics result = zsxqService.getGroupRankingStats(groupId);

        assertNotNull(result);
        verify(rankingRequest).getGroupRankingStats(groupId);
    }

    @Test
    public void testGetScoreRanking() {
        long groupId = 300L;
        RankingItem r1 = new RankingItem();
        RankingItem r2 = new RankingItem();
        List<RankingItem> mockRankings = Arrays.asList(r1, r2);

        when(rankingRequest.getScoreRanking(groupId)).thenReturn(mockRankings);

        List<RankingItem> result = zsxqService.getScoreRanking(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(rankingRequest).getScoreRanking(groupId);
    }

    @Test
    public void testGetMyScoreStats() {
        long groupId = 400L;
        Map<String, Object> mockStats = new HashMap<>();
        mockStats.put("total_score", 1000);
        mockStats.put("rank", 5);

        when(rankingRequest.getMyScoreStats(groupId)).thenReturn(mockStats);

        Map<String, Object> result = zsxqService.getMyScoreStats(groupId);

        assertNotNull(result);
        assertEquals(result.get("total_score"), 1000);
        assertEquals(result.get("rank"), 5);
        verify(rankingRequest).getMyScoreStats(groupId);
    }

    @Test
    public void testGetScoreboardSettings() {
        long groupId = 500L;
        ScoreboardSettings mockSettings = new ScoreboardSettings();

        when(rankingRequest.getScoreboardSettings(groupId)).thenReturn(mockSettings);

        ScoreboardSettings result = zsxqService.getScoreboardSettings(groupId);

        assertNotNull(result);
        verify(rankingRequest).getScoreboardSettings(groupId);
    }

    @Test
    public void testGetInvitationRanking() {
        long groupId = 600L;
        RankingItem r1 = new RankingItem();
        RankingItem r2 = new RankingItem();
        List<RankingItem> mockRankings = Arrays.asList(r1, r2);

        when(rankingRequest.getInvitationRanking(groupId)).thenReturn(mockRankings);

        List<RankingItem> result = zsxqService.getInvitationRanking(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(rankingRequest).getInvitationRanking(groupId);
    }

    @Test
    public void testGetContributionRanking() {
        long groupId = 700L;
        RankingItem r1 = new RankingItem();
        RankingItem r2 = new RankingItem();
        RankingItem r3 = new RankingItem();
        List<RankingItem> mockRankings = Arrays.asList(r1, r2, r3);

        when(rankingRequest.getContributionRanking(groupId)).thenReturn(mockRankings);

        List<RankingItem> result = zsxqService.getContributionRanking(groupId);

        assertNotNull(result);
        assertEquals(result.size(), 3);
        verify(rankingRequest).getContributionRanking(groupId);
    }
}
