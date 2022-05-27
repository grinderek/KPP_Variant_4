package com.web.kpp.controller;

import com.web.kpp.dto.TriangleListDto;
import com.web.kpp.entity.Triangle;
import com.web.kpp.entity.TriangleIdentification;
import com.web.kpp.service.TriangleIdentificationLogic;
import com.web.kpp.service.TriangleIdentificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class TriangleIdentificationController {
    private static final Logger logger = LogManager.getLogger(ResponseEntityExceptionHandler.class);
    private static final String VALUE_MUST_BE_POSITIVE = "Value must be positive!";

    @Autowired
    private TriangleIdentificationService triangleIdentificationService;

    @Autowired
    private TriangleIdentificationLogic triangleIdentificationLogic;

    @GetMapping("/identification")
    public ResponseEntity<Object> triangleParams(
            @RequestParam(name = "side1", required = true) @Positive(message = VALUE_MUST_BE_POSITIVE)
                    int side1,
            @RequestParam(name = "side2", required = true) @Positive(message = VALUE_MUST_BE_POSITIVE)
                    int side2,
            @RequestParam(name = "side3", required = true) @Positive(message = VALUE_MUST_BE_POSITIVE)
                    int side3) {
        logger.info("GET /identification");

        return ResponseEntity.ok(triangleIdentificationService.identify(side1, side2, side3));
    }

    @GetMapping("/cache")
    public Map<Triangle, TriangleIdentification> getCache() {
        logger.info("GET /cache");
        return triangleIdentificationService.getCache();
    }

    @PostMapping("/triangleList")
    public ResponseEntity<Object> calculateBulkParams(@Valid @RequestBody List<Triangle> bodyList) {
        List<TriangleIdentification> resultList = new LinkedList<>();
        if (bodyList.isEmpty()) {
            return ResponseEntity.ok(new TriangleListDto(null, null, null, null));
        }
        bodyList.forEach((currentElement) -> {
            try {
                resultList.add(triangleIdentificationLogic.calculateResult(currentElement));
            } catch (IllegalArgumentException e) {
                logger.error("Error while PostMapping!");
            }
        });

        logger.info("POST /triangleList");
        Integer sum = triangleIdentificationLogic.calculateSum(bodyList);
        Integer max = triangleIdentificationLogic.findMax(bodyList);
        Integer min = triangleIdentificationLogic.findMin(bodyList);

        TriangleListDto triangleListDto = new TriangleListDto(resultList, sum, max, min);
        return ResponseEntity.ok(triangleListDto);
    }
}
