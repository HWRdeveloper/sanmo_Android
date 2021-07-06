package com.ruehyeon.sanmo;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Locale;

import static com.ruehyeon.sanmo.Constants.TAG;

/**
 * JumpUp
 * Created by KimWonJun on 2018-07-27.
 */
public class BottomSheetDialog extends BottomSheetDialogFragment {


    int newValue;
    int type;
    private BottomSheetDialogListener bottomSheetDialogListener;

    public BottomSheetDialog(Context context, BottomSheetDialogListener bottomSheetDialogListener, int type){
        this.type = type;
        this.bottomSheetDialogListener = bottomSheetDialogListener;
    }

    public interface BottomSheetDialogListener{
        void exit(int data);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_chatting, container, false);


        final NumberPicker numberPicker = view.findViewById(R.id.number_picker);


        // Set divider color
        numberPicker.setDividerColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryLight));
        numberPicker.setDividerColorResource(R.color.colorPrimaryLight);

        // Set formatter
        numberPicker.setFormatter(getString(R.string.number_picker_formatter));
        numberPicker.setFormatter(R.string.number_picker_formatter);

        // Set selected text color
        numberPicker.setSelectedTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        numberPicker.setSelectedTextColorResource(R.color.colorPrimaryDark);

        // Set selected text size
        numberPicker.setSelectedTextSize(getResources().getDimension(R.dimen.selected_text_size));
        numberPicker.setSelectedTextSize(R.dimen.selected_text_size);

        // Set selected typeface
        numberPicker.setSelectedTypeface(Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL));
        numberPicker.setSelectedTypeface(getString(R.string.roboto_light), Typeface.NORMAL);
        numberPicker.setSelectedTypeface(getString(R.string.roboto_light));
        numberPicker.setSelectedTypeface(R.string.roboto_light, Typeface.NORMAL);
        numberPicker.setSelectedTypeface(R.string.roboto_light);

        // Set text color
        numberPicker.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_grey));
        numberPicker.setTextColorResource(R.color.dark_grey);

        // Set text size
        numberPicker.setTextSize(getResources().getDimension(R.dimen.text_size));
        numberPicker.setTextSize(R.dimen.text_size);

        // Set typeface
        numberPicker.setTypeface(Typeface.create(getString(R.string.roboto_light), Typeface.NORMAL));
        numberPicker.setTypeface(getString(R.string.roboto_light), Typeface.NORMAL);
        numberPicker.setTypeface(getString(R.string.roboto_light));
        numberPicker.setTypeface(R.string.roboto_light, Typeface.NORMAL);
        numberPicker.setTypeface(R.string.roboto_light);

        // Set value
        switch (type) {
            // 수축기
            case  0:
                numberPicker.setMaxValue(220);
                numberPicker.setMinValue(60);
                numberPicker.setValue(100);
                newValue = 100;

                break;

            //이완기
            case  1:
                numberPicker.setMaxValue(140);
                numberPicker.setMinValue(40);
                numberPicker.setValue(60);
                newValue = 60;

                break;

            case  2:
                numberPicker.setMaxValue(300);
                numberPicker.setMinValue(65);
                numberPicker.setValue(120);
                newValue = 120;
                break;
            case  3:
                break;
            case  4:
                break;
            case  5:
                break;
        }


        // Set string values
//        String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
//        numberPicker.setMinValue(1);
//        numberPicker.setMaxValue(data.length);
//        numberPicker.setDisplayedValues(data);

        // Set fading edge enabled
        numberPicker.setFadingEdgeEnabled(true);

        // Set scroller enabled
        numberPicker.setScrollerEnabled(true);

        // Set wrap selector wheel
        numberPicker.setWrapSelectorWheel(true);

        // Set accessibility description enabled
        numberPicker.setAccessibilityDescriptionEnabled(true);

        // OnClickListener
        numberPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click on current value");
            }
        });

        // OnValueChangeListener
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                newValue = newVal;
                Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
            }
        });

        // OnScrollListener
        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker picker, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    Log.d(TAG, String.format(Locale.US, "newVal: %d", picker.getValue()));
                }
            }
        });








        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        bottomSheetDialogListener.exit(newValue);
    }

}
