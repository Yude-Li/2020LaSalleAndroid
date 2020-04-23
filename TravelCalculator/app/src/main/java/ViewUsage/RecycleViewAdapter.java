package ViewUsage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lasalle2020android.travelcalculator.R;

import org.w3c.dom.Text;

import java.util.List;

import DataConfig.CountryConfigAccess;
import Model.CountryModel;
import Model.ExpenseModel;
import Model.TripInfoModel;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private List<TripInfoModel> mTripList = null;
    private List<ExpenseModel> mExpenseList = null;

    private  Context context;

    public RecycleViewAdapter(List<TripInfoModel> mTripList, Context context, int type) {
        this.mTripList = mTripList;
        this.context = context;
    }

    public RecycleViewAdapter(List<ExpenseModel> mExpenseList, Context context) {
        this.mExpenseList = mExpenseList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView countryImg;
        public TextView tripNameTextView;
        public TextView tripDateTextView;

        public TextView expenseNameTextView;
        public TextView expenseSpendAmountTextView;
        public TextView expenseConvertedAmountTextView;
        public TextView expenseDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            countryImg = (ImageView) itemView.findViewById(R.id.singlerow_icon_white);
            tripNameTextView = (TextView) itemView.findViewById(R.id.singlerow_title_white);
//            tripDateTextView = (TextView) itemView.findViewById(R.id.doublerow_tripDate);

            expenseNameTextView = (TextView) itemView.findViewById(R.id.text_expenseName);
            expenseSpendAmountTextView = (TextView) itemView.findViewById(R.id.text_expenseSpendAmount);
            expenseConvertedAmountTextView = (TextView) itemView.findViewById(R.id.text_expenseConvertedAmount);
            expenseDateTextView = (TextView) itemView.findViewById(R.id.text_expenseDate);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView;

        if (mExpenseList == null) {
            // Inflate the custom layout
            contactView = inflater.inflate(R.layout.listview_row_single_img_white, parent, false);

        }
        else {
            // Inflate the custom layout
            contactView = inflater.inflate(R.layout.listview_expense, parent, false);
        }

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder viewHolder, int position) {

        if (mExpenseList == null) {
            TripInfoModel tripInfo = mTripList.get(position);

            CountryConfigAccess countryConfig = new CountryConfigAccess(this.context);
            if(tripInfo.getTravelCountry()!=9999) {
                CountryModel country = countryConfig.getCountryById(tripInfo.getTravelCountry());

                // Get the country info by the countryId
                // Get the country icon from database (or maybe drawable)
                Bitmap bitmap = BitmapFactory.decodeFile("");
                ImageView imgCountry = viewHolder.countryImg;
                imgCountry.setImageResource(country.getFlagId(this.context));

                TextView textViewName = viewHolder.tripNameTextView;
                textViewName.setText(tripInfo.getTripName());
            }

//            TextView textViewDate = viewHolder.tripDateTextView;
//            textViewDate.setText(tripInfo.getStartDate() + " - " + tripInfo.getEndDate());
        }
        else {
            ExpenseModel expenseInfo = mExpenseList.get(position);

            TextView textViewName = viewHolder.expenseNameTextView;
            textViewName.setText(expenseInfo.getExpenseName());

            TextView textViewSpendAmount = viewHolder.expenseSpendAmountTextView;
            textViewSpendAmount.setText(expenseInfo.getSpendAmount());

            TextView textViewConvertedAmount = viewHolder.expenseConvertedAmountTextView;
            textViewConvertedAmount.setText(expenseInfo.getConvertedAmount());

            TextView textViewDate = viewHolder.expenseDateTextView;
            textViewDate.setText(expenseInfo.getDate());
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if (mExpenseList == null) {
            return mTripList.size();
        }
        else {
            return mExpenseList.size();
        }
    }

    public void add(int position, TripInfoModel item) {
        mTripList.add(item);
        notifyItemInserted(position);
    }

    public void add(int position, ExpenseModel item) {
        mExpenseList.add(item);
        notifyItemChanged(position);
    }

    public void remove(int position) {
        if (mExpenseList == null){
            mTripList.remove(position);
            notifyItemRemoved(position);
        }
        else {
            mExpenseList.remove(position);
            notifyItemRemoved(position);
        }
    }
}