package main

import (
	"context"
	"os"
	"strconv"
	"testing"
	"time"

	zsxq "github.com/zsxq-sdk/zsxq-sdk-go"
	"github.com/zsxq-sdk/zsxq-sdk-go/request"
)

var (
	testClient  *zsxq.Client
	testGroupID int64
	ctx         context.Context
)

// TestMain 初始化测试环境
func TestMain(m *testing.M) {
	// 从环境变量获取配置
	token := os.Getenv("ZSXQ_TOKEN")
	groupIDStr := os.Getenv("ZSXQ_GROUP_ID")

	if token == "" || groupIDStr == "" {
		panic("请设置环境变量 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID")
	}

	var err error
	testGroupID, err = strconv.ParseInt(groupIDStr, 10, 64)
	if err != nil {
		panic("无效的 ZSXQ_GROUP_ID: " + err.Error())
	}

	// 创建客户端
	testClient = zsxq.NewClientBuilder().
		SetToken(token).
		SetTimeout(10 * time.Second).
		SetRetryCount(3).
		MustBuild()

	ctx = context.Background()

	// 运行测试
	code := m.Run()

	os.Exit(code)
}

// ============================================================
// Users 模块测试
// ============================================================

func TestUsersSelf(t *testing.T) {
	user, err := testClient.Users().Self(ctx)
	if err != nil {
		t.Fatalf("获取当前用户失败: %v", err)
	}

	// 验证返回数据
	if user == nil {
		t.Fatal("用户对象不应为 nil")
	}
	if user.UserID == 0 {
		t.Error("用户 ID 不应为 0")
	}
	if user.Name == "" {
		t.Error("用户名不应为空")
	}
	if user.AvatarURL == "" {
		t.Error("头像 URL 不应为空")
	}

	t.Logf("✓ 当前用户: %s (ID: %d)", user.Name, user.UserID)
}

func TestUsersGetStatistics(t *testing.T) {
	user, err := testClient.Users().Self(ctx)
	if err != nil {
		t.Fatalf("获取当前用户失败: %v", err)
	}

	stats, err := testClient.Users().GetStatistics(ctx, user.UserID)
	if err != nil {
		t.Fatalf("获取用户统计失败: %v", err)
	}

	// 验证返回数据
	if stats == nil {
		t.Fatal("统计数据不应为 nil")
	}

	t.Logf("✓ 用户统计数据: %v", stats)
}

func TestUsersInvalidToken(t *testing.T) {
	invalidClient := zsxq.NewClientBuilder().
		SetToken("invalid-token").
		SetTimeout(5 * time.Second).
		MustBuild()

	_, err := invalidClient.Users().Self(ctx)
	if err == nil {
		t.Error("使用无效 token 应该返回错误")
	}

	t.Logf("✓ 无效 token 正确返回错误")
}

// ============================================================
// Groups 模块测试
// ============================================================

func TestGroupsList(t *testing.T) {
	groups, err := testClient.Groups().List(ctx)
	if err != nil {
		t.Fatalf("获取星球列表失败: %v", err)
	}

	// 验证返回数据
	if groups == nil {
		t.Fatal("星球列表不应为 nil")
	}
	if len(groups) == 0 {
		t.Fatal("星球列表不应为空")
	}

	for _, group := range groups {
		if group.GroupID == 0 {
			t.Error("星球 ID 不应为 0")
		}
		if group.Name == "" {
			t.Error("星球名称不应为空")
		}
		if group.MemberCount < 0 {
			t.Errorf("成员数量应该 >= 0, got %d", group.MemberCount)
		}
	}

	t.Logf("✓ 获取星球列表成功: %d 个星球", len(groups))
}

func TestGroupsGet(t *testing.T) {
	group, err := testClient.Groups().Get(ctx, testGroupID)
	if err != nil {
		t.Fatalf("获取星球详情失败: %v", err)
	}

	// 验证返回数据
	if group == nil {
		t.Fatal("星球对象不应为 nil")
	}
	if group.GroupID != testGroupID {
		t.Errorf("星球 ID 应该匹配, got %d, want %d", group.GroupID, testGroupID)
	}
	if group.Name == "" {
		t.Error("星球名称不应为空")
	}
	if group.Type == "" {
		t.Error("星球类型不应为空")
	}

	t.Logf("✓ 星球详情: %s (成员数: %d)", group.Name, group.MemberCount)
}

func TestGroupsGetStatistics(t *testing.T) {
	stats, err := testClient.Groups().GetStatistics(ctx, testGroupID)
	if err != nil {
		t.Fatalf("获取星球统计失败: %v", err)
	}

	// 验证返回数据
	if stats == nil {
		t.Fatal("统计数据不应为 nil")
	}

	t.Logf("✓ 星球统计数据: %v", stats)
}

func TestGroupsDataConsistency(t *testing.T) {
	groups, err := testClient.Groups().List(ctx)
	if err != nil {
		t.Fatalf("获取星球列表失败: %v", err)
	}

	var fromList *zsxq.Group
	for _, g := range groups {
		if g.GroupID == testGroupID {
			fromList = g
			break
		}
	}

	if fromList == nil {
		t.Fatal("测试星球应该在列表中")
	}

	fromDetail, err := testClient.Groups().Get(ctx, testGroupID)
	if err != nil {
		t.Fatalf("获取星球详情失败: %v", err)
	}

	// 验证数据一致性
	if fromList.GroupID != fromDetail.GroupID {
		t.Errorf("星球 ID 不一致")
	}
	if fromList.Name != fromDetail.Name {
		t.Errorf("星球名称不一致")
	}
	if fromList.Type != fromDetail.Type {
		t.Errorf("星球类型不一致")
	}

	t.Logf("✓ 星球列表和详情数据一致")
}

func TestGroupsInvalidID(t *testing.T) {
	invalidGroupID := int64(999999999999999)

	_, err := testClient.Groups().Get(ctx, invalidGroupID)
	if err == nil {
		t.Error("使用无效 groupId 应该返回错误")
	}

	t.Logf("✓ 无效 groupId 正确返回错误")
}

// ============================================================
// Topics 模块测试
// ============================================================

func TestTopicsList(t *testing.T) {
	topics, err := testClient.Topics().List(ctx, testGroupID, nil)
	if err != nil {
		t.Fatalf("获取话题列表失败: %v", err)
	}

	// 验证返回数据
	if topics == nil {
		t.Fatal("话题列表不应为 nil")
	}

	if len(topics) > 0 {
		first := topics[0]
		if first.TopicID == 0 {
			t.Error("话题 ID 不应为 0")
		}
		if first.Type == "" {
			t.Error("话题类型不应为空")
		}
		if first.LikesCount < 0 {
			t.Errorf("点赞数应该 >= 0, got %d", first.LikesCount)
		}
	}

	t.Logf("✓ 获取话题列表成功: %d 个话题", len(topics))
}

func TestTopicsListWithOptions(t *testing.T) {
	digests, err := testClient.Topics().List(ctx, testGroupID, &request.ListTopicsOptions{
		Scope: "digests",
		Count: 5,
	})
	if err != nil {
		t.Fatalf("获取精华话题失败: %v", err)
	}

	// 验证返回数据
	if digests == nil {
		t.Fatal("精华话题列表不应为 nil")
	}
	if len(digests) > 5 {
		t.Errorf("返回数量应该 <= 5, got %d", len(digests))
	}

	t.Logf("✓ 获取精华话题成功: %d 个话题", len(digests))
}

func TestTopicsGet(t *testing.T) {
	topics, err := testClient.Topics().List(ctx, testGroupID, nil)
	if err != nil {
		t.Fatalf("获取话题列表失败: %v", err)
	}

	if len(topics) == 0 {
		t.Skip("该星球没有话题")
	}

	topicID := topics[0].TopicID
	topic, err := testClient.Topics().Get(ctx, topicID)
	if err != nil {
		t.Fatalf("获取话题详情失败: %v", err)
	}

	// 验证返回数据
	if topic == nil {
		t.Fatal("话题对象不应为 nil")
	}
	if topic.TopicID != topicID {
		t.Errorf("话题 ID ��该匹配, got %d, want %d", topic.TopicID, topicID)
	}

	t.Logf("✓ 获取话题详情成功: ID=%d", topicID)
}

// ============================================================
// Checkins 模块测试
// ============================================================

func TestCheckinsList(t *testing.T) {
	checkins, err := testClient.Checkins().List(ctx, testGroupID, nil)
	if err != nil {
		t.Fatalf("获取打卡项目列表失败: %v", err)
	}

	// 验证返回数据
	if checkins == nil {
		t.Fatal("打卡项目列表不应为 nil")
	}

	if len(checkins) > 0 {
		first := checkins[0]
		if first.CheckinID == 0 {
			t.Error("打卡项目 ID 不应为 0")
		}
		if first.Name == "" {
			t.Error("打卡项目名称不应为空")
		}
		if first.Status == "" {
			t.Error("打卡项目状态不应为空")
		}
	}

	t.Logf("✓ 获取打卡项目列表成功: %d 个项目", len(checkins))
}

func TestCheckinsGetStatistics(t *testing.T) {
	checkins, err := testClient.Checkins().List(ctx, testGroupID, nil)
	if err != nil {
		t.Fatalf("获取打卡项目列表失败: %v", err)
	}

	if len(checkins) == 0 {
		t.Skip("该星球没有打卡项目")
	}

	checkinID := checkins[0].CheckinID
	stats, err := testClient.Checkins().GetStatistics(ctx, testGroupID, checkinID)
	if err != nil {
		t.Fatalf("获取打卡统计失败: %v", err)
	}

	// 验证返回数据
	if stats == nil {
		t.Fatal("打卡统计不应为 nil")
	}
	if stats.JoinedCount < 0 {
		t.Errorf("参与人数应该 >= 0, got %d", stats.JoinedCount)
	}
	if stats.CompletedCount < 0 {
		t.Errorf("完成人数应该 >= 0, got %d", stats.CompletedCount)
	}
	if stats.CompletedCount > stats.JoinedCount {
		t.Errorf("完成人数应该 <= 参与人数, got %d > %d", stats.CompletedCount, stats.JoinedCount)
	}

	t.Logf("✓ 打卡统计: 参与=%d, 完成=%d", stats.JoinedCount, stats.CompletedCount)
}

func TestCheckinsGetRankingList(t *testing.T) {
	checkins, err := testClient.Checkins().List(ctx, testGroupID, nil)
	if err != nil {
		t.Fatalf("获取打卡项目列表失败: %v", err)
	}

	if len(checkins) == 0 {
		t.Skip("该星球没有打卡项目")
	}

	checkinID := checkins[0].CheckinID
	ranking, err := testClient.Checkins().GetRankingList(ctx, testGroupID, checkinID, nil)
	if err != nil {
		t.Fatalf("获取打卡排行榜失败: %v", err)
	}

	// 验证返回数据
	if ranking == nil {
		t.Fatal("排行榜不应为 nil")
	}

	t.Logf("✓ 获取打卡排行榜成功: %d 个用户", len(ranking))
}

// ============================================================
// Dashboard 模块测试
// ============================================================

func TestDashboardGetOverview(t *testing.T) {
	overview, err := testClient.Dashboard().GetOverview(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠ Dashboard 需要星主权限: %v", err)
		return
	}

	// 验证返回数据
	if overview == nil {
		t.Fatal("概览数据不应为 nil")
	}

	t.Logf("✓ 获取星球概览成功: %v", overview)
}

func TestDashboardGetIncomes(t *testing.T) {
	incomes, err := testClient.Dashboard().GetIncomes(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠ Dashboard 需要星主权限: %v", err)
		return
	}

	// 验证返回数据
	if incomes == nil {
		t.Fatal("收入数据不应为 nil")
	}

	t.Logf("✓ 获取收入概览成功: %v", incomes)
}

// ============================================================
// 完整业务流程测试
// ============================================================

func TestCompleteWorkflow(t *testing.T) {
	// 1. 获取当前用户
	user, err := testClient.Users().Self(ctx)
	if err != nil {
		t.Fatalf("获取当前用户失败: %v", err)
	}
	if user == nil {
		t.Fatal("用户对象不应为 nil")
	}

	// 2. 获取星球列表
	groups, err := testClient.Groups().List(ctx)
	if err != nil {
		t.Fatalf("获取星球列表失败: %v", err)
	}
	if len(groups) == 0 {
		t.Fatal("星球列表不应为空")
	}

	// 3. 查看星球详情
	group, err := testClient.Groups().Get(ctx, testGroupID)
	if err != nil {
		t.Fatalf("获取星球详情失败: %v", err)
	}
	if group.GroupID != testGroupID {
		t.Errorf("星球 ID 应该匹配")
	}

	// 4. 浏览话题列表
	topics, err := testClient.Topics().List(ctx, testGroupID, nil)
	if err != nil {
		t.Fatalf("获取话题列表失败: %v", err)
	}
	if topics == nil {
		t.Fatal("话题列表不应为 nil")
	}

	// 5. 查看精华话题
	digests, err := testClient.Topics().List(ctx, testGroupID, &request.ListTopicsOptions{
		Scope: "digests",
		Count: 5,
	})
	if err != nil {
		t.Fatalf("获取精华话题失败: %v", err)
	}
	if digests == nil {
		t.Fatal("精华话题列表不应为 nil")
	}

	t.Logf("✓ 完整业务流程测试通过")
}

func TestErrorRecovery(t *testing.T) {
	// 触发一个错误
	_, err := testClient.Groups().Get(ctx, 999999999999999)
	if err == nil {
		t.Error("应该返回错误")
	}

	// 客户端应该仍然可用
	groups, err := testClient.Groups().List(ctx)
	if err != nil {
		t.Fatalf("错误后客户端应该仍然可用: %v", err)
	}
	if groups == nil {
		t.Fatal("星球列表不应为 nil")
	}

	t.Logf("✓ 错误恢复测试通过")
}
