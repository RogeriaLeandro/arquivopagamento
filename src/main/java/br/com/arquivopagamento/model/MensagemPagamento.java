package br.com.arquivopagamento.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MensagemPagamento {

    private String documentoAssociado;
    private String idBoleto;
    private String valorBoleto;

}
