package com.motrixi.datacollection.network;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public interface HttpApi {

    @POST("{key}/{key1}/{key2}/")
    @FormUrlEncoded
    Call<JsonObject> request(@Path("key") String key, @Path("key1") String key1, @Path("key2") String key2, @FieldMap Map<String, Object> map);

    @GET("{key}/{key1}/{key2}/")
    Call<JsonObject> requestAuth2(@HeaderMap Map<String, String> map, @Path("key") String key, @Path("key1") String key1, @Path("key2") String key2);

    @GET("{key}/{key1}/{key2}/{key3}/")
    Call<JsonObject> requestAuth3(@HeaderMap Map<String, String> map, @Path("key") String key, @Path("key1") String key1, @Path("key2") String key2, @Path("key3") String key3);


    @GET("{key}/{key1}/{key2}/")
    Call<JsonObject> requestAuth(@Header("Authorization") String authorization, @Path("key") String key, @Path("key1") String key1, @Path("key2") String key2);

    @GET("{key}/{key1}/{key2}/")
    Call<JsonObject> requestAuth(@HeaderMap Map<String, String> headerMap, @Path("key") String key, @Path("key1") String key1, @Path("key2") String key2, @QueryMap Map<String, Object> map);

    @PUT("{key}/{key1}/{key2}/")
    @FormUrlEncoded
    Call<JsonObject> requestAuthPut(@HeaderMap Map<String, String> headerMap, @Path("key") String key, @Path("key1") String key1, @Path("key2") String key2, @FieldMap Map<String, Object> map);

    @POST("{key}/{key1}/{key2}/")
    @FormUrlEncoded
    Call<JsonObject> requestAuthPost(@HeaderMap Map<String, String> headerMap, @Path("key") String key, @Path("key1") String key1, @Path("key2") String key2, @FieldMap Map<String, Object> map);


}
