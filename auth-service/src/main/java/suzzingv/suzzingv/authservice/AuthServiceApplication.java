package suzzingv.suzzingv.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = {"suzzingv.suzzingv.authservice", "suzzingv.suzzingv.commonmodule"})
public class AuthServiceApplication {

    public static void main(String[] args) {
        // 변경 테스트 1
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
