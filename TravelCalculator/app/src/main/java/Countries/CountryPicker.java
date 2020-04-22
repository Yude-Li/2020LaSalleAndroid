package Countries;

import android.annotation.SuppressLint;
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

import DataConfig.CountryConfigAccess;
import Model.CountryModel;
import currencies.CurrencyListAdapter;
import currencies.ExtendedCurrency;

public class CountryPicker extends DialogFragment {

    // View objects
    private EditText searchEditText;
    private ListView countryListView;

    // Variables
    private static Context mContext;
    private CountryConfigAccess countryConfig = null;
    private CountryListAdapter adapter;
    private List<CountryModel> countriesList = new ArrayList<>();
    private List<CountryModel> selectedCountriesList = new ArrayList<>();
    private CountryPickerLietener listener;

    public static CountryPicker newInstance(String dialogTitle, Context context) {
        mContext = context;

        CountryPicker picker = new CountryPicker();
        Bundle bundle = new Bundle();
        bundle.putString("dialogTitle", dialogTitle);
        picker.setArguments(bundle);
        return picker;
    }

    public CountryPicker() {
        countryConfig = new CountryConfigAccess(mContext);
        setCountryList(countryConfig.getCountriesList());
    }
    public void setListener(CountryPickerLietener listener) {
        this.listener = listener;
    }

    @Override
    public void dismiss() {
        if (getDialog() != null) {
            super.dismiss();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_picker, null);
        Bundle args = getArguments();
        if (args != null && getDialog() != null) {
            String dialogTitle = args.getString("dialogTitle");
            getDialog().setTitle(dialogTitle);

            int width = getResources().getDimensionPixelSize(R.dimen.cp_dialog_width);
            int height = getResources().getDimensionPixelSize(R.dimen.cp_dialog_height);
            getDialog().getWindow().setLayout(width, height);
        }
        searchEditText = (EditText) view.findViewById(R.id.country_picker_search);
        countryListView = (ListView) view.findViewById(R.id.country_picker_listview);

        selectedCountriesList = new ArrayList<>(countriesList.size());
        selectedCountriesList.addAll(countriesList);

        adapter = new CountryListAdapter(mContext, selectedCountriesList);
        countryListView.setAdapter(adapter);

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    CountryModel country = selectedCountriesList.get(position);
                    listener.onSelectCountry(country.getId(), country.getDisplayName(), country.getCountryNum()
                            , country.getCountryCode(), country.getCurrencyCode()
                            , country.getCurrency(), country.getCurrencyName()
                            , country.getCurrencySymbol(), country.getFlagId(mContext));
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

    public void setCountryList(List<CountryModel> newCurrencies) {
        this.countriesList.clear();
        this.countriesList.addAll(newCurrencies);
    }

    @SuppressLint("DefaultLocale")
    private void search(String text) {
        selectedCountriesList.clear();
        for (CountryModel country : countriesList) {
            if (country.getDisplayName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())) {
                selectedCountriesList.add(country);
            }
        }
        adapter.notifyDataSetChanged();
    }

}
