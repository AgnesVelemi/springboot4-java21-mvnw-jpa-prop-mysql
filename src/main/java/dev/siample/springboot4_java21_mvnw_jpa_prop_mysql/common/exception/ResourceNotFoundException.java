package dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
