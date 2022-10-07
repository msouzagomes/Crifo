package br.com.calcred.api.service;

import br.com.calcred.api.exception.BusinessErrorException;
import br.com.calcred.api.helper.MessageHelper;
import br.com.calcred.api.integration.crivo.CrivoClient;
import br.com.calcred.api.integration.crivo.builder.CrivoParametersBuilder;
import br.com.calcred.api.integration.crivo.builder.ExplainBuilder;
import br.com.calcred.api.integration.crivo.builder.SetPolicyEvalValuesObjectXmlParametersBuilder;
import br.com.calcred.api.integration.crivo.dto.CrivoConsultaLimiteRequestDTO;
import br.com.calcred.api.integration.crivo.dto.CrivoConsultaLimiteResponseDTO;
import br.com.calcred.api.integration.crivo.enumeration.CrivoStatusResultEnum;
import br.com.calcred.api.integration.crivo.parse.CrivoParse;
import br.com.calcred.api.integration.exception.IntegrationErrorException;
import br.com.calcred.api.integration.helper.ThreadLocalHelper;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static br.com.calcred.api.integration.crivo.enumeration.ErrorCode.*;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrivoService {

    private final MessageHelper messageHelper;
    private final CrivoClient crivoClient;
    private final SetPolicyEvalValuesObjectXmlParametersBuilder setPolicyEvalValuesObjectXmlParametersBuilder;
    private final ExplainBuilder explainBuilder;
    private final CrivoParse crivoParse;

    @CircuitBreaker(name = "CrivoClient", fallbackMethod = "fallbackCircuitBreacker")
    public CrivoConsultaLimiteResponseDTO crivoConsultaLimite(final CrivoConsultaLimiteRequestDTO crivoConsultaLimiteRequestDTO) {
        try {

            var parametersDTO = CrivoParametersBuilder.buildParamsPersonalLoan(crivoConsultaLimiteRequestDTO);
            var setPolicyEval = setPolicyEvalValuesObjectXmlParametersBuilder.buildParamsSetPolicyEvalValuesObjectXml(parametersDTO);

            //Alterando dinamicamente o SoapAction via ThreadLocal
            ThreadLocalHelper.threadLocalSoapAction.set("SetPolicyEvalValuesObjectXml");

            var setPEVOXmlResponse = crivoClient.setPolicyEvalValuesObjectXml(setPolicyEval);
            var policyResult = ofNullable(crivoParse.parse(setPEVOXmlResponse.getResultadoXml(), parametersDTO.getPolicy()))
                                .orElseThrow(() -> new BusinessErrorException(ERRO_INTERNO,
                                        messageHelper.get(ERRO_INTERNO)));

            if (CrivoStatusResultEnum.APROVADA.equals(policyResult.getStatus())) {

                ThreadLocalHelper.threadLocalSoapAction.set("Explain");
                var explainResponse = crivoClient.explain(explainBuilder.buildParamsExplain(policyResult.getOperationCode()));
                var crivoExplainResultDTO =  of(CrivoParse.parse(explainResponse.getExplainResult(),
                                                             policyResult.getOperationCode(), parametersDTO.getPolicy()))
                                              .orElseThrow(() -> new BusinessErrorException(ERRO_INTERNO,
                                                    messageHelper.get(ERRO_INTERNO)));

                return CrivoConsultaLimiteResponseDTO.builder()
                        .codigoOperacao(crivoExplainResultDTO.getCodigoOperacao())
                        .cpf(crivoConsultaLimiteRequestDTO.getCpf())
                        .limiteSugerido(crivoExplainResultDTO.getLimiteSugerido())
                        .mensagem(policyResult.getMessage())
                        .rendaPresumida(crivoExplainResultDTO.getRendaPresumida())
                        .scoreCredito(crivoExplainResultDTO.getScoreCredito())
                        .status(policyResult.getStatus())
                        .politica(crivoConsultaLimiteRequestDTO.getPolitica())
                        .dataConsultaLimite(LocalDateTime.now())
                        .build();
            }

            else {
                throw new BusinessErrorException(ERRO_CONSULTA_LIMITE_NEGADO,
                        messageHelper.get(ERRO_CONSULTA_LIMITE_NEGADO));
            }
        } catch (BusinessErrorException e) {
            throw new BusinessErrorException(ERRO_INTERNO, e.getReason());
        }
    }

    public CrivoConsultaLimiteResponseDTO fallbackRetry(final Exception cause)
            throws ResponseStatusException {
        log.debug("> fallbackRetry : do Resilience4j");
        if (cause instanceof IntegrationErrorException) {
            log.debug(cause.getMessage());
            log.debug(String.valueOf(cause.getCause()));
            log.debug(String.valueOf(((IntegrationErrorException) cause).getStatus()));
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, messageHelper.get(ERRO_SERVICO_CRIVO_INDISPONIVEL));
    }

    public CrivoConsultaLimiteResponseDTO fallbackCircuitBreacker(final CallNotPermittedException cause)
            throws ResponseStatusException {
        log.debug("> fallbackCircuitBreacker : do Resilience4j");
        if (cause instanceof RuntimeException) {
            log.debug(cause.getMessage());
            log.debug(String.valueOf(cause.getCause()));
        }

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, messageHelper.get(ERRO_SERVICO_CRIVO_INDISPONIVEL));
    }
}
