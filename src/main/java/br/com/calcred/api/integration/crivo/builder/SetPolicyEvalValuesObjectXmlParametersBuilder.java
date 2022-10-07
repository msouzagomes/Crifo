package br.com.calcred.api.integration.crivo.builder;

import br.com.calcred.api.integration.crivo.dto.ParametersDTO;
import br.com.calcred.api.integration.soap.crivo.ObjectFactory;
import br.com.calcred.api.integration.soap.crivo.SetPolicyEvalValuesObjectXml;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SetPolicyEvalValuesObjectXmlParametersBuilder {

    @Value("${api.path.crivo.user}")
    private String user;
    @Value("${api.path.crivo.password}")
    private String password;

    public SetPolicyEvalValuesObjectXml buildParamsSetPolicyEvalValuesObjectXml(ParametersDTO parametersDTO) {
        SetPolicyEvalValuesObjectXml setPolicyRequest = new ObjectFactory().createSetPolicyEvalValuesObjectXml();
        setPolicyRequest.setSUser(user);
        setPolicyRequest.setSPassword(password);
        setPolicyRequest.setSPolitica(parametersDTO.getPolicy().getValue());
        setPolicyRequest.setSParametros(parametersDTO.parseToString());

        return setPolicyRequest;
    }
}
