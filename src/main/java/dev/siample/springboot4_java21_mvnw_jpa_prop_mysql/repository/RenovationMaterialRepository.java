package dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.repository;

import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.entity.RenovationMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RenovationMaterialRepository extends JpaRepository<RenovationMaterial, Long> {
    List<RenovationMaterial> findByCategoryIgnoreCase(String category);
}
