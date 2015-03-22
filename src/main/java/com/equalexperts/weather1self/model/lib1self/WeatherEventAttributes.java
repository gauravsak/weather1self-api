package com.equalexperts.weather1self.model.lib1self;

import java.util.Arrays;
import java.util.List;

public interface WeatherEventAttributes {
    public static final List<String> OBJECT_TAGS = Arrays.asList("weather", "city");
    public static final List<String> ACTION_TAGS = Arrays.asList("record");
    public static final String PROPERTY = "temperature";
}
