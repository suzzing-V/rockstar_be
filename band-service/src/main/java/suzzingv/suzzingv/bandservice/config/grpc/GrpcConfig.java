package suzzingv.suzzingv.bandservice.config.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Value("${grpc.user.name}")
    private String userServerName;

    @Value("${grpc.user.port}")
    private int userServerPort;

    @Bean
    public ManagedChannel userGrpcChannel() {
        return ManagedChannelBuilder
                .forAddress(userServerName, userServerPort)
                .usePlaintext()
                .build();
    }
}
