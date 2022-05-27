package com.web.kpp.service;

import com.web.kpp.entity.Triangle;
import com.web.kpp.entity.TriangleIdentification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class TriangleIdentificationLogic {
    private static final Logger logger = LogManager.getLogger(TriangleIdentificationLogic.class);

    @Autowired
    private TriangleIdentificationService triangleService;

    public TriangleIdentification calculateResult(Triangle requestParams) throws IllegalArgumentException{
        int side1 = requestParams.getSide1();
        int side2 = requestParams.getSide2();
        int side3 = requestParams.getSide3();
        logger.info("Successfully calculateResult!");
        return triangleService.identify(side1, side2, side3);
    }

    public Integer calculateSum(List<Triangle> resultList) {
        Integer sum = null;
        if(!resultList.isEmpty()) {
            sum = resultList.stream().mapToInt(Triangle::parseInt).sum();
            logger.info("Successfully calculateSum!");
        }
        return sum;
    }

    public Integer findMax(List<Triangle> resultList) {
        Integer max = null;
        if(!resultList.isEmpty()) {
            max = resultList.stream().mapToInt(Triangle::parseInt).max().getAsInt();
            logger.info("Successfully findMax!");
        }

        return max;
    }

    public Integer findMin(List<Triangle> resultList) {
        Integer min = null;
        if(!resultList.isEmpty()) {
            min = resultList.stream().mapToInt(Triangle::parseInt).min().getAsInt();
            logger.info("Successfully findMin!");
        }

        return min;
    }
}
