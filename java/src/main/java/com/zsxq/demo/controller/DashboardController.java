package com.zsxq.demo.controller;

import com.zsxq.demo.model.ApiResponse;
import com.zsxq.demo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 数据面板 API 控制器
 */
@RestController
@RequestMapping("/api/zsxq/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 获取星球数据概览
     */
    @GetMapping("/groups/{groupId}/overview")
    public ApiResponse<Map<String, Object>> getOverview(@PathVariable Long groupId) {
        try {
            Map<String, Object> overview = dashboardService.getOverview(groupId);
            return ApiResponse.success(overview);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球收入概览
     */
    @GetMapping("/groups/{groupId}/incomes")
    public ApiResponse<Map<String, Object>> getIncomes(@PathVariable Long groupId) {
        try {
            Map<String, Object> incomes = dashboardService.getIncomes(groupId);
            return ApiResponse.success(incomes);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
