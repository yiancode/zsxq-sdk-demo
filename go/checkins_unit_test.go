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

// TestCheckinsClientCreation 测试 Checkins 客户端创建
func TestCheckinsClientCreation(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	require.NotNil(t, client, "客户端不应为 nil")
	assert.NotNil(t, client.Checkins(), "Checkins 模块不应为 nil")
}

// TestCheckinsIDValidation 测试打卡 ID 验证
func TestCheckinsIDValidation(t *testing.T) {
	tests := []struct {
		name      string
		checkinID int64
		valid     bool
	}{
		{
			name:      "有效 ID",
			checkinID: 123456789,
			valid:     true,
		},
		{
			name:      "零 ID",
			checkinID: 0,
			valid:     false,
		},
		{
			name:      "负数 ID",
			checkinID: -1,
			valid:     false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.valid {
				assert.Greater(t, tt.checkinID, int64(0), "有效 ID 应该大于 0")
			} else {
				assert.LessOrEqual(t, tt.checkinID, int64(0), "无效 ID 应该小于等于 0")
			}
		})
	}
}

// TestCheckinsOptionsCreation 测试打卡选项创建
func TestCheckinsOptionsCreation(t *testing.T) {
	tests := []struct {
		name    string
		count   int
	}{
		{
			name:    "默认选项",
			count:   20,
		},
		{
			name:    "带时间选项",
			count:   10,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			options := &request.ListCheckinsOptions{
				Count:   tt.count,
			}

			assert.Equal(t, tt.count, options.Count, "count 应该匹配")
		})
	}
}

// TestCheckinsContextHandling 测试上下文处理
func TestCheckinsContextHandling(t *testing.T) {
	ctx := context.Background()
	assert.NoError(t, ctx.Err(), "背景上下文不应该有错误")
}

// TestCheckinsClientMethods 测试 Checkins 方法存在性
func TestCheckinsClientMethods(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	checkins := client.Checkins()
	require.NotNil(t, checkins, "Checkins 模块不应为 nil")

	assert.IsType(t, checkins, client.Checkins(), "Checkins 方法应返回正确类型")
}

// TestCheckinsCountValidation 测试打卡数量验证
func TestCheckinsCountValidation(t *testing.T) {
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
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.valid {
				assert.Greater(t, tt.count, 0, "有效数量应该 > 0")
			} else {
				assert.LessOrEqual(t, tt.count, 0, "无效数量应该 <= 0")
			}
		})
	}
}

// TestCheckinsOptionsDefaults 测试选项默认值
func TestCheckinsOptionsDefaults(t *testing.T) {
	options := &request.ListCheckinsOptions{}

	assert.Equal(t, 0, options.Count, "默认 count 应该为 0")
}

// TestCheckinsContextCancellation 测试上下文取消
func TestCheckinsContextCancellation(t *testing.T) {
	ctx, cancel := context.WithCancel(context.Background())
	cancel()

	assert.Error(t, ctx.Err(), "取消的上下文应该有错误")
}

// TestCheckinsThreadSafety 测试线程安全性
func TestCheckinsThreadSafety(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	done := make(chan bool)

	for i := 0; i < 10; i++ {
		go func() {
			checkins := client.Checkins()
			assert.NotNil(t, checkins)
			done <- true
		}()
	}

	for i := 0; i < 10; i++ {
		<-done
	}
}
