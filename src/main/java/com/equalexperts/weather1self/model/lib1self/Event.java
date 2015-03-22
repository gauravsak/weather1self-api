package com.equalexperts.weather1self.model.lib1self;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/*
    {
        "objectTags": [“teeth”,”oral”,”mouth”],
        “actionTags”: [“brush”,”clean”],
        “properties”: {
            “duration”: 120,
            “pressureKgSqm”: 0.15,
            “make”:”Brushomatic5000”,
            “color”:”red”
        },
        “dateTime”: “2014-11-11T22:30:00.000Z”
    }
*/
public class Event {
    private List<String> objectTags;
    private List<String> actionTags;
    private Map<String, Object> properties;
    private final String dateTime;

    public Event(List<String> objectTags, List<String> actionTags, Map<String, Object> properties,
                 String dateTime) {
        this.objectTags = objectTags;
        this.actionTags = actionTags;
        this.properties = properties;
        this.dateTime = dateTime;
    }

    public static JSONObject toJSON(Event event) {
        JSONObject eventJSON = new JSONObject();
        eventJSON.put("objectTags", event.objectTags);
        eventJSON.put("actionTags", event.actionTags);
        JSONObject propertiesJSON = new JSONObject();
        for (Map.Entry<String, Object> property : event.properties.entrySet()) {
            propertiesJSON.put(property.getKey(), property.getValue());
        }
        eventJSON.put("properties", propertiesJSON);
        eventJSON.put("dateTime", event.dateTime);
        return eventJSON;
    }
}
