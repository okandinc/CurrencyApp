package com.okandinc.testcase.ui.currencylist;

import com.okandinc.testcase.api.RetrofitClient;
import com.okandinc.testcase.data.model.Currency;
import com.okandinc.testcase.data.remote.CurrencyService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyListFragmentPresenter {

    private CurrencyListFragmentView currencyListFragmentView;

    private List<Currency> currencyList;

    public CurrencyListFragmentPresenter(CurrencyListFragmentView currencyListFragmentView) {
        this.currencyListFragmentView = currencyListFragmentView;
    }

    public void loadCurrencyList() {
        RetrofitClient.getInstance().create(CurrencyService.class).getCurrencyList().enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                currencyList = response.body();
                currencyListFragmentView.displayCurrencies(currencyList);

            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {
                if (currencyList == null) {
                    currencyList = new ArrayList<>();
                }
                currencyListFragmentView.displayNoCurrencies(currencyList);
            }
        });
    }
}
