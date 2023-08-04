package com.betrybe.museumfinder.solution;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.betrybe.museumfinder.controller.CollectionTypeController;
import com.betrybe.museumfinder.dto.CollectionTypeCount;
import com.betrybe.museumfinder.service.CollectionTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(CollectionTypeController.class)
public class CollectionTypeControllerTest {

  @MockBean
  private CollectionTypeService collectionTypeService;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp(WebApplicationContext wac) {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(wac)
        .build();
  }

  @Test
  @DisplayName("Test getCollectionTypesCount with valid types")
  void testGetCollectionTypesCountWithValidTypes() throws Exception {
    String types = "Art,History,Science";
    long totalCount = 15;

    CollectionTypeCount result = new CollectionTypeCount(types.split(","), totalCount);

    when(collectionTypeService.countByCollectionTypes(anyString())).thenReturn(result);

    mockMvc.perform(
            get("/collections/count/{typesList}", types)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Art")))
        .andExpect(content().string(containsString("History")))
        .andExpect(content().string(containsString("Science")))
        .andExpect(content().string(containsString("15")));
  }

  @Test
  @DisplayName("Test getCollectionTypesCount with empty types")
  void testGetCollectionTypesCountWithEmptyTypes() throws Exception {
    String types = "";
    CollectionTypeCount result = new CollectionTypeCount(new String[]{}, 0);

    when(collectionTypeService.countByCollectionTypes(anyString())).thenReturn(result);

    mockMvc.perform(
            get("/collections/count/{typesList}", types)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
  }
}

