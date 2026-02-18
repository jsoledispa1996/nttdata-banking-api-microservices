package com.nttdata.banking.shared.exceptions;

/**
 * Results in HTTP 404 Not Found response.
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s no encontrado con identificador: %s", resourceName, identifier),
                "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
