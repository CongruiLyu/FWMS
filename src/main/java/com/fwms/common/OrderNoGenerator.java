package com.fwms.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class OrderNoGenerator {

    private static final AtomicLong SEQ = new AtomicLong(0);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generate(String prefix) {
        return prefix + LocalDateTime.now().format(FMT) + String.format("%04d", SEQ.incrementAndGet() % 10000);
    }
}
