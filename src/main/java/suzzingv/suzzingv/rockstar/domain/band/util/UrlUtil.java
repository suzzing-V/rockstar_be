package suzzingv.suzzingv.rockstar.domain.band.util;

import java.security.SecureRandom;
import java.util.Base64;

public class UrlUtil {

    private static final String BASE_URL = "https://rtr.com";

    public static String generateInvitationUrl() {
        String raw = System.currentTimeMillis() + ":" + new SecureRandom().nextInt();
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(raw.getBytes());

        return BASE_URL + "/" + token;
    }
}
