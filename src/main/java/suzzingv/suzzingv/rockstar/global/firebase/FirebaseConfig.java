package suzzingv.suzzingv.rockstar.global.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount =
                        new FileInputStream("src/main/resources/firebase/rockstar-f9295-firebase-adminsdk-fbsvc-505233da04.json");

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            throw new IllegalStateException("🔥 Firebase 초기화 실패", e);
        }
    }
}
