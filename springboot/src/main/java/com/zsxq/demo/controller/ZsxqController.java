package com.zsxq.demo.controller;

import com.zsxq.demo.model.ApiResponse;
import com.zsxq.demo.service.ZsxqService;
import com.zsxq.sdk.model.Group;
import com.zsxq.sdk.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 知识星球 API 控制器
 */
@RestController
@RequestMapping("/api/zsxq")
@RequiredArgsConstructor
public class ZsxqController {

    private final ZsxqService zsxqService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/user/self")
    public ApiResponse<User> getCurrentUser() {
        try {
            User user = zsxqService.getCurrentUser();
            return ApiResponse.success(user);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取用户统计
     */
    @GetMapping("/user/{userId}/statistics")
    public ApiResponse<Map<String, Object>> getUserStatistics(@PathVariable Long userId) {
        try {
            Map<String, Object> stats = zsxqService.getUserStatistics(userId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取我的星球列表
     */
    @GetMapping("/groups")
    public ApiResponse<List<Group>> getMyGroups() {
        try {
            List<Group> groups = zsxqService.getMyGroups();
            return ApiResponse.success(groups);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球详情
     */
    @GetMapping("/groups/{groupId}")
    public ApiResponse<Group> getGroup(@PathVariable Long groupId) {
        try {
            Group group = zsxqService.getGroup(groupId);
            return ApiResponse.success(group);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取默认星球详情
     */
    @GetMapping("/groups/default")
    public ApiResponse<Group> getDefaultGroup() {
        try {
            Group group = zsxqService.getDefaultGroup();
            return ApiResponse.success(group);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球统计
     */
    @GetMapping("/groups/{groupId}/statistics")
    public ApiResponse<Map<String, Object>> getGroupStatistics(@PathVariable Long groupId) {
        try {
            Map<String, Object> stats = zsxqService.getGroupStatistics(groupId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
