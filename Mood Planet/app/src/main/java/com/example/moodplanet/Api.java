package com.example.moodplanet;
import com.example.moodplanet.Model.Quotes;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    //json string
    String BASE_URL = "https://type.fit/api/";

    // get the array named 'quotes'
    @GET("quotes")
    // an invocation of a retrofit method that sends a request
    // to a webserver and returns a response.
    // each call yields its own HTTP request and response pair
    Call<List<Quotes>> getAllQuotes();


}
