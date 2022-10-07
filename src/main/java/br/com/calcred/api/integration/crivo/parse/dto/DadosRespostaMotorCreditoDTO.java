package br.com.calcred.api.integration.crivo.parse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosRespostaMotorCreditoDTO {

	private static final String CODIGO_ERRO = "Erro";

	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "regra")
	private List<RegraMotorCreditoDTO> regras;

	@JacksonXmlProperty(localName = "criterio", isAttribute = true)
	private String criterio;

	@JacksonXmlProperty(localName = "total", isAttribute = true)
	private String total;

	/**
	 * Verifica se a consulta realizada para o criterio informado possui
	 * situacao de erro no Motor de Credito (Crivo).
	 *
	 * Considera sempre o primeiro elemento '&lt;regra&gt;&lt;/regra&gt;' de
	 * cada nodo.
	 *
	 * Tal estrutura e considerada para realizar o parser da situacao de erro:
	 *
	 * <br>
	 * <br>
	 * |&lt;regra&gt; <br>
	 * | &nbsp;&nbsp;&lt;motivo&gt; <br>
	 * | &nbsp;&nbsp;&nbsp;&nbsp;&lt;texto&gt;Erro&lt;/texto&gt; <br>
	 * | &nbsp;&nbsp;&lt;/motivo&gt; <br>
	 * |&lt;/regra&gt;
	 *
	 * @return true: Erro na politica
	 */
	public boolean isErro() {

		if (regras.isEmpty() || regras.get(0) == null || regras.get(0).getMotivo() == null) {
			return false;
		}

		String codigo = regras.get(0).getMotivo().getTexto();

		return CODIGO_ERRO.equalsIgnoreCase(codigo);

	}

	public String getDescricaoTexto() {

		if (regras.isEmpty() || regras.get(0) == null || regras.get(0).getValor() == null) {
			return null;
		}

		return regras.get(0).getValor().getTexto();

	}

	public List<RegraMotorCreditoDTO> getRegras() {
		return regras;
	}

	public void setRegras(List<RegraMotorCreditoDTO> regras) {
		this.regras = regras;
	}

	public String getCriterio() {
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
