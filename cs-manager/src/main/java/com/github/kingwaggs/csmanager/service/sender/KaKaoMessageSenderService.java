package com.github.kingwaggs.csmanager.service.sender;

import com.github.kingwaggs.csmanager.domain.dto.Shipping;
import com.github.kingwaggs.csmanager.domain.dto.Welcome;
import com.github.kingwaggs.csmanager.util.SlackMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.KakaoOption;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@PropertySource("classpath:kakao.properties")
@RequiredArgsConstructor
public class KaKaoMessageSenderService implements MessageSender{

    private static final boolean DISABLE = true;
    private static final String NAME = "#{name}";
    private static final String TRACKING_NUMBER = "#{shipping_id}";

    private final DefaultMessageService messageService;
    private final SlackMessageUtil slackMessageUtil;

    @Value("${plus.friend.id}")
    private String pfId;

    @Value("${welcome.template.id}")
    private String welcomeTemplateId;

    @Value("${shipping.template.id}")
    private String shippingTemplateId;

    @Value("${outgoing.phone.number}")
    private String outgoingPhoneNumber;

    @Override
    public void sendWelcomeAlarm(Welcome welcome) {
        try {
            Map<String, String> variables = new HashMap<>();
            variables.put(NAME, welcome.getCustomerName());

            Message message = createKakaoMessage(welcome.getCustomerPhoneNumber(), welcomeTemplateId, variables);
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
            log.error("Exception occurred during sending kakao welcome-message to customer. FailedMessage: {}, " +
                    "exception detail: {}", exception.getFailedMessageList(), exception.getMessage(), exception);
            slackMessageUtil.sendErrorMessage("`cs-manager`::`send welcome-message` 도중 예외가 발생했습니다.\n상세 내용:: "
                    + exception.getFailedMessageList());
        } catch (Exception exception) {
            log.error("Exception occurred during sending kakao welcome-message to customer.", exception);
            slackMessageUtil.sendErrorMessage("`cs-manager`::`send welcome-message` 도중 예외가 발생했습니다.\n상세 내용:: "
                    + exception);
        }
    }

    @Override
    public void sendShippingAlarm(Shipping shipping) {
        try {
            Map<String, String> variables = new HashMap<>();
            variables.put(NAME, shipping.getCustomerName());
            variables.put(TRACKING_NUMBER, shipping.getShippingInvoice());

            Message message = createKakaoMessage(shipping.getCustomerPhoneNumber(), shippingTemplateId, variables);
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException exception) {
            log.error("Exception occurred during sending kakao shipping-message to customer. FailedMessage: {}, " +
                    "exception detail: {}", exception.getFailedMessageList(), exception.getMessage(), exception);
            slackMessageUtil.sendErrorMessage("`cs-manager`::`send shipping-message` 도중 예외가 발생했습니다.\n상세 내용:: "
                    + exception.getFailedMessageList());
        } catch (Exception exception) {
            log.error("Exception occurred during sending kakao shipping-message to customer.", exception);
            slackMessageUtil.sendErrorMessage("`cs-manager`::`send shipping-message` 도중 예외가 발생했습니다.\n상세 내용:: "
                    + exception);
        }
    }

    private Message createKakaoMessage(String customerPhoneNumber, String templateId, Map<String, String> variables) {
        Message message = new Message();
        message.setFrom(outgoingPhoneNumber);
        message.setTo(customerPhoneNumber);
        message.setKakaoOptions(createKakaoOption(templateId, variables));
        return message;
    }

    private KakaoOption createKakaoOption(String templateId, Map<String, String> variables) {
        KakaoOption kakaoOption = new KakaoOption();
        kakaoOption.setPfId(pfId);
        kakaoOption.setTemplateId(templateId);
        kakaoOption.setDisableSms(DISABLE);
        kakaoOption.setVariables(variables);
        return kakaoOption;
    }
}
