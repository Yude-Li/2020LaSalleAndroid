package Countries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lasalle2020android.travelcalculator.R;

import java.util.List;

import Model.CountryModel;

public class CountryListAdapter extends BaseAdapter {

    private Context context;
    List<CountryModel> countries;
    LayoutInflater inflater;

    public CountryListAdapter(Context context, List<CountryModel> countries) {
        super();
        this.context = context;
        this.countries = countries;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        CountryModel country = countries.get(position);

        if (view == null)
            view = inflater.inflate(R.layout.listview_row_single_img, null);

        Cell cell = Cell.from(view);
        cell.textView.setText(country.getDisplayName());
        cell.imageView.setImageResource(country.getFlagId(this.context));

        return view;
    }

    static class Cell {
        TextView textView;
        ImageView imageView;

        static CountryListAdapter.Cell from(View view) {
            if (view == null)
                return null;

            if (view.getTag() == null) {
                CountryListAdapter.Cell cell = new CountryListAdapter.Cell();
                cell.textView = view.findViewById(R.id.singlerow_title);
                cell.imageView =view.findViewById(R.id.singlerow_icon);
                view.setTag(cell);
                return cell;
            } else {
                return (CountryListAdapter.Cell) view.getTag();
            }
        }
    }
}
