package com.valhalla.studiac.adapters.bulletinboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.valhalla.studiac.R;
import com.valhalla.studiac.models.SpinnerItem;

import java.util.ArrayList;

public class BulletinSpinnerAdapter extends ArrayAdapter<SpinnerItem> {
    private int mSelectedIndex = -1; // initially this will be ignored.

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        mSelectedIndex = selectedIndex;
    }

    public BulletinSpinnerAdapter(@NonNull Context context, ArrayList<SpinnerItem> spinnerItems) {
        super(context, 0, spinnerItems);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent, "normal");
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent, "dropdown");
    }


    /*
     * initializes the views at index "position"
     * the method is used to inflate the views at index "position"
     */
    private View initView(int position, View convertView, ViewGroup parent, String type) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_spinner, parent, false
            );
        }
        ImageView imageView = convertView.findViewById(R.id.spinner_image_id);
        TextView textViewName = convertView.findViewById(R.id.spinner_text_id);

        SpinnerItem currentItem = getItem(position);

        if (currentItem != null) {
            imageView.setImageResource(currentItem.getImageId());
            textViewName.setText(currentItem.getName());

            textViewName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlackShade));
            imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAshHint));

          /*  // TODO: *BUG: the following code, creates lag, but works flawlessly. Fix it. Try it with toggleButtons instead of TextView
            // highlights the item that is selected
            if(type.equals("dropdown")){
                // if the item is selected, then change its color
                if (position == mSelectedIndex) {
                    Toast.makeText(getContext(), "changing color", Toast.LENGTH_SHORT).show();
                    textViewName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    ImageView imageViewSelected = convertView.findViewById(R.id.spinner_check_id);
                    imageViewSelected.setVisibility(View.VISIBLE);
                    imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }

            }*/

        }
        return convertView;
    }
}
