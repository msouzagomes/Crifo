package br.com.calcred.api.integration.crivo.parse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "regra")
public class RegraMotorCreditoDTO {

    @JacksonXmlProperty(localName = "motivo", isAttribute = false)
    private MotivoMotorCreditoDTO motivo;

    @JacksonXmlProperty(localName = "valor", isAttribute = false)
    private ValorMotorCreditoDTO valor;

    public MotivoMotorCreditoDTO getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoMotorCreditoDTO motivo) {
        this.motivo = motivo;
    }

    public ValorMotorCreditoDTO getValor() {
        return valor;
    }

    public void setValor(ValorMotorCreditoDTO valor) {
        this.valor = valor;
    }

}
