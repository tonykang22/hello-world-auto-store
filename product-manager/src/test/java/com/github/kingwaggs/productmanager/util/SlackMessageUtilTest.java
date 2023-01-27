package com.github.kingwaggs.productmanager.util;

import com.github.kingwaggs.productmanager.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest(properties = "spring.profiles.active:local", classes = TestConfig.class)
class SlackMessageUtilTest {

    @Autowired
    private SlackMessageUtil slackMessageUtil;

    @Test
    @DisplayName("슬랙 에러 메세지 전송")
    void sendErrorMessage() {
        // given
        String message = "Testing : Error occurred. At " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // when
        slackMessageUtil.sendError(message);

        // then
        // Check Slack Message
    }


    @Test
    @DisplayName("슬랙 성공 메세지 전송")
    void sendProductMessage() {
        // given
         String message = "Testing : Job A finished successfully. At " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // when
        slackMessageUtil.sendSuccess(message);

        // then
        // Check Slack Message
    }
}