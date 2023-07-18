package com.betrybe.museumfinder.service;

import com.betrybe.museumfinder.database.MuseumFakeDatabase;
import com.betrybe.museumfinder.dto.CollectionTypeCount;
import org.springframework.stereotype.Service;

/**
 * CollectionType service class.
 */
@Service
public class CollectionTypeService {

  MuseumFakeDatabase database;

  public CollectionTypeService(MuseumFakeDatabase database) {
    this.database = database;
  }

  /**
   * Count Museums that match collection types.
   */
  public CollectionTypeCount countByCollectionTypes(String typesList) {
    String[] collectionTypes = splitTypesByComma(typesList);

    long totalCount = 0;

    if (collectionTypes.length == 1) {
      totalCount = database.countByCollectionType(collectionTypes[0]);
    } else {
      for (String collectionType : collectionTypes) {
        totalCount = totalCount + database.countByCollectionType(collectionType);
      }
    }

    if (totalCount > 0) {
      return new CollectionTypeCount(collectionTypes, totalCount);
    } else {
      return new CollectionTypeCount(collectionTypes, 0);
    }
  }

  private String[] splitTypesByComma(String collections) {
    String sep = ",";
    String[] collectionTypes;

    if (collections.contains(sep)) {
      collectionTypes = collections.split(sep);
    } else {
      collectionTypes = new String[]{collections};
    }

    for (int i = 0; i < collectionTypes.length; i++) {
      collectionTypes[i] = collectionTypes[i].strip();
    }

    return collectionTypes;
  }
}
