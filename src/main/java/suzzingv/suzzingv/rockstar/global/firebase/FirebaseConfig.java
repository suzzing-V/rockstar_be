package suzzingv.suzzingv.rockstar.global.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
//        String encoded = System.getenv("FIREBASE_CREDENTIALS_BASE64");
//        System.out.println("✅ FIREBASE_CREDENTIALS_BASE64: " + (encoded != null ? "존재함" : "없음"));
//
//        if (encoded == null) {
//            throw new IllegalStateException("❌ FIREBASE_CREDENTIALS_BASE64 환경변수가 설정되지 않았습니다.");
//        }
//
//        try {
//            byte[] decodedBytes = Base64.getDecoder().decode(encoded);
//            InputStream serviceAccount = new ByteArrayInputStream(decodedBytes);
//
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//        } catch (Exception e) {
//            throw new IllegalStateException("🔥 Firebase 초기화 실패", e);
//        }
    }
}
