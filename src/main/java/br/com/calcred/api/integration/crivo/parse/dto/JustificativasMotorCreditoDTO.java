package br.com.calcred.api.integration.crivo.parse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "justificativas")
public class JustificativasMotorCreditoDTO {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "justificativaLimite")
    public List<DadosRespostaMotorCreditoDTO> justificativasLimite = new ArrayList<>();

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "justificativaResposta")
    public List<DadosRespostaMotorCreditoDTO> justificativasResposta = new ArrayList<>();

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "justificativaPontos")
    public List<DadosRespostaMotorCreditoDTO> justificativasPonto = new ArrayList<>();

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "justificativaTextual")
    public List<DadosRespostaMotorCreditoDTO> justificativasTextual = new ArrayList<>();

    public List<DadosRespostaMotorCreditoDTO> getJustificativasLimite() {
        return justificativasLimite;
    }

    public void setJustificativasLimite(DadosRespostaMotorCreditoDTO justificativasLimite) {
        this.justificativasLimite.add(justificativasLimite);
    }

    public List<DadosRespostaMotorCreditoDTO> getJustificativasResposta() {
        return justificativasResposta;
    }

    public void setJustificativasResposta(DadosRespostaMotorCreditoDTO justificativasResposta) {
        this.justificativasResposta.add(justificativasResposta);
    }

    public List<DadosRespostaMotorCreditoDTO> getJustificativasPonto() {
        return justificativasPonto;
    }

    public void setJustificativasPonto(DadosRespostaMotorCreditoDTO justificativasPonto) {
        this.justificativasPonto.add(justificativasPonto);
    }

    public List<DadosRespostaMotorCreditoDTO> getJustificativasTextual() {
        return justificativasTextual;
    }

    public void setJustificativasTextual(DadosRespostaMotorCreditoDTO justificativasTextual) {
        this.justificativasTextual.add(justificativasTextual);
    }
}
