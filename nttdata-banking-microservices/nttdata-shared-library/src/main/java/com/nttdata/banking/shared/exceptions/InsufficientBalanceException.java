package com.nttdata.banking.shared.exceptions;


/**
 * F3: "Saldo no disponible" .
 */
public class InsufficientBalanceException extends BusinessException{

    public InsufficientBalanceException(String accountNumber) {
        super(String.format("Saldo no disponible para cuenta: %s", accountNumber),
                "INSUFFICIENT_BALANCE");
    }

    public InsufficientBalanceException() {
        super("Saldo no disponible", "INSUFFICIENT_BALANCE");
    }

}
