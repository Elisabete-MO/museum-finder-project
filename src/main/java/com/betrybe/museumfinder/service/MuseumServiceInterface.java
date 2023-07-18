package com.betrybe.museumfinder.service;

import com.betrybe.museumfinder.model.Coordinate;
import com.betrybe.museumfinder.model.Museum;

/**
 * Interface for Museum service class.
 */
public interface MuseumServiceInterface {

  Museum getClosestMuseum(Coordinate coordinate, Double maxDistance);

  Museum createMuseum(Museum museum);

  Museum getMuseum(Long id);
}
