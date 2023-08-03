package com.betrybe.museumfinder.controller;

import com.betrybe.museumfinder.dto.MuseumDto;
import com.betrybe.museumfinder.model.Museum;
import com.betrybe.museumfinder.service.MuseumServiceInterface;
import com.betrybe.museumfinder.util.ModelDtoConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling museum related operations.
 */
@RestController
@RequestMapping("/museums")
public class MuseumController {
  MuseumServiceInterface museumService;

  public MuseumController(MuseumServiceInterface museumService) {
    this.museumService = museumService;
  }

  public MuseumServiceInterface getMuseumService() {
    return museumService;
  }

  /** Route to create a new Museum.
   *
   * @param newMuseum MuseumDto object containing the data to be used to create a new Museum.
   * @return ResponseEntity with the created Museum.
   */
  @PostMapping
  public ResponseEntity<Museum> createMuseum(@RequestBody MuseumDto newMuseum) {
    Museum museum = ModelDtoConverter.dtoToModel(newMuseum);
    Museum createdMuseum = museumService.createMuseum(museum);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdMuseum);
  }
}
