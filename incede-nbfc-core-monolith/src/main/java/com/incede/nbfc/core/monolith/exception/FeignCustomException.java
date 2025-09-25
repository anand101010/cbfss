package com.incede.nbfc.core.monolith.exception;

import java.util.Map;

public class FeignCustomException extends RuntimeException
{
    private Map<String, Object> details;

    public FeignCustomException(Map<String, Object> details) {

        this.details = details;
    }

    public Map<String, Object>  getErrorResponse() {
        return details;
    }
}