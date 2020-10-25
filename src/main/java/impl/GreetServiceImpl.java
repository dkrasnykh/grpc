package impl;

import io.grpc.stub.StreamObserver;
import org.example.grpc.Example.Greeting;
import org.example.grpc.Example.GreetingRequest;
import org.example.grpc.Example.GreetingResponse;
import org.example.grpc.GreetServiceGrpc.GreetServiceImplBase;

public class GreetServiceImpl extends GreetServiceImplBase {
    @Override
    public void greet(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        Greeting greeting = request.getGreeting();
        GreetingResponse response = GreetingResponse.newBuilder().setResult("Hello, " + greeting.getFirstName() + "!").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        Greeting greeting = request.getGreeting();
        for (int i = 0; i < 10; i++) {
            GreetingResponse response = GreetingResponse.newBuilder()
                    .setResult(String.format("Hello, %s! (%d)", greeting.getFirstName(), i))
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GreetingRequest> longGreet(StreamObserver<GreetingResponse> responseObserver) {
        return new StreamObserver<GreetingRequest>() {
            String result = "";

            @Override
            public void onNext(GreetingRequest longGreetRequest) {
                result += "Hello " + longGreetRequest.getGreeting().getFirstName() + "!";
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(GreetingResponse.newBuilder().setResult(result).build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<GreetingRequest> greetEveryone(StreamObserver<GreetingResponse> responseObserver) {
        return new StreamObserver<GreetingRequest>() {
            @Override
            public void onNext(GreetingRequest greetEveryoneRequest) {
                String result = "Hello " + greetEveryoneRequest.getGreeting().getFirstName();
                GreetingResponse greetEveryoneResponce = GreetingResponse.newBuilder().setResult(result).build();
                responseObserver.onNext(greetEveryoneResponce);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
