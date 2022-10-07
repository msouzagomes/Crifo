package br.com.calcred.api.integration.crivo.parse;


import br.com.calcred.api.exception.BusinessErrorException;
import br.com.calcred.api.exception.InternalErrorException;
import br.com.calcred.api.helper.MessageHelper;
import br.com.calcred.api.integration.crivo.dto.CrivoExplainResultDTO;
import br.com.calcred.api.integration.crivo.dto.CrivoSetPolicyResultDTO;
import br.com.calcred.api.integration.crivo.enumeration.CrivoCriterionEnum;
import br.com.calcred.api.integration.crivo.enumeration.CrivoPolicyEnum;
import br.com.calcred.api.integration.crivo.enumeration.CrivoStatusResultEnum;
import br.com.calcred.api.integration.crivo.parse.dto.DadosRespostaMotorCreditoDTO;
import br.com.calcred.api.integration.crivo.parse.dto.JustificativasMotorCreditoDTO;
import br.com.calcred.api.integration.soap.crivo.ResultEvalValuesXml;
import br.com.calcred.api.integration.soap.crivo.ResultRespostasXml;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

import static br.com.calcred.api.integration.crivo.enumeration.CrivoPolicyEnum.EMPRESTIMO_PESSOAL;
import static br.com.calcred.api.integration.crivo.enumeration.ErrorCode.ERRO_PARSER_XML_JUSTIFICATIVAS;

@Component
@RequiredArgsConstructor
public class CrivoParse {

    private static final Locale PT_BR = new Locale("pt", "BR");

    private static final MessageHelper messageHelper = null;

    public CrivoSetPolicyResultDTO parse(ResultEvalValuesXml xml, CrivoPolicyEnum policyEnum){

        CrivoSetPolicyResultDTO setPolicyResult = new CrivoSetPolicyResultDTO();
        CrivoStatusResultEnum crivoStatusResult = null;
        String crivoMessageResult = "";
        if (!CollectionUtils.isEmpty(xml.getRespostas().getResposta())) {

            ResultRespostasXml r = CollectionUtils.find(xml.getRespostas().getResposta(),
                resultRespostasXml -> EMPRESTIMO_PESSOAL.getValue().equalsIgnoreCase(resultRespostasXml.getCriterio()));

            if(StringUtils.isNotEmpty(r.getSistema())) {
                crivoStatusResult = CrivoStatusResultEnum.doDeparaRespostaCrivo(r.getSistema());
            }
            crivoMessageResult = r.getResposta();
        }
        setPolicyResult.setOperationCode(xml.getCodigoOperacao());
        setPolicyResult.setStatus(crivoStatusResult);
        setPolicyResult.setPolicy(policyEnum.getValue());
        setPolicyResult.setMessage(crivoMessageResult);
        return setPolicyResult;
    }

    public static CrivoExplainResultDTO parse(String xml, String operationCode, CrivoPolicyEnum policy) throws InternalErrorException {

        JustificativasMotorCreditoDTO explainDTO = null;
        try {
            explainDTO = parseXML(xml);
        } catch (IOException ex) {
            throw new BusinessErrorException(ERRO_PARSER_XML_JUSTIFICATIVAS, messageHelper.get(ERRO_PARSER_XML_JUSTIFICATIVAS));
        }

        CrivoExplainResultDTO dadosRetornoCrivo = new CrivoExplainResultDTO();
        dadosRetornoCrivo.setCodigoOperacao(operationCode);
        Collection<DadosRespostaMotorCreditoDTO> dadosJustificativaLimite = explainDTO.getJustificativasLimite();

        BigDecimal limiteSugerido = getBigDecimalNullIfErro(dadosJustificativaLimite, CrivoCriterionEnum.LIMITE_SUGERIDO) != null ?
                                    getBigDecimalNullIfErro(dadosJustificativaLimite, CrivoCriterionEnum.LIMITE_SUGERIDO) :
                                    getBigDecimalProposta(dadosJustificativaLimite, policy.getValue());
        dadosRetornoCrivo.setLimiteSugerido(limiteSugerido);

        Collection<DadosRespostaMotorCreditoDTO> dadosResposta = explainDTO.getJustificativasResposta();
        dadosRetornoCrivo.setNivelRiscoDescricao(getDescricaoTexto(dadosResposta, CrivoCriterionEnum.POLITICA_EMPRETIMO_PESSOAL));

        Collection<DadosRespostaMotorCreditoDTO> dadosJustificativasPonto = explainDTO.getJustificativasPonto();
        dadosRetornoCrivo.setScoreCredito(getIntegerProposta(dadosJustificativasPonto, policy.getValue()));
        dadosRetornoCrivo.setRendaPresumida(getBigDecimalNullIfErro(dadosJustificativasPonto, CrivoCriterionEnum.RC_RENDA_PRESUMIDA));

        return dadosRetornoCrivo;
    }

    private static JustificativasMotorCreditoDTO parseXML(String xml) throws IOException {
        return new XmlMapper().readValue(xml, JustificativasMotorCreditoDTO.class);
    }

    private static Predicate<DadosRespostaMotorCreditoDTO> predicate(final CrivoCriterionEnum criterion) {
        return dadosRespostaMotorCreditoDTO -> criterion.getCode().equals(dadosRespostaMotorCreditoDTO.getCriterio());
    }

    /**
     * Retorna o valor do criterio buscado. Caso o Motor de Credito retornar o
     * campo com status de erro o valor retornado sera 'null'.
     *
     * @param dados
     *            Lista de dados resultados o parser
     * @param criterion
     *            Tipo de criterio a ser buscado
     * @return Valor do criterio
     */
    public static BigDecimal getBigDecimalNullIfErro(Collection<DadosRespostaMotorCreditoDTO> dados, CrivoCriterionEnum criterion) {
        DadosRespostaMotorCreditoDTO resposta = CollectionUtils.find(dados, predicate(criterion));
        if (Objects.isNull(resposta)) {
            return null;
        }
        if (resposta.isErro()) {
            return null;
        }
        return getBigDecimal(resposta);
    }

    public static String getDescricaoTexto(Collection<DadosRespostaMotorCreditoDTO> dados, CrivoCriterionEnum criterion) {

        DadosRespostaMotorCreditoDTO resposta = CollectionUtils.find(dados, predicate(criterion));

        if (Objects.isNull(resposta)){
            return null;
        }

        return resposta.getDescricaoTexto();

    }

    /**
     * Retorna o valor do criterion buscado. Caso o Motor de Credito retornar o
     * campo com status de erro o valor retornado sera 'null'.
     *
     * @param dados
     *            Lista de dados resultados o parser
     * @param criterion
     *            Tipo de criterion a ser buscado
     * @return Valor do criterion
     */
    public static Integer getIntegerNullIfErro(Collection<DadosRespostaMotorCreditoDTO> dados, CrivoCriterionEnum criterion) {
        DadosRespostaMotorCreditoDTO resposta = CollectionUtils.find(dados, predicate(criterion));
        if (Objects.isNull(resposta)) {
            return null;
        }
        if (resposta.isErro()) {
            return null;
        }
        return getInteger(resposta);
    }

    private static BigDecimal getBigDecimal(DadosRespostaMotorCreditoDTO resposta) {
        if (resposta != null && resposta.getTotal() != null) {
            NumberFormat nf = NumberFormat.getInstance(PT_BR);
            try (Scanner s = new Scanner(resposta.getTotal())) {
                s.useLocale(PT_BR);
                if (s.hasNextBigDecimal()) {
                    return new BigDecimal(nf.parse(resposta.getTotal()).toString());
                }
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    private static Integer getInteger(DadosRespostaMotorCreditoDTO resposta) {

        if (Objects.isNull(resposta) || Objects.isNull(resposta.getTotal())) {
            return null;
        }

        NumberFormat nf = NumberFormat.getInstance(PT_BR);
        try(Scanner s = new Scanner(resposta.getTotal())) {
            s.useLocale(PT_BR);
            if (s.hasNextInt()) {
                return Integer.parseInt(nf.parse(resposta.getTotal()).toString());
            } else if (s.hasNextBigDecimal()) {
                return new BigDecimal(nf.parse(resposta.getTotal()).toString()).intValue();
            } else {
                return null;
            }
        } catch (ParseException e) {
            return null;
        }
    }
    public static Integer getIntegerProposta(Collection<DadosRespostaMotorCreditoDTO> dados, String politica) {
        DadosRespostaMotorCreditoDTO resposta = CollectionUtils.find(dados, predicateProposta(politica));
        return getInteger(resposta);
    }

    public static BigDecimal getBigDecimalProposta(Collection<DadosRespostaMotorCreditoDTO> dados, String politica) {
        DadosRespostaMotorCreditoDTO resposta = CollectionUtils.find(dados, predicateProposta(politica));
        return getBigDecimal(resposta);
    }

    private static Predicate<DadosRespostaMotorCreditoDTO> predicateProposta(final String politica) {
        return dadosRespostaCredito -> politica.equals(dadosRespostaCredito.getCriterio());
    }
}
