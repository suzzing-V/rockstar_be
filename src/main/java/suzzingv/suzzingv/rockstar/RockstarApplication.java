package suzzingv.suzzingv.rockstar;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class RockstarApplication {

    public static void main(String[] args) {
        SpringApplication.run(RockstarApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.out.println("✅ TimeZone set to Asia/Seoul");
    }
}
