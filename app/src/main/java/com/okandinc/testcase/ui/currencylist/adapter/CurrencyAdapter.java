package com.okandinc.testcase.ui.currencylist.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okandinc.testcase.R;
import com.okandinc.testcase.data.model.Currency;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private List<Currency> currencyList;

    private CurrencyListListener currencyListListener;

    public CurrencyAdapter(List<Currency> currencyList, CurrencyListListener currencyListListener) {
        this.currencyList = currencyList;
        this.currencyListListener = currencyListListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.currency_list_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position) {
        final Currency currency = currencyList.get(position);
        itemViewHolder.getTxtCurrencyAbbreviation().setText(currency.getCur_Abbreviation());
        itemViewHolder.getTxtCurrencyName().setText(currency.getCur_Name());
        itemViewHolder.getTxtCurrencyNameBel().setText(currency.getCur_Name_Bel());
        itemViewHolder.getTxtCurrencyNameEng().setText(currency.getCur_Name_Eng());
        itemViewHolder.getLytCurrencyItemContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currencyListListener != null) {
                    currencyListListener.onCurrencySelected(currency);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        currencyList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Currency prev = currencyList.remove(fromPosition);
        currencyList.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition,toPosition);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        private View lytCurrencyItemContainer;
        private TextView txtCurrencyAbbreviation;
        private TextView txtCurrencyName;
        private TextView txtCurrencyNameBel;
        private TextView txtCurrencyNameEng;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            lytCurrencyItemContainer = itemView.findViewById(R.id.lyt_currency_item_container);
            txtCurrencyAbbreviation = itemView.findViewById(R.id.txt_currency_abbreviation);
            txtCurrencyName = itemView.findViewById(R.id.txt_currency_name);
            txtCurrencyNameBel = itemView.findViewById(R.id.txt_currency_name_bel);
            txtCurrencyNameEng = itemView.findViewById(R.id.txt_currency_name_eng);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        public View getLytCurrencyItemContainer() {
            return lytCurrencyItemContainer;
        }

        public TextView getTxtCurrencyAbbreviation() {
            return txtCurrencyAbbreviation;
        }

        public TextView getTxtCurrencyName() {
            return txtCurrencyName;
        }

        public TextView getTxtCurrencyNameBel() {
            return txtCurrencyNameBel;
        }

        public TextView getTxtCurrencyNameEng() {
            return txtCurrencyNameEng;
        }
    }

    public static interface CurrencyListListener {

        public void onCurrencySelected(Currency currency);

    }
}
