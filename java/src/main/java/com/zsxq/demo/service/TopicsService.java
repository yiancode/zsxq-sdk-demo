package com.zsxq.demo.service;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.Topic;
import com.zsxq.sdk.request.TopicsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 话题服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TopicsService {

    private final ZsxqClient zsxqClient;

    /**
     * 获取话题列表
     */
    public List<Topic> listTopics(long groupId, Integer count, String scope) {
        try {
            TopicsRequest.ListTopicsOptions options = new TopicsRequest.ListTopicsOptions();
            if (count != null) {
                options.count(count);
            }
            if (scope != null) {
                options.scope(scope);
            }
            return zsxqClient.topics().list(groupId, options);
        } catch (ZsxqException e) {
            log.error("获取话题列表失败: groupId={}", groupId, e);
            throw new RuntimeException("获取话题列表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取话题详情
     */
    public Topic getTopic(long topicId) {
        try {
            return zsxqClient.topics().get(topicId);
        } catch (ZsxqException e) {
            log.error("获取话题详情失败: topicId={}", topicId, e);
            throw new RuntimeException("获取话题详情失败: " + e.getMessage(), e);
        }
    }
}
