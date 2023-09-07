package br.com.arquivopagamento.service;

import br.com.arquivopagamento.exceptions.DocumentoInvalidoException;
import br.com.arquivopagamento.exceptions.MensagemNaoEnviadaAQueue;
import br.com.arquivopagamento.model.MensagemPagamento;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;
import java.math.BigDecimal;
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

                this.trataDocumento(documentoAssociado);
                this.trataIdBoleto(idBoleto);
                this.trataValor(valorBoleto);

                mensagem.setDocumentoAssociado(documentoAssociado);
                mensagem.setIdBoleto(idBoleto);
                mensagem.setValorBoleto(valorBoleto);

                String json =  new Gson().toJson(mensagem.toString());
//                rabbitTemplate.convertAndSend("direct-exchange-default", "queue-a-key", json);
                rabbitTemplate.convertAndSend("queue", json);

             });
        } catch (IOException e) {
            throw new MensagemNaoEnviadaAQueue("Mensagem não enviada(Documento/IdBoleto/ValorBoleto): " + mensagem.getDocumentoAssociado() + " / "
                                                                                + mensagem.getIdBoleto() + " / "
                                                                                + mensagem.getValorBoleto());
        }
    }

    public String trataValor(String valor) {

        BigDecimal valorTratado = new BigDecimal(valor);
        String valorString = valorTratado.toString();
        StringBuffer stringBuffer = new StringBuffer(valorString);

        stringBuffer.insert(valorString.length() - 2, ".");
        return stringBuffer.toString();

    }

    public String trataIdBoleto(String id) {

        return id.trim();
    }

    public String trataDocumento(String documentoPagador) throws DocumentoInvalidoException {
        String cpf = documentoPagador.substring(3, 11);
        if(this.documentoEValido(cpf)){
            return cpf;
        }

        if(this.documentoEValido(documentoPagador)){
            return documentoPagador;
        }
        throw new DocumentoInvalidoException("Documento inválido");
    }


    public boolean documentoEValido(String documento) {

        documento = documento.replace("-", "");
        documento = documento.replace(".", "");

        if (documento.length() == 11) {
            CPFValidator cpfValidator = new CPFValidator();
            try {
                cpfValidator.assertValid(documento);
                return true;
            } catch (Exception e) {
                logger.error("CPF Inválido");
            }
        } else {
            CNPJValidator cnpjValidator = new CNPJValidator();
            try {
                cnpjValidator.assertValid(documento);
                return true;
            } catch (Exception e) {
                logger.error("CNPJ Inválido");
            }
        }

        return false;
    }





}
