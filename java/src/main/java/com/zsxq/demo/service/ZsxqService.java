package com.zsxq.demo.service;

import com.zsxq.demo.config.ZsxqProperties;
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.Group;
import com.zsxq.sdk.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 知识星球服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ZsxqService {

    private final ZsxqClient zsxqClient;
    private final ZsxqProperties properties;

    /**
     * 获取当前用户信息
     */
    public User getCurrentUser() {
        try {
            return zsxqClient.users().self();
        } catch (ZsxqException e) {
            log.error("获取当前用户失败", e);
            throw new RuntimeException("获取当前用户失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取用户统计
     */
    public Map<String, Object> getUserStatistics(long userId) {
        try {
            return zsxqClient.users().getStatistics(userId);
        } catch (ZsxqException e) {
            log.error("获取用户统计失败", e);
            throw new RuntimeException("获取用户统计失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取我的星球列表
     */
    public List<Group> getMyGroups() {
        try {
            return zsxqClient.groups().list();
        } catch (ZsxqException e) {
            log.error("获取星球列表失败", e);
            throw new RuntimeException("获取星球列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取星球详情
     */
    public Group getGroup(long groupId) {
        try {
            return zsxqClient.groups().get(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球详情失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球详情失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取默认星球详情
     */
    public Group getDefaultGroup() {
        return getGroup(properties.getGroupId());
    }

    /**
     * 获取星球统计
     */
    public Map<String, Object> getGroupStatistics(long groupId) {
        try {
            return zsxqClient.groups().getStatistics(groupId);
        } catch (ZsxqException e) {
            log.error("获取星球统计失败: groupId={}", groupId, e);
            throw new RuntimeException("获取星球统计失败: " + e.getMessage(), e);
        }
    }
}
