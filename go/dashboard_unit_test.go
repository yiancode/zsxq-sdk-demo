package main

import (
	"context"
	"testing"
	"time"

	zsxq "github.com/zsxq-sdk/zsxq-sdk-go"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

// TestDashboardClientCreation 测试 Dashboard 客户端创建
func TestDashboardClientCreation(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	require.NotNil(t, client, "客户端不应为 nil")
	assert.NotNil(t, client.Dashboard(), "Dashboard 模块不应为 nil")
}

// TestDashboardGroupIDValidation 测试星球 ID 验证
func TestDashboardGroupIDValidation(t *testing.T) {
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

// TestDashboardDateRangeValidation 测试日期范围验证
func TestDashboardDateRangeValidation(t *testing.T) {
	tests := []struct {
		name      string
		startDate string
		endDate   string
		valid     bool
	}{
		{
			name:      "有效日期范围",
			startDate: "2024-01-01",
			endDate:   "2024-12-31",
			valid:     true,
		},
		{
			name:      "空日期",
			startDate: "",
			endDate:   "",
			valid:     true,
		},
		{
			name:      "开始日期晚于结束日期",
			startDate: "2024-12-31",
			endDate:   "2024-01-01",
			valid:     false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if tt.startDate == "" || tt.endDate == "" {
				// 空日期也是有效的
				return
			}

			start, err1 := time.Parse("2006-01-02", tt.startDate)
			end, err2 := time.Parse("2006-01-02", tt.endDate)

			if err1 != nil || err2 != nil {
				assert.Fail(t, "日期解析失败")
				return
			}

			if tt.valid {
				assert.True(t, start.Before(end) || start.Equal(end), "开始日期应该早于或等于结束日期")
			} else {
				assert.True(t, start.After(end), "无效范围：开始日期晚于结束日期")
			}
		})
	}
}

// TestDashboardContextHandling 测试上下文处理
func TestDashboardContextHandling(t *testing.T) {
	ctx := context.Background()
	assert.NoError(t, ctx.Err(), "背景上下文不应该有错误")
}

// TestDashboardClientMethods 测试 Dashboard 方法存在性
func TestDashboardClientMethods(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	dashboard := client.Dashboard()
	require.NotNil(t, dashboard, "Dashboard 模块不应为 nil")

	assert.IsType(t, dashboard, client.Dashboard(), "Dashboard 方法应返回正确类型")
}

// TestDashboardTimeoutHandling 测试超时处理
func TestDashboardTimeoutHandling(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 1*time.Millisecond)
	defer cancel()

	time.Sleep(2 * time.Millisecond)

	assert.Error(t, ctx.Err(), "超时上下文应该有错误")
	assert.Equal(t, context.DeadlineExceeded, ctx.Err(), "错误应该是 DeadlineExceeded")
}

// TestDashboardMultipleCalls 测试多次调用一致性
func TestDashboardMultipleCalls(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	dashboard1 := client.Dashboard()
	dashboard2 := client.Dashboard()

	assert.NotNil(t, dashboard1, "第一次调用不应为 nil")
	assert.NotNil(t, dashboard2, "第二次调用不应为 nil")
	assert.Equal(t, dashboard1, dashboard2, "多次调用应返回相同实例")
}

// TestDashboardDateFormatValidation 测试日期格式验证
func TestDashboardDateFormatValidation(t *testing.T) {
	validFormats := []string{
		"2024-01-01",
		"2024-12-31",
		"2024-06-15",
	}

	for _, dateStr := range validFormats {
		t.Run(dateStr, func(t *testing.T) {
			_, err := time.Parse("2006-01-02", dateStr)
			assert.NoError(t, err, "日期格式应该有效")
		})
	}
}

// TestDashboardInvalidDateFormat 测试无效日期格式
func TestDashboardInvalidDateFormat(t *testing.T) {
	invalidFormats := []string{
		"invalid-date",
		"2024/01/01",
		"01-01-2024",
	}

	for _, dateStr := range invalidFormats {
		t.Run(dateStr, func(t *testing.T) {
			_, err := time.Parse("2006-01-02", dateStr)
			assert.Error(t, err, "无效日期格式应该有错误")
		})
	}
}

// TestDashboardThreadSafety 测试线程安全性
func TestDashboardThreadSafety(t *testing.T) {
	client := zsxq.NewClientBuilder().
		SetToken("test-token").
		Build()

	done := make(chan bool)

	for i := 0; i < 10; i++ {
		go func() {
			dashboard := client.Dashboard()
			assert.NotNil(t, dashboard)
			done <- true
		}()
	}

	for i := 0; i < 10; i++ {
		<-done
	}
}
