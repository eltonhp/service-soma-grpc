package com.example.grpc.soma;

import com.example.grpc.soma.service.CalculadoraServiceGrpc;
import com.example.grpc.soma.service.SomaRequest;
import com.example.grpc.soma.service.SomaResponse;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SomaApplicationTests {

	@Test
	void contextLoads() {

	}

	@Test
	void testarSoma() throws InterruptedException {
		String target = "localhost:8082";
		ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
				.build();


       double resultado = 0.0;

		try {
			CalculadoraServiceGrpc.CalculadoraServiceBlockingStub stub = CalculadoraServiceGrpc.newBlockingStub(channel);
			var requisicao = SomaRequest.newBuilder()
					.setFirstNumber(Double.valueOf("3"))
					.setSecondNumber(Double.valueOf("7"))
					.build();
			SomaResponse somaResponse = stub.somar(requisicao);
			System.out.println("Valor da soma: "+ somaResponse.getResultadoSoma());
			resultado = somaResponse.getResultadoSoma();
		} finally {
			// ManagedChannels use resources like threads and TCP connections. To prevent leaking these
			// resources the channel should be shut down when it will no longer be used. If it may be used
			// again leave it running.
			channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
		}

		assertEquals(10, resultado);

	}

}
