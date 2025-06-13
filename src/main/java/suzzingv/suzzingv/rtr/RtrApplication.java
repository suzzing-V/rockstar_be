package suzzingv.suzzingv.rtr;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class RtrApplication {

    public static void main(String[] args) {
        SpringApplication.run(RtrApplication.class, args);
    }
}
