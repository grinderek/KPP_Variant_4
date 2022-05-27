package com.web.kpp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.kpp.entity.TriangleIdentification;

import java.util.List;

public class TriangleListDto {
    @JsonProperty
    private List<TriangleIdentification> list;
    @JsonProperty
    private Integer sumOfSides;
    @JsonProperty
    private Integer maxPerimeter;
    @JsonProperty
    private Integer minPerimeter;

    public TriangleListDto(List<TriangleIdentification> list, Integer sumOfSides, Integer maxPerimeter, Integer minPerimeter) {
        this.list = list;
        this.sumOfSides = sumOfSides;
        this.maxPerimeter = maxPerimeter;
        this.minPerimeter = minPerimeter;
    }
}
