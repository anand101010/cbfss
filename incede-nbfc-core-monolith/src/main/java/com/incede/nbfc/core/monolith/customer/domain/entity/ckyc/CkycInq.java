package com.incede.nbfc.core.monolith.customer.domain.entity.ckyc;


import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class CkycInq {
    @XmlElement(name = "PID")
    private String pid;



    @XmlElement(name = "SESSION_KEY")
    private String sessionKey;

    public CkycInq() {}
    public CkycInq(String pid, String sessionKey) {
        this.pid = pid; this.sessionKey = sessionKey;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

}

