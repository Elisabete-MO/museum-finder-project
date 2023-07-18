package com.betrybe.museumfinder.util;

import com.betrybe.museumfinder.model.Coordinate;

/**
 * Util class to calculate distance between coordinates.
 */
public class CoordinateUtil {

  public static Boolean isCoordinateValid(Coordinate coordinate) {
    return Math.abs(coordinate.latitude()) <= 90.0
        && Math.abs(coordinate.longitude()) <= 180.0;
  }

  /**
   * Calculates distance in Km between two coordinates.
   */
  public static Double coordinateDistance(Coordinate coor1, Coordinate coor2) {
    return CoordinateUtil
        .coordinateDistance(
            Math.toRadians(coor1.latitude()),
            Math.toRadians(coor2.longitude()),
            Math.toRadians(coor2.latitude()),
            Math.toRadians(coor2.longitude()));
  }

  /**
   * Calculates distance in Km between two coordinates using Harvesine formula. The parameters are
   * all in radians
   */
  private static double coordinateDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
    // Harvesine formula:
    // a = sin^2(latDif/2) + cos(lat1)*cos(lat2)*sin^2(lngDif/2)
    // c = 2 * atan2(sqrt(a), sqrt(1-a))
    // distance = c * 6371
    // Note: 6371 is Earth's radius in km

    double sinLatDif = Math.sin((lat2 - lat1) / 2);
    double sinLngDif = Math.sin((lng2 - lng1) / 2);

    double a = sinLatDif * sinLatDif
        + Math.cos(lat1) * Math.cos(lat2)
        * sinLngDif * sinLngDif;
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return c * 6371;
  }
}
