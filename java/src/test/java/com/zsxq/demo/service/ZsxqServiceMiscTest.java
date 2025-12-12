package com.zsxq.demo.service;

import com.zsxq.demo.config.ZsxqProperties;
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.model.Activity;
import com.zsxq.sdk.model.GlobalConfig;
import com.zsxq.sdk.model.PkBattle;
import com.zsxq.sdk.model.PkGroup;
import com.zsxq.sdk.request.MiscRequest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * ZsxqService Misc 模块单元测试
 * 使用 TestNG + Mockito 框架
 */
public class ZsxqServiceMiscTest {

    @Mock
    private ZsxqClient zsxqClient;

    @Mock
    private ZsxqProperties properties;

    @Mock
    private MiscRequest miscRequest;

    private ZsxqService zsxqService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        zsxqService = new ZsxqService(zsxqClient, properties);
        when(zsxqClient.misc()).thenReturn(miscRequest);
    }

    @Test
    public void testGetActivities() {
        Activity a1 = new Activity();
        Activity a2 = new Activity();
        List<Activity> mockActivities = Arrays.asList(a1, a2);

        when(miscRequest.getActivities()).thenReturn(mockActivities);

        List<Activity> result = zsxqService.getActivities();

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(miscRequest).getActivities();
    }

    @Test
    public void testGetActivitiesWithOptions() {
        String scope = "general";
        Integer count = 10;
        Activity a1 = new Activity();
        List<Activity> mockActivities = Arrays.asList(a1);

        when(miscRequest.getActivities(any(MiscRequest.ActivitiesOptions.class))).thenReturn(mockActivities);

        List<Activity> result = zsxqService.getActivities(scope, count);

        assertNotNull(result);
        assertEquals(result.size(), 1);
        verify(miscRequest).getActivities(any(MiscRequest.ActivitiesOptions.class));
    }

    @Test
    public void testGetGlobalConfig() {
        GlobalConfig mockConfig = new GlobalConfig();

        when(miscRequest.getGlobalConfig()).thenReturn(mockConfig);

        GlobalConfig result = zsxqService.getGlobalConfig();

        assertNotNull(result);
        verify(miscRequest).getGlobalConfig();
    }

    @Test
    public void testGetPkGroup() {
        long pkGroupId = 100L;
        PkGroup mockPkGroup = new PkGroup();

        when(miscRequest.getPkGroup(pkGroupId)).thenReturn(mockPkGroup);

        PkGroup result = zsxqService.getPkGroup(pkGroupId);

        assertNotNull(result);
        verify(miscRequest).getPkGroup(pkGroupId);
    }

    @Test
    public void testGetPkBattles() {
        long pkGroupId = 200L;
        PkBattle b1 = new PkBattle();
        PkBattle b2 = new PkBattle();
        List<PkBattle> mockBattles = Arrays.asList(b1, b2);

        when(miscRequest.getPkBattles(pkGroupId)).thenReturn(mockBattles);

        List<PkBattle> result = zsxqService.getPkBattles(pkGroupId);

        assertNotNull(result);
        assertEquals(result.size(), 2);
        verify(miscRequest).getPkBattles(pkGroupId);
    }
}
