package dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record RenovationMaterialRequest(
        @NotBlank(message = "A név megadása kötelező")
        @Size(max = 100, message = "A név maximum 100 karakter lehet")
        String name,

        @NotBlank(message = "A kategória megadása kötelező")
        @Size(max = 50, message = "A kategória maximum 50 karakter lehet")
        String category,

        @NotBlank(message = "A mértékegység megadása kötelező")
        @Size(max = 20, message = "A mértékegység maximum 20 karakter lehet")
        String unit,

        @NotNull(message = "Az egységár megadása kötelező")
        @DecimalMin(value = "0.01", message = "Az egységárnak nagyobbnak kell lennie 0-nál")
        BigDecimal unitPrice,

        @NotNull(message = "A készletmennyiség megadása kötelező")
        @Min(value = 0, message = "A készletmennyiség nem lehet negatív")
        Integer stockQuantity
) {}
