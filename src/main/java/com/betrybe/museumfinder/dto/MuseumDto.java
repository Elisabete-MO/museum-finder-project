package com.betrybe.museumfinder.dto;

import com.betrybe.museumfinder.model.Coordinate;

/**
 * Museum DTO class to be returned by the API endpoints and used by the service layer.
 */
public record MuseumDto(Long id, String name, String description, String address,
                        String collectionType, String subject, String url,
                        Coordinate coordinate) {}
