package br.com.calcred.api.integration.crivo.dto;


import br.com.calcred.api.integration.crivo.enumeration.CrivoPolicyEnum;
import br.com.calcred.api.integration.crivo.enumeration.CrivoStatusResultEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@With
@JsonDeserialize(builder = CrivoConsultaLimiteResponseDTO.JacksonBuilder.class)
@Builder(builderClassName = "JacksonBuilder")
public class CrivoConsultaLimiteResponseDTO implements Serializable {

    String cpf;
    String codigoOperacao;
    String mensagem;
    CrivoPolicyEnum politica;
    CrivoStatusResultEnum status;
    BigDecimal limiteSugerido;
    BigDecimal rendaPresumida;
    Integer scoreCredito;
    LocalDateTime dataConsultaLimite;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JacksonBuilder {}

}
