package suzzingv.suzzingv.rockstar.domain.user.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationCodeGenerator {

    public static String getCode() {
        Random random = new Random();
        int number = random.nextInt(1000000);
        return String.format("%06d", number);
    }
}
