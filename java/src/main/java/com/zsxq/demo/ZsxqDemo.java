package com.zsxq.demo;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.*;
import com.zsxq.sdk.request.CheckinsRequest;
import com.zsxq.sdk.request.TopicsRequest;

import java.util.List;
import java.util.Map;

/**
 * 知识星球 SDK Java 示例
 * 
 * 演示 zsxq-sdk 的所有功能模块
 */
public class ZsxqDemo {

    private static final String SEPARATOR = "=".repeat(60);

    public static void main(String[] args) {
        // 从环境变量获取配置
        String token = getEnv("ZSXQ_TOKEN", null);
        String groupIdStr = getEnv("ZSXQ_GROUP_ID", null);

        if (token == null || groupIdStr == null) {
            System.err.println("请设置环境变量:");
            System.err.println("  ZSXQ_TOKEN=your-token");
            System.err.println("  ZSXQ_GROUP_ID=your-group-id");
            System.exit(1);
        }

        long groupId = Long.parseLong(groupIdStr);

        System.out.println(SEPARATOR);
        System.out.println("知识星球 SDK Java Demo");
        System.out.println(SEPARATOR);

        // 创建客户端
        ZsxqClient client = new ZsxqClientBuilder()
                .token(token)
                .timeout(10000)
                .retry(3)
                .build();

        // 运行所有测试
        testUsers(client);
        testGroups(client, groupId);
        testTopics(client, groupId);
        testCheckins(client, groupId);
        testDashboard(client, groupId);

        System.out.println(SEPARATOR);
        System.out.println("所有测试完成!");
        System.out.println(SEPARATOR);
    }

    /**
     * 测试用户模块
     */
    private static void testUsers(ZsxqClient client) {
        System.out.println("\n[Users] 用户模块测试");
        System.out.println("-".repeat(40));

        try {
            // 获取当前用户
            User self = client.users().self();
            System.out.println("✓ self() - 当前用户: " + self.getName());
            System.out.println("  用户ID: " + self.getUserId());
            System.out.println("  头像: " + self.getAvatarUrl());

            // 获取用户统计
            Map<String, Object> stats = client.users().getStatistics(self.getUserId());
            System.out.println("✓ getStatistics() - 用户统计: " + stats);

        } catch (ZsxqException e) {
            System.err.println("✗ 用户模块错误: " + e.getMessage());
        }
    }

    /**
     * 测试星球模块
     */
    private static void testGroups(ZsxqClient client, long groupId) {
        System.out.println("\n[Groups] 星球模块测试");
        System.out.println("-".repeat(40));

        try {
            // 获取星球列表
            List<Group> groups = client.groups().list();
            System.out.println("✓ list() - 我的星球数量: " + groups.size());
            for (Group g : groups) {
                System.out.println("  - " + g.getName() + " (ID: " + g.getGroupId() + ")");
            }

            // 获取星球详情
            Group group = client.groups().get(groupId);
            System.out.println("✓ get() - 星球详情: " + group.getName());
            System.out.println("  成员数: " + group.getMemberCount());
            System.out.println("  类型: " + group.getType());

            // 获取星球统计
            Map<String, Object> stats = client.groups().getStatistics(groupId);
            System.out.println("✓ getStatistics() - 星球统计: " + stats);

        } catch (ZsxqException e) {
            System.err.println("✗ 星球模块错误: " + e.getMessage());
        }
    }

    /**
     * 测试话题模块
     */
    private static void testTopics(ZsxqClient client, long groupId) {
        System.out.println("\n[Topics] 话题模块测试");
        System.out.println("-".repeat(40));

        try {
            // 获取话题列表
            List<Topic> topics = client.topics().list(groupId);
            System.out.println("✓ list() - 话题数量: " + topics.size());

            if (!topics.isEmpty()) {
                Topic first = topics.get(0);
                System.out.println("  最新话题ID: " + first.getTopicId());
                System.out.println("  类型: " + first.getType());
                System.out.println("  点赞数: " + first.getLikesCount());

                // 获取话题详情
                Topic detail = client.topics().get(first.getTopicId());
                System.out.println("✓ get() - 话题详情获取成功");
            }

            // 测试带参数的列表查询
            List<Topic> digests = client.topics().list(groupId,
                    new TopicsRequest.ListTopicsOptions().scope("digests").count(5));
            System.out.println("✓ list(options) - 精华话题数量: " + digests.size());

        } catch (ZsxqException e) {
            System.err.println("✗ 话题模块错误: " + e.getMessage());
        }
    }

    /**
     * 测试打卡模块
     */
    private static void testCheckins(ZsxqClient client, long groupId) {
        System.out.println("\n[Checkins] 打卡模块测试");
        System.out.println("-".repeat(40));

        try {
            // 获取打卡项目列表
            List<Checkin> checkins = client.checkins().list(groupId);
            System.out.println("✓ list() - 打卡项目数量: " + checkins.size());

            if (!checkins.isEmpty()) {
                Checkin first = checkins.get(0);
                System.out.println("  项目名称: " + first.getName());
                System.out.println("  状态: " + first.getStatus());

                long checkinId = first.getCheckinId();

                // 获取打卡项目详情
                Checkin detail = client.checkins().get(groupId, checkinId);
                System.out.println("✓ get() - 打卡项目详情获取成功");

                // 获取打卡统计
                CheckinStatistics stats = client.checkins().getStatistics(groupId, checkinId);
                System.out.println("✓ getStatistics() - 打卡统计:");
                System.out.println("  参与人数: " + stats.getJoinedCount());
                System.out.println("  完成人数: " + stats.getCompletedCount());

                // 获取打卡排行榜
                List<RankingItem> ranking = client.checkins().getRankingList(groupId, checkinId);
                System.out.println("✓ getRankingList() - 排行榜人数: " + ranking.size());

                // 测试带参数的排行榜查询
                List<RankingItem> continuous = client.checkins().getRankingList(groupId, checkinId,
                        new CheckinsRequest.RankingListOptions().type("continuous"));
                System.out.println("✓ getRankingList(options) - 连续打卡排行: " + continuous.size());
            } else {
                System.out.println("  (该星球没有打卡项目)");
            }

            // 测试带参数的列表查询
            List<Checkin> ongoing = client.checkins().list(groupId,
                    new CheckinsRequest.ListCheckinsOptions().scope("ongoing"));
            System.out.println("✓ list(options) - 进行中的打卡: " + ongoing.size());

        } catch (ZsxqException e) {
            System.err.println("✗ 打卡模块错误: " + e.getMessage());
        }
    }

    /**
     * 测试数据面板模块
     */
    private static void testDashboard(ZsxqClient client, long groupId) {
        System.out.println("\n[Dashboard] 数据面板模块测试");
        System.out.println("-".repeat(40));

        try {
            // 获取星球概览
            Map<String, Object> overview = client.dashboard().getOverview(groupId);
            System.out.println("✓ getOverview() - 星球概览: " + overview);

            // 获取收入概览
            Map<String, Object> incomes = client.dashboard().getIncomes(groupId);
            System.out.println("✓ getIncomes() - 收入概览: " + incomes);

        } catch (ZsxqException e) {
            System.err.println("✗ 数据面板模块错误: " + e.getMessage());
            System.out.println("  (可能需要星主权限)");
        }
    }

    /**
     * 获取环境变量
     */
    private static String getEnv(String name, String defaultValue) {
        String value = System.getenv(name);
        return value != null ? value : defaultValue;
    }
}
