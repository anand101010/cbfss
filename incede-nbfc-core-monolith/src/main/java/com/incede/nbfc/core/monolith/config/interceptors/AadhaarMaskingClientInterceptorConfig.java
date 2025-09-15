package com.incede.nbfc.core.monolith.config.interceptors;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AadhaarMaskingClientInterceptorConfig {
    /**
     * these are providing headers for the pixl for masking the aadhaar
     */
    @Value("${pixl.key}")
    private String key;

    @Value("${pixl.api.aadhaar-mask}")
    private String requestUrl;

    @Bean(name = "aadhaarMaskingInterceptor")
    public RequestInterceptor authHeaderInterceptor() {
        return requestTemplate -> {
            if (requestTemplate.url().contains(requestUrl))
            {
                requestTemplate.header(CommonConstants.KEY, key);
            }
        };
    }

}

