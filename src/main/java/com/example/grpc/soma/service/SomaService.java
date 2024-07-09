package com.example.grpc.soma.service;


import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

@Service
public class SomaService extends CalculadoraServiceGrpc.CalculadoraServiceImplBase {


    private static final Logger logger = Logger.getLogger(SomaService.class.getName());

    public int somar(int a, int b) {
        return a + b;
    }


    public void somar(SomaRequest request,
                      io.grpc.stub.StreamObserver<SomaResponse> responseObserver) {



        var valor = request.getFirstNumber() + request.getSecondNumber();
        var resposta = SomaResponse.newBuilder()
                .setResultadoSoma(valor)
                .build();
        responseObserver.onNext(resposta);
        responseObserver.onCompleted();
    }

    private Server server;

   public void start() throws IOException {
        /* The port on which the server should run */
        int port = 8082;

        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(new SomaService())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    SomaService.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination();
        }
    }

}
