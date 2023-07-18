package com.betrybe.museumfinder.controller;

import com.betrybe.museumfinder.dto.CollectionTypeCount;
import com.betrybe.museumfinder.service.CollectionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CollectionType controller class.
 */
@RestController
@RequestMapping("/collections")
public class CollectionTypeController {

  private final CollectionTypeService service;

  @Autowired
  CollectionTypeController(CollectionTypeService service) {
    this.service = service;
  }

  /**
   * Route to count number of Museums with certain collection types.
   */
  @GetMapping("/count/{typesList}")
  public ResponseEntity<CollectionTypeCount>
      getCollectionTypesCount(@PathVariable String typesList) {
    CollectionTypeCount result = service.countByCollectionTypes(typesList);

    if (result.count() > 0) {
      return ResponseEntity.ok(result);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
