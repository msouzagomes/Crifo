package br.com.calcred.api.integration.crivo.dto;


import br.com.calcred.api.integration.crivo.enumeration.CrivoStatusResultEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CrivoSetPolicyResultDTO {

    private String operationCode;
    private CrivoStatusResultEnum status;
    private String policy;
    private String message;

}
