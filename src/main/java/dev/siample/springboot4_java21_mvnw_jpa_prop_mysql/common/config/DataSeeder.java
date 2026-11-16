package dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.common.config;

import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.entity.RenovationMaterial;
import dev.siample.springboot4_java21_mvnw_jpa_prop_mysql.repository.RenovationMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
// @Profile("!test") // Tesztek futtatásakor nem fut le, ezért nem tölt be adatot!
@Profile("dev") // app.properties fájlban spring.profiles.active=dev esetén fut csak le!
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RenovationMaterialRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            List<RenovationMaterial> materials = List.of(
                    RenovationMaterial.builder()
                            .name("Beltéri diszperziós falfesték 15L")
                            .category("Festékek")
                            .unit("vödör")
                            .unitPrice(new BigDecimal("14990.00"))
                            .stockQuantity(25)
                            .build(),
                    RenovationMaterial.builder()
                            .name("Flexibilis csemperagasztó 25kg")
                            .category("Ragasztók")
                            .unit("zsák")
                            .unitPrice(new BigDecimal("4290.00"))
                            .stockQuantity(60)
                            .build(),
                    RenovationMaterial.builder()
                            .name("Gipszkarton lap 12,5mm (120x200cm)")
                            .category("Szárazépítészet")
                            .unit("tábla")
                            .unitPrice(new BigDecimal("3150.00"))
                            .stockQuantity(40)
                            .build(),
                    RenovationMaterial.builder()
                            .name("Mélyalapozó 5L")
                            .category("Alapozók")
                            .unit("kanna")
                            .unitPrice(new BigDecimal("5490.00"))
                            .stockQuantity(18)
                            .build()
            );

            repository.saveAll(materials);
            System.out.println(">>> Mintaadatok sikeresen betöltve a MySQL adatbázisba!");
        }
    }
}
