package br.com.calcred.api.integration.crivo.parse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "valor")
public class ValorMotorCreditoDTO {

    @JacksonXmlProperty(localName = "texto", isAttribute = false)
    private String texto;

    @JacksonXmlProperty(localName = "numero", isAttribute = false)
    private String numero;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

}
