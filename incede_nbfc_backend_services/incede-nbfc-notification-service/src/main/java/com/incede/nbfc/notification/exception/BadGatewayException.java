package com.incede.nbfc.notification.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class BadGatewayException  extends RuntimeException
{

    private final String errorCode;

    public BadGatewayException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public BadGatewayException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    public BadGatewayException(String message, String errorCode, Throwable cause)
    {
        super(message, cause);
        this.errorCode = errorCode;
    }




}
