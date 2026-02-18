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
@Table("ba_movimientos")
public class Movement {

    @Id
    @Column("mo_id_movimiento")
    private Long id;

    @Column("mo_fecha")
    private LocalDateTime movementDate;

    @Column("mo_tipo_movimiento")
    private MovementType movementType;

    @Column("mo_valor")
    private BigDecimal amount;

    @Column("mo_saldo")
    private BigDecimal balance;

    @Column("mo_descripcion")
    private String description;

    @Column("mo_id_cuenta")
    private Long accountId;
}
