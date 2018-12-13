package com.okandinc.testcase.ui.currencylist;

import com.okandinc.testcase.data.model.Currency;

import java.util.List;

public interface CurrencyListFragmentView {

    void displayCurrencies(List<Currency> currencyList);
    void displayNoCurrencies(List<Currency> currencyList);
}
