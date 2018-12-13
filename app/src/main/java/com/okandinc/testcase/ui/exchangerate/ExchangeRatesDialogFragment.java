package com.okandinc.testcase.ui.exchangerate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.okandinc.testcase.R;
import com.okandinc.testcase.data.model.Currency;
import com.okandinc.testcase.data.model.ExchangeRate;
import com.okandinc.testcase.ui.MainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExchangeRatesDialogFragment extends DialogFragment implements ExchangeRatesDialogFragmentView {

    private ExchangeRatesDialogFragmentPresenter exchangeRatesDialogFragmentPresenter;

    private TextView txtCurrencyAbbr;

    private TextView txtCurrencyName;

    private TextView txtCurrencyUpdateDate;

    private TextView txtExchangeRate;

    private Spinner spinnerCommonExchange;

    private Button btnOK;

    private String selectedCurrency;

    public ExchangeRatesDialogFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ExchangeRatesDialogFragment.this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_fragment_exchange_rates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
        exchangeRatesDialogFragmentPresenter = new ExchangeRatesDialogFragmentPresenter(this);

        /**
         * To decide which Exchange Rate to request.
         * If Argument is not null then it has to have a Currency in Bundle.
         * If not then user wants to see common Currencies.
         */
        if (getArguments() != null) {
            Currency currency = getArguments().getParcelable(getString(R.string.selected_currency_key_for_bundle));
            if (currency != null) {
                spinnerCommonExchange.setVisibility(View.GONE);
                exchangeRatesDialogFragmentPresenter.loadExchangeRate(currency.getCur_Abbreviation());
            } else {
                exchangeRatesDialogFragmentPresenter.loadExchangeRate(selectedCurrency);
            }
        } else {
            exchangeRatesDialogFragmentPresenter.loadExchangeRate(selectedCurrency);
        }

    }

    @Override
    public void displayExchangeRate(ExchangeRate exchangeRate) {
        String currencyAbbr = getString(R.string.currency_abbr_prefix) + " " + exchangeRate.getCur_Abbreviation();
        String currencyName = getString(R.string.currency_name_prefix) + " " + exchangeRate.getCur_Name();
        String currencyDate = getString(R.string.currency_date_prefix) + " " + convertStringDateToDate(exchangeRate.getDate());
        String currencyRateAsString = getString(R.string.currency_rate_prefix) + " " + exchangeRate.getCur_OfficialRate()/exchangeRate.getCur_Scale();

        txtCurrencyAbbr.setText(currencyAbbr);
        txtCurrencyName.setText(currencyName);
        txtCurrencyUpdateDate.setText(currencyDate);
        txtExchangeRate.setText(currencyRateAsString);

        stopAnim();
        btnOK.setClickable(true);
    }

    @Override
    public void displayNoExchangeRate() {
        stopAnim();
        Toast.makeText(getContext(),getString(R.string.error_exchange_rate),Toast.LENGTH_LONG).show();
        dismissDialog();
    }

    public void setUpViews(View view) {
        startAnim();

        txtCurrencyAbbr = view.findViewById(R.id.txt_currency_abbreviation);
        txtCurrencyName = view.findViewById(R.id.txt_currency_name);
        txtCurrencyUpdateDate = view.findViewById(R.id.txt_currency_update_date);
        txtExchangeRate = view.findViewById(R.id.txt_exchange_rate);
        spinnerCommonExchange = view.findViewById(R.id.spin_common_exchange);
        btnOK = view.findViewById(R.id.btnOk);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.isClickable()) {
                    dismissDialog();
                }
            }
        });
        btnOK.setClickable(false);
        setUpSpinnerForCommonCurrencies();
    }

    /**
     * Setting up Spinner to choose between USD and EUR.
     */
    private void setUpSpinnerForCommonCurrencies() {
        final String[] commonCurrencies = getResources().getStringArray(R.array.common_currencies);
        ArrayAdapter<String> spinnerAdapterForCommonCurrencies = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, commonCurrencies);
        spinnerAdapterForCommonCurrencies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCommonExchange.setAdapter(spinnerAdapterForCommonCurrencies);
        spinnerCommonExchange.setSelection(0);
        selectedCurrency = commonCurrencies[0];
        spinnerCommonExchange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String currencyAbbr = commonCurrencies[i];
                exchangeRatesDialogFragmentPresenter.loadExchangeRate(currencyAbbr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Converts String Date from one format to another.
     * @param dateAsString String Date with Time.
     * @return String Date without Time.
     */
    private String convertStringDateToDate(String dateAsString){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        Date date = null;
        try {
            date = dateFormat.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(date);
    }

    /**
     * Shows loading animation in case it is needed.
     */
    private void startAnim() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.startAnim();
        }
    }

    /**
     * Stops loading animation
     */
    private void stopAnim() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.stopAnim();
        }
    }

    /**
     * Dismisses itself
     */
    private void dismissDialog() {
        ExchangeRatesDialogFragment.this.dismiss();
    }
}
