package com.okandinc.testcase.ui.exchangerate;

import com.okandinc.testcase.api.RetrofitClient;
import com.okandinc.testcase.data.model.ExchangeRate;
import com.okandinc.testcase.data.remote.ExchangeRateService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeRatesDialogFragmentPresenter {

    private ExchangeRatesDialogFragmentView exchangeRatesDialogView;

    private ExchangeRate exchangeRate;

    public ExchangeRatesDialogFragmentPresenter(ExchangeRatesDialogFragmentView exchangeRatesDialogFragmentView) {
        this.exchangeRatesDialogView = exchangeRatesDialogFragmentView;
    }

    public void loadExchangeRate(String currencyAbbreviation) {
        RetrofitClient.getInstance().create(ExchangeRateService.class).getExchangeRate(currencyAbbreviation).enqueue(new Callback<ExchangeRate>() {
            @Override
            public void onResponse(Call<ExchangeRate> call, Response<ExchangeRate> response) {
                exchangeRate = response.body();
                if(exchangeRate != null) {
                    exchangeRatesDialogView.displayExchangeRate(exchangeRate);
                } else {
                    exchangeRatesDialogView.displayNoExchangeRate();
                }
            }

            @Override
            public void onFailure(Call<ExchangeRate> call, Throwable t) {
                exchangeRatesDialogView.displayNoExchangeRate();
            }
        });
    }
}
