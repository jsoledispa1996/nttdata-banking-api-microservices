package com.nttdata.banking.customer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("ba_personas")
public class Person {

    @Id
    @Column("pe_id_persona")
    private Long id;

    @Column("pe_nombre")
    private String name;

    @Column("pe_genero")
    private String gender;

    @Column("pe_edad")
    private Integer age;

    @Column("pe_identificacion")
    private String identification;

    @Column("pe_direccion")
    private String address;

    @Column("pe_telefono")
    private String phone;

    @CreatedDate
    @Column("pe_created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("pe_updated_at")
    private LocalDateTime updatedAt;
}