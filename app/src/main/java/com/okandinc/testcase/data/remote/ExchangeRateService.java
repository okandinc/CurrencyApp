package com.okandinc.testcase.data.remote;

import com.okandinc.testcase.data.model.ExchangeRate;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExchangeRateService {

    @GET("Rates/{currency}?ParamMode=2")
    Call<ExchangeRate> getExchangeRate(@Path("currency") String Cur_Abbreviation);
}
