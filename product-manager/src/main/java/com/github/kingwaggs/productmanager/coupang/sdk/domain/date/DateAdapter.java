package com.github.kingwaggs.productmanager.coupang.sdk.domain.date;

import com.github.kingwaggs.productmanager.coupang.sdk.service.CoupangMarketPlaceApiClient;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private final CoupangMarketPlaceApiClient apiClient;

    public DateAdapter(CoupangMarketPlaceApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return (JsonElement)(src == null ? JsonNull.INSTANCE : new JsonPrimitive(this.apiClient.formatDatetime(src)));
    }

    public Date deserialize(JsonElement json, Type date, JsonDeserializationContext context) throws JsonParseException {
        String str = json.getAsJsonPrimitive().getAsString();

        try {
            return this.apiClient.parseDateOrDatetime(str);
        } catch (RuntimeException var6) {
            throw new JsonParseException(var6);
        }
    }
}
