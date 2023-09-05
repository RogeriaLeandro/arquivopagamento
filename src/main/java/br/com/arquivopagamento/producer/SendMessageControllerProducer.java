package br.com.arquivopagamento.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class SendMessageControllerProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping(path = "/")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        rabbitTemplate.convertAndSend("direct-exchange-default", "queue-a-key", message);
        return ResponseEntity.ok("Mensagem enviada para fila.");
    }

}
