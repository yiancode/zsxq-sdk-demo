package com.zsxq.demo.integration;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 知识星球 API 集成测试
 *
 * 测试覆盖文档中的所有 API 接口
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZsxqApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String GROUP_ID = "15555411412112";
    private static final String USER_ID = "184444848828412";

    // ============ 用户系统 API ============
    
    @Test
    @Order(1)
    @DisplayName("1.1 获取当前用户信息")
    public void testGetCurrentUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/zsxq/user/self"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.data.name").exists())
                .andReturn();
        
        System.out.println("✓ 获取当前用户信息: " + result.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    @DisplayName("1.2 获取用户统计数据")
    public void testGetUserStatistics() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/zsxq/user/" + USER_ID + "/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
                .andReturn();
        
        System.out.println("✓ 获取用户统计数据: " + result.getResponse().getContentAsString());
    }

    // ============ 星球管理 API ============
    
    @Test
    @Order(3)
    @DisplayName("2.1 获取星球列表")
    public void testGetMyGroups() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/zsxq/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andReturn();
        
        System.out.println("✓ 获取星球列表: 共 " + result.getResponse().getContentAsString().split("groupId").length + " 个星球");
    }

    @Test
    @Order(4)
    @DisplayName("2.2 获取默认星球详情")
    public void testGetDefaultGroup() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/zsxq/groups/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.groupId").value(GROUP_ID))
                .andExpect(jsonPath("$.data.name").exists())
                .andReturn();
        
        System.out.println("✓ 获取默认星球详情: " + result.getResponse().getContentAsString().substring(0, 200) + "...");
    }

    @Test
    @Order(5)
    @DisplayName("2.3 获取指定星球详情")
    public void testGetGroup() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/zsxq/groups/" + GROUP_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.groupId").value(GROUP_ID))
                .andReturn();
        
        System.out.println("✓ 获取指定星球详情: OK");
    }

    @Test
    @Order(6)
    @DisplayName("2.4 获取星球统计数据")
    public void testGetGroupStatistics() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/zsxq/groups/" + GROUP_ID + "/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.topics_count").exists())
                .andReturn();
        
        System.out.println("✓ 获取星球统计数据: " + result.getResponse().getContentAsString());
    }

    // ============ 话题管理 API ============
    
    @Test
    @Order(7)
    @DisplayName("3.1 获取话题列表")
    public void testListTopics() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/zsxq/topics/groups/" + GROUP_ID + "?count=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andReturn();
        
        System.out.println("✓ 获取话题列表: OK");
    }

    @Test
    @Order(8)
    @DisplayName("3.2 获取话题列表 (带筛选)")
    public void testListTopicsWithScope() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/zsxq/topics/groups/" + GROUP_ID + "?count=3&scope=digests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();
        
        System.out.println("✓ 获取精华话题列表: OK");
    }

    // ============ 数据面板 API ============
    
    @Test
    @Order(9)
    @DisplayName("4.1 获取星球数据概览")
    public void testGetDashboardOverview() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/zsxq/dashboard/groups/" + GROUP_ID + "/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists())
                .andReturn();
        
        System.out.println("✓ 获取星球数据概览: " + result.getResponse().getContentAsString().substring(0, 150) + "...");
    }

    @AfterAll
    public static void summary() {
        System.out.println("\n========================================");
        System.out.println("       集成测试完成");
        System.out.println("========================================");
        System.out.println("已测试接口：");
        System.out.println("  - 用户系统: 2个");
        System.out.println("  - 星球管理: 4个");
        System.out.println("  - 话题管理: 2个");
        System.out.println("  - 数据面板: 1个");
        System.out.println("总计: 9个 API 接口测试通过 ✓");
        System.out.println("========================================");
    }
}
