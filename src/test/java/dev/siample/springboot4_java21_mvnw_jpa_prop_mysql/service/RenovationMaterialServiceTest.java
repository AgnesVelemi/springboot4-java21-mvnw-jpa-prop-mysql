package dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.service;

import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.common.exception.ResourceNotFoundException;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto.RenovationMaterialRequest;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto.RenovationMaterialResponse;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto.UpsertResult;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.entity.RenovationMaterial;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.repository.RenovationMaterialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RenovationMaterialServiceTest {

    @Mock
    private RenovationMaterialRepository repository;

    @InjectMocks
    private RenovationMaterialService service;

    @Test
    void getAll_ShouldReturnListOfResponses() {
        // Given
        RenovationMaterial entity = RenovationMaterial.builder()
                .id(1L)
                .name("Falfesték")
                .category("Festékek")
                .unit("vödör")
                .unitPrice(new BigDecimal("14990.00"))
                .stockQuantity(10)
                .build();

        when(repository.findAll()).thenReturn(List.of(entity));

        // When
        List<RenovationMaterialResponse> result = service.getAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Falfesték");
        verify(repository, times(1)).findAll();
    }

    @Test
    void getById_WhenExists_ShouldReturnResponse() {
        // Given
        RenovationMaterial entity = RenovationMaterial.builder()
                .id(1L)
                .name("Csemperagasztó")
                .category("Ragasztók")
                .unit("zsák")
                .unitPrice(new BigDecimal("4290.00"))
                .stockQuantity(50)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        // When
        RenovationMaterialResponse response = service.getById(1L);

        // Then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Csemperagasztó");
    }

    @Test
    void getById_WhenNotExists_ShouldThrowException() {
        // Given
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_ShouldSaveAndReturnResponse() {
        // Given
        RenovationMaterialRequest request = new RenovationMaterialRequest(
                "Mélyalapozó", "Alapozók", "kanna", new BigDecimal("5490.00"), 15
        );

        RenovationMaterial savedEntity = RenovationMaterial.builder()
                .id(1L)
                .name(request.name())
                .category(request.category())
                .unit(request.unit())
                .unitPrice(request.unitPrice())
                .stockQuantity(request.stockQuantity())
                .build();

        when(repository.save(any(RenovationMaterial.class))).thenReturn(savedEntity);

        // When
        RenovationMaterialResponse response = service.create(request);

        // Then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Mélyalapozó");
        verify(repository, times(1)).save(any(RenovationMaterial.class));
    }

    @Test
    void upsert_WhenExists_ShouldUpdateAndReturnCreatedFalse() {
        // Given
        RenovationMaterial existing = RenovationMaterial.builder()
                .id(1L)
                .name("Régi név")
                .category("Festékek")
                .unit("vödör")
                .unitPrice(new BigDecimal("10000.00"))
                .stockQuantity(5)
                .build();

        RenovationMaterialRequest request = new RenovationMaterialRequest(
                "Új név", "Festékek", "vödör", new BigDecimal("12000.00"), 8
        );

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(RenovationMaterial.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        UpsertResult result = service.upsert(1L, request);

        // Then
        assertThat(result.created()).isFalse();
        assertThat(result.data().name()).isEqualTo("Új név");
        assertThat(result.data().unitPrice()).isEqualByComparingTo("12000.00");
    }

    @Test
    void upsert_WhenNotExists_ShouldCreateAndReturnCreatedTrue() {
        // Given
        RenovationMaterialRequest request = new RenovationMaterialRequest(
                "Új anyag", "Ragasztók", "zsák", new BigDecimal("3500.00"), 20
        );

        RenovationMaterial savedEntity = RenovationMaterial.builder()
                .id(10L)
                .name(request.name())
                .category(request.category())
                .unit(request.unit())
                .unitPrice(request.unitPrice())
                .stockQuantity(request.stockQuantity())
                .build();

        when(repository.findById(999L)).thenReturn(Optional.empty());
        when(repository.save(any(RenovationMaterial.class))).thenReturn(savedEntity);

        // When
        UpsertResult result = service.upsert(999L, request);

        // Then
        assertThat(result.created()).isTrue();
        assertThat(result.data().id()).isEqualTo(10L);
        assertThat(result.data().name()).isEqualTo("Új anyag");
    }

    @Test
    void delete_WhenExists_ShouldDeleteEntity() {
        // Given
        RenovationMaterial existing = RenovationMaterial.builder()
                .id(1L)
                .name("Törlendő anyag")
                .category("Festékek")
                .unit("db")
                .unitPrice(new BigDecimal("1000.00"))
                .stockQuantity(1)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        // When
        service.delete(1L);

        // Then
        verify(repository, times(1)).delete(existing);
    }
}

