package io.github.jonathanfrosto.clients.i18n;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.messageinterpolation.LocaleResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;

import java.util.Collections;
import java.util.Locale;

@Configuration
public class I18n {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("ISO-8859-1");
        messageSource.setDefaultLocale(Locale.getDefault());
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean(MessageSource messageSource, LocaleResolver resolver) {
        MessageSourceResourceBundleLocator bundle = new MessageSourceResourceBundleLocator(messageSource);
        ResourceBundleMessageInterpolator interpolator = new ResourceBundleMessageInterpolator(bundle,
                Collections.emptySet(),
                Locale.getDefault(),
                resolver,
                false);

        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setMessageInterpolator(interpolator);
        return localValidatorFactoryBean;
    }
}
