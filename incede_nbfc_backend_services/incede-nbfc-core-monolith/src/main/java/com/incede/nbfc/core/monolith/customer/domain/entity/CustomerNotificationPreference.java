package com.incede.nbfc.core.monolith.customer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_notifications", schema = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerNotificationPreference extends CustomerBaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;

    @ManyToOne(fetch= FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name="customer_Id",referencedColumnName = "customer_id")
    @NotNull(message="CustomerId must not be null")
    private Customer customer;

    @Column(name = "consent_sms", nullable = false)
    @NotNull(message = "SMS consent flag must be set")
    private Boolean consentSms = false;

    @Column(name = "consent_email", nullable = false)
    @NotNull(message = "Email consent flag must be set")
    private Boolean consentEmail = false;

    @Column(name = "consent_whatsapp", nullable = false)
    @NotNull(message = "WhatsApp consent flag must be set")
    private Boolean consentWhatsapp = false;

}