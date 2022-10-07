package br.com.calcred.api.integration.crivo.dto;

import br.com.calcred.api.integration.crivo.enumeration.CrivoPolicyEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;

@Value
@With
@JsonDeserialize(builder = CrivoConsultaLimiteRequestDTO.JacksonBuilder.class)
@Builder(builderClassName = "JacksonBuilder")
@AllArgsConstructor
public class CrivoConsultaLimiteRequestDTO implements Serializable {

    @NotBlank(message = "CPF informado é inválido.")
    String cpf;

    @NotBlank(message = "Canal deve ser informado.")
    String canal;

    @NotBlank(message = "Id da loja informado é inválido.")
    String idLoja;

    @NotNull(message = "Política do Empréstimo deve ser informada.")
    CrivoPolicyEnum politica;

    @NotNull(message = "Parâmetros do Crivo deve ser informado.")
    HashMap<String, String> parametrosCrivo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class JacksonBuilder {}

}
