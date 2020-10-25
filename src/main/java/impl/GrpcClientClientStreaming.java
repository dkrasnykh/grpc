package impl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc.Example.Greeting;
import org.example.grpc.Example.GreetingRequest;
import org.example.grpc.Example.GreetingResponse;
import org.example.grpc.GreetServiceGrpc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GrpcClientClientStreaming {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
        StreamObserver<GreetingRequest> requestObserver = asyncClient.withDeadlineAfter(5, TimeUnit.SECONDS).longGreet(new StreamObserver<GreetingResponse>() {
            @Override
            public void onNext(GreetingResponse longGreetResponse) {
                String result = longGreetResponse.getResult();
                System.out.println(result);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onCompleted() {
            }
        });
        GreetingRequest longGreetRequest = GreetingRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Lev")
                        .build())
                .build();
        requestObserver.onNext(longGreetRequest);
        requestObserver.onCompleted();
        finishLatch.await(1, TimeUnit.SECONDS);
    }
}
