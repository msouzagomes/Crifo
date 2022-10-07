package br.com.calcred.api.integration.crivo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CrivoExplainResultDTO {

    private String codigoOperacao;
    private BigDecimal limiteSugerido;
    private BigDecimal rendaPresumida;
    private String nivelRiscoDescricao;
    private Integer scoreCredito;

}
