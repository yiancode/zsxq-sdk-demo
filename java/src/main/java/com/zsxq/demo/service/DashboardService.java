package com.zsxq.demo.service;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.exception.ZsxqException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 数据面板服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ZsxqClient zsxqClient;

    /**
     * 获取星球数据概览
     */
    public Map<String, Object> getOverview(long groupId) {
        try {
            return zsxqClient.dashboard().getOverview(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球数据概览失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球数据概览失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球收入概览
     */
    public Map<String, Object> getIncomes(long groupId) {
        try {
            return zsxqClient.dashboard().getIncomes(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球收入概览失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球收入概览失败: " + e.getMessage(), e);
        }
    }
}
