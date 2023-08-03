package com.betrybe.museumfinder.service;

import com.betrybe.museumfinder.database.MuseumFakeDatabase;
import com.betrybe.museumfinder.exception.InvalidCoordinateException;
import com.betrybe.museumfinder.model.Coordinate;
import com.betrybe.museumfinder.model.Museum;
import com.betrybe.museumfinder.util.CoordinateUtil;
import org.springframework.stereotype.Service;

@Service
public class MuseumService implements MuseumServiceInterface{
  private MuseumFakeDatabase database;

  public MuseumService(MuseumFakeDatabase database) {
    this.database = database;
  }

  @Override
  public Museum createMuseum(Museum museum) {
    double latitude = museum.getCoordinate().latitude();
    double longitude = museum.getCoordinate().longitude();
    validateCoordinates(latitude, longitude);
    return database.saveMuseum(museum);
  }

  @Override
  public Museum getClosestMuseum(Coordinate coordinate, Double maxDistance) {
    return null;
  }

  @Override
  public Museum getMuseum(Long id) {
    return null;
  }

  private static void validateCoordinates(double latitude, double longitude) {
    Coordinate coordinate = new Coordinate(latitude, longitude);
    if (!CoordinateUtil.isCoordinateValid(coordinate)) {
      throw new InvalidCoordinateException("Invalid data.");
    }
  }
}
