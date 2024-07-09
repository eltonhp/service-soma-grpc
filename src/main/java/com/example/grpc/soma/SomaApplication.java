package com.example.grpc.soma;

import com.example.grpc.soma.service.SomaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class SomaApplication {

	@Autowired
	SomaService somaService;
	public static void main(String[] args) throws IOException {

		ApplicationContext contexto = SpringApplication.run(SomaApplication.class, args);

		SomaService somaService = (SomaService) contexto.getBean("somaService");
		somaService.start();

	}

}
