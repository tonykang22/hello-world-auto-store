package com.github.kingwaggs.csmanager.sdk.coupang.domain;

import com.github.kingwaggs.csmanager.sdk.coupang.domain.date.DateAdapter;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.date.DateTimeTypeAdapter;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.date.LocalDateTypeAdapter;
import com.github.kingwaggs.csmanager.sdk.coupang.service.CoupangMarketPlaceApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Date;

public class JSON {
    private CoupangMarketPlaceApiClient apiClient;
    private Gson gson;

    public JSON(CoupangMarketPlaceApiClient apiClient) {
        this.apiClient = apiClient;
        this.gson = (new GsonBuilder()).registerTypeAdapter(Date.class, new DateAdapter(apiClient)).registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter()).registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter()).create();
    }

    public Gson getGson() {
        return this.gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public String serialize(Object obj) {
        return this.gson.toJson(obj);
    }

    public <T> T deserialize(String body, Type returnType) {

        try {
            if (this.apiClient.isLenientOnJson()) {
                JsonReader jsonReader = new JsonReader(new StringReader(body));
                jsonReader.setLenient(true);
                return this.gson.fromJson(jsonReader, returnType);
            } else {
                return this.gson.fromJson(body, returnType);
            }
        } catch (JsonParseException var4) {
            if (returnType.equals(String.class)) {
                return (T) body;
            } else if (returnType.equals(Date.class)) {
                return (T) this.apiClient.parseDateOrDatetime(body);
            } else {
                throw var4;
            }
        }
    }
}