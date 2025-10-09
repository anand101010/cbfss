package com.incede.nbfc.notification.config.interceptors;

import com.incede.nbfc.notification.util.RequestUrlHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignRequestLoggingInterceptor implements RequestInterceptor
{
    /**
     * for logging the url
     * @param template
     */
    @Override
    public void apply(RequestTemplate template)
    {
        String fullUrl = template.url();
        RequestUrlHolder.setUrl(fullUrl);
    }
}
