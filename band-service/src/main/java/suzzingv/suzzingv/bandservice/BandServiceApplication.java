package suzzingv.suzzingv.bandservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"suzzingv.suzzingv.commonmodule", "suzzingv.suzzingv.bandservice", "suzzingv.suzzingv.grpc"})
public class BandServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BandServiceApplication.class, args);
    }

}
