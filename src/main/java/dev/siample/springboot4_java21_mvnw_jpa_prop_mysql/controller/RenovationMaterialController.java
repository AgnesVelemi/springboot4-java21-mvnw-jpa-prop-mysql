package dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.controller;

import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto.RenovationMaterialRequest;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto.RenovationMaterialResponse;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.service.RenovationMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class RenovationMaterialController {

    private final RenovationMaterialService service;

    @GetMapping
    public ResponseEntity<List<RenovationMaterialResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RenovationMaterialResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<RenovationMaterialResponse> create(@Valid @RequestBody RenovationMaterialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RenovationMaterialResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RenovationMaterialRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
