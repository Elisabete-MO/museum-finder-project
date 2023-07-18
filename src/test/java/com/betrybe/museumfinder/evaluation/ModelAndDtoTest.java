package com.betrybe.museumfinder.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Req 01")
public class ModelAndDtoTest {

  @Test
  @DisplayName("01 - Classes de Modelo e DTO criadas")
  void testModelAndDtoClasses() {
    Map<String, Map<String, String>> expectedMap = Map.of(
        "com.betrybe.museumfinder.model.Museum", Map.of(
            "id", Long.class.getName(),
            "name", String.class.getName(),
            "description", String.class.getName(),
            "address", String.class.getName(),
            "collectionType", String.class.getName(),
            "subject", String.class.getName(),
            "url", String.class.getName(),
            "coordinate", "com.betrybe.museumfinder.model.Coordinate",
            "legacyId", Long.class.getName()
        ),
        "com.betrybe.museumfinder.dto.MuseumDto", Map.of(
            "id", Long.class.getName(),
            "name", String.class.getName(),
            "description", String.class.getName(),
            "address", String.class.getName(),
            "collectionType", String.class.getName(),
            "subject", String.class.getName(),
            "url", String.class.getName(),
            "coordinate", "com.betrybe.museumfinder.model.Coordinate"
        ),
        "com.betrybe.museumfinder.dto.MuseumCreationDto", Map.of(
            "name", String.class.getName(),
            "description", String.class.getName(),
            "address", String.class.getName(),
            "collectionType", String.class.getName(),
            "subject", String.class.getName(),
            "url", String.class.getName(),
            "coordinate", "com.betrybe.museumfinder.model.Coordinate"
        )
    );

    try {
      for (String className : expectedMap.keySet()) {
        Class<?> classToTest = Class.forName(className);
        assertEquals(
            expectedMap.get(className),
            Arrays.stream(classToTest.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, (f) -> f.getType().getName())),
            "Os atributos precisam estar definidos e com os tipos corretos para a classe"
                + className
        );
      }

      assertTrue(
          Class.forName("com.betrybe.museumfinder.dto.MuseumDto").isRecord(),
          "O MuseumDto devem ser implementado como um 'record'"
      );

      assertTrue(
          Class.forName("com.betrybe.museumfinder.dto.MuseumCreationDto").isRecord(),
          "O MuseumCreationDto devem ser implementado como um 'record'"
      );

    } catch (ClassNotFoundException e) {
      fail("As classes de modelo e DTO precisam existir: " + e.getMessage());
    }

  }
}
