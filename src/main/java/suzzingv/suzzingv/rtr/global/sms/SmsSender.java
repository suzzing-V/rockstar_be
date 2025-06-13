package suzzingv.suzzingv.rtr.global.sms;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsSender {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecret;
    @Value("${coolsms.from-number}")
    private String fromNumber;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret,
                "https://api.coolsms.co.kr");
    }

    // 단일 메시지 발송 예제
    public void sendMessage(String to, String text) {
        Message coolsms = new Message();
        coolsms.setFrom(fromNumber);
        coolsms.setTo(to);
        coolsms.setText(text);
        messageService.sendOne(new SingleMessageSendingRequest(coolsms));
    }
}
