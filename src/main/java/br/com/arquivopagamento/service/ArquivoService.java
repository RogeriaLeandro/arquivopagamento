package br.com.arquivopagamento.service;

import br.com.arquivopagamento.exceptions.MensagemNaoEnviadaAQueue;
import br.com.arquivopagamento.model.MensagemPagamento;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Slf4j
public class ArquivoService implements CommandLineRunner {

    @Autowired
    RabbitTemplate rabbitTemplate;

    private static Logger logger = LoggerFactory.getLogger(ArquivoService.class);

    @Override
    public void run(String... args) throws Exception {

        MensagemPagamento mensagem = new MensagemPagamento();

        try (Stream<String> stream = Files.lines(Path.of("boletos/arquivo.txt")).parallel()) {
            stream.forEach((String line) -> {

                String documentoAssociado =  line.substring(1, 14);
                String idBoleto = line.substring(15, 28);
                String valorBoleto = (line.substring(29,48));

                //settar em mensagem

                String json =  new Gson().toJson(mensagem.toString());
                rabbitTemplate.convertAndSend("direct-exchange-default", "queue-a-key", json);
             });
        } catch (IOException e) {
            throw new MensagemNaoEnviadaAQueue("Mensagem não enviada(Documento/IdBoleto/ValorBoleto): " + mensagem.getDocumentoAssociado() + " / "
                                                                                + mensagem.getIdBoleto() + " / "
                                                                                + mensagem.getValorBoleto());
        }
    }






}
