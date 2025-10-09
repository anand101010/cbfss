package com.incede.nbfc.core.monolith.config.interceptors;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * these are provide headers for the decentro vendor
 */
@Configuration
public class DecentroClientInterceptorConfig {

    @Value("${decentro.client.id}")
    private String clientId;

    @Value("${decentro.client.secret}")
    private String clientSecret;

    @Value("${decentro.module.secret}")
    private String moduleSecret;

    @Value("${decentro.api.url}")
    private String requestUrl;

    @Value("${decentro.module.secretbank}")
    private String modulebankSecret;

    @Value("${decentro.provider.secret}")
    private String providerSecret;
    /**
     * for PENNY drop details validation url
     */
    @Value("${decentro.api.validate-bank}")
    private String pennyDropUrl;

    /**
     * for UPI details validation url
     */
    @Value("${decentro.api.upiId-validate}")
    private String upiValidateUrl;

    /**
     * for kyc details validation url
     */
    @Value("${decentro.api.kyc-validate}")
    private String kycValidateUrl;


    @Bean
    public RequestInterceptor authHeaderInterceptor() {
        return requestTemplate ->
        {
            /***
             * these conditions are based on url and its header
             */
            if (requestTemplate.url().contains(CommonConstants.APIV2VALIADTOR)||requestTemplate.url().contains(kycValidateUrl))
            {

                requestTemplate.header(CommonConstants.CLIENTID, clientId);
                requestTemplate.header(CommonConstants.CLIENTSECREAT, clientSecret);
                requestTemplate.header(CommonConstants.MODULESECREAT, moduleSecret);
            }
            else if (requestTemplate.url().contains(pennyDropUrl))
            {
                requestTemplate.header(CommonConstants.CLIENTID, clientId);
                requestTemplate.header(CommonConstants.CLIENTSECREAT, clientSecret);
                requestTemplate.header(CommonConstants.MODULESECREAT, modulebankSecret);
                requestTemplate.header(CommonConstants.PROVIDERSECREAT, providerSecret);
            }
            else if (requestTemplate.url().contains(upiValidateUrl)) {
                requestTemplate.header(CommonConstants.CLIENTID, clientId);
                requestTemplate.header(CommonConstants.CLIENTSECREAT, clientSecret);
                requestTemplate.header(CommonConstants.MODULESECREAT, modulebankSecret);
            }

        };
    }


}