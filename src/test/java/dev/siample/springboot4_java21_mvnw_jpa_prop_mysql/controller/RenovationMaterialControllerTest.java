package dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.controller;

import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.common.exception.ResourceNotFoundException;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto.RenovationMaterialRequest;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto.RenovationMaterialResponse;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.dto.UpsertResult;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.service.RenovationMaterialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 *  Spring Boot 3 4 verziófüggetlen
 *  Tiszta Unit Teszt (JUnit 5 + Mockito)
 *  Közvetlenül ellenőrizheted vele a HTTP státuszkódokat (200, 201, 204),
 *  a visszakapott DTO-kat és a Location fejlécet is
 */
@ExtendWith(MockitoExtension.class)
class RenovationMaterialControllerTest {

    @Mock
    private RenovationMaterialService service;

    @InjectMocks
    private RenovationMaterialController controller;

    @BeforeEach
    void setUp() {
        // Beállítunk egy ál HTTP kérést, hogy a ServletUriComponentsBuilder tudja az alap URL-t
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void getAll_ShouldReturn200AndList() {
        RenovationMaterialResponse item = new RenovationMaterialResponse(
                1L, "Beltéri falfesték", "Festékek", "vödör", new BigDecimal("14990.00"), 10
        );
        when(service.getAll()).thenReturn(List.of(item));

        ResponseEntity<List<RenovationMaterialResponse>> response = controller.getAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).name()).isEqualTo("Beltéri falfesték");
    }

    @Test
    void getById_WhenNotFound_ShouldThrowResourceNotFoundException() {
        when(service.getById(99L))
                .thenThrow(new ResourceNotFoundException("Nem található felújítási anyag ezzel az ID-val: 99"));

        assertThatThrownBy(() -> controller.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_ShouldReturn201AndLocationHeader() {
        RenovationMaterialRequest request = new RenovationMaterialRequest(
                "Mélyalapozó 5L", "Alapozók", "kanna", new BigDecimal("5490.00"), 15
        );
        RenovationMaterialResponse created = new RenovationMaterialResponse(
                5L, "Mélyalapozó 5L", "Alapozók", "kanna", new BigDecimal("5490.00"), 15
        );
        when(service.create(any(RenovationMaterialRequest.class))).thenReturn(created);

        ResponseEntity<RenovationMaterialResponse> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).hasToString("http://localhost/api/materials/5");
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(5L);
    }

    @Test
    void update_WhenExisting_ShouldReturn200WithoutLocation() {
        RenovationMaterialRequest request = new RenovationMaterialRequest(
                "Frissített név", "Festékek", "vödör", new BigDecimal("12000.00"), 8
        );
        RenovationMaterialResponse updated = new RenovationMaterialResponse(
                1L, "Frissített név", "Festékek", "vödör", new BigDecimal("12000.00"), 8
        );
        when(service.upsert(eq(1L), any(RenovationMaterialRequest.class)))
                .thenReturn(new UpsertResult(updated, false));

        ResponseEntity<RenovationMaterialResponse> response = controller.update(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getLocation()).isNull();
        assertThat(response.getBody().name()).isEqualTo("Frissített név");
    }

    @Test
    void update_WhenNotExisting_ShouldReturn201AndLocationHeader() {
        RenovationMaterialRequest request = new RenovationMaterialRequest(
                "Új elem PUT-tal", "Festékek", "vödör", new BigDecimal("12000.00"), 8
        );
        RenovationMaterialResponse created = new RenovationMaterialResponse(
                10L, "Új elem PUT-tal", "Festékek", "vödör", new BigDecimal("12000.00"), 8
        );
        when(service.upsert(eq(999L), any(RenovationMaterialRequest.class)))
                .thenReturn(new UpsertResult(created, true));

        ResponseEntity<RenovationMaterialResponse> response = controller.update(999L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).hasToString("http://localhost/api/materials/10");
        assertThat(response.getBody().id()).isEqualTo(10L);
    }

    @Test
    void smartUpdate_WhenExisting_ShouldReturn204NoContent() {
        RenovationMaterialRequest request = new RenovationMaterialRequest(
                "Okos frissítés", "Festékek", "vödör", new BigDecimal("12000.00"), 8
        );
        RenovationMaterialResponse updated = new RenovationMaterialResponse(
                1L, "Okos frissítés", "Festékek", "vödör", new BigDecimal("12000.00"), 8
        );
        when(service.upsert(eq(1L), any(RenovationMaterialRequest.class)))
                .thenReturn(new UpsertResult(updated, false));

        ResponseEntity<Void> response = controller.smartUpdate(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void smartUpdate_WhenNotExisting_ShouldReturn201AndLocationHeaderWithoutBody() {
        RenovationMaterialRequest request = new RenovationMaterialRequest(
                "Okos létrehozás", "Festékek", "vödör", new BigDecimal("12000.00"), 8
        );
        RenovationMaterialResponse created = new RenovationMaterialResponse(
                11L, "Okos létrehozás", "Festékek", "vödör", new BigDecimal("12000.00"), 8
        );
        when(service.upsert(eq(999L), any(RenovationMaterialRequest.class)))
                .thenReturn(new UpsertResult(created, true));

        ResponseEntity<Void> response = controller.smartUpdate(999L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).hasToString("http://localhost/api/materials/11");
        assertThat(response.getBody()).isNull();
    }

    @Test
    void delete_ShouldReturn204() {
        doNothing().when(service).delete(1L);

        ResponseEntity<Void> response = controller.delete(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(service, times(1)).delete(1L);
    }
}
