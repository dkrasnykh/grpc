package impl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc.Example;
import org.example.grpc.GreetServiceGrpc;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GrpcClientBiDirectional {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
        StreamObserver<Example.GreetingRequest> requestObserver = asyncClient.greetEveryone(new StreamObserver<Example.GreetingResponse>() {
            @Override
            public void onNext(Example.GreetingResponse greetEveryoneResponce) {
                String result = greetEveryoneResponce.getResult();
                System.out.println(result);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onCompleted() {
            }
        });

        Arrays.asList("Ivan", "Petya", "Lev").forEach(name -> {
            requestObserver.onNext(Example.GreetingRequest.newBuilder()
                    .setGreeting(Example.Greeting.newBuilder().setFirstName(name).build()).build());
        });
        requestObserver.onCompleted();
        finishLatch.await(1, TimeUnit.SECONDS);
    }
}
