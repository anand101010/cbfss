package com.incede.nbfc.core.monolith.user.domain.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "users")
public class User extends UserBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;


    @Column(name = "user_code", nullable = false, length = 20, unique = true)
    @NotBlank(message = "User code is required")
    @Size(max = 20, message = "User code must not exceed 20 characters")
    private String userCode;

    @Column(name = "user_name", nullable = false, length = 100)
    @NotBlank(message = "User name is required")
    @Size(max = 100, message = "User name must not exceed 100 characters")
    private String userName;
}
