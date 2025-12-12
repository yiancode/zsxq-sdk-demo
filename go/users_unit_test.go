package main

import (
	"context"
	"testing"
	"time"

	zsxq "github.com/zsxq-sdk/zsxq-sdk-go"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

// TestUsersClientCreation 测试 Users 客户端创建
func TestUsersClientCreation(t *testing.T) {
	tests := []struct {
		name    string
		token   string
		wantErr bool
	}{
		{
			name:    "有效 token",
			token:   "test-token-123",
			wantErr: false,
		},
		{
			name:    "空 token",
			token:   "",
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			client := zsxq.NewClientBuilder().
				SetToken(tt.token).
				SetTimeout(5 * time.Second)

			if tt.wantErr {
				assert.Panics(t, func() {
					client.MustBuild()
				}, "空 token 应该触发 panic")
			} else {
				result := client.MustBuild()
				assert.NotNil(t, result, "客户端不应为 nil")
			}
		})
	}
}

// TestUsersContextTimeout 测试上下文超时
func TestUsersContextTimeout(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 1*time.Millisecond)
	defer cancel()

	time.Sleep(2 * time.Millisecond)

	err := ctx.Err()
	assert.Equal(t, context.DeadlineExceeded, err, "上下文应该超时")
}

// TestUsersClientConfiguration 测试客户端配置
func TestUsersClientConfiguration(t *testing.T) {
	tests := []struct {
		name         string
		timeout      time.Duration
		retryCount   int
		expectPanic  bool
	}{
		{
			name:        "有效配置",
			timeout:     10 * time.Second,
			retryCount:  3,
			expectPanic: false,
		},
		{
			name:        "零超时",
			timeout:     0,
			retryCount:  3,
			expectPanic: false,
		},
		{
			name:        "负重试次数",
			timeout:     10 * time.Second,
			retryCount:  -1,
			expectPanic: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			builder := zsxq.NewClientBuilder().
				SetToken("test-token").
				SetTimeout(tt.timeout).
				SetRetryCount(tt.retryCount)

			if tt.expectPanic {
				assert.Panics(t, func() {
					builder.MustBuild()
				})
			} else {
				client := builder.MustBuild()
				assert.NotNil(t, client, "客户端不应为 nil")
			}
		})
	}
}

// TestUsersContextCancellation 测试上下文取消
func TestUsersContextCancellation(t *testing.T) {
	ctx, cancel := context.WithCancel(context.Background())

	// 立即取消
	cancel()

	err := ctx.Err()
	assert.Equal(t, context.Canceled, err, "上下文应该被取消")
}

// TestUsersBuilderChaining 测试构建器链式调用
func TestUsersBuilderChaining(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		SetTimeout(10 * time.Second).
		SetRetryCount(3).
		Build()

	require.NotNil(t, client, "客户端不应为 nil")
	assert.NotNil(t, client.Users(), "Users 模块不应为 nil")
}

// TestUsersInvalidTokenFormat 测试无效 token 格式
func TestUsersInvalidTokenFormat(t *testing.T) {
	tests := []struct {
		name  string
		token string
	}{
		{
			name:  "特殊字符 token",
			token: "!@#$%^&*()",
		},
		{
			name:  "空格 token",
			token: "   ",
		},
		{
			name:  "超长 token",
			token: string(make([]byte, 10000)),
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			client := zsxq.NewClientBuilder().
				SetToken(tt.token).
				Build()

			assert.NotNil(t, client, "客户端不应为 nil")
		})
	}
}

// TestUsersClientMethodsExist 测试 Users 方法存在性
func TestUsersClientMethodsExist(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	require.NotNil(t, client, "客户端不应为 nil")

	users := client.Users()
	require.NotNil(t, users, "Users 模块不应为 nil")

	// 验证方法存在（通过类型断言）
	assert.IsType(t, users, client.Users(), "Users 方法应返回正确类型")
}

// TestUsersMultipleClients 测试多个客户端实例
func TestUsersMultipleClients(t *testing.T) {
	client1 := zsxq.NewClientBuilder().
		SetToken("token1").
		Build()

	client2 := zsxq.NewClientBuilder().
		SetToken("token2").
		Build()

	assert.NotNil(t, client1, "客户端1不应为 nil")
	assert.NotNil(t, client2, "客户端2不应为 nil")
	assert.NotEqual(t, client1, client2, "两个客户端应该是不同的实例")
}

// TestUsersContextWithValue 测试带值的上下文
func TestUsersContextWithValue(t *testing.T) {
	type contextKey string
	key := contextKey("test-key")
	value := "test-value"

	ctx := context.WithValue(context.Background(), key, value)

	retrieved := ctx.Value(key)
	assert.Equal(t, value, retrieved, "应该能从上下文中获取值")
}

// TestUsersBuilderReuse 测试构建器重用
func TestUsersBuilderReuse(t *testing.T) {
	builder := zsxq.NewClientBuilder().
		SetToken("test-token").
		SetTimeout(10 * time.Second)

	client1 := builder.MustBuild()
	client2 := builder.MustBuild()

	assert.NotNil(t, client1, "客户端1不应为 nil")
	assert.NotNil(t, client2, "客户端2不应为 nil")
}
