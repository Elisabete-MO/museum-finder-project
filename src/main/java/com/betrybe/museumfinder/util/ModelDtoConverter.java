package com.betrybe.museumfinder.util;

import com.betrybe.museumfinder.dto.MuseumCreationDto;
import com.betrybe.museumfinder.dto.MuseumDto;
import com.betrybe.museumfinder.model.Museum;

/**
 * Utility class to convert between model and dto.
 */
public class ModelDtoConverter {

  /**
   * Convert from model to dto.
   */
  public static MuseumDto modelToDto(Museum museum) {
    return new MuseumDto(
        museum.getId(),
        museum.getName(),
        museum.getDescription(),
        museum.getAddress(),
        museum.getCollectionType(),
        museum.getSubject(),
        museum.getUrl(),
        museum.getCoordinate()
    );
  }

  /**
   * Convert from dto to model.
   */
  public static Museum dtoToModel(MuseumDto dto) {
    Museum museum = new Museum();

    museum.setId(dto.id());
    museum.setName(dto.name());
    museum.setDescription(dto.description());
    museum.setAddress(dto.address());
    museum.setCollectionType(dto.collectionType());
    museum.setSubject(dto.subject());
    museum.setUrl(dto.url());
    museum.setCoordinate(dto.coordinate());

    return museum;
  }

  /**
   * Convert from creation dto to model.
   */
  public static Museum dtoToModel(MuseumCreationDto dto) {
    Museum museum = new Museum();

    museum.setName(dto.name());
    museum.setDescription(dto.description());
    museum.setAddress(dto.address());
    museum.setCollectionType(dto.collectionType());
    museum.setSubject(dto.subject());
    museum.setUrl(dto.url());
    museum.setCoordinate(dto.coordinate());

    return museum;
  }
}
