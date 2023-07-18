package com.betrybe.museumfinder.evaluation;

import static com.betrybe.museumfinder.util.ModelDtoConverter.modelToDto;
import static com.betrybe.museumfinder.evaluation.utils.TestHelpers.createMockMuseum;
import static com.betrybe.museumfinder.evaluation.utils.TestHelpers.objectToJson;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.betrybe.museumfinder.dto.MuseumDto;
import com.betrybe.museumfinder.model.Coordinate;
import com.betrybe.museumfinder.model.Museum;
import com.betrybe.museumfinder.service.MuseumServiceInterface;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Req 07")
public class ControllerErrorTest {
  @MockBean
  MuseumServiceInterface service;

  MockMvc mockMvc;

  @BeforeEach
  public void setup(WebApplicationContext wac) {
    // We need this to make sure the response body is in UTF-8,
    // since we're testing raw strings
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(wac)
        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
        .build();
  }

  @Test
  @DisplayName("07 - Classe com @ControllerAdvice tratando erros")
  void testControllerErrorCodes() throws Exception {
    Exception runtimeException = new RuntimeException();
    Exception invalidCoordinateException = createInvalidCoordinateException();
    Exception museumNotFoundException = createMuseumNotFoundException();

    testGetForException(runtimeException, HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno!");
    testGetForException(invalidCoordinateException, HttpStatus.BAD_REQUEST, "Coordenada inválida!");
    testGetForException(museumNotFoundException, HttpStatus.NOT_FOUND, "Museu não encontrado!");

    testPostForException(runtimeException, HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno!");
    testPostForException(invalidCoordinateException, HttpStatus.BAD_REQUEST, "Coordenada inválida!");
  }

  private void testGetForException(Exception exception, HttpStatus status, String expectedBody) throws Exception {
    Mockito.reset(service);
    Mockito.when(service.getClosestMuseum(any(Coordinate.class), anyDouble())).thenThrow(exception);
    mockMvc.perform(
            get("/museums/closest?lat=12.34&lng=23.45&max_dist_km=10")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is(status.value()))
        .andExpect(content().string(containsString(expectedBody)));

    Mockito.verify(service).getClosestMuseum(any(), any());
  }

  private void testPostForException(Exception exception, HttpStatus status, String expectedBody) throws Exception {
    Mockito.reset(service);
    Mockito.when(service.createMuseum(any())).thenThrow(exception);

    performCreationPost(createMockMuseum(null))
        .andExpect(status().is(status.value()))
        .andExpect(content().string(containsString(expectedBody)));

    Mockito.verify(service).createMuseum(any());
  }

  private Exception createInvalidCoordinateException() {
    return (Exception) instantiateClassByName(
        "com.betrybe.museumfinder.exception.InvalidCoordinateException");
  }
  private Exception createMuseumNotFoundException() {
    return (Exception) instantiateClassByName(
        "com.betrybe.museumfinder.exception.MuseumNotFoundException");
  }

  private Object instantiateClassByName(String className) {
    try {
      Class<?> c = Class.forName(className);
      Constructor<?> constructor = c.getConstructor();
      return constructor.newInstance();
    } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
             InstantiationException | IllegalAccessException e) {
      fail("Classe precisa estar implementada corretamente para este teste executar: " + className);
      return null;
    }
  }

  private ResultActions performCreationPost(Museum museum) throws Exception {
    MuseumDto museumDto = modelToDto(museum);

    return mockMvc.perform(post("/museums")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(museumDto)));
  }
}
