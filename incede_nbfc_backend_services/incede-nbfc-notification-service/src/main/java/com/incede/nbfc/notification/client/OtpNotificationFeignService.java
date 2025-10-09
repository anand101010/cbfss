package com.incede.nbfc.notification.client;

import com.incede.nbfc.notification.common.CommonConstants;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "Otp-notification-service", url = "${otp-vendor.api.url}")
public interface OtpNotificationFeignService
{

    /**
     *
     * @param enterpriseId
     * @param subEnterpriseId
     * @param pusheId
     * @param pushePwd
     * @param msisdn  phone no
     * @param sender
     * @param msgText  text have otp
     * @return string with proper return id
     */
     @PostMapping("${otp-vendor.api.otp}")
     @CircuitBreaker(name = "post-api", fallbackMethod = "getOtpNotificationFallBack")
     String generateOtpNotification(@RequestParam("enterpriseid") String enterpriseId,@RequestParam("subEnterpriseid") String subEnterpriseId,@RequestParam("pusheid") String pusheId, @RequestParam("pushepwd") String pushePwd,@RequestParam("msisdn") String msisdn, @RequestParam("sender") String sender,@RequestParam("msgtext") String msgText) ;

    /**
     *
     * @param throwable will be parameter to get deceoder error
     * @return custom fallback response
     */
    default String getOtpNotificationFallBack(Throwable throwable)
    {
        return CommonConstants.SERVICE_UNAVAILABLE;
    }

}
