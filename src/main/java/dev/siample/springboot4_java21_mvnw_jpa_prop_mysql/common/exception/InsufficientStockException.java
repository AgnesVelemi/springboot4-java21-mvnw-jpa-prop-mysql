package dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.common.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String message) {
        super(message);
    }
}
