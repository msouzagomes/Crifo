package br.com.calcred.api.integration.crivo.builder;

import br.com.calcred.api.integration.soap.crivo.Explain;
import br.com.calcred.api.integration.soap.crivo.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExplainBuilder {

    @Value("${api.path.crivo.user}")
    private String user;
    @Value("${api.path.crivo.password}")
    private String password;

    private ExplainBuilder() {}

    public Explain buildParamsExplain(String operationCode) {
        var explain = new ObjectFactory().createExplain();
        explain.setSOperacao(operationCode);
        explain.setSUser(user);
        explain.setSPassword(password);

        return explain;
    }

}
