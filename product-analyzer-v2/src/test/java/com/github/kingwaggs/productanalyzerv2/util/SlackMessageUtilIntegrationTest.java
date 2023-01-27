package com.github.kingwaggs.productanalyzerv2.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active:local")
class SlackMessageUtilIntegrationTest {

    @Autowired
    private SlackMessageUtil slackMessageUtil;

    @Test
    @DisplayName("성공/에러 메시지 발신 성공")
    void sendMessageSuccessfully() {
        // given
        String message = "테스트 메시지 입니다.";

        // when
        slackMessageUtil.sendSuccess(message);
        slackMessageUtil.sendError(message);

        // then
        // Slack 확인
    }

}