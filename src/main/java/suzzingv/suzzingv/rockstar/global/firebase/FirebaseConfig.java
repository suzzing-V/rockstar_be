package suzzingv.suzzingv.rockstar.global.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                String base64Creds = System.getenv("FIREBASE_CREDENTIALS_BASE64");
                if (base64Creds == null) {
                    throw new IllegalStateException("❌ FIREBASE_CREDENTIALS_BASE64 환경변수가 설정되지 않았습니다.");
                }

                byte[] decodedBytes = Base64.getDecoder().decode(base64Creds);
                ByteArrayInputStream serviceAccount = new ByteArrayInputStream(decodedBytes);

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase 초기화 성공");
            }
        } catch (Exception e) {
            throw new IllegalStateException("🔥 Firebase 초기화 실패", e);
        }
    }
}
