package com.zsxq.demo.controller;

import com.zsxq.demo.model.ApiResponse;
import com.zsxq.demo.service.ZsxqService;
import com.zsxq.sdk.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 知识星球 API 控制器 - 完整版
 */
@RestController
@RequestMapping("/api/zsxq")
@RequiredArgsConstructor
public class ZsxqController {

    private final ZsxqService zsxqService;

    // ==================== Users 模块 ====================

    /**
     * 获取当前用户信息
     */
    @GetMapping("/users/self")
    public ApiResponse<User> getCurrentUser() {
        try {
            User user = zsxqService.getCurrentUser();
            return ApiResponse.success(user);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取指定用户信息
     */
    @GetMapping("/users/{userId}")
    public ApiResponse<User> getUser(@PathVariable Long userId) {
        try {
            User user = zsxqService.getUser(userId);
            return ApiResponse.success(user);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取用户统计
     */
    @GetMapping("/users/{userId}/statistics")
    public ApiResponse<Map<String, Object>> getUserStatistics(@PathVariable Long userId) {
        try {
            Map<String, Object> stats = zsxqService.getUserStatistics(userId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取用户头像URL
     */
    @GetMapping("/users/{userId}/avatar")
    public ApiResponse<String> getUserAvatarUrl(@PathVariable Long userId) {
        try {
            String url = zsxqService.getUserAvatarUrl(userId);
            return ApiResponse.success(url);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取用户动态足迹
     */
    @GetMapping("/users/{userId}/footprints")
    public ApiResponse<List<Topic>> getUserFootprints(@PathVariable Long userId) {
        try {
            List<Topic> topics = zsxqService.getUserFootprints(userId);
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取用户创建的星球
     */
    @GetMapping("/users/{userId}/created-groups")
    public ApiResponse<List<Group>> getUserCreatedGroups(@PathVariable Long userId) {
        try {
            List<Group> groups = zsxqService.getUserCreatedGroups(userId);
            return ApiResponse.success(groups);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取屏蔽用户列表
     */
    @GetMapping("/users/blocked")
    public ApiResponse<List<User>> getBlockedUsers() {
        try {
            List<User> users = zsxqService.getBlockedUsers();
            return ApiResponse.success(users);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取贡献记录
     */
    @GetMapping("/users/self/contributions")
    public ApiResponse<List<Contribution>> getContributions() {
        try {
            List<Contribution> contributions = zsxqService.getContributions();
            return ApiResponse.success(contributions);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取贡献统计
     */
    @GetMapping("/users/self/contributions/statistics")
    public ApiResponse<ContributionStatistics> getContributionStats() {
        try {
            ContributionStatistics stats = zsxqService.getContributionStats();
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取成就摘要
     */
    @GetMapping("/users/self/achievements")
    public ApiResponse<List<AchievementSummary>> getAchievementsSummary() {
        try {
            List<AchievementSummary> summaries = zsxqService.getAchievementsSummary();
            return ApiResponse.success(summaries);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取关注者统计
     */
    @GetMapping("/users/self/followers/statistics")
    public ApiResponse<FollowerStatistics> getFollowerStats() {
        try {
            FollowerStatistics stats = zsxqService.getFollowerStats();
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取用户偏好配置
     */
    @GetMapping("/users/self/preferences")
    public ApiResponse<Map<String, Object>> getUserPreferences() {
        try {
            Map<String, Object> prefs = zsxqService.getUserPreferences();
            return ApiResponse.success(prefs);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球周榜排名
     */
    @GetMapping("/users/self/weekly-ranking")
    public ApiResponse<WeeklyRanking> getWeeklyRanking(@RequestParam Long groupId) {
        try {
            WeeklyRanking ranking = zsxqService.getWeeklyRanking(groupId);
            return ApiResponse.success(ranking);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== Groups 模块 ====================

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

    /**
     * 获取星球标签
     */
    @GetMapping("/groups/{groupId}/hashtags")
    public ApiResponse<List<Hashtag>> getGroupHashtags(@PathVariable Long groupId) {
        try {
            List<Hashtag> hashtags = zsxqService.getGroupHashtags(groupId);
            return ApiResponse.success(hashtags);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球菜单
     */
    @GetMapping("/groups/{groupId}/menus")
    public ApiResponse<List<Menu>> getGroupMenus(@PathVariable Long groupId) {
        try {
            List<Menu> menus = zsxqService.getGroupMenus(groupId);
            return ApiResponse.success(menus);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球角色成员
     */
    @GetMapping("/groups/{groupId}/role-members")
    public ApiResponse<RoleMembers> getGroupRoleMembers(@PathVariable Long groupId) {
        try {
            RoleMembers members = zsxqService.getGroupRoleMembers(groupId);
            return ApiResponse.success(members);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取成员信息
     */
    @GetMapping("/groups/{groupId}/members/{memberId}")
    public ApiResponse<User> getGroupMember(@PathVariable Long groupId, @PathVariable Long memberId) {
        try {
            User member = zsxqService.getGroupMember(groupId, memberId);
            return ApiResponse.success(member);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取成员活跃摘要
     */
    @GetMapping("/groups/{groupId}/members/{memberId}/summary")
    public ApiResponse<ActivitySummary> getMemberActivitySummary(@PathVariable Long groupId, @PathVariable Long memberId) {
        try {
            ActivitySummary summary = zsxqService.getMemberActivitySummary(groupId, memberId);
            return ApiResponse.success(summary);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球专栏列表
     */
    @GetMapping("/groups/{groupId}/columns")
    public ApiResponse<List<Column>> getGroupColumns(@PathVariable Long groupId) {
        try {
            List<Column> columns = zsxqService.getGroupColumns(groupId);
            return ApiResponse.success(columns);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取专栏汇总信息
     */
    @GetMapping("/groups/{groupId}/columns/summary")
    public ApiResponse<Map<String, Object>> getGroupColumnsSummary(@PathVariable Long groupId) {
        try {
            Map<String, Object> summary = zsxqService.getGroupColumnsSummary(groupId);
            return ApiResponse.success(summary);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球自定义标签
     */
    @GetMapping("/groups/{groupId}/labels")
    public ApiResponse<List<CustomTag>> getGroupCustomTags(@PathVariable Long groupId) {
        try {
            List<CustomTag> tags = zsxqService.getGroupCustomTags(groupId);
            return ApiResponse.success(tags);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球定时任务
     */
    @GetMapping("/groups/{groupId}/scheduled-tasks")
    public ApiResponse<List<ScheduledJob>> getGroupScheduledTasks(@PathVariable Long groupId) {
        try {
            List<ScheduledJob> tasks = zsxqService.getGroupScheduledTasks(groupId);
            return ApiResponse.success(tasks);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球风险预警
     */
    @GetMapping("/groups/{groupId}/warnings")
    public ApiResponse<GroupWarning> getGroupRiskWarnings(@PathVariable Long groupId) {
        try {
            GroupWarning warning = zsxqService.getGroupRiskWarnings(groupId);
            return ApiResponse.success(warning);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取未读话题数量
     */
    @GetMapping("/groups/unread-count")
    public ApiResponse<Map<String, Integer>> getUnreadCount() {
        try {
            Map<String, Integer> count = zsxqService.getUnreadCount();
            return ApiResponse.success(count);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }


    // 注意: Topics、Checkins、Dashboard、Ranking、Misc 等模块的接口
    // 已在各自的专门 Controller 中实现 (TopicsController, CheckinsController, DashboardController)
    // 避免路由冲突,此处不再重复定义
}
