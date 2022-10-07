package br.com.calcred.api.integration.crivo.builder;

import br.com.calcred.api.integration.crivo.dto.CrivoConsultaLimiteRequestDTO;
import br.com.calcred.api.integration.crivo.dto.ParametersDTO;
import br.com.calcred.api.integration.helper.CrivoParams;
import org.springframework.stereotype.Component;

@Component
public class CrivoParametersBuilder {

    public static ParametersDTO buildParamsPersonalLoan(final CrivoConsultaLimiteRequestDTO crivoConsultaLimiteRequestDTO) {
        ParametersDTO parametersDTO = new ParametersDTO();
        parametersDTO.setPolicy(crivoConsultaLimiteRequestDTO.getPolitica());
        parametersDTO.addParameter(CrivoParams.PersonalLoan.CPF.getValue(), crivoConsultaLimiteRequestDTO.getCpf());
        parametersDTO.addParameter(CrivoParams.PersonalLoan.CODIGO_DA_LOJA_IN.getValue(), crivoConsultaLimiteRequestDTO.getIdLoja());
        parametersDTO.addParameter(CrivoParams.PersonalLoan.CANAL.getValue(), crivoConsultaLimiteRequestDTO.getCanal());

        crivoConsultaLimiteRequestDTO.getParametrosCrivo()
                .forEach(parametersDTO::addParameter);

        return parametersDTO;
    }
}

