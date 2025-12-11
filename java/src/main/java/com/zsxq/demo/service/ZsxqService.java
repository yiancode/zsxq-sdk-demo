package com.zsxq.demo.service;

import com.zsxq.demo.config.ZsxqProperties;
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.*;
import com.zsxq.sdk.request.CheckinsRequest;
import com.zsxq.sdk.request.MiscRequest;
import com.zsxq.sdk.request.TopicsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 知识星球服务 - 完整版
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ZsxqService {

    private final ZsxqClient zsxqClient;
    private final ZsxqProperties properties;

    // ==================== Users 模块 ====================

    /**
     * 获取当前用户信息
     */
    public User getCurrentUser() {
        try {
            return zsxqClient.users().self();
        } catch (ZsxqException e) {
            log.error("获取当前用户失败", e);
            throw new RuntimeException("获取当前用户失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取指定用户信息
     */
    public User getUser(long userId) {
        try {
            return zsxqClient.users().get(userId);
        } catch (ZsxqException e) {
            log.error("获取用户信息失败: userId={}", userId, e);
            throw new RuntimeException("获取用户信息失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取用户统计
     */
    public Map<String, Object> getUserStatistics(long userId) {
        try {
            return zsxqClient.users().getStatistics(userId);
        } catch (ZsxqException e) {
            log.error("获取用户统计失败", e);
            throw new RuntimeException("获取用户统计失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取用户头像URL
     */
    public String getUserAvatarUrl(long userId) {
        try {
            return zsxqClient.users().getAvatarUrl(userId);
        } catch (ZsxqException e) {
            log.error("获取用户头像失败: userId={}", userId, e);
            throw new RuntimeException("获取用户头像失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取用户动态足迹
     */
    public List<Topic> getUserFootprints(long userId) {
        try {
            return zsxqClient.users().getFootprints(userId);
        } catch (ZsxqException e) {
            log.error("获取用户动态足迹失败: userId={}", userId, e);
            throw new RuntimeException("获取用户动态足迹失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取用户创建的星球
     */
    public List<Group> getUserCreatedGroups(long userId) {
        try {
            return zsxqClient.users().getCreatedGroups(userId);
        } catch (ZsxqException e) {
            log.error("获取用户创建的星球失败: userId={}", userId, e);
            throw new RuntimeException("获取用户创建的星球失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取屏蔽用户列表
     */
    public List<User> getBlockedUsers() {
        try {
            return zsxqClient.users().getBlockedUsers();
        } catch (ZsxqException e) {
            log.error("获取屏蔽用户列表失败", e);
            throw new RuntimeException("获取屏蔽用户列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取贡献记录
     */
    public List<Contribution> getContributions() {
        try {
            return zsxqClient.users().getContributions();
        } catch (ZsxqException e) {
            log.error("获取贡献记录失败", e);
            throw new RuntimeException("获取贡献记录失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取贡献统计
     */
    public ContributionStatistics getContributionStats() {
        try {
            return zsxqClient.users().getContributionStats();
        } catch (ZsxqException e) {
            log.error("获取贡献统计失败", e);
            throw new RuntimeException("获取贡献统计失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取成就摘要
     */
    public List<AchievementSummary> getAchievementsSummary() {
        try {
            return zsxqClient.users().getAchievementsSummary();
        } catch (ZsxqException e) {
            log.error("获取成就摘要失败", e);
            throw new RuntimeException("获取成就摘要失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取关注者统计
     */
    public FollowerStatistics getFollowerStats() {
        try {
            return zsxqClient.users().getFollowerStats();
        } catch (ZsxqException e) {
            log.error("获取关注者统计失败", e);
            throw new RuntimeException("获取关注者统计失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取用户偏好配置
     */
    public Map<String, Object> getUserPreferences() {
        try {
            return zsxqClient.users().getPreferences();
        } catch (ZsxqException e) {
            log.error("获取用户偏好配置失败", e);
            throw new RuntimeException("获取用户偏好配置失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球周榜排名
     */
    public WeeklyRanking getWeeklyRanking(long groupId) {
        try {
            return zsxqClient.users().getWeeklyRanking(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球周榜排名失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球周榜排名失败: " + e.getMessage(), e);
        }
    }

    // ==================== Groups 模块 ====================

    /**
     * 获取我的星球列表
     */
    public List<Group> getMyGroups() {
        try {
            return zsxqClient.groups().list();
        } catch (ZsxqException e) {
            log.error("获取星球列表失败", e);
            throw new RuntimeException("获取星球列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球详情
     */
    public Group getGroup(long groupId) {
        try {
            return zsxqClient.groups().get(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球详情失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球详情失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取默认星球详情
     */
    public Group getDefaultGroup() {
        return getGroup(properties.getGroupId());
    }

    /**
     * 获取星球统计
     */
    public Map<String, Object> getGroupStatistics(long groupId) {
        try {
            return zsxqClient.groups().getStatistics(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球统计失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球统计失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球标签
     */
    public List<Hashtag> getGroupHashtags(long groupId) {
        try {
            return zsxqClient.groups().getHashtags(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球标签失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球标签失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球菜单
     */
    public List<Menu> getGroupMenus(long groupId) {
        try {
            return zsxqClient.groups().getMenus(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球菜单失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球菜单失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球角色成员
     */
    public RoleMembers getGroupRoleMembers(long groupId) {
        try {
            return zsxqClient.groups().getRoleMembers(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球角色成员失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球角色成员失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取成员信息
     */
    public User getGroupMember(long groupId, long memberId) {
        try {
            return zsxqClient.groups().getMember(groupId, memberId);
        } catch (ZsxqException e) {
            log.error("获取成员信息失败: groupId={}, memberId={}", groupId, memberId, e);
            throw new RuntimeException("获取成员信息失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取成员活跃摘要
     */
    public ActivitySummary getMemberActivitySummary(long groupId, long memberId) {
        try {
            return zsxqClient.groups().getMemberActivitySummary(groupId, memberId);
        } catch (ZsxqException e) {
            log.error("获取成员活跃摘要失败: groupId={}, memberId={}", groupId, memberId, e);
            throw new RuntimeException("获取成员活跃摘要失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球专栏列表
     */
    public List<Column> getGroupColumns(long groupId) {
        try {
            return zsxqClient.groups().getColumns(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球专栏失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球专栏失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取专栏汇总信息
     */
    public Map<String, Object> getGroupColumnsSummary(long groupId) {
        try {
            return zsxqClient.groups().getColumnsSummary(groupId);
        } catch (ZsxqException e) {
            log.error("获取专栏汇总失败: groupId={}", groupId, e);
            throw new RuntimeException("获取专栏汇总失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球自定义标签
     */
    public List<CustomTag> getGroupCustomTags(long groupId) {
        try {
            return zsxqClient.groups().getCustomTags(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球自定义标签失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球自定义标签失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球定时任务
     */
    public List<ScheduledJob> getGroupScheduledTasks(long groupId) {
        try {
            return zsxqClient.groups().getScheduledTasks(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球定时任务失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球定时任务失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球风险预警
     */
    public GroupWarning getGroupRiskWarnings(long groupId) {
        try {
            return zsxqClient.groups().getRiskWarnings(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球风险预警失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球风险预警失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取未读话题数量
     */
    public Map<String, Integer> getUnreadCount() {
        try {
            return zsxqClient.groups().getUnreadCount();
        } catch (ZsxqException e) {
            log.error("获取未读话题数量失败", e);
            throw new RuntimeException("获取未读话题数量失败: " + e.getMessage(), e);
        }
    }

    // ==================== Topics 模块 ====================

    /**
     * 获取话题列表
     */
    public List<Topic> getTopics(long groupId) {
        try {
            return zsxqClient.topics().list(groupId);
        } catch (ZsxqException e) {
            log.error("获取话题列表失败: groupId={}", groupId, e);
            throw new RuntimeException("获取话题列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取话题列表（带参数）
     */
    public List<Topic> getTopics(long groupId, String scope, Integer count) {
        try {
            TopicsRequest.ListTopicsOptions options = new TopicsRequest.ListTopicsOptions();
            if (scope != null) options.scope(scope);
            if (count != null) options.count(count);
            return zsxqClient.topics().list(groupId, options);
        } catch (ZsxqException e) {
            log.error("获取话题列表失败: groupId={}", groupId, e);
            throw new RuntimeException("获取话题列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取话题详情
     */
    public Topic getTopic(long topicId) {
        try {
            return zsxqClient.topics().get(topicId);
        } catch (ZsxqException e) {
            log.error("获取话题详情失败: topicId={}", topicId, e);
            throw new RuntimeException("获取话题详情失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取话题评论
     */
    public List<Comment> getTopicComments(long topicId) {
        try {
            return zsxqClient.topics().getComments(topicId);
        } catch (ZsxqException e) {
            log.error("获取话题评论失败: topicId={}", topicId, e);
            throw new RuntimeException("获取话题评论失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取话题打赏列表
     */
    public List<Reward> getTopicRewards(long topicId) {
        try {
            return zsxqClient.topics().getRewards(topicId);
        } catch (ZsxqException e) {
            log.error("获取话题打赏失败: topicId={}", topicId, e);
            throw new RuntimeException("获取话题打赏失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取置顶话题
     */
    public List<Topic> getStickyTopics(long groupId) {
        try {
            return zsxqClient.topics().listSticky(groupId);
        } catch (ZsxqException e) {
            log.error("获取置顶话题失败: groupId={}", groupId, e);
            throw new RuntimeException("获取置顶话题失败: " + e.getMessage(), e);
        }
    }

    /**
     * 按标签获取话题
     */
    public List<Topic> getTopicsByHashtag(long hashtagId) {
        try {
            return zsxqClient.topics().listByHashtag(hashtagId);
        } catch (ZsxqException e) {
            log.error("按标签获取话题失败: hashtagId={}", hashtagId, e);
            throw new RuntimeException("按标签获取话题失败: " + e.getMessage(), e);
        }
    }

    /**
     * 按专栏获取话题
     */
    public List<Topic> getTopicsByColumn(long groupId, long columnId) {
        try {
            return zsxqClient.topics().listByColumn(groupId, columnId);
        } catch (ZsxqException e) {
            log.error("按专栏获取话题失败: groupId={}, columnId={}", groupId, columnId, e);
            throw new RuntimeException("按专栏获取话题失败: " + e.getMessage(), e);
        }
    }

    // ==================== Checkins 模块 ====================

    /**
     * 获取打卡项目列表
     */
    public List<Checkin> getCheckins(long groupId) {
        try {
            return zsxqClient.checkins().list(groupId);
        } catch (ZsxqException e) {
            log.error("获取打卡列表失败: groupId={}", groupId, e);
            throw new RuntimeException("获取打卡列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取打卡项目列表（带参数）
     */
    public List<Checkin> getCheckins(long groupId, String scope) {
        try {
            CheckinsRequest.ListCheckinsOptions options = new CheckinsRequest.ListCheckinsOptions();
            if (scope != null) options.scope(scope);
            return zsxqClient.checkins().list(groupId, options);
        } catch (ZsxqException e) {
            log.error("获取打卡列表失败: groupId={}", groupId, e);
            throw new RuntimeException("获取打卡列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取打卡项目详情
     */
    public Checkin getCheckin(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().get(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取打卡详情失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取打卡详情失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取打卡统计
     */
    public CheckinStatistics getCheckinStatistics(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().getStatistics(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取打卡统计失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取打卡统计失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取打卡排行榜
     */
    public List<RankingItem> getCheckinRankingList(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().getRankingList(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取打卡排行榜失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取打卡排行榜失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取打卡话题列表
     */
    public List<Topic> getCheckinTopics(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().getTopics(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取打卡话题失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取打卡话题失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取打卡每日统计
     */
    public List<DailyStatistics> getCheckinDailyStatistics(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().getDailyStatistics(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取打卡每日统计失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取打卡每日统计失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取打卡参与用户
     */
    public List<User> getCheckinJoinedUsers(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().getJoinedUsers(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取打卡参与用户失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取打卡参与用户失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取我的打卡记录
     */
    public List<Topic> getMyCheckins(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().getMyCheckins(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取我的打卡记录失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取我的打卡记录失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取我的打卡日期
     */
    public List<String> getMyCheckinDays(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().getMyCheckinDays(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取我的打卡日期失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取我的打卡日期失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取我的打卡统计
     */
    public MyCheckinStatistics getMyCheckinStatistics(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().getMyStatistics(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取我的打卡统计失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取我的打卡统计失败: " + e.getMessage(), e);
        }
    }

    // ==================== Dashboard 模块 ====================

    /**
     * 获取星球概览
     */
    public Map<String, Object> getDashboardOverview(long groupId) {
        try {
            return zsxqClient.dashboard().getOverview(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球概览失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球概览失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取收入概览
     */
    public Map<String, Object> getDashboardIncomes(long groupId) {
        try {
            return zsxqClient.dashboard().getIncomes(groupId);
        } catch (ZsxqException e) {
            log.error("获取收入概览失败: groupId={}", groupId, e);
            throw new RuntimeException("获取收入概览失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球权限配置
     */
    public Map<String, Object> getDashboardPrivileges(long groupId) {
        try {
            return zsxqClient.dashboard().getPrivileges(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球权限配置失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球权限配置失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取发票统计
     */
    public InvoiceStats getInvoiceStats() {
        try {
            return zsxqClient.dashboard().getInvoiceStats();
        } catch (ZsxqException e) {
            log.error("获取发票统计失败", e);
            throw new RuntimeException("获取发票统计失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取积分排行
     */
    public List<RankingItem> getScoreboardRanking(long groupId) {
        try {
            return zsxqClient.dashboard().getScoreboardRanking(groupId);
        } catch (ZsxqException e) {
            log.error("获取积分排行失败: groupId={}", groupId, e);
            throw new RuntimeException("获取积分排行失败: " + e.getMessage(), e);
        }
    }

    // ==================== Ranking 模块 ====================

    /**
     * 获取星球排行榜
     */
    public List<RankingItem> getGroupRanking(long groupId) {
        try {
            return zsxqClient.ranking().getGroupRanking(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球排行榜失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球排行榜失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球排行统计
     */
    public RankingStatistics getGroupRankingStats(long groupId) {
        try {
            return zsxqClient.ranking().getGroupRankingStats(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球排行统计失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球排行统计失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取积分排行榜
     */
    public List<RankingItem> getScoreRanking(long groupId) {
        try {
            return zsxqClient.ranking().getScoreRanking(groupId);
        } catch (ZsxqException e) {
            log.error("获取积分排行榜失败: groupId={}", groupId, e);
            throw new RuntimeException("获取积分排行榜失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取我的积分统计
     */
    public Map<String, Object> getMyScoreStats(long groupId) {
        try {
            return zsxqClient.ranking().getMyScoreStats(groupId);
        } catch (ZsxqException e) {
            log.error("获取我的积分统计失败: groupId={}", groupId, e);
            throw new RuntimeException("获取我的积分统计失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取积分榜设置
     */
    public ScoreboardSettings getScoreboardSettings(long groupId) {
        try {
            return zsxqClient.ranking().getScoreboardSettings(groupId);
        } catch (ZsxqException e) {
            log.error("获取积分榜设置失败: groupId={}", groupId, e);
            throw new RuntimeException("获取积分榜设置失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取邀请排行榜
     */
    public List<RankingItem> getInvitationRanking(long groupId) {
        try {
            return zsxqClient.ranking().getInvitationRanking(groupId);
        } catch (ZsxqException e) {
            log.error("获取邀请排行榜失败: groupId={}", groupId, e);
            throw new RuntimeException("获取邀请排行榜失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取贡献排行榜
     */
    public List<RankingItem> getContributionRanking(long groupId) {
        try {
            return zsxqClient.ranking().getContributionRanking(groupId);
        } catch (ZsxqException e) {
            log.error("获取贡献排行榜失败: groupId={}", groupId, e);
            throw new RuntimeException("获取贡献排行榜失败: " + e.getMessage(), e);
        }
    }

    // ==================== Misc 模块 ====================

    /**
     * 获取动态列表
     */
    public List<Activity> getActivities() {
        try {
            return zsxqClient.misc().getActivities();
        } catch (ZsxqException e) {
            log.error("获取动态列表失败", e);
            throw new RuntimeException("获取动态列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取动态列表（带参数）
     */
    public List<Activity> getActivities(String scope, Integer count) {
        try {
            MiscRequest.ActivitiesOptions options = new MiscRequest.ActivitiesOptions();
            if (scope != null) options.scope(scope);
            if (count != null) options.count(count);
            return zsxqClient.misc().getActivities(options);
        } catch (ZsxqException e) {
            log.error("获取动态列表失败", e);
            throw new RuntimeException("获取动态列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取全局配置
     */
    public GlobalConfig getGlobalConfig() {
        try {
            return zsxqClient.misc().getGlobalConfig();
        } catch (ZsxqException e) {
            log.error("获取全局配置失败", e);
            throw new RuntimeException("获取全局配置失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取 PK 群组详情
     */
    public PkGroup getPkGroup(long pkGroupId) {
        try {
            return zsxqClient.misc().getPkGroup(pkGroupId);
        } catch (ZsxqException e) {
            log.error("获取PK群组详情失败: pkGroupId={}", pkGroupId, e);
            throw new RuntimeException("获取PK群组详情失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取 PK 对战记录
     */
    public List<PkBattle> getPkBattles(long pkGroupId) {
        try {
            return zsxqClient.misc().getPkBattles(pkGroupId);
        } catch (ZsxqException e) {
            log.error("获取PK对战记录失败: pkGroupId={}", pkGroupId, e);
            throw new RuntimeException("获取PK对战记录失败: " + e.getMessage(), e);
        }
    }
}
