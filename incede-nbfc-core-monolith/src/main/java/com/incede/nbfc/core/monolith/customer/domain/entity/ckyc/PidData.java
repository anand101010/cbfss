package com.incede.nbfc.core.monolith.customer.domain.entity.ckyc;


import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "PID_DATA")
@XmlAccessorType(XmlAccessType.FIELD)
public class PidData {
    @XmlElement(name = "DATE_TIME")
    private String dateTime;

    @XmlElement(name = "ID_NO")
    private String idNo;

    @XmlElement(name = "ID_TYPE")
    private String idType;

    public PidData() {}

    public PidData(String dateTime, String idNo, String idType) {
        this.dateTime = dateTime;
        this.idNo = idNo;
        this.idType = idType;
    }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String getIdNo() { return idNo; }
    public void setIdNo(String idNo) { this.idNo = idNo; }

    public String getIdType() { return idType; }
    public void setIdType(String idType) { this.idType = idType; }
}

