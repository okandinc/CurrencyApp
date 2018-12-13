package com.okandinc.testcase.ui.exchangerate;

import com.okandinc.testcase.data.model.ExchangeRate;

public interface ExchangeRatesDialogFragmentView {

    void displayExchangeRate(ExchangeRate exchangeRate);
    void displayNoExchangeRate();
}
