package com.okandinc.testcase.ui.currencylist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.okandinc.testcase.R;
import com.okandinc.testcase.data.model.Currency;
import com.okandinc.testcase.ui.currencylist.adapter.CurrencyAdapter;
import com.okandinc.testcase.ui.currencylist.adapter.SimpleItemTouchHelperCallback;
import com.okandinc.testcase.ui.exchangerate.ExchangeRatesDialogFragment;

import java.util.List;

public class CurrencyListFragment extends Fragment implements CurrencyListFragmentView {

    private CurrencyListFragmentPresenter currencyListFragmentPresenter;

    private ItemTouchHelper itemTouchHelper;

    private ItemTouchHelper.Callback callback;

    private CurrencyAdapter adapter;

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView txtNoRecordFound;

    public CurrencyListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currency_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
        currencyListFragmentPresenter = new CurrencyListFragmentPresenter(this);
        currencyListFragmentPresenter.loadCurrencyList();
    }

    @Override
    public void displayCurrencies(List<Currency> currencyList) {
        initRecyclerView(currencyList);
    }

    @Override
    public void displayNoCurrencies(List<Currency> currencyList) {
        initRecyclerView(currencyList);
        if (currencyList.size() == 0) {
            txtNoRecordFound.setText(getString(R.string.no_record_found));
            txtNoRecordFound.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(),getString(R.string.error_currency),Toast.LENGTH_LONG).show();
    }

    private void setUpViews(View view) {
        txtNoRecordFound = view.findViewById(R.id.txt_no_record_found);
        swipeRefreshLayout =view.findViewById(R.id.swipe_container);
        recyclerView = view.findViewById(R.id.rw_currency_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                txtNoRecordFound.setVisibility(View.GONE);
                resetRecyclerViewToUpdateList();
                currencyListFragmentPresenter.loadCurrencyList();
            }
        });
    }

    /**
     * initialize adapter and other needs of RecylerView to display Currency List
     * with a Drag, drop and swipe support.
     * @param currencyList List of Currency.
     */
    private void initRecyclerView(List<Currency> currencyList) {
        adapter = new CurrencyAdapter(currencyList, new CurrencyAdapter.CurrencyListListener() {
            @Override
            public void onCurrencySelected(Currency currency) {
                ExchangeRatesDialogFragment exchangeRatesDialogFragment = new ExchangeRatesDialogFragment();
                exchangeRatesDialogFragment.setCancelable(false);
                Bundle bundle = new Bundle();
                bundle.putParcelable(getString(R.string.selected_currency_key_for_bundle),currency);
                exchangeRatesDialogFragment.setArguments(bundle);
                exchangeRatesDialogFragment.show(getFragmentManager(),getString(R.string.exchange_rate_dialog_tag));
            }
        });
        if (callback == null && itemTouchHelper == null) {
            callback = new SimpleItemTouchHelperCallback(adapter);
            itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Resets adapter and the other needs of RecyclerView for reInitialize.
     */
    private void resetRecyclerViewToUpdateList() {
        itemTouchHelper.attachToRecyclerView(null);
        itemTouchHelper = null;
        callback = null;
        adapter = null;
    }

}
