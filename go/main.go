package main

import (
	"context"
	"fmt"
	"os"
	"strconv"
	"strings"
	"time"

	zsxq "github.com/zsxq-sdk/zsxq-sdk-go"
	"github.com/zsxq-sdk/zsxq-sdk-go/model"
	"github.com/zsxq-sdk/zsxq-sdk-go/request"
)

const SEPARATOR = "============================================================"

func main() {
	// 从环境变量获取配置
	token := os.Getenv("ZSXQ_TOKEN")
	groupIDStr := os.Getenv("ZSXQ_GROUP_ID")

	if token == "" || groupIDStr == "" {
		fmt.Fprintln(os.Stderr, "请设置环境变量:")
		fmt.Fprintln(os.Stderr, "  ZSXQ_TOKEN=your-token")
		fmt.Fprintln(os.Stderr, "  ZSXQ_GROUP_ID=your-group-id")
		os.Exit(1)
	}

	groupID, err := strconv.ParseInt(groupIDStr, 10, 64)
	if err != nil {
		fmt.Fprintf(os.Stderr, "无效的 GROUP_ID: %s\n", groupIDStr)
		os.Exit(1)
	}

	fmt.Println(SEPARATOR)
	fmt.Println("知识星球 SDK Go Demo")
	fmt.Println(SEPARATOR)

	// 创建客户端
	client := zsxq.NewClientBuilder().
		SetToken(token).
		SetTimeout(10 * time.Second).
		SetRetryCount(3).
		MustBuild()

	ctx := context.Background()

	// 运行所有测试
	testUsers(ctx, client)
	testGroups(ctx, client, groupID)
	testTopics(ctx, client, groupID)
	testCheckins(ctx, client, groupID)
	testDashboard(ctx, client, groupID)
	testRanking(ctx, client, groupID)

	fmt.Println(SEPARATOR)
	fmt.Println("所有测试完成!")
	fmt.Println(SEPARATOR)
}

// testUsers 测试用户模块
func testUsers(ctx context.Context, client *zsxq.Client) {
	fmt.Println("\n[Users] 用户模块测试")
	fmt.Println(strings.Repeat("-", 40))

	// 获取当前用户
	self, err := client.Users().Self(ctx)
	if err != nil {
		fmt.Printf("✗ 用户模块错误: %v\n", err)
		return
	}
	fmt.Printf("✓ Self() - 当前用户: %s\n", self.Name)
	fmt.Printf("  用户ID: %s\n", self.UserID)
	fmt.Printf("  头像: %s\n", self.AvatarURL)

	// 获取用户统计
	stats, err := client.Users().GetStatistics(ctx, self.UserID)
	if err != nil {
		fmt.Printf("✗ GetStatistics() 错误: %v\n", err)
		return
	}
	fmt.Printf("✓ GetStatistics() - 用户统计: %v\n", stats)
}

// testGroups 测试星球模块
func testGroups(ctx context.Context, client *zsxq.Client, groupID int64) {
	fmt.Println("\n[Groups] 星球模块测试")
	fmt.Println(strings.Repeat("-", 40))

	// 获取星球列表
	groups, err := client.Groups().List(ctx)
	if err != nil {
		fmt.Printf("✗ 星球模块错误: %v\n", err)
		return
	}
	fmt.Printf("✓ List() - 我的星球数量: %d\n", len(groups))
	for _, g := range groups {
		fmt.Printf("  - %s (ID: %d)\n", g.Name, g.GroupID)
	}

	// 获取星球详情
	group, err := client.Groups().Get(ctx, groupID)
	if err != nil {
		fmt.Printf("✗ Get() 错误: %v\n", err)
		return
	}
	fmt.Printf("✓ Get() - 星球详情: %s\n", group.Name)
	if group.MemberCount != nil {
		fmt.Printf("  成员数: %d\n", *group.MemberCount)
	} else {
		fmt.Printf("  成员数: null\n")
	}
	fmt.Printf("  类型: %s\n", group.Type)

	// 获取星球统计
	stats, err := client.Groups().GetStatistics(ctx, groupID)
	if err != nil {
		fmt.Printf("✗ GetStatistics() 错误: %v\n", err)
		return
	}
	fmt.Printf("✓ GetStatistics() - 星球统计: %v\n", stats)
}

// testTopics 测试话题模块
func testTopics(ctx context.Context, client *zsxq.Client, groupID int64) {
	fmt.Println("\n[Topics] 话题模块测试")
	fmt.Println(strings.Repeat("-", 40))

	// 获取话题列表
	topics, err := client.Topics().List(ctx, groupID, nil)
	if err != nil {
		fmt.Printf("✗ 话题模块错误: %v\n", err)
		return
	}
	fmt.Printf("✓ List() - 话题数量: %d\n", len(topics))

	if len(topics) > 0 {
		first := topics[0]
		fmt.Printf("  最新话题ID: %d\n", first.TopicID)
		fmt.Printf("  类型: %s\n", first.Type)
		fmt.Printf("  点赞数: %d\n", first.LikesCount)

		// 获取话题详情
		_, err := client.Topics().Get(ctx, first.TopicID)
		if err != nil {
			fmt.Printf("✗ Get() 错误: %v\n", err)
		} else {
			fmt.Println("✓ Get() - 话题详情获取成功")
		}
	}

	// 测试带参数的列表查询
	digests, err := client.Topics().List(ctx, groupID, &request.ListTopicsOptions{
		Scope: "digests",
		Count: 5,
	})
	if err != nil {
		fmt.Printf("✗ List(options) 错误: %v\n", err)
		return
	}
	fmt.Printf("✓ List(options) - 精华话题数量: %d\n", len(digests))
}

// testCheckins 测试打卡模块
func testCheckins(ctx context.Context, client *zsxq.Client, groupID int64) {
	fmt.Println("\n[Checkins] 打卡模块测试")
	fmt.Println(strings.Repeat("-", 40))

	// 获取打卡项目列表
	checkins, err := client.Checkins().List(ctx, groupID, nil)
	if err != nil {
		fmt.Printf("✗ 打卡模块错误: %v\n", err)
		return
	}
	fmt.Printf("✓ List() - 打卡项目数量: %d\n", len(checkins))

	if len(checkins) > 0 {
		first := checkins[0]
		fmt.Printf("  项目名称: %s\n", first.Name)
		fmt.Printf("  状态: %s\n", first.Status)

		checkinID := first.CheckinID

		// 获取打卡项目详情
		_, err := client.Checkins().Get(ctx, groupID, checkinID)
		if err != nil {
			fmt.Printf("✗ Get() 错误: %v\n", err)
		} else {
			fmt.Println("✓ Get() - 打卡项目详情获取成功")
		}

		// 获取打卡统计
		stats, err := client.Checkins().GetStatistics(ctx, groupID, checkinID)
		if err != nil {
			fmt.Printf("✗ GetStatistics() 错误: %v\n", err)
		} else {
			fmt.Println("✓ GetStatistics() - 打卡统计:")
			fmt.Printf("  参与人数: %d\n", stats.JoinedCount)
			fmt.Printf("  完成人数: %d\n", stats.CompletedCount)
		}

		// 获取打卡排行榜
		ranking, err := client.Checkins().GetRankingList(ctx, groupID, checkinID, nil)
		if err != nil {
			fmt.Printf("✗ GetRankingList() 错误: %v\n", err)
		} else {
			fmt.Printf("✓ GetRankingList() - 排行榜人数: %d\n", len(ranking))
		}

		// 测试带参数的排行榜查询
		continuous, err := client.Checkins().GetRankingList(ctx, groupID, checkinID, &request.ListRankingOptions{
			Type: "continuous",
		})
		if err != nil {
			fmt.Printf("✗ GetRankingList(options) 错误: %v\n", err)
		} else {
			fmt.Printf("✓ GetRankingList(options) - 连续打卡排行: %d\n", len(continuous))
		}
	} else {
		fmt.Println("  (该星球没有打卡项目)")
	}

	// 测试带参数的列表查询
	ongoing, err := client.Checkins().List(ctx, groupID, &request.ListCheckinsOptions{
		Scope: "ongoing",
	})
	if err != nil {
		fmt.Printf("✗ List(options) 错误: %v\n", err)
		return
	}
	fmt.Printf("✓ List(options) - 进行中的打卡: %d\n", len(ongoing))
}

// testDashboard 测试数据面板模块
func testDashboard(ctx context.Context, client *zsxq.Client, groupID int64) {
	fmt.Println("\n[Dashboard] 数据面板模块测试")
	fmt.Println(strings.Repeat("-", 40))

	// 获取星球概览
	overview, err := client.Dashboard().GetOverview(ctx, groupID)
	if err != nil {
		fmt.Printf("✗ 数据面板模块错误: %v\n", err)
		fmt.Println("  (可能需要星主权限)")
		return
	}
	fmt.Printf("✓ GetOverview() - 星球概览: %v\n", overview)

	// 获取收入概览
	incomes, err := client.Dashboard().GetIncomes(ctx, groupID)
	if err != nil {
		fmt.Printf("✗ GetIncomes() 错误: %v\n", err)
		fmt.Println("  (可能需要星主权限)")
		return
	}
	fmt.Printf("✓ GetIncomes() - 收入概览: %v\n", incomes)
}

func formatZsxqTime(t time.Time) string {
	loc, _ := time.LoadLocation("Asia/Shanghai")
	return t.In(loc).Format("2006-01-02T15:04:05.000-0700")
}

func printInvitationRanking(title, rangeText string, ranking []model.InvitationRankingItem) {
	fmt.Printf("\n%s\n", title)
	fmt.Printf("  区间: %s\n", rangeText)
	if len(ranking) == 0 {
		fmt.Println("  (还没有人上榜)")
		return
	}
	fmt.Println("  排名  成员昵称            编号    邀请人数")
	for _, item := range ranking {
		name := "?"
		number := "-"
		if item.Member != nil {
			name = item.Member.Name
			if item.Member.Number != 0 {
				number = fmt.Sprintf("%d", item.Member.Number)
			}
		}
		fmt.Printf("  %2d    %-16s  %6s    %d\n", item.Rankings, name, number, item.InviteesCount)
	}
}

func invitationOpts(beginTime, endTime string, count int) *request.InvitationRankingOptions {
	withExtra := true
	opts := &request.InvitationRankingOptions{
		BeginTime: beginTime,
		Count:     count,
		WithExtra: &withExtra,
	}
	if endTime != "" {
		opts.EndTime = endTime
	}
	return opts
}

// testRanking 测试邀请排行榜：日榜 / 周榜 / 月榜 / 自定义
func testRanking(ctx context.Context, client *zsxq.Client, groupID int64) {
	fmt.Println("\n[Ranking] 邀请排行榜（日/周/月/自定义）")
	fmt.Println(strings.Repeat("-", 40))

	loc, _ := time.LoadLocation("Asia/Shanghai")
	now := time.Now().In(loc)
	dailyBegin := time.Date(now.Year(), now.Month(), now.Day(), 0, 0, 0, 0, loc)
	weekday := int(dailyBegin.Weekday())
	if weekday == 0 {
		weekday = 7
	}
	weeklyBegin := dailyBegin.AddDate(0, 0, -(weekday - 1))
	monthlyBegin := time.Date(now.Year(), now.Month(), 1, 0, 0, 0, 0, loc)
	customEnd := time.Date(now.Year(), now.Month(), now.Day(), 23, 59, 0, 0, loc)

	daily, err := client.Ranking().GetInvitationRanking(ctx, groupID, invitationOpts(formatZsxqTime(dailyBegin), "", 10))
	if err != nil {
		fmt.Printf("✗ 日榜错误: %v\n", err)
		return
	}
	printInvitationRanking("日榜", formatZsxqTime(dailyBegin)+" ~ 当日 23:59", daily)

	weekly, err := client.Ranking().GetInvitationRanking(ctx, groupID, invitationOpts(formatZsxqTime(weeklyBegin), "", 10))
	if err != nil {
		fmt.Printf("✗ 周榜错误: %v\n", err)
		return
	}
	printInvitationRanking("周榜", formatZsxqTime(weeklyBegin)+" ~ 本周日 23:59", weekly)

	monthly, err := client.Ranking().GetInvitationRanking(ctx, groupID, invitationOpts(formatZsxqTime(monthlyBegin), "", 20))
	if err != nil {
		fmt.Printf("✗ 月榜错误: %v\n", err)
		return
	}
	printInvitationRanking("月榜", formatZsxqTime(monthlyBegin)+" ~ 本月末 23:59", monthly)

	custom, err := client.Ranking().GetInvitationRanking(ctx, groupID, invitationOpts(formatZsxqTime(dailyBegin), formatZsxqTime(customEnd), 10))
	if err != nil {
		fmt.Printf("✗ 自定义错误: %v\n", err)
		return
	}
	printInvitationRanking("自定义", formatZsxqTime(dailyBegin)+" ~ "+formatZsxqTime(customEnd)+"（最大 31 天）", custom)
}
