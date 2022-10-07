package br.com.calcred.api.helper;

import br.com.calcred.api.integration.crivo.enumeration.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class MessageHelper {

    private final MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    public void init() {
        accessor = new MessageSourceAccessor( messageSource, Locale.getDefault() );
    }

    public String get(String code, Object ... args) {
        return accessor.getMessage(code, args);
    }

    public String get(ErrorCode code, Object ... args) {
        return accessor.getMessage(code.getMessageKey(), args);
    }
}
