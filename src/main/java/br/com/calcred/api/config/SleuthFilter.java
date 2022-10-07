package br.com.calcred.api.config;


import brave.baggage.BaggageField;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;


@Slf4j
@Component
class SleuthFilter extends GenericFilterBean {

	private static final String X_CCRED_TRANSACTIONAL_ID = "X-CCRED-TRANSACTIONAL-ID";
	private static final String CCRED_TRANSACTIONAL_ID = "CCRED-TRANSACTIONAL-ID";

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

		final BaggageField baggageFieldTransactionalId = BaggageField.getByName(X_CCRED_TRANSACTIONAL_ID);
		if(baggageFieldTransactionalId == null || baggageFieldTransactionalId.getValue() == null){
			String transactionalId = httpRequest.getHeader(X_CCRED_TRANSACTIONAL_ID);
			if(transactionalId != null) {
				BaggageField baggageField1 = BaggageField.create(X_CCRED_TRANSACTIONAL_ID);
				baggageField1.updateValue(transactionalId);
				MDC.put(CCRED_TRANSACTIONAL_ID, transactionalId);
			} else {
				MDC.put(CCRED_TRANSACTIONAL_ID, "undefined");
			}
		} else {
			MDC.put(CCRED_TRANSACTIONAL_ID, baggageFieldTransactionalId.getValue());
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}
}
