package dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.service;

import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto.RenovationMaterialRequest;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto.RenovationMaterialResponse;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.entity.RenovationMaterial;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.exception.ResourceNotFoundException;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.repository.RenovationMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RenovationMaterialService {

    private final RenovationMaterialRepository repository;

    @Transactional(readOnly = true)
    public List<RenovationMaterialResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RenovationMaterialResponse getById(Long id) {
        RenovationMaterial entity = findEntityById(id);
        return toResponse(entity);
    }

    @Transactional
    public RenovationMaterialResponse create(RenovationMaterialRequest request) {
        RenovationMaterial entity = RenovationMaterial.builder()
                .name(request.name())
                .category(request.category())
                .unit(request.unit())
                .unitPrice(request.unitPrice())
                .stockQuantity(request.stockQuantity())
                .build();

        return toResponse(repository.save(entity));
    }

    @Transactional
    public RenovationMaterialResponse update(Long id, RenovationMaterialRequest request) {
        RenovationMaterial entity = findEntityById(id);
        entity.setName(request.name());
        entity.setCategory(request.category());
        entity.setUnit(request.unit());
        entity.setUnitPrice(request.unitPrice());
        entity.setStockQuantity(request.stockQuantity());

        return toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        RenovationMaterial entity = findEntityById(id);
        repository.delete(entity);
    }

    private RenovationMaterial findEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nem található felújítási anyag ezzel az ID-val: " + id));
    }

    private RenovationMaterialResponse toResponse(RenovationMaterial entity) {
        return new RenovationMaterialResponse(
                entity.getId(),
                entity.getName(),
                entity.getCategory(),
                entity.getUnit(),
                entity.getUnitPrice(),
                entity.getStockQuantity()
        );
    }
}
