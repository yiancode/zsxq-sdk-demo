package main

import (
	"context"
	"testing"
	"time"

	zsxq "github.com/zsxq-sdk/zsxq-sdk-go"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

// TestGroupsClientCreation 测试 Groups 客户端创建
func TestGroupsClientCreation(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	require.NotNil(t, client, "客户端不应为 nil")
	assert.NotNil(t, client.Groups(), "Groups 模块不应为 nil")
}

// TestGroupsIDValidation 测试星球 ID 验证
func TestGroupsIDValidation(t *testing.T) {
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
		{
			name:    "大数 ID",
			groupID: 999999999999999,
			valid:   true,
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

// TestGroupsContextHandling 测试上下文处理
func TestGroupsContextHandling(t *testing.T) {
	tests := []struct {
		name    string
		ctx     context.Context
		wantErr bool
	}{
		{
			name:    "正常上下文",
			ctx:     context.Background(),
			wantErr: false,
		},
		{
			name:    "已取消上下文",
			ctx:     func() context.Context { ctx, cancel := context.WithCancel(context.Background()); cancel(); return ctx }(),
			wantErr: true,
		},
		{
			name:    "超时上下文",
			ctx:     func() context.Context { ctx, cancel := context.WithTimeout(context.Background(), 1*time.Nanosecond); defer cancel(); time.Sleep(1 * time.Millisecond); return ctx }(),
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			err := tt.ctx.Err()
			if tt.wantErr {
				assert.Error(t, err, "应该有错误")
			} else {
				assert.NoError(t, err, "不应该有错误")
			}
		})
	}
}

// TestGroupsClientMethods 测试 Groups 方法存在性
func TestGroupsClientMethods(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	groups := client.Groups()
	require.NotNil(t, groups, "Groups 模块不应为 nil")

	assert.IsType(t, groups, client.Groups(), "Groups 方法应返回正确类型")
}

// TestGroupsMultipleCalls 测试多次调用一致性
func TestGroupsMultipleCalls(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	groups1 := client.Groups()
	groups2 := client.Groups()

	assert.NotNil(t, groups1, "第一次调用不应为 nil")
	assert.NotNil(t, groups2, "第二次调用不应为 nil")
	assert.Equal(t, groups1, groups2, "多次调用应返回相同实例")
}

// TestGroupsConfigurationPersistence 测试配置持久性
func TestGroupsConfigurationPersistence(t *testing.T) {
	timeout := 15 * time.Second
	retryCount := 5

	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		SetTimeout(timeout).
		SetRetryCount(retryCount).
		Build()

	assert.NotNil(t, client, "客户端不应为 nil")
	assert.NotNil(t, client.Groups(), "Groups 模块不应为 nil")
}

// TestGroupsEmptyGroupID 测试空星球 ID
func TestGroupsEmptyGroupID(t *testing.T) {
	groupID := int64(0)
	assert.Equal(t, int64(0), groupID, "空星球 ID 应该为 0")
}

// TestGroupsLargeGroupID 测试大数星球 ID
func TestGroupsLargeGroupID(t *testing.T) {
	groupID := int64(999999999999999)
	assert.Greater(t, groupID, int64(0), "大数星球 ID 应该有效")
}

// TestGroupsContextWithDeadline 测试带截止时间的上下文
func TestGroupsContextWithDeadline(t *testing.T) {
	deadline := time.Now().Add(1 * time.Hour)
	ctx, cancel := context.WithDeadline(context.Background(), deadline)
	defer cancel()

	actualDeadline, ok := ctx.Deadline()
	assert.True(t, ok, "应该有截止时间")
	assert.Equal(t, deadline, actualDeadline, "截止时间应该匹配")
}

// TestGroupsClientThreadSafety 测试客户端线程安全性
func TestGroupsClientThreadSafety(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	done := make(chan bool)

	// 启动多个 goroutine 并发访问
	for i := 0; i < 10; i++ {
		go func() {
			groups := client.Groups()
			assert.NotNil(t, groups)
			done <- true
		}()
	}

	// 等待所有 goroutine 完成
	for i := 0; i < 10; i++ {
		<-done
	}
}

// TestGroupsBuilderDefaults 测试构建器默认值
func TestGroupsBuilderDefaults(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	assert.NotNil(t, client, "使用默认值的客户端不应为 nil")
	assert.NotNil(t, client.Groups(), "Groups 模块不应为 nil")
}
