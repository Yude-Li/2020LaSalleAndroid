package currencies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import com.lasalle2020android.travelcalculator.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import Model.CountryModel;

/**
 * Created by Bugalia on 24/03/2020.
 */
public class CurrencyPicker extends DialogFragment {

    private EditText searchEditText;
    private ListView currencyListView;
    private CurrencyListAdapter adapter;
    private List<CountryModel> currenciesList = new ArrayList<>();
    private List<CountryModel> selectedCurrenciesList = new ArrayList<>();
    private CurrencyPickerListener listener;
    private Context context;

    /**
     * To support show as dialog
     */
    public static CurrencyPicker newInstance(String dialogTitle, ArrayList<CountryModel> currencyList, Context mCtx) {
        CurrencyPicker picker = new CurrencyPicker();
        Bundle bundle = new Bundle();
        bundle.putString("dialogTitle", dialogTitle);
        picker.setArguments(bundle);
        picker.setCurrenciesList(currencyList);
        picker.context=mCtx;
        return picker;
    }

    public CurrencyPicker() {

       // setCurrenciesList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.currency_picker, null);
        Bundle args = getArguments();
        if (args != null && getDialog() != null) {
            String dialogTitle = args.getString("dialogTitle");
            getDialog().setTitle(dialogTitle);

            int width = getResources().getDimensionPixelSize(R.dimen.cp_dialog_width);
            int height = getResources().getDimensionPixelSize(R.dimen.cp_dialog_height);
            getDialog().getWindow().setLayout(width, height);
        }
        searchEditText = (EditText) view.findViewById(R.id.currency_code_picker_search);
        currencyListView = (ListView) view.findViewById(R.id.currency_code_picker_listview);

        selectedCurrenciesList = new ArrayList<>(currenciesList.size());
        selectedCurrenciesList.addAll(currenciesList);

        adapter = new CurrencyListAdapter(getActivity(), selectedCurrenciesList);
        currencyListView.setAdapter(adapter);

        currencyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    CountryModel currency = selectedCurrenciesList.get(position);
                    listener.onSelectCurrency(currency.getCurrencyName(), currency.getCurrencyCode(), currency.getCurrencySymbol(),
                            currency.getFlagId(context));
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });

        return view;
    }

    @Override
    public void dismiss() {
        if (getDialog() != null) {
            super.dismiss();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public void setListener(CurrencyPickerListener listener) {
        this.listener = listener;
    }

    @SuppressLint("DefaultLocale")
    private void search(String text) {
        selectedCurrenciesList.clear();
        for (CountryModel currency : currenciesList) {
            if (currency.getCurrencyName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())) {
                selectedCurrenciesList.add(currency);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void setCurrenciesList(List<CountryModel> newCurrencies) {
        this.currenciesList.clear();
        this.currenciesList.addAll(newCurrencies);
    }

//    public void setCurrenciesList(Set<String> savedCurrencies) {
//        this.currenciesList.clear();
//        for(String code : savedCurrencies){
//            this.currenciesList.add(ExtendedCurrency.getCurrencyByISO(code));
//        }
//    }
}
