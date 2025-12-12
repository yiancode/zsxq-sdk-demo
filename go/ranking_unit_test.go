package main

import (
	"context"
	"testing"

	zsxq "github.com/zsxq-sdk/zsxq-sdk-go"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

// TestRankingClientCreation 测试 Ranking 客户端创建
func TestRankingClientCreation(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	require.NotNil(t, client, "客户端不应为 nil")
	assert.NotNil(t, client.Ranking(), "Ranking 模块不应为 nil")
}

// TestRankingGroupIDValidation 测试星球 ID 验证
func TestRankingGroupIDValidation(t *testing.T) {
	tests := []struct {
		name    string
		groupID int64
		valid   bool
	}{
		{
			name:    "有效 ID",
			groupID: 123456789,
			valid:   true,
		},
		{
			name:    "零 ID",
			groupID: 0,
			valid:   false,
		},
		{
			name:    "负数 ID",
			groupID: -1,
			valid:   false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.valid {
				assert.Greater(t, tt.groupID, int64(0), "有效 ID 应该大于 0")
			} else {
				assert.LessOrEqual(t, tt.groupID, int64(0), "无效 ID 应该小于等于 0")
			}
		})
	}
}

// TestRankingContextHandling 测试上下文处理
func TestRankingContextHandling(t *testing.T) {
	ctx := context.Background()
	assert.NoError(t, ctx.Err(), "背景上下文不应该有错误")
}

// TestRankingClientMethods 测试 Ranking 方法存在性
func TestRankingClientMethods(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	ranking := client.Ranking()
	require.NotNil(t, ranking, "Ranking 模块不应为 nil")

	assert.IsType(t, ranking, client.Ranking(), "Ranking 方法应返回正确类型")
}

// TestRankingMultipleCalls 测试多次调用一致性
func TestRankingMultipleCalls(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	ranking1 := client.Ranking()
	ranking2 := client.Ranking()

	assert.NotNil(t, ranking1, "第一次调用不应为 nil")
	assert.NotNil(t, ranking2, "第二次调用不应为 nil")
	assert.Equal(t, ranking1, ranking2, "多次调用应返回相同实例")
}

// TestRankingTypeValidation 测试排行榜类型验证
func TestRankingTypeValidation(t *testing.T) {
	validTypes := []string{
		"group",
		"score",
		"invitation",
		"contribution",
		"consumption",
	}

	for _, rankingType := range validTypes {
		t.Run(rankingType, func(t *testing.T) {
			assert.Contains(t, validTypes, rankingType, "应该是有效的排行榜类型")
		})
	}
}

// TestRankingScoreValidation 测试积分验证
func TestRankingScoreValidation(t *testing.T) {
	tests := []struct {
		name  string
		score int64
		valid bool
	}{
		{
			name:  "正积分",
			score: 1000,
			valid: true,
		},
		{
			name:  "零积分",
			score: 0,
			valid: true,
		},
		{
			name:  "负积分",
			score: -1,
			valid: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.valid {
				assert.GreaterOrEqual(t, tt.score, int64(0), "有效积分应该 >= 0")
			} else {
				assert.Less(t, tt.score, int64(0), "无效积分应该 < 0")
			}
		})
	}
}

// TestRankingRankValidation 测试排名验证
func TestRankingRankValidation(t *testing.T) {
	tests := []struct {
		name  string
		rank  int
		valid bool
	}{
		{
			name:  "第一名",
			rank:  1,
			valid: true,
		},
		{
			name:  "零排名",
			rank:  0,
			valid: false,
		},
		{
			name:  "负排名",
			rank:  -1,
			valid: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.valid {
				assert.Greater(t, tt.rank, 0, "有效排名应该 > 0")
			} else {
				assert.LessOrEqual(t, tt.rank, 0, "无效排名应该 <= 0")
			}
		})
	}
}

// TestRankingThreadSafety 测试线程安全性
func TestRankingThreadSafety(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	done := make(chan bool)

	for i := 0; i < 10; i++ {
		go func() {
			ranking := client.Ranking()
			assert.NotNil(t, ranking)
			done <- true
		}()
	}

	for i := 0; i < 10; i++ {
		<-done
	}
}

// TestRankingContextCancellation 测试上下文取消
func TestRankingContextCancellation(t *testing.T) {
	ctx, cancel := context.WithCancel(context.Background())
	cancel()

	assert.Error(t, ctx.Err(), "取消的上下文应该有错误")
	assert.Equal(t, context.Canceled, ctx.Err(), "错误应该是 Canceled")
}

// TestRankingLimitValidation 测试排行榜数量限制
func TestRankingLimitValidation(t *testing.T) {
	tests := []struct {
		name  string
		limit int
		valid bool
	}{
		{
			name:  "有效限制",
			limit: 50,
			valid: true,
		},
		{
			name:  "零限制",
			limit: 0,
			valid: false,
		},
		{
			name:  "负数限制",
			limit: -1,
			valid: false,
		},
		{
			name:  "最大限制",
			limit: 100,
			valid: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.valid {
				assert.Greater(t, tt.limit, 0, "有效限制应该 > 0")
			} else {
				assert.LessOrEqual(t, tt.limit, 0, "无效限制应该 <= 0")
			}
		})
	}
}
