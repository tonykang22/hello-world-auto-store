package com.github.kingwaggs.ordermanager.util;

import com.github.kingwaggs.ordermanager.config.TestConfig;
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
    @DisplayName("슬랙 제품관련 메세지 전송")
    void sendProductMessage() {
        // given
         String message = "Testing : Product A has been purchased via Zinc API. At " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // when
        slackMessageUtil.sendOrder(message);

        // then
        // Check Slack Message
    }
}