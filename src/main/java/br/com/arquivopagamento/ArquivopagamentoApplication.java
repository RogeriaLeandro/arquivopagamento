package br.com.arquivopagamento;

import br.com.arquivopagamento.service.ArquivoService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class ArquivopagamentoApplication implements CommandLineRunner {

	@Autowired
	private ArquivoService arquivoService;

	public static void main(String[] args) {
		SpringApplication.run(ArquivopagamentoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		arquivoService.run();
	}
}