package com.okandinc.testcase.data.remote;

import com.okandinc.testcase.data.model.Currency;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CurrencyService {

    @GET("Currencies")
    Call<List<Currency>> getCurrencyList();
}
