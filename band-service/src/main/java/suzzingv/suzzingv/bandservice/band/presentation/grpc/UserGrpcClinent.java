package suzzingv.suzzingv.bandservice.band.presentation.grpc;

import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import suzzingv.suzzingv.grpc.UserProto;
import suzzingv.suzzingv.grpc.UserServiceGrpc;

@Component
@RequiredArgsConstructor
public class UserGrpcClinent {

    private final ManagedChannel channel;

    public UserProto.UserExistResponse isUserExist(Long userId) {
        UserServiceGrpc.UserServiceBlockingStub stub =
                UserServiceGrpc.newBlockingStub(channel);

        UserProto.UserFindRequest request = UserProto.UserFindRequest.newBuilder().setId(userId).build();
        return stub.isExist(request);
    }
}
