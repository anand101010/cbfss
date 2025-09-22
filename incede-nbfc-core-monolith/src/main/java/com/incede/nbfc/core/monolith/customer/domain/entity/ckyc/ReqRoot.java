package com.incede.nbfc.core.monolith.customer.domain.entity.ckyc;


import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "REQ_ROOT")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReqRoot {
    @XmlElement(name = "HEADER")
    private Header header;



    @XmlElement(name = "CKYC_INQ")
    private CkycInq ckycInq;

    public ReqRoot() {}
    public ReqRoot(Header header, CkycInq ckycInq) {
        this.header = header; this.ckycInq = ckycInq;
    }
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public CkycInq getCkycInq() {
        return ckycInq;
    }

    public void setCkycInq(CkycInq ckycInq) {
        this.ckycInq = ckycInq;
    }

}

