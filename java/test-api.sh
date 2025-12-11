#!/bin/bash

# API 完整测试脚本
# 测试所有知识星球 SDK API 接口

BASE_URL="http://localhost:8080/api/zsxq"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 计数器
TOTAL=0
PASSED=0
FAILED=0

# 测试函数
test_api() {
    local name="$1"
    local url="$2"
    local expected_success="${3:-true}"

    TOTAL=$((TOTAL + 1))

    response=$(curl -s -w "\n%{http_code}" "$url")
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')

    # 检查 HTTP 状态码和 API 响应
    success=$(echo "$body" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('success', False))" 2>/dev/null || echo "false")

    if [[ "$http_code" == "200" && "$success" == "True" ]]; then
        echo -e "${GREEN}✓${NC} $name"
        PASSED=$((PASSED + 1))
        return 0
    else
        echo -e "${RED}✗${NC} $name (HTTP: $http_code)"
        if [[ "$expected_success" == "true" ]]; then
            error=$(echo "$body" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('message', 'Unknown error'))" 2>/dev/null)
            echo -e "   ${YELLOW}Error: $error${NC}"
        fi
        FAILED=$((FAILED + 1))
        return 1
    fi
}

# 检查服务是否运行
check_service() {
    echo -e "${BLUE}检查服务状态...${NC}"
    if ! curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/users/self" | grep -q "200\|500"; then
        echo -e "${RED}错误: 服务未运行，请先启动应用${NC}"
        echo "运行: ./start.sh"
        exit 1
    fi
    echo -e "${GREEN}✓ 服务已运行${NC}\n"
}

# 获取默认星球 ID
get_group_id() {
    response=$(curl -s "$BASE_URL/groups/default")
    GROUP_ID=$(echo "$response" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('data', {}).get('groupId', '') or d.get('data', {}).get('group_id', ''))" 2>/dev/null)
    if [[ -z "$GROUP_ID" || "$GROUP_ID" == "None" ]]; then
        echo -e "${RED}错误: 无法获取默认星球 ID${NC}"
        exit 1
    fi
    echo -e "${BLUE}默认星球 ID: $GROUP_ID${NC}\n"
}

# 获取当前用户 ID
get_user_id() {
    response=$(curl -s "$BASE_URL/users/self")
    USER_ID=$(echo "$response" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('data', {}).get('userId', '') or d.get('data', {}).get('user_id', ''))" 2>/dev/null)
    if [[ -z "$USER_ID" || "$USER_ID" == "None" ]]; then
        echo -e "${YELLOW}警告: 无法获取用户 ID${NC}"
        USER_ID=""
    fi
}

echo "========================================"
echo "   知识星球 SDK API 完整测试"
echo "========================================"
echo ""

check_service
get_group_id
get_user_id

# ==================== Users 模块 ====================
echo -e "${BLUE}[Users 模块]${NC}"
test_api "获取当前用户信息" "$BASE_URL/users/self"
if [[ -n "$USER_ID" ]]; then
    test_api "获取指定用户信息" "$BASE_URL/users/$USER_ID"
    test_api "获取用户统计" "$BASE_URL/users/$USER_ID/statistics"
    test_api "获取用户头像URL" "$BASE_URL/users/$USER_ID/avatar"
    test_api "获取用户动态足迹" "$BASE_URL/users/$USER_ID/footprints"
    test_api "获取用户创建的星球" "$BASE_URL/users/$USER_ID/created-groups"
fi
test_api "获取屏蔽用户列表" "$BASE_URL/users/blocked"
test_api "获取贡献记录" "$BASE_URL/users/self/contributions"
test_api "获取贡献统计" "$BASE_URL/users/self/contributions/statistics"
test_api "获取成就摘要" "$BASE_URL/users/self/achievements"
test_api "获取关注者统计" "$BASE_URL/users/self/followers/statistics"
test_api "获取用户偏好配置" "$BASE_URL/users/self/preferences"
test_api "获取星球周榜排名" "$BASE_URL/users/self/weekly-ranking?groupId=$GROUP_ID"
echo ""

# ==================== Groups 模块 ====================
echo -e "${BLUE}[Groups 模块]${NC}"
test_api "获取我的星球列表" "$BASE_URL/groups"
test_api "获取默认星球详情" "$BASE_URL/groups/default"
test_api "获取星球详情" "$BASE_URL/groups/$GROUP_ID"
test_api "获取星球统计" "$BASE_URL/groups/$GROUP_ID/statistics"
test_api "获取星球标签" "$BASE_URL/groups/$GROUP_ID/hashtags"
test_api "获取星球菜单" "$BASE_URL/groups/$GROUP_ID/menus"
test_api "获取星球角色成员" "$BASE_URL/groups/$GROUP_ID/role-members"
test_api "获取星球专栏列表" "$BASE_URL/groups/$GROUP_ID/columns"
test_api "获取专栏汇总信息" "$BASE_URL/groups/$GROUP_ID/columns/summary"
test_api "获取星球自定义标签" "$BASE_URL/groups/$GROUP_ID/labels"
test_api "获取星球定时任务" "$BASE_URL/groups/$GROUP_ID/scheduled-tasks"
test_api "获取星球风险预警" "$BASE_URL/groups/$GROUP_ID/warnings"
test_api "获取未读话题数量" "$BASE_URL/groups/unread-count"
echo ""

# ==================== Topics 模块 ====================
echo -e "${BLUE}[Topics 模块]${NC}"
test_api "获取话题列表" "$BASE_URL/groups/$GROUP_ID/topics"
test_api "获取话题列表(精华)" "$BASE_URL/groups/$GROUP_ID/topics?scope=digests"
test_api "获取话题列表(限制数量)" "$BASE_URL/groups/$GROUP_ID/topics?count=5"
test_api "获取置顶话题" "$BASE_URL/groups/$GROUP_ID/topics/sticky"

# 获取第一个话题 ID 用于测试话题详情
TOPIC_ID=$(curl -s "$BASE_URL/groups/$GROUP_ID/topics?count=1" | python3 -c "import sys,json; d=json.load(sys.stdin); topics=d.get('data',[]); t=topics[0] if topics else {}; print(t.get('topicId','') or t.get('topic_id',''))" 2>/dev/null)
if [[ -n "$TOPIC_ID" && "$TOPIC_ID" != "None" ]]; then
    test_api "获取话题详情" "$BASE_URL/topics/$TOPIC_ID"
    test_api "获取话题评论" "$BASE_URL/topics/$TOPIC_ID/comments"
    test_api "获取话题打赏列表" "$BASE_URL/topics/$TOPIC_ID/rewards"
fi

# 获取标签 ID 用于测试
HASHTAG_ID=$(curl -s "$BASE_URL/groups/$GROUP_ID/hashtags" | python3 -c "import sys,json; d=json.load(sys.stdin); tags=d.get('data',[]); t=tags[0] if tags else {}; print(t.get('hashtagId','') or t.get('hashtag_id',''))" 2>/dev/null)
if [[ -n "$HASHTAG_ID" && "$HASHTAG_ID" != "None" ]]; then
    test_api "按标签获取话题" "$BASE_URL/hashtags/$HASHTAG_ID/topics"
fi
echo ""

# ==================== Checkins 模块 ====================
echo -e "${BLUE}[Checkins 模块]${NC}"
test_api "获取打卡项目列表" "$BASE_URL/groups/$GROUP_ID/checkins"
test_api "获取进行中的打卡" "$BASE_URL/groups/$GROUP_ID/checkins?scope=ongoing"

# 获取第一个打卡项目 ID 用于测试
CHECKIN_ID=$(curl -s "$BASE_URL/groups/$GROUP_ID/checkins" | python3 -c "import sys,json; d=json.load(sys.stdin); checkins=d.get('data',[]); c=checkins[0] if checkins else {}; print(c.get('checkinId','') or c.get('checkin_id',''))" 2>/dev/null)
if [[ -n "$CHECKIN_ID" && "$CHECKIN_ID" != "None" ]]; then
    test_api "获取打卡项目详情" "$BASE_URL/groups/$GROUP_ID/checkins/$CHECKIN_ID"
    test_api "获取打卡统计" "$BASE_URL/groups/$GROUP_ID/checkins/$CHECKIN_ID/statistics"
    test_api "获取打卡排行榜" "$BASE_URL/groups/$GROUP_ID/checkins/$CHECKIN_ID/ranking"
    test_api "获取打卡话题列表" "$BASE_URL/groups/$GROUP_ID/checkins/$CHECKIN_ID/topics"
    test_api "获取打卡每日统计" "$BASE_URL/groups/$GROUP_ID/checkins/$CHECKIN_ID/daily-statistics"
    test_api "获取打卡参与用户" "$BASE_URL/groups/$GROUP_ID/checkins/$CHECKIN_ID/joined-users"
    test_api "获取我的打卡记录" "$BASE_URL/groups/$GROUP_ID/checkins/$CHECKIN_ID/my-records"
    test_api "获取我的打卡日期" "$BASE_URL/groups/$GROUP_ID/checkins/$CHECKIN_ID/my-days"
    test_api "获取我的打卡统计" "$BASE_URL/groups/$GROUP_ID/checkins/$CHECKIN_ID/my-statistics"
else
    echo -e "${YELLOW}  (该星球没有打卡项目，跳过打卡详情测试)${NC}"
fi
echo ""

# ==================== Dashboard 模块 ====================
echo -e "${BLUE}[Dashboard 模块]${NC}"
test_api "获取星球概览" "$BASE_URL/dashboard/groups/$GROUP_ID/overview"
test_api "获取收入概览" "$BASE_URL/dashboard/groups/$GROUP_ID/incomes"
test_api "获取星球权限配置" "$BASE_URL/dashboard/groups/$GROUP_ID/privileges"
test_api "获取发票统计" "$BASE_URL/dashboard/invoices/statistics"
test_api "获取积分排行" "$BASE_URL/dashboard/groups/$GROUP_ID/scoreboard/ranking"
echo ""

# ==================== Ranking 模块 ====================
echo -e "${BLUE}[Ranking 模块]${NC}"
test_api "获取星球排行榜" "$BASE_URL/ranking/groups/$GROUP_ID"
test_api "获取星球排行统计" "$BASE_URL/ranking/groups/$GROUP_ID/statistics"
test_api "获取积分排行榜" "$BASE_URL/ranking/groups/$GROUP_ID/score"
test_api "获取我的积分统计" "$BASE_URL/ranking/groups/$GROUP_ID/score/my-statistics"
test_api "获取积分榜设置" "$BASE_URL/ranking/groups/$GROUP_ID/scoreboard/settings"
test_api "获取邀请排行榜" "$BASE_URL/ranking/groups/$GROUP_ID/invitations"
test_api "获取贡献排行榜" "$BASE_URL/ranking/groups/$GROUP_ID/contributions"
echo ""

# ==================== Misc 模块 ====================
echo -e "${BLUE}[Misc 模块]${NC}"
test_api "获取动态列表" "$BASE_URL/dynamics"
test_api "获取动态列表(带参数)" "$BASE_URL/dynamics?scope=general&count=5"
test_api "获取全局配置" "$BASE_URL/settings"
test_api "获取 PK 群组详情" "$BASE_URL/pk-groups/$GROUP_ID"
test_api "获取 PK 对战记录" "$BASE_URL/pk-groups/$GROUP_ID/battles"
echo ""

# ==================== 测试结果统计 ====================
echo "========================================"
echo -e "   ${BLUE}测试结果统计${NC}"
echo "========================================"
echo -e "   总计: $TOTAL"
echo -e "   ${GREEN}通过: $PASSED${NC}"
echo -e "   ${RED}失败: $FAILED${NC}"
echo ""

if [[ $FAILED -eq 0 ]]; then
    echo -e "${GREEN}所有测试通过!${NC}"
    exit 0
else
    echo -e "${YELLOW}部分测试失败，请检查日志${NC}"
    exit 1
fi
