package com.nttdata.banking.account.infrastructure.rest;

import com.nttdata.banking.account.application.service.ReportService;
import com.nttdata.banking.account.infrastructure.rest.dto.AccountStatementResponse;
import com.nttdata.banking.shared.utils.CorrelationIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    /**
     * Constructor injection.
     */
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Generate account statement for a customer (F4 requirement).
     */
    @GetMapping(value = "/{customerCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<AccountStatementResponse> generateAccountStatement(
            @PathVariable String customerCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/reports/{} - Generando estado de cuenta desde {} hasta {}",
                correlationId, customerCode, startDate, endDate);

        return reportService.generateAccountStatement(customerCode, startDate.atStartOfDay(), endDate.atTime(23, 59, 59))
                .doOnSuccess(response -> log.info("[{}] Reporte generado exitosamente: {} cuentas, {} movimientos totales",
                        correlationId,
                        response.getAccounts().size(),
                        response.getAccounts().stream()
                                .mapToInt(acc -> acc.getMovements().size())
                                .sum()))
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }

    /**
     * Get complete customer report with all accounts and all movements.
     * This endpoint returns full customer details without date filtering.
     */
    @GetMapping(value = "/customer/{customerCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<AccountStatementResponse> getCustomerFullReport(@PathVariable String customerCode) {
        String correlationId = CorrelationIdUtil.generateCorrelationId();
        log.info("[{}] GET /api/v1/reports/customer/{} - Obteniendo reporte completo del cliente",
                correlationId, customerCode);

        return reportService.getCustomerFullReport(customerCode)
                .doOnSuccess(response -> {
                    int totalMovements = response.getAccounts().stream()
                            .mapToInt(acc -> acc.getMovements().size())
                            .sum();
                    log.info("[{}] Reporte completo generado: cliente={}, cuentas={}, movimientos={}",
                            correlationId, customerCode, response.getAccounts().size(), totalMovements);
                })
                .doFinally(signalType -> CorrelationIdUtil.clear());
    }
}

