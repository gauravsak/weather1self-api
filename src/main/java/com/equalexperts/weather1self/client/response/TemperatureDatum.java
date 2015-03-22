package com.equalexperts.weather1self.client.response;

import com.equalexperts.weather1self.model.lib1self.Event;

import java.math.BigDecimal;

public interface TemperatureDatum {
    BigDecimal getTemperature();
    String getISOTimestamp();
    Event to1SelfEvent();
}
