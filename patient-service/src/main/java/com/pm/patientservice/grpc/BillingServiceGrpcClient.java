package com.pm.patientservice.grpc;


import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub; // variable to hold the grpc client; blockingStub is a nested class within BillingServiceGrpcClass provides synchronous client calls to grpc server in billing service; the execution will wait before receiving a response from the grpc server

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort) { // adding env variables that point to where our billing service is

        log.info("Connecting to billing service at {}:{}", serverAddress, serverPort); // help us debug errors

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext().build(); //it takes in serverAddress and serverPort; and it creates a managed channel; usePlainText() disables encryption

        blockingStub = BillingServiceGrpc.newBlockingStub(channel); // client created
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email){
        BillingRequest request = BillingRequest.newBuilder().
                setPatientId(patientId).setName(name).setEmail(email).build(); // will create a new billingRequest for us

        BillingResponse response = blockingStub.createBillingAccount(request); // blockingStub is a grpc client which waits for a response from server before moving on

        log.info("Received response from billing service via GRPC : {}", response);
        return response;
    }
}