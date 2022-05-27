package com.web.kpp.service;

import com.web.kpp.entity.Triangle;
import com.web.kpp.exceptions.TriangleDoesNotExistException;
import com.web.kpp.repository.TriangleInMemoryCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web.kpp.entity.TriangleIdentification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;

@Service
public class TriangleIdentificationService {
  private static final Logger logger = LogManager.getLogger(TriangleIdentificationService.class);

  @Autowired
  private CounterService counterService;

  @Autowired
  private TriangleInMemoryCache hashMap;

  private void validateTriangle(int side1, int side2, int side3) {
    if (side1 + side2 <= side3 || side1 + side3 <= side2 || side2 + side3 <= side1) {
      logger.error("Identify error! Wrong sides length.");
      throw new TriangleDoesNotExistException("Triangle does not exist!");
    }
  }

  public TriangleIdentification identify(int side1, int side2, int side3) {
    Triangle triangle = new Triangle(side1, side2, side3);
    if (hashMap.findByKey(triangle)) {
      logger.info("get hashMap");
      return hashMap.getParameters(triangle);
    }

    validateTriangle(side1, side2, side3);

    TriangleIdentification triangleIdentification = new TriangleIdentification();

    triangleIdentification.setIsEquilateral(side1 == side2 && side2 == side3);
    triangleIdentification.setIsIsosceles(side1 == side2 || side2 == side3 || side1 == side3);
    triangleIdentification.setIsRectangular(
        Math.pow(side1, 2) == Math.pow(side2, 2) + Math.pow(side3, 2)
            || Math.pow(side2, 2) == Math.pow(side1, 2) + Math.pow(side3, 2)
            || Math.pow(side3, 2) == Math.pow(side1, 2) + Math.pow(side2, 2));

    triangleIdentification.setCounter(counterService.increment());
    logger.info("Successful identify!");

    logger.info("put to hashMap");
    return hashMap.putToMap(triangle, triangleIdentification);
  }

  public Map<Triangle, TriangleIdentification> getCache() {
    return hashMap.getTriangleCache();
  }
}
