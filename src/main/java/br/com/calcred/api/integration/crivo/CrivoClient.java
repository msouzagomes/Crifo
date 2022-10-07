package br.com.calcred.api.integration.crivo;


import br.com.calcred.api.integration.config.FeignIntegrationSoapConfig;
import br.com.calcred.api.integration.soap.crivo.Explain;
import br.com.calcred.api.integration.soap.crivo.ExplainResponse;
import br.com.calcred.api.integration.soap.crivo.SetPolicyEvalValuesObjectXml;
import br.com.calcred.api.integration.soap.crivo.SetPolicyEvalValuesObjectXmlResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "CrivoClient",
            url =  "${api.path.crivo.urlService}",
            configuration = FeignIntegrationSoapConfig.class
        )
public interface CrivoClient {

    @PostMapping
    SetPolicyEvalValuesObjectXmlResponse setPolicyEvalValuesObjectXml(SetPolicyEvalValuesObjectXml setPolicyEvalValuesObjectXml);

    @PostMapping
    ExplainResponse explain(Explain explain);

}
