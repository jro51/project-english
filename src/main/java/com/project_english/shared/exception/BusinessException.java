package com.project_english.shared.exception;

// Extiende de RuntimeException para que Spring pueda hacer
// rollback automático de las transacciones de base de datos si algo falla
public class BusinessException extends RuntimeException{
    private final String errorCode;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode(){
        return errorCode;
    }
}
