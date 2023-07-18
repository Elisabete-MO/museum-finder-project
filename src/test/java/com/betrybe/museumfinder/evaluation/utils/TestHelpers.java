package com.betrybe.museumfinder.evaluation.utils;

import com.betrybe.museumfinder.model.Coordinate;
import com.betrybe.museumfinder.model.Museum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class TestHelpers {

  public static Map<?, ?> objectToMap(Object obj) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(obj, Map.class);
  }

  public static String objectToJson(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static Museum createMockMuseum(Long id) {
    Museum museum = new Museum();
    museum.setId(id);
    museum.setName("museum name");
    museum.setCoordinate(new Coordinate(12.345, -54.321));

    return museum;
  }
}
