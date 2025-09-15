package com.incede.nbfc.core.monolith.config.interceptors;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
/**
 * these are provide headers for the decentro vendor for decentro UAT
 */
@Slf4j
@Configuration
@Profile("uat")
public class DecentroUATClientInterceptorConfig
{

    @Value("${decentro.uiclient.id}")
    private String clientId;

    @Value("${decentro.uiclient.secret}")
    private String clientSecret;

    @Value("${decentro.uatmodule.secret}")
    private String moduleSecret;

    @Value("${decentro.uat.photo-liveness}")
    private String photoLivenessPath;


    @Bean(name = "decentroUatInterceptor")
    public RequestInterceptor authHeaderInterceptor() {
        return requestTemplate -> {

            if (requestTemplate.url().contains(photoLivenessPath))
            {
                requestTemplate.header(CommonConstants.CLIENTID, clientId);
                requestTemplate.header(CommonConstants.CLIENTSECREAT, clientSecret);
                requestTemplate.header(CommonConstants.MODULESECREAT, moduleSecret);
            }
        };
    }


}

