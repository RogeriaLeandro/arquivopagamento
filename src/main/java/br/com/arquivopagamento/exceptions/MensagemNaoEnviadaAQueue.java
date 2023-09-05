package br.com.arquivopagamento.exceptions;

import java.io.IOException;

public class MensagemNaoEnviadaAQueue extends IOException {

    private static final long serialVersionUID = 1L;

    public MensagemNaoEnviadaAQueue(String mensagem) {
        super(mensagem);
    }

}
