package com.zsxq.demo.controller;

import com.zsxq.demo.model.ApiResponse;
import com.zsxq.demo.service.CheckinsService;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.model.CheckinStatistics;
import com.zsxq.sdk.model.RankingItem;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 打卡 API 控制器
 */
@RestController
@RequestMapping("/api/zsxq/checkins")
@RequiredArgsConstructor
public class CheckinsController {

    private final CheckinsService checkinsService;

    /**
     * 获取打卡项目列表
     */
    @GetMapping("/groups/{groupId}")
    public ApiResponse<List<Checkin>> listCheckins(
            @PathVariable Long groupId,
            @RequestParam(required = false) String scope) {
        try {
            List<Checkin> checkins = checkinsService.listCheckins(groupId, scope);
            return ApiResponse.success(checkins);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取打卡项目详情
     */
    @GetMapping("/groups/{groupId}/{checkinId}")
    public ApiResponse<Checkin> getCheckin(
            @PathVariable Long groupId,
            @PathVariable Long checkinId) {
        try {
            Checkin checkin = checkinsService.getCheckin(groupId, checkinId);
            return ApiResponse.success(checkin);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取打卡统计
     */
    @GetMapping("/groups/{groupId}/{checkinId}/statistics")
    public ApiResponse<CheckinStatistics> getStatistics(
            @PathVariable Long groupId,
            @PathVariable Long checkinId) {
        try {
            CheckinStatistics stats = checkinsService.getStatistics(groupId, checkinId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取打卡排行榜
     */
    @GetMapping("/groups/{groupId}/{checkinId}/ranking")
    public ApiResponse<List<RankingItem>> getRanking(
            @PathVariable Long groupId,
            @PathVariable Long checkinId) {
        try {
            List<RankingItem> ranking = checkinsService.getRanking(groupId, checkinId);
            return ApiResponse.success(ranking);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
