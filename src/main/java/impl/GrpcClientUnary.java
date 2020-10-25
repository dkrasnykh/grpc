package impl;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.Example;
import org.example.grpc.GreetServiceGrpc;

public class GrpcClientUnary {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

        Example.Greeting greeting = Example.Greeting.newBuilder()
                .setFirstName("Ivan")
                .build();

        Example.GreetingRequest greetingRequest = Example.GreetingRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        Example.GreetingResponse greetingResponse = greetClient.greet(greetingRequest);
        String result = greetingResponse.getResult();

        System.out.println(result);
    }
}
