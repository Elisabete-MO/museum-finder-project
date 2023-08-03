package com.betrybe.museumfinder.exception;

/**
 * This exception is thrown when the given museum is not found.
 */
public class MuseumNotFoundException extends RuntimeException {
  public MuseumNotFoundException(String message) {
    super(message);
  }

}
