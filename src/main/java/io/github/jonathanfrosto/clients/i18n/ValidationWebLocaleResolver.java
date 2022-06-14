package io.github.jonathanfrosto.clients.i18n;

import org.hibernate.validator.spi.messageinterpolation.LocaleResolver;
import org.hibernate.validator.spi.messageinterpolation.LocaleResolverContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
public class ValidationWebLocaleResolver implements LocaleResolver {

    private final org.springframework.web.servlet.LocaleResolver webLocaleResolver;
    private final HttpServletRequest request;

    public ValidationWebLocaleResolver(org.springframework.web.servlet.LocaleResolver webLocaleResolver,
                                       HttpServletRequest request) {
        this.webLocaleResolver = webLocaleResolver;
        this.request = request;
    }

    @Override
    public Locale resolve(LocaleResolverContext localeResolverContext) {
        return webLocaleResolver.resolveLocale(request);
    }
}
