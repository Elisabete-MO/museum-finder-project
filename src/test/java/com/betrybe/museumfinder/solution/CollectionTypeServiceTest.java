package com.betrybe.museumfinder.solution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.betrybe.museumfinder.database.MuseumFakeDatabase;
import com.betrybe.museumfinder.dto.CollectionTypeCount;
import com.betrybe.museumfinder.service.CollectionTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CollectionTypeServiceTest {

  private MuseumFakeDatabase mockDatabase;
  private CollectionTypeService collectionTypeService;

  @BeforeEach
  void setUp() {
    mockDatabase = mock(MuseumFakeDatabase.class);
    collectionTypeService = new CollectionTypeService(mockDatabase);
  }

  @Test
  @DisplayName("Test countByCollectionTypes with one type")
  void testCountByCollectionTypesWithOneType() {
    // Arrange
    String type = "Art";
    long count = 5;
    CollectionTypeCount expected = new CollectionTypeCount(new String[]{"Art"}, count);
    when(mockDatabase.countByCollectionType(type)).thenReturn(count);

    // Act
    CollectionTypeCount result = collectionTypeService.countByCollectionTypes(type);

    // Assert
    assertEquals(1, result.collectionTypes().length);
    assertEquals(type, result.collectionTypes()[0]);
    assertEquals(count, result.count());
  }

  @Test
  @DisplayName("Test countByCollectionTypes with multiple types")
  void testCountByCollectionTypesWithMultipleTypes() {
    String[] types = {"Art", "History", "Science"};
    long[] counts = {5, 3, 7};

    for (int i = 0; i < types.length; i++) {
      when(mockDatabase.countByCollectionType(types[i])).thenReturn(counts[i]);
    }

    String typesList = String.join(",", types);
    CollectionTypeCount result = collectionTypeService.countByCollectionTypes(typesList);

    assertEquals(types.length, result.collectionTypes().length);
    assertEquals(types[0], result.collectionTypes()[0]);
    assertEquals(types[1], result.collectionTypes()[1]);
    assertEquals(types[2], result.collectionTypes()[2]);
    assertEquals(counts[0] + counts[1] + counts[2], result.count());
  }
}
