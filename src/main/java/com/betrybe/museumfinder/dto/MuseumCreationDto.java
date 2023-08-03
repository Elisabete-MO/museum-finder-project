package com.betrybe.museumfinder.dto;

import com.betrybe.museumfinder.model.Coordinate;

/**
 * Museum Creation DTO class to be used by the service layer to create a new museum.
 */
public record MuseumCreationDto(String name, String description, String address,
                                String collectionType, String subject, String url,
                                Coordinate coordinate) {}
