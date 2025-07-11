package suzzingv.suzzingv.rockstar.global.sms;

import lombok.experimental.UtilityClass;

@UtilityClass
// 자동으로 모든 필드와 메서드 static으로
// private 생성자 자동 추가
public class MessageUtils {

    public static String generateVerificationText(String verificationCode) {
        return "[RuleTheRock] 인증번호는 " + verificationCode + "입니다.";
    }
}
