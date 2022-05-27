package com.web.kpp.repository;

import com.web.kpp.entity.Triangle;
import com.web.kpp.entity.TriangleIdentification;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class TriangleInMemoryCache {
  private final Map<Triangle, TriangleIdentification> triangleCache = new LinkedHashMap<>();

  public boolean findByKey(Triangle key) {
    return triangleCache.containsKey(key);
  }

  public TriangleIdentification putToMap(Triangle triangle, TriangleIdentification triangleIdentification) {
    triangleCache.put(triangle, triangleIdentification);
    return triangleIdentification;
  }

  public TriangleIdentification getParameters(Triangle key) {
    return triangleCache.get(key);
  }

  public Map<Triangle, TriangleIdentification> getTriangleCache() {
    return triangleCache;
  }
}
