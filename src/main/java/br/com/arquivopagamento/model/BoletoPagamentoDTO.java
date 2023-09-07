package br.com.arquivopagamento.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoletoPagamentoDTO {

    @JsonProperty("documentoAssociado")
    private String documentoAssociado;

    @JsonProperty("idBoleto")
    private String idBoleto;

    @JsonProperty("valorBoleto")
    private String valorBoleto;


}
