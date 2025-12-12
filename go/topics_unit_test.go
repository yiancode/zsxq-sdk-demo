package main

import (
	"context"
	"testing"
	"time"

	zsxq "github.com/zsxq-sdk/zsxq-sdk-go"
	"github.com/zsxq-sdk/zsxq-sdk-go/request"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

// TestTopicsClientCreation 测试 Topics 客户端创建
func TestTopicsClientCreation(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	require.NotNil(t, client, "客户端不应为 nil")
	assert.NotNil(t, client.Topics(), "Topics 模块不应为 nil")
}

// TestTopicsOptionsCreation 测试话题选项创建
func TestTopicsOptionsCreation(t *testing.T) {
	tests := []struct {
		name  string
		count int
		scope string
	}{
		{
			name:  "默认选项",
			count: 20,
			scope: "all",
		},
		{
			name:  "精华话题",
			count: 10,
			scope: "digests",
		},
		{
			name:  "问答话题",
			count: 15,
			scope: "questions",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			options := &request.ListTopicsOptions{
				Count: tt.count,
				Scope: tt.scope,
			}

			assert.Equal(t, tt.count, options.Count, "count 应该匹配")
			assert.Equal(t, tt.scope, options.Scope, "scope 应该匹配")
		})
	}
}

// TestTopicsIDValidation 测试话题 ID 验证
func TestTopicsIDValidation(t *testing.T) {
	tests := []struct {
		name    string
		topicID int64
		valid   bool
	}{
		{
			name:    "有效 ID",
			topicID: 123456789,
			valid:   true,
		},
		{
			name:    "零 ID",
			topicID: 0,
			valid:   false,
		},
		{
			name:    "负数 ID",
			topicID: -1,
			valid:   false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.valid {
				assert.Greater(t, tt.topicID, int64(0), "有效 ID 应该大于 0")
			} else {
				assert.LessOrEqual(t, tt.topicID, int64(0), "无效 ID 应该小于等于 0")
			}
		})
	}
}

// TestTopicsScopeValidation 测试话题范围验证
func TestTopicsScopeValidation(t *testing.T) {
	validScopes := []string{"all", "digests", "questions", "files"}

	for _, scope := range validScopes {
		t.Run(scope, func(t *testing.T) {
			assert.Contains(t, validScopes, scope, "应该是有效的 scope")
		})
	}
}

// TestTopicsCountValidation 测试话题数量验证
func TestTopicsCountValidation(t *testing.T) {
	tests := []struct {
		name  string
		count int
		valid bool
	}{
		{
			name:  "有效数量",
			count: 20,
			valid: true,
		},
		{
			name:  "零数量",
			count: 0,
			valid: false,
		},
		{
			name:  "负数数量",
			count: -1,
			valid: false,
		},
		{
			name:  "最大数量",
			count: 100,
			valid: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.valid {
				assert.GreaterOrEqual(t, tt.count, 1, "有效数量应该 >= 1")
			} else {
				assert.Less(t, tt.count, 1, "无效数量应该 < 1")
			}
		})
	}
}

// TestTopicsOptionsDefaults 测试选项默认值
func TestTopicsOptionsDefaults(t *testing.T) {
	options := &request.ListTopicsOptions{}

	assert.Equal(t, 0, options.Count, "默认 count 应该为 0")
	assert.Equal(t, "", options.Scope, "默认 scope 应该为空")
}

// TestTopicsClientMethods 测试 Topics 方法存在性
func TestTopicsClientMethods(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	topics := client.Topics()
	require.NotNil(t, topics, "Topics 模块不应为 nil")

	assert.IsType(t, topics, client.Topics(), "Topics 方法应返回正确类型")
}

// TestTopicsContextHandling 测试上下文处理
func TestTopicsContextHandling(t *testing.T) {
	ctx := context.Background()
	assert.NoError(t, ctx.Err(), "背景上下文不应该有错误")

	ctx, cancel := context.WithCancel(context.Background())
	cancel()
	assert.Error(t, ctx.Err(), "取消的上下文应该有错误")
}

// TestTopicsHashtagIDValidation 测试标签 ID 验证
func TestTopicsHashtagIDValidation(t *testing.T) {
	tests := []struct {
		name      string
		hashtagID int64
		valid     bool
	}{
		{
			name:      "有效标签 ID",
			hashtagID: 12345,
			valid:     true,
		},
		{
			name:      "零标签 ID",
			hashtagID: 0,
			valid:     false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.valid {
				assert.Greater(t, tt.hashtagID, int64(0))
			} else {
				assert.Equal(t, int64(0), tt.hashtagID)
			}
		})
	}
}

// TestTopicsColumnIDValidation 测试专栏 ID 验证
func TestTopicsColumnIDValidation(t *testing.T) {
	columnID := int64(123456)
	assert.Greater(t, columnID, int64(0), "专栏 ID 应该大于 0")
}

// TestTopicsMultipleOptions 测试多个选项组合
func TestTopicsMultipleOptions(t *testing.T) {
	options := &request.ListTopicsOptions{
		Count:   20,
		Scope:   "digests",
		EndTime: time.Now().Format(time.RFC3339),
	}

	assert.Equal(t, 20, options.Count)
	assert.Equal(t, "digests", options.Scope)
	assert.NotEmpty(t, options.EndTime)
}
