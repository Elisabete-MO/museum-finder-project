package com.betrybe.museumfinder.evaluation;

import static com.betrybe.museumfinder.evaluation.utils.TestHelpers.createMockMuseum;
import static com.betrybe.museumfinder.evaluation.utils.TestHelpers.objectToMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.betrybe.museumfinder.database.MuseumFakeDatabase;
import com.betrybe.museumfinder.model.Coordinate;
import com.betrybe.museumfinder.model.Museum;
import com.betrybe.museumfinder.service.MuseumServiceInterface;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("Req 03-04")
class ServiceLayerTest {

  @MockBean
  MuseumFakeDatabase museumFakeDatabase;

  @Autowired
  MuseumServiceInterface service;

  @Test
  @DisplayName("03 - Método createMuseum da classe de serviço implementado")
  void testMuseumCreation() {
    // Regular test
    Museum mockMuseum = createMockMuseum(33L);
    Mockito.when(museumFakeDatabase.saveMuseum(any())).thenReturn(mockMuseum);

    Museum toSaveMuseum = createMockMuseum(null);
    Museum createdMuseum = service.createMuseum(toSaveMuseum);
    Museum expectedMuseum = mockMuseum;

    assertEquals(objectToMap(expectedMuseum), objectToMap(createdMuseum));

    Mockito.verify(museumFakeDatabase).saveMuseum(any());

    // Exception InvalidCoordinateException test
    toSaveMuseum.setCoordinate(new Coordinate(100, 200));
    testException(
        () -> service.createMuseum(toSaveMuseum),
        "com.betrybe.museumfinder.exception.InvalidCoordinateException",
        "Classe de serviço deve lançar uma unchecked exception se a coordenada for inválida"
    );
  }


  @Test
  @DisplayName("04 - Método getClosestMuseum da classe de serviço implementado")
  void testGetClosestMuseum() {
    testGetClosestMuseumOk();
    testGetClosestMuseumNotFoundException();
    testGetClosestMuseumInvalidCoordinateException();
  }

  private void testGetClosestMuseumInvalidCoordinateException() {
    testException(
        () -> service.getClosestMuseum(new Coordinate(100, 200), 10.0),
        "com.betrybe.museumfinder.exception.InvalidCoordinateException",
        "Classe de serviço deve lançar uma 'unchecked exception' se a coordenada for inválida"
    );
  }


  private void testGetClosestMuseumNotFoundException() {
    Mockito.reset(museumFakeDatabase);

    Mockito.when(museumFakeDatabase.getClosestMuseum(any(), any())).thenReturn(Optional.empty());

    Coordinate coordinate = new Coordinate(12.34, 34.56);
    testException(() -> service
            .getClosestMuseum(coordinate, 10.0),
        "com.betrybe.museumfinder.exception.MuseumNotFoundException",
        "Classe de serviço deve lançar uma 'unchecked exception' se nenhum museu encontrado"
    );

    Mockito.verify(museumFakeDatabase)
        .getClosestMuseum(eq(coordinate), eq(10.0));
  }

  private void testException(Executable executable, String expectedException, String message) {
    RuntimeException exception = assertThrows(
        RuntimeException.class,
        executable,
        message
    );
    assertEquals(
        expectedException,
        exception.getClass().getName()
    );
  }

  private void testGetClosestMuseumOk() {
    Museum museum = createMockMuseum(44L);
    Mockito.when(museumFakeDatabase.getClosestMuseum(any(), any()))
        .thenReturn(Optional.of(museum));
    Museum returnedMuseum = service
        .getClosestMuseum(museum.getCoordinate(), 10.0);
    Mockito.verify(museumFakeDatabase)
        .getClosestMuseum(eq(museum.getCoordinate()), eq(10.0));

    assertEquals(museum.getName(), returnedMuseum.getName());
  }

}
