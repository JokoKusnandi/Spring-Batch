package com.joko.batchprocessing;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.core.convert.converter.Converter;

// Definisikan DecimalNumberConverter
public class DecimalNumberConverter implements Converter<String, BigDecimal> {
    private final int scale;

    public DecimalNumberConverter(int scale) {
        this.scale = scale;
    }

    @Override
    public BigDecimal convert(String source) {
        if (source == null) {
            return null;
        }
        return new BigDecimal(source.replaceAll(",", ".")).setScale(scale, RoundingMode.HALF_UP);
    }
}
