package suzzingv.suzzingv.authservice.user.presentation.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import suzzingv.suzzingv.authservice.user.application.service.UserService;
import suzzingv.suzzingv.authservice.user.exception.UserException;
import suzzingv.suzzingv.commonmodule.response.grpc.GrpcStatusMapper;
import suzzingv.suzzingv.grpc.UserProto;
import suzzingv.suzzingv.grpc.UserServiceGrpc;

@RequiredArgsConstructor
@GrpcService
@Slf4j
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;
    @Override
    public void isExist(UserProto.UserFindRequest request, StreamObserver<UserProto.UserExistResponse> responseObserver) {
        try {
            userService.findUserById(request.getId());
            UserProto.UserExistResponse response = UserProto.UserExistResponse.newBuilder()
                    .setIsExist(true)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserException e) {
            responseObserver.onError(GrpcStatusMapper.toGrpcError(e));
        }
    }
}
