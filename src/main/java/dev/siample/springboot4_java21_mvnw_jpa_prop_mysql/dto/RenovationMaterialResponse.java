package dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto;

import java.math.BigDecimal;

public record RenovationMaterialResponse(
        Long id,
        String name,
        String category,
        String unit,
        BigDecimal unitPrice,
        Integer stockQuantity
) {}
