package com.betrybe.museumfinder.database;

import static com.betrybe.museumfinder.util.CoordinateUtil.coordinateDistance;

import com.betrybe.museumfinder.model.Coordinate;
import com.betrybe.museumfinder.model.Museum;
import com.betrybe.museumfinder.util.MuseumLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Class that simulates a fake database/repository.
 */
@Component
public class MuseumFakeDatabase {

  private final Map<Long, Museum> museums;

  MuseumFakeDatabase(MuseumLoader museumsLoader) {
    List<Museum> museumsList = museumsLoader.fromClasspath("data/museums.json");

    for (int i = 0; i < museumsList.size(); i++) {
      long id = (long) (i + 1);
      museumsList.get(i).setId(id);
      museumsList.get(i).setLegacyId(id * 1000);
    }

    museums = museumsList.stream().collect(Collectors.toMap(Museum::getId, Function.identity()));
  }

  public List<Museum> getAllMuseums() {
    return museums.values().stream().toList();
  }

  public Optional<Museum> getMuseum(Long id) {
    return Optional.ofNullable(museums.get(id));
  }

  /**
   * Takes an object, assigns it an ID and saves to the fake database.
   */
  public Museum saveMuseum(Museum museum) {
    Museum newMuseum = deepCopy(museum);

    if (newMuseum.getId() == null) {
      Long newId = museums.keySet().stream().max(Long::compareTo).orElse(0L) + 1;
      newMuseum.setId(newId);
    }

    museums.put(newMuseum.getId(), newMuseum);

    return newMuseum;
  }

  public Museum deleteMuseum(Long id) {
    return museums.remove(id);
  }

  private Museum deepCopy(Museum museum) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(objectMapper.writeValueAsString(museum), Museum.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get the closest museum to a coordinate within a max distance.
   */
  public Optional<Museum> getClosestMuseum(Coordinate coordinate, Double maxDistance) {
    return museums.values().stream()
        // Map museum to entries with distance
        .map(museum -> Map.entry(museum, coordinateDistance(museum.getCoordinate(), coordinate)))
        // filter based on distance (map value)
        .filter(entry -> entry.getValue() <= maxDistance)
        // sort by distance (map value)
        .sorted(Comparator.comparingDouble(Entry::getValue))
        // map entry back to Museum
        .map(Entry::getKey)
        // get first, if it exists
        .findFirst();
  }

  /**
   * Count number of museums that match the collection type.
   */
  public long countByCollectionType(String collectionType) {
    return getAllMuseums().stream()
        .filter((m) -> m.getCollectionType().toLowerCase().contains(collectionType))
        .count();
  }
}
