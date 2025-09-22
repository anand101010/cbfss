package com.incede.nbfc.core.monolith.customer.domain.entity.ckyc;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Header {
    @XmlElement(name = "FI_CODE")
    private String fiCode;



    @XmlElement(name = "REQUEST_ID")
    private String requestId;

    @XmlElement(name = "VERSION")
    private String version;

    public Header() {}
    public Header(String fiCode, String requestId, String version) {
        this.fiCode = fiCode; this.requestId = requestId; this.version = version;
    }

    public String getFiCode() {
        return fiCode;
    }

    public void setFiCode(String fiCode) {
        this.fiCode = fiCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

