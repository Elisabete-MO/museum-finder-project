package com.betrybe.museumfinder.evaluation;

import static com.betrybe.museumfinder.util.ModelDtoConverter.modelToDto;
import static com.betrybe.museumfinder.evaluation.utils.TestHelpers.createMockMuseum;
import static com.betrybe.museumfinder.evaluation.utils.TestHelpers.objectToJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.betrybe.museumfinder.dto.MuseumDto;
import com.betrybe.museumfinder.model.Museum;
import com.betrybe.museumfinder.service.MuseumServiceInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Req 05-06")
public class ControllerLayerTest {

  @MockBean
  MuseumServiceInterface museumsServiceInterface;

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("05 - Rota POST /museums implementada")
  void testMuseumCreation() throws Exception {
    Museum museum = createMockMuseum(33L);
    Mockito.when(museumsServiceInterface.createMuseum(any())).thenReturn(museum);

    MuseumDto toSaveMuseum = modelToDto(createMockMuseum(null));

    mockMvc.perform(post("/museums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectToJson(toSaveMuseum)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value(museum.getName()))
        .andExpect(jsonPath("$.address").value(museum.getAddress()))
        .andExpect(jsonPath("$.description").value(museum.getDescription()))
        .andExpect(jsonPath("$.collectionType").value(museum.getCollectionType()))
        .andExpect(jsonPath("$.subject").value(museum.getSubject()))
        .andExpect(jsonPath("$.url").value(museum.getUrl()))
        .andExpect(jsonPath("$.coordinate.latitude").value(museum.getCoordinate().latitude()))
        .andExpect(jsonPath("$.coordinate.longitude").value(museum.getCoordinate().longitude()));

    Mockito.verify(museumsServiceInterface).createMuseum(any());
  }

  @Test
  @DisplayName("06 - Rota GET /museums/closest implementada")
  void testGetClosestMuseum() throws Exception {
    Museum museum = createMockMuseum(11L);
    Mockito.when(museumsServiceInterface.getClosestMuseum(any(), any())).thenReturn(museum);

    mockMvc.perform(
          get("/museums/closest?lat=12.34&lng=23.45&max_dist_km=10")
          .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name").value(museum.getName()))
        .andExpect(jsonPath("$.coordinate.latitude").value(museum.getCoordinate().latitude()))
        .andExpect(jsonPath("$.coordinate.longitude").value(museum.getCoordinate().longitude()));

    Mockito.verify(museumsServiceInterface).getClosestMuseum(any(), any());

    Mockito.reset(museumsServiceInterface);
  }
}
