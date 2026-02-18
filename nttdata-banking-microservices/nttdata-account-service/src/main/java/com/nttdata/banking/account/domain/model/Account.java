package com.nttdata.banking.account.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ba_cuentas")
public class Account {

    @Id
    @Column("cu_id_cuenta")
    private Long id;

    @Column("cu_numero_cuenta")
    private String accountNumber;

    @Column("cu_tipo_cuenta")
    private AccountType accountType;

    @Column("cu_saldo_inicial")
    private BigDecimal initialBalance;

    @Column("cu_saldo_actual")
    private BigDecimal currentBalance;

    @Column("cu_estado")
    private Boolean active;

    @Column("cu_id_cliente")
    private String customerId;

    @Column("cu_fecha_creacion")
    private LocalDateTime createdDate;

    @Column("cu_fecha_actualizacion")
    private LocalDateTime updatedDate;
}
