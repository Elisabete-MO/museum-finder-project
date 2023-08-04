package com.betrybe.museumfinder.controller;

import com.betrybe.museumfinder.dto.MuseumDto;
import com.betrybe.museumfinder.model.Coordinate;
import com.betrybe.museumfinder.model.Museum;
import com.betrybe.museumfinder.service.MuseumServiceInterface;
import com.betrybe.museumfinder.util.ModelDtoConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  /** Route to get a closest Museum.
   *
   * @param lat Latitude of the coordinate.
   * @param lng Longitude of the coordinate.
   * @param maxDistKm Maximum distance in kilometers.
   * @return ResponseEntity with the closest Museum.
   */
  @GetMapping("/closest")
  public ResponseEntity<MuseumDto> getClosestMuseum(@RequestParam double lat,
                               @RequestParam double lng,
                               @RequestParam("max_dist_km") double maxDistKm) {
    Coordinate coordinate = new Coordinate(lat, lng);
    Museum museum = museumService.getClosestMuseum(coordinate, maxDistKm);
    return ResponseEntity.ok(ModelDtoConverter.modelToDto(museum));
  }

  /** Route to get a Museum by id.
   *
   * @param id Museum id.
   * @return ResponseEntity with the Museum.
   */
  @GetMapping("/{id}")
  public ResponseEntity<Museum> getFindMuseumById(@RequestParam long id) {
    Museum museum = museumService.getMuseum(id);
    return ResponseEntity.ok(museum);
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
