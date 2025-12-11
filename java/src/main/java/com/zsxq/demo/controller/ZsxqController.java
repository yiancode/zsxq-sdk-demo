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

    // ==================== Topics 模块 ====================

    /**
     * 获取话题列表
     */
    @GetMapping("/groups/{groupId}/topics")
    public ApiResponse<List<Topic>> getTopics(
            @PathVariable Long groupId,
            @RequestParam(required = false) String scope,
            @RequestParam(required = false) Integer count) {
        try {
            List<Topic> topics;
            if (scope != null || count != null) {
                topics = zsxqService.getTopics(groupId, scope, count);
            } else {
                topics = zsxqService.getTopics(groupId);
            }
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取话题详情
     */
    @GetMapping("/topics/{topicId}")
    public ApiResponse<Topic> getTopic(@PathVariable Long topicId) {
        try {
            Topic topic = zsxqService.getTopic(topicId);
            return ApiResponse.success(topic);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取话题评论
     */
    @GetMapping("/topics/{topicId}/comments")
    public ApiResponse<List<Comment>> getTopicComments(@PathVariable Long topicId) {
        try {
            List<Comment> comments = zsxqService.getTopicComments(topicId);
            return ApiResponse.success(comments);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取话题打赏列表
     */
    @GetMapping("/topics/{topicId}/rewards")
    public ApiResponse<List<Reward>> getTopicRewards(@PathVariable Long topicId) {
        try {
            List<Reward> rewards = zsxqService.getTopicRewards(topicId);
            return ApiResponse.success(rewards);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取置顶话题
     */
    @GetMapping("/groups/{groupId}/topics/sticky")
    public ApiResponse<List<Topic>> getStickyTopics(@PathVariable Long groupId) {
        try {
            List<Topic> topics = zsxqService.getStickyTopics(groupId);
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 按标签获取话题
     */
    @GetMapping("/hashtags/{hashtagId}/topics")
    public ApiResponse<List<Topic>> getTopicsByHashtag(@PathVariable Long hashtagId) {
        try {
            List<Topic> topics = zsxqService.getTopicsByHashtag(hashtagId);
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 按专栏获取话题
     */
    @GetMapping("/groups/{groupId}/columns/{columnId}/topics")
    public ApiResponse<List<Topic>> getTopicsByColumn(@PathVariable Long groupId, @PathVariable Long columnId) {
        try {
            List<Topic> topics = zsxqService.getTopicsByColumn(groupId, columnId);
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== Checkins 模块 ====================

    /**
     * 获取打卡项目列表
     */
    @GetMapping("/groups/{groupId}/checkins")
    public ApiResponse<List<Checkin>> getCheckins(
            @PathVariable Long groupId,
            @RequestParam(required = false) String scope) {
        try {
            List<Checkin> checkins;
            if (scope != null) {
                checkins = zsxqService.getCheckins(groupId, scope);
            } else {
                checkins = zsxqService.getCheckins(groupId);
            }
            return ApiResponse.success(checkins);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取打卡项目详情
     */
    @GetMapping("/groups/{groupId}/checkins/{checkinId}")
    public ApiResponse<Checkin> getCheckin(@PathVariable Long groupId, @PathVariable Long checkinId) {
        try {
            Checkin checkin = zsxqService.getCheckin(groupId, checkinId);
            return ApiResponse.success(checkin);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取打卡统计
     */
    @GetMapping("/groups/{groupId}/checkins/{checkinId}/statistics")
    public ApiResponse<CheckinStatistics> getCheckinStatistics(@PathVariable Long groupId, @PathVariable Long checkinId) {
        try {
            CheckinStatistics stats = zsxqService.getCheckinStatistics(groupId, checkinId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取打卡排行榜
     */
    @GetMapping("/groups/{groupId}/checkins/{checkinId}/ranking")
    public ApiResponse<List<RankingItem>> getCheckinRankingList(@PathVariable Long groupId, @PathVariable Long checkinId) {
        try {
            List<RankingItem> ranking = zsxqService.getCheckinRankingList(groupId, checkinId);
            return ApiResponse.success(ranking);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取打卡话题列表
     */
    @GetMapping("/groups/{groupId}/checkins/{checkinId}/topics")
    public ApiResponse<List<Topic>> getCheckinTopics(@PathVariable Long groupId, @PathVariable Long checkinId) {
        try {
            List<Topic> topics = zsxqService.getCheckinTopics(groupId, checkinId);
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取打卡每日统计
     */
    @GetMapping("/groups/{groupId}/checkins/{checkinId}/daily-statistics")
    public ApiResponse<List<DailyStatistics>> getCheckinDailyStatistics(@PathVariable Long groupId, @PathVariable Long checkinId) {
        try {
            List<DailyStatistics> stats = zsxqService.getCheckinDailyStatistics(groupId, checkinId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取打卡参与用户
     */
    @GetMapping("/groups/{groupId}/checkins/{checkinId}/joined-users")
    public ApiResponse<List<User>> getCheckinJoinedUsers(@PathVariable Long groupId, @PathVariable Long checkinId) {
        try {
            List<User> users = zsxqService.getCheckinJoinedUsers(groupId, checkinId);
            return ApiResponse.success(users);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取我的打卡记录
     */
    @GetMapping("/groups/{groupId}/checkins/{checkinId}/my-records")
    public ApiResponse<List<Topic>> getMyCheckins(@PathVariable Long groupId, @PathVariable Long checkinId) {
        try {
            List<Topic> topics = zsxqService.getMyCheckins(groupId, checkinId);
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取我的打卡日期
     */
    @GetMapping("/groups/{groupId}/checkins/{checkinId}/my-days")
    public ApiResponse<List<String>> getMyCheckinDays(@PathVariable Long groupId, @PathVariable Long checkinId) {
        try {
            List<String> days = zsxqService.getMyCheckinDays(groupId, checkinId);
            return ApiResponse.success(days);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取我的打卡统计
     */
    @GetMapping("/groups/{groupId}/checkins/{checkinId}/my-statistics")
    public ApiResponse<MyCheckinStatistics> getMyCheckinStatistics(@PathVariable Long groupId, @PathVariable Long checkinId) {
        try {
            MyCheckinStatistics stats = zsxqService.getMyCheckinStatistics(groupId, checkinId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== Dashboard 模块 ====================

    /**
     * 获取星球概览
     */
    @GetMapping("/dashboard/groups/{groupId}/overview")
    public ApiResponse<Map<String, Object>> getDashboardOverview(@PathVariable Long groupId) {
        try {
            Map<String, Object> overview = zsxqService.getDashboardOverview(groupId);
            return ApiResponse.success(overview);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取收入概览
     */
    @GetMapping("/dashboard/groups/{groupId}/incomes")
    public ApiResponse<Map<String, Object>> getDashboardIncomes(@PathVariable Long groupId) {
        try {
            Map<String, Object> incomes = zsxqService.getDashboardIncomes(groupId);
            return ApiResponse.success(incomes);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球权限配置
     */
    @GetMapping("/dashboard/groups/{groupId}/privileges")
    public ApiResponse<Map<String, Object>> getDashboardPrivileges(@PathVariable Long groupId) {
        try {
            Map<String, Object> privileges = zsxqService.getDashboardPrivileges(groupId);
            return ApiResponse.success(privileges);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取发票统计
     */
    @GetMapping("/dashboard/invoices/statistics")
    public ApiResponse<InvoiceStats> getInvoiceStats() {
        try {
            InvoiceStats stats = zsxqService.getInvoiceStats();
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取积分排行
     */
    @GetMapping("/dashboard/groups/{groupId}/scoreboard/ranking")
    public ApiResponse<List<RankingItem>> getScoreboardRanking(@PathVariable Long groupId) {
        try {
            List<RankingItem> ranking = zsxqService.getScoreboardRanking(groupId);
            return ApiResponse.success(ranking);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== Ranking 模块 ====================

    /**
     * 获取星球排行榜
     */
    @GetMapping("/ranking/groups/{groupId}")
    public ApiResponse<List<RankingItem>> getGroupRanking(@PathVariable Long groupId) {
        try {
            List<RankingItem> ranking = zsxqService.getGroupRanking(groupId);
            return ApiResponse.success(ranking);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球排行统计
     */
    @GetMapping("/ranking/groups/{groupId}/statistics")
    public ApiResponse<RankingStatistics> getGroupRankingStats(@PathVariable Long groupId) {
        try {
            RankingStatistics stats = zsxqService.getGroupRankingStats(groupId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取积分排行榜
     */
    @GetMapping("/ranking/groups/{groupId}/score")
    public ApiResponse<List<RankingItem>> getScoreRanking(@PathVariable Long groupId) {
        try {
            List<RankingItem> ranking = zsxqService.getScoreRanking(groupId);
            return ApiResponse.success(ranking);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取我的积分统计
     */
    @GetMapping("/ranking/groups/{groupId}/score/my-statistics")
    public ApiResponse<Map<String, Object>> getMyScoreStats(@PathVariable Long groupId) {
        try {
            Map<String, Object> stats = zsxqService.getMyScoreStats(groupId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取积分榜设置
     */
    @GetMapping("/ranking/groups/{groupId}/scoreboard/settings")
    public ApiResponse<ScoreboardSettings> getScoreboardSettings(@PathVariable Long groupId) {
        try {
            ScoreboardSettings settings = zsxqService.getScoreboardSettings(groupId);
            return ApiResponse.success(settings);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取邀请排行榜
     */
    @GetMapping("/ranking/groups/{groupId}/invitations")
    public ApiResponse<List<RankingItem>> getInvitationRanking(@PathVariable Long groupId) {
        try {
            List<RankingItem> ranking = zsxqService.getInvitationRanking(groupId);
            return ApiResponse.success(ranking);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取贡献排行榜
     */
    @GetMapping("/ranking/groups/{groupId}/contributions")
    public ApiResponse<List<RankingItem>> getContributionRanking(@PathVariable Long groupId) {
        try {
            List<RankingItem> ranking = zsxqService.getContributionRanking(groupId);
            return ApiResponse.success(ranking);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== Misc 模块 ====================

    /**
     * 获取动态列表
     */
    @GetMapping("/dynamics")
    public ApiResponse<List<Activity>> getActivities(
            @RequestParam(required = false) String scope,
            @RequestParam(required = false) Integer count) {
        try {
            List<Activity> activities;
            if (scope != null || count != null) {
                activities = zsxqService.getActivities(scope, count);
            } else {
                activities = zsxqService.getActivities();
            }
            return ApiResponse.success(activities);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取全局配置
     */
    @GetMapping("/settings")
    public ApiResponse<GlobalConfig> getGlobalConfig() {
        try {
            GlobalConfig config = zsxqService.getGlobalConfig();
            return ApiResponse.success(config);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取 PK 群组详情
     */
    @GetMapping("/pk-groups/{pkGroupId}")
    public ApiResponse<PkGroup> getPkGroup(@PathVariable Long pkGroupId) {
        try {
            PkGroup group = zsxqService.getPkGroup(pkGroupId);
            return ApiResponse.success(group);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取 PK 对战记录
     */
    @GetMapping("/pk-groups/{pkGroupId}/battles")
    public ApiResponse<List<PkBattle>> getPkBattles(@PathVariable Long pkGroupId) {
        try {
            List<PkBattle> battles = zsxqService.getPkBattles(pkGroupId);
            return ApiResponse.success(battles);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    // ==================== 兼容旧接口 ====================

    /**
     * 获取当前用户信息（旧接口，兼容保留）
     */
    @GetMapping("/user/self")
    public ApiResponse<User> getCurrentUserLegacy() {
        return getCurrentUser();
    }

    /**
     * 获取用户统计（旧接口，兼容保留）
     */
    @GetMapping("/user/{userId}/statistics")
    public ApiResponse<Map<String, Object>> getUserStatisticsLegacy(@PathVariable Long userId) {
        return getUserStatistics(userId);
    }
}
