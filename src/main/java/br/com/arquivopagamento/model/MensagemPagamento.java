package br.com.arquivopagamento.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MensagemPagamento {

    private String documentoAssociado;
    private String idBoleto;
    private String valorBoleto;

}
