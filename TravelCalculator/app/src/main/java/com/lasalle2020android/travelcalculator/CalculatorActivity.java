package com.lasalle2020android.travelcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

private TextView mDisplayTextView;


private String mEnteredString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_layout);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, CalculatorFragment.newInstance())
//                    .commitNow();
//        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){


            case R.id.zero_btn:

                break;

            case R.id.one_btn:

                break;

            case R.id.two_btn:

                break;


            case R.id.three_btn:

                break;
            case R.id.four_Btn:

                break;
            case R.id.five_Btn:

                break;
            case R.id.six_Btn:

                break;
            case R.id.seven_Btn:

                break;
            case R.id.eight_Btn:

                break;
            case R.id.nine_Btn:

                break;



            case R.id.addition_Btn:
                break;

            case R.id.substraction_Btn:

                break;
            case R.id.division_Btn:
                break;

            case R.id.multiplication_Btn:

                break;

            case R.id.backspace_Btn:
performBackSpace();
                break;





        }

    }


    private void performBackSpace(){

        if(mEnteredString.length()>0){

            mEnteredString = mEnteredString.substring(0, mEnteredString.length()-1);
        }

    }

    private void performOperations(){


    }





}
