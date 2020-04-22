package commonutilities;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import Model.TripInfoModel;

public class SpinnerAdapter extends ArrayAdapter<TripInfoModel> {


    private Context context;

    private TripInfoModel[] values;

    public SpinnerAdapter(Context context, int textViewResourceId,
                       TripInfoModel[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public TripInfoModel getItem(int position){
        return values[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.WHITE);

        label.setText(values[position].getTripName()+" "+values[position].getStartDate());


        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values[position].getTripName()+" "+values[position].getStartDate());


        return label;
    }
}