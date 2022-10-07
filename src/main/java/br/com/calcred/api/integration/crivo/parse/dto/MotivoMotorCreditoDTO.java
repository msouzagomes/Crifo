package br.com.calcred.api.integration.crivo.parse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "motivo")
public class MotivoMotorCreditoDTO {

    @JacksonXmlProperty(localName = "texto", isAttribute = false)
    private String texto;

    @JacksonXmlProperty(localName = "formula", isAttribute = false)
    private String formula;

    @JacksonXmlProperty(localName = "valor", isAttribute = false)
    private String valor;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
