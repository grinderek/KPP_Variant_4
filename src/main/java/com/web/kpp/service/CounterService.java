package com.web.kpp.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CounterService {
    private int counter;

    public synchronized int increment() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return ++counter;
    }
}
