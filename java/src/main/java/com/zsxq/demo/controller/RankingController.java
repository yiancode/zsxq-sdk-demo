package com.zsxq.demo.controller;

import com.zsxq.demo.model.ApiResponse;
import com.zsxq.demo.service.ZsxqService;
import com.zsxq.sdk.model.InvitationRankingItem;
import com.zsxq.sdk.model.RankingItem;
import com.zsxq.sdk.model.RankingStatistics;
import com.zsxq.sdk.model.ScoreboardSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 排行榜 API 控制器
 */
@RestController
@RequestMapping("/api/zsxq/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final ZsxqService zsxqService;

    /**
     * 获取星球排行榜
     */
    @GetMapping("/groups/{groupId}")
    public ApiResponse<List<RankingItem>> getGroupRanking(@PathVariable Long groupId) {
        try {
            return ApiResponse.success(zsxqService.getGroupRanking(groupId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取星球排行统计
     */
    @GetMapping("/groups/{groupId}/statistics")
    public ApiResponse<RankingStatistics> getGroupRankingStats(@PathVariable Long groupId) {
        try {
            return ApiResponse.success(zsxqService.getGroupRankingStats(groupId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取积分排行榜
     */
    @GetMapping("/groups/{groupId}/score")
    public ApiResponse<List<RankingItem>> getScoreRanking(@PathVariable Long groupId) {
        try {
            return ApiResponse.success(zsxqService.getScoreRanking(groupId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取我的积分统计
     */
    @GetMapping("/groups/{groupId}/score/my-statistics")
    public ApiResponse<Map<String, Object>> getMyScoreStats(@PathVariable Long groupId) {
        try {
            return ApiResponse.success(zsxqService.getMyScoreStats(groupId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取积分榜设置
     */
    @GetMapping("/groups/{groupId}/scoreboard/settings")
    public ApiResponse<ScoreboardSettings> getScoreboardSettings(@PathVariable Long groupId) {
        try {
            return ApiResponse.success(zsxqService.getScoreboardSettings(groupId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取邀请排行榜，对齐 App 日榜/周榜/月榜/自定义。
     *
     * period=daily|weekly|monthly 时只传 begin_time + count + with_extra。
     * period=custom 时必须带 beginTime、endTime（最大跨度 31 天）。
     */
    @GetMapping("/groups/{groupId}/invitations")
    public ApiResponse<List<InvitationRankingItem>> getInvitationRanking(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "weekly") String period,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime) {
        try {
            return ApiResponse.success(
                    zsxqService.getInvitationRankingByPeriod(groupId, period, beginTime, endTime));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取贡献排行榜
     */
    @GetMapping("/groups/{groupId}/contributions")
    public ApiResponse<List<RankingItem>> getContributionRanking(@PathVariable Long groupId) {
        try {
            return ApiResponse.success(zsxqService.getContributionRanking(groupId));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
