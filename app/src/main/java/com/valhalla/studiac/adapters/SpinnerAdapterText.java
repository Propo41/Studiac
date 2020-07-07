package com.valhalla.studiac.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.valhalla.studiac.R;
import com.valhalla.studiac.holders.TextItem;

import java.util.ArrayList;

public class SpinnerAdapterText extends ArrayAdapter<TextItem> {

    private int mColourResId;

    public SpinnerAdapterText(Context context, ArrayList<TextItem> spinnerList, int colourResId) {
        super(context, 0, spinnerList); // since we are handling the layouts
        mColourResId = colourResId;
        // ourselves we pass 2nd parameter 0
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_spinner_text, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.spinner_item_text_id);
        TextItem currentItem = getItem(position);
        if (currentItem != null) {
            textViewName.setText(currentItem.getText());
            textViewName.setTextColor(mColourResId);
        }

        return convertView;
    }


}
