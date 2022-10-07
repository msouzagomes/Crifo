package br.com.calcred.api.integration.config;

import br.com.calcred.api.exception.BusinessErrorException;
import br.com.calcred.api.helper.MessageHelper;
import br.com.calcred.api.integration.exception.IntegrationErrorException;
import br.com.calcred.api.integration.helper.ThreadLocalHelper;
import feign.Request;
import feign.RequestInterceptor;
import feign.Response;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static br.com.calcred.api.integration.crivo.enumeration.ErrorCode.ERRO_INTERNO;
import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static javax.xml.soap.SOAPConstants.SOAP_1_1_PROTOCOL;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FeignIntegrationSoapConfig {

    @Value("${api.path.crivo.namespace}")
    private String namespaceCrivo;

    private final MessageHelper messageHelper;

    @Bean
    public Encoder getSoapEncoder(){
        JAXBContextFactory jaxbContextFactory =
                new JAXBContextFactory.Builder().withMarshallerJAXBEncoding("UTF-8").build();
        return new SOAPEncoder.Builder()
                .withJAXBContextFactory(jaxbContextFactory)
                .withCharsetEncoding(UTF_8)
                .build();
    }

    @Bean
    public Decoder getSoapDecoder(){
        return new SOAPDecoder.Builder()
                .withJAXBContextFactory(new JAXBContextFactory.Builder().build())
                .useFirstChild()
                .build();
    }

    @Bean
    RequestInterceptor remoteFeignInterceptor() {
        return requestTemplate -> requestTemplate
            .headers(Map.of(
                "SOAPAction", Collections.singletonList(namespaceCrivo + "/" + ThreadLocalHelper.threadLocalSoapAction.get()),
                "Content-Type", Collections.singletonList("text/xml;charset=utf-8")
            ));
    }

    @Bean
    ErrorDecoder errorDecoder() {
        return (s, response) -> {
            try {
                Optional<HttpStatus> responseStatus = Optional.of(HttpStatus.valueOf(response.status()));
                logHttpError(response, responseStatus);
                SOAPMessage message = MessageFactory.newInstance(SOAP_1_1_PROTOCOL).createMessage((MimeHeaders) null,
                        response.body().asInputStream());
                return new BusinessErrorException(ERRO_INTERNO, message.getSOAPBody().getFault().getTextContent());
            } catch (IOException | SOAPException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
            return new IntegrationErrorException(ERRO_INTERNO, messageHelper.get(ERRO_INTERNO));
        };
    }

    private static void logHttpError(final Response response, final Optional<HttpStatus> responseStatus) {
        responseStatus.ifPresent(status -> {
            Request request = response.request();
            String responseBody = Try.withResources(() -> response.body().asReader(UTF_8))
                    .of(IOUtils::toString)
                    .getOrElse(EMPTY);
            if (HttpStatus.BAD_REQUEST.equals(status)) {
                log.warn(buildLogFullMessage(),
                        request.httpMethod() + " " + request.url(),
                        new String(ofNullable(request.body()).orElse(EMPTY.getBytes()), UTF_8),
                        responseBody);
            } else if (status.is5xxServerError()) {
                log.error(buildLogFullMessage(),
                        request.httpMethod() + " " + request.url(),
                        new String(ofNullable(request.body()).orElse(EMPTY.getBytes()), UTF_8),
                        responseBody);
            }
        });
    }

    private static String buildLogFullMessage(){
        return lineSeparator() +
                "RequestUrl: {}" + lineSeparator() +
                "RequestBody: {}" + lineSeparator() +
                "Response: {}" + lineSeparator();
    }

    @Bean
    Retryer retryer(@Value("${feign.client.retryer.period:1000}") long period,
                    @Value("${feign.client.retryer.max-period:5000}") long maxPeriod,
                    @Value("${feign.client.retryer.max-attempts:3}") int maxAttempts) {
        return new CustomRetryer(period, maxPeriod, maxAttempts);
    }
}
