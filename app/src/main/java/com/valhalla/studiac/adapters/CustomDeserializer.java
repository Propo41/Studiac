package com.valhalla.studiac.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.valhalla.studiac.holders.ListItem;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CustomDeserializer implements JsonDeserializer<ListItem> {

    private String typeName;
    private Gson gson;
    private Map<String, Class<? extends ListItem>> typeRegistry;


    public CustomDeserializer(String typeName) {
        this.typeName = typeName;
        this.gson = new Gson();
        this.typeRegistry = new HashMap<>();
    }


    public void registerBarnType(String contentTypeName, Class<? extends ListItem> type) {
        typeRegistry.put(contentTypeName, type);
    }


    @Override
    public ListItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject contentObject = json.getAsJsonObject();
        JsonElement typeElement = contentObject.get(typeName);

        Class<? extends ListItem> type = typeRegistry.get(typeElement.getAsString());
        return gson.fromJson(contentObject, type);
    }
}
