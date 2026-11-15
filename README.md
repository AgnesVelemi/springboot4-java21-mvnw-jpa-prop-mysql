# springboot4-java21-mvnw-jpa-prop-mysql

Ez a projekt egy Spring Boot 4 alapú alkalmazás Java 21, Maven Wrapper, Spring Data JPA, `application.properties` konfiguráció és Dockerben futó MySQL 8.0 adatbázis használatával.

---

## 1. Projekt inicializálása (Spring Initializr)

1. Nyisd meg a [Spring Initializr](https://start.spring.io/index.html) felületét.
2. Állítsd be a következő paramétereket:
    * **Group / Package:** `dev.siample`
    * **Artifact / Name:** `springboot4-java21-mvnw-jpa-prop-mysql`
    * **Java:** `21`
    * **Configuration:** `Properties` (`application.properties`)
3. Add hozzá a függőségeket:
    * **Spring Web** (`web`)
    * **Spring Data JPA** (`jpa`)
    * **Lombok** (`lombok`)
    * **MySQL Driver** (`mysql`)
    * **Validation** (`validation`)
4. Töltsd le a `.zip` fájlt, és másold a tartalmát a munkakönyvtáradba (`workdir`).
5. Nyisd meg a projektet az **IntelliJ IDEA** környezetben (`Open Project`).

---

## 2. Docker és MySQL 8.0 beállítása

### Előfeltételek (Windows)
* Telepítsd a [Docker Desktop](https://www.docker.com/products/docker-desktop/) alkalmazást (győződj meg róla, hogy a `PATH` környezeti változó rendben van).
* **Virtualizáció ellenőrzése:** Nyisd meg a Feladatkezelőt (`Ctrl + Shift + Esc`) → *Teljesítmény* fül → *Virtualizálás: Engedélyezve*. 
  * Ha le van tiltva, indítsd újra a gépet (`F2`, `Del` vagy `ESC`), és kapcsold be a BIOS/UEFI menüben.
* **WSL hiba esetén:** Futtasd parancssorban (CMD) a következő parancsot:
  ```cmd
  wsl --update
  ```

### MySQL konténer indítása
Futtasd a következő parancsot a terminálban a MySQL 8.0 szerver elindításához:

```bash
docker run -d \
  --name mysql-server \
  -e MYSQL_ROOT_PASSWORD=test1234 \
  -p 3306:3306 \
  -v mysqldata:/var/lib/mysql \
  mysql:8.0
```

### Adatbázis létrehozása
1. Nyisd meg a terminált a Docker Desktop jobb alsó sarkában (vagy saját terminált).
2. Ellenőrizd a futó konténereket, majd lépj be a MySQL CLI felületére:
   ```powershell
   docker ps
   docker exec -it mysql-server mysql -u root -p
   ```
3. Hozd létre az adatbázist és ellenőrizd a struktúrát:
   ```sql
   CREATE DATABASE renovation_materials_db CHARACTER SET utf8mb4;
   SHOW DATABASES;
   USE renovation_materials_db;
   SHOW TABLES;
   DESCRIBE renovation_material;
   SELECT * FROM renovation_material;
   EXIT;
   ```

---

## 3. Alkalmazás konfigurációja (`application.properties`)

Másold be az alábbi beállításokat a `src/main/resources/application.properties` fájlba:

```properties
spring.application.name=springboot4-java21-mvnw-jpa-prop-mysql
spring.datasource.url=jdbc:mysql://localhost:3306/renovation_materials_db
spring.datasource.username=root
spring.datasource.password=test1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8084
```

---

## 4. IntelliJ IDEA beállítások

### Java 21 (Eclipse Temurin) ellenőrzése
* **Project SDK:** `File → Project Structure → Project` → Válaszd ki az **Eclipse Temurin 21** JDK-t, és állítsd a *Language level*-t `21`-re.
* **Maven Importer:** `File → Settings → Build, Execution, Deployment → Build Tools → Maven → Importing` → *JDK for importer:* **Temurin Java 21**.
* **Maven Runner:** `File → Settings → Build, Execution, Deployment → Build Tools → Maven → Runner` → *JRE:* **Eclipse Temurin Java 21**.

### Maven Wrapper beállítása
A projekt a hordozható Maven Wrappert használja, így nincs szükség külön globális Maven telepítésre:
* `File → Settings → Build, Execution, Deployment → Build Tools → Maven` → *Maven home path:* **Use Maven wrapper**.

### Főosztály átnevezése
A rövidebb név érdekében nevezd át a belépési pontot (`Refactor → Rename`), beleértve a tesztosztályokat is:
* `Springboot4Java21MvnwJpaPropMysqlApplication.java` → **`SB4Java21MvnwMysqlApp.java`**

---

## 5. Hibaelhárítás (Troubleshooting)

### 1. Guice hiba és Maven verzió kompatibilitás (Java 21)
A Java 21 nem kompatibilis a Maven `3.10.0` verziójával. Amennyiben `.\mvnw.cmd -v` parancsra `3.10.0`-t ír ki, állítsd vissza `3.9.9`-re:

1. Nyisd meg a `.mvn/wrapper/maven-wrapper.properties` fájlt, és módosítsd a `distributionUrl` sort:
   ```properties
   wrapperVersion=3.3.4
   distributionType=only-script
   # distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.10.0/apache-maven-3.10.0-bin.zip
   distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip
   ```
2. Töröld a régi letöltött Maven mappát a gépedről:
   ```text
   C:\Users\User\.m2\wrapper\dists\apache-maven-3.10.0
   ```
3. Indítsd újra az IntelliJ IDEA-t, majd ellenőrizd a verziót:
   ```powershell
   .\mvnw.cmd -v
   ```
   *(Győződj meg róla, hogy már az `Apache Maven 3.9.9` verziót írja ki.)*

### 2. Fordítási hiba: Hiányzó JUnit / Spring Boot Test csomagok
Ha a `.\mvnw.cmd clean package` futtatásakor a `package org.junit.jupiter.api does not exist` vagy `package org.springframework.boot.test.context does not exist` hibát kapod, a `pom.xml`-ből hiányzik a tesztfüggőség.

Add hozzá a `pom.xml` `<dependencies>` blokkjához (pl. a Lombok alá):

```xml
<!-- Testing: JUnit 5, Spring Boot Test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

**Mit biztosít a `spring-boot-starter-test`?**
* **JUnit 5:** Tesztek írása és futtatása.
* **Spring Boot Test:** A Spring alkalmazás tesztkörnyezetének indítása.
* **Mockito:** Objektumok és függőségek mockolása.
* **AssertJ:** Olvasható ellenőrzések (assertions).

---

## 6. Build és futtatás

A projekt tisztítása, csomagolása és a tesztek futtatása:
```powershell
.\mvnw.cmd clean package
```

Az alkalmazás elindítása:
```powershell
.\mvnw.cmd spring-boot:run
```
