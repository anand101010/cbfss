package com.incede.nbfc.core.monolith.config;
import com.incede.nbfc.core.monolith.common.TextHtmlJsonMessageConverter;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;

/**
 * Feign client configuration for FinaVault integration.
 *
 * This configuration ensures that Feign can correctly decode responses
 * from FinaVault API even when the server incorrectly returns JSON data
 * with a Content-Type of "text/html" or "text/plain".
 *
 * By registering {@link TextHtmlJsonMessageConverter}, we extend Spring's
 * default JSON decoding to also accept these non-standard content types.
 */
@Configuration
public class FinaVaultFeignConfig {

    /**
     * Custom Feign decoder bean that plugs in our
     * {@link TextHtmlJsonMessageConverter}.
     *
     * This decoder ensures that responses with misdeclared
     * Content-Type (like text/html or text/plain) are still
     * deserialized into JSON objects.
     *
     * @return Decoder for Feign responses
     */
    @Bean
    public Decoder feignDecoder() {
        ObjectFactory<HttpMessageConverters> objectFactory =
                () -> new HttpMessageConverters(new TextHtmlJsonMessageConverter());
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }
}
