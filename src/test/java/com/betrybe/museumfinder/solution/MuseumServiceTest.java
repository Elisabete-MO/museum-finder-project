package com.betrybe.museumfinder.solution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.betrybe.museumfinder.database.MuseumFakeDatabase;
import com.betrybe.museumfinder.exception.MuseumNotFoundException;
import com.betrybe.museumfinder.model.Museum;
import com.betrybe.museumfinder.service.MuseumService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class MuseumServiceTest {

  @Test
  @DisplayName("Test get museum success with valid information")
  public void testGetMuseumSuccess() {
    Long museumId = 1L;
    MuseumFakeDatabase database = mock(MuseumFakeDatabase.class);
    Museum expectedMuseum = new Museum();
    when(database.getMuseum(museumId)).thenReturn(Optional.of(expectedMuseum));

    MuseumService service = new MuseumService(database);
    Museum resultMuseum = service.getMuseum(museumId);

    assertEquals(expectedMuseum, resultMuseum);
  }

  @Test
  @DisplayName("Test get museum with a invalid id")
  public void testGetMuseumNotFound() {
    Long museumId = 999L;
    MuseumFakeDatabase database = mock(MuseumFakeDatabase.class);
    when(database.getMuseum(museumId)).thenReturn(Optional.empty());

    MuseumService service = new MuseumService(database);

    assertThrows(MuseumNotFoundException.class, () -> service.getMuseum(museumId));
  }
}
