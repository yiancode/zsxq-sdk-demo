package com.zsxq.demo.service;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.model.CheckinStatistics;
import com.zsxq.sdk.model.RankingItem;
import com.zsxq.sdk.request.CheckinsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 打卡服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckinsService {

    private final ZsxqClient zsxqClient;

    /**
     * 获取打卡项目列表
     */
    public List<Checkin> listCheckins(long groupId, String scope) {
        try {
            CheckinsRequest.ListCheckinsOptions options = null;
            if (scope != null) {
                options = new CheckinsRequest.ListCheckinsOptions().scope(scope);
            }
            return zsxqClient.checkins().list(groupId, options);
        } catch (ZsxqException e) {
            log.error("获取打卡列表失败: groupId={}", groupId, e);
            throw new RuntimeException("获取打卡列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取打卡项目详情
     */
    public Checkin getCheckin(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().get(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取打卡详情失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取打卡详情失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取打卡统计
     */
    public CheckinStatistics getStatistics(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().getStatistics(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取打卡统计失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取打卡统计失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取打卡排行榜
     */
    public List<RankingItem> getRanking(long groupId, long checkinId) {
        try {
            return zsxqClient.checkins().getRankingList(groupId, checkinId);
        } catch (ZsxqException e) {
            log.error("获取打卡排行榜失败: groupId={}, checkinId={}", groupId, checkinId, e);
            throw new RuntimeException("获取打卡排行榜失败: " + e.getMessage(), e);
        }
    }
}
