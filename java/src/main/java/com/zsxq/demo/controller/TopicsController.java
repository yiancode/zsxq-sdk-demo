package com.zsxq.demo.controller;

import com.zsxq.demo.model.ApiResponse;
import com.zsxq.demo.service.TopicsService;
import com.zsxq.sdk.model.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 话题 API 控制器
 */
@RestController
@RequestMapping("/api/zsxq/topics")
@RequiredArgsConstructor
public class TopicsController {

    private final TopicsService topicsService;

    /**
     * 获取星球话题列表
     */
    @GetMapping("/groups/{groupId}")
    public ApiResponse<List<Topic>> listTopics(
            @PathVariable Long groupId,
            @RequestParam(required = false) Integer count,
            @RequestParam(required = false) String scope) {
        try {
            List<Topic> topics = topicsService.listTopics(groupId, count, scope);
            return ApiResponse.success(topics);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取话题详情
     */
    @GetMapping("/{topicId}")
    public ApiResponse<Topic> getTopic(@PathVariable Long topicId) {
        try {
            Topic topic = topicsService.getTopic(topicId);
            return ApiResponse.success(topic);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
