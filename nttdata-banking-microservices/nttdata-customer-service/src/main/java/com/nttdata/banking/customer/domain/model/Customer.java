package com.nttdata.banking.customer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("ba_clientes")
public class Customer {

    @Id
    @Column("cl_id_persona")
    private Long id;

    @Column("cl_id_persona")
    private Long personId;

    @Column("cl_id_cliente")
    private String customerCode;

    @Column("cl_contrasena")
    private String password;

    @Column("cl_estado")
    private Boolean active;

    @CreatedDate
    @Column("cl_created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("cl_updated_at")
    private LocalDateTime updatedAt;

    /**
     * Transient field - Person details are joined separately
     */
    @Transient
    private Person person;
}
