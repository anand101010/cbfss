package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "REQ_ROOT")
public class CkycRequest {

    private Header header;
    private CkycInquiry ckycInq;

    public CkycRequest() {}

    public CkycRequest(String fiCode, String requestId, String pid, String sessionKey) {
        this.header = new Header(fiCode, requestId, "1.2");
        this.ckycInq = new CkycInquiry(pid, sessionKey);
    }

    @XmlElement(name = "HEADER")
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    @XmlElement(name = "CKYC_INQ")
    public CkycInquiry getCkycInq() {
        return ckycInq;
    }

    public void setCkycInq(CkycInquiry ckycInq) {
        this.ckycInq = ckycInq;
    }

    public static class Header {
        private String fiCode;
        private String requestId;
        private String version;

        public Header() {}

        public Header(String fiCode, String requestId, String version) {
            this.fiCode = fiCode;
            this.requestId = requestId;
            this.version = version;
        }

        @XmlElement(name = "FI_CODE")
        public String getFiCode() {
            return fiCode;
        }

        public void setFiCode(String fiCode) {
            this.fiCode = fiCode;
        }

        @XmlElement(name = "REQUEST_ID")
        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        @XmlElement(name = "VERSION")
        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    public static class CkycInquiry {
        private String pid;
        private String sessionKey;

        public CkycInquiry() {}

        public CkycInquiry(String pid, String sessionKey) {
            this.pid = pid;
            this.sessionKey = sessionKey;
        }

        @XmlElement(name = "PID")
        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        @XmlElement(name = "SESSION_KEY")
        public String getSessionKey() {
            return sessionKey;
        }

        public void setSessionKey(String sessionKey) {
            this.sessionKey = sessionKey;
        }
    }
}

