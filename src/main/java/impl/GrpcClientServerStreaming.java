package impl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.Example;
import org.example.grpc.GreetServiceGrpc;

public class GrpcClientServerStreaming {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        Example.GreetingRequest greetManyTimeRequest = Example.GreetingRequest.newBuilder()
                .setGreeting(Example.Greeting.newBuilder().setFirstName("Ivan"))
                .build();

        greetClient.greetManyTimes(greetManyTimeRequest)
                .forEachRemaining(greetManyTimeResponse -> {
                    String result = greetManyTimeResponse.getResult();
                    System.out.println(result);
                });
    }
}
