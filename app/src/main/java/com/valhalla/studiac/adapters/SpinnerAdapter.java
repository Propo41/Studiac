package com.valhalla.studiac.adapters;

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
import com.valhalla.studiac.activities.setup.ProfileSetup2Activity;
import com.valhalla.studiac.adapters.result_tracker.CalculateGPARecycleAdapter;
import com.valhalla.studiac.fragments.dialogs.AddCourseBottomSheetDialog;
import com.valhalla.studiac.holders.ImageItem;

import java.util.ArrayList;

/**
 * A common adapter for all spinners used in the project
 * to change the color of the texts use conditional statements
 * The spinner items show IMAGE + TEXT
 */
public class SpinnerAdapter extends ArrayAdapter<ImageItem> {
    //private int mSelectedIndex = -1; // initially this will be ignored.
    String mCaller;

/*    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        mSelectedIndex = selectedIndex;
    }*/

    /**
     * @param caller the simple name of the caller class, ie if the adapter is used from ProfileSetupActivity2,
     *               then caller = ProfileSetupActivity2.
     *               NOTE: to get the simple name of a class, do this: ProfileSetupActivity2.class.getSimpleName()
     */
    public SpinnerAdapter(@NonNull Context context, ArrayList<ImageItem> spinnerItems, String caller) {
        super(context, 0, spinnerItems);
        mCaller = caller;

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

        ImageItem currentItem = getItem(position);

        if (currentItem != null) {
            imageView.setImageResource(currentItem.getImageId());
            textViewName.setText(currentItem.getName());
            // if adapter is used from ProfileSetupActivity2, then set the color of the text and ic
            // to white
            if (mCaller.equals(ProfileSetup2Activity.class.getSimpleName())) {
                textViewName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorWhite));
            } else if (mCaller.equals(AddCourseBottomSheetDialog.class.getSimpleName())) {
                textViewName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlackShade));
                imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorCuteBlue));
            } else if (mCaller.equals(CalculateGPARecycleAdapter.class.getSimpleName())) {
                // if the calling class is from calculate semester gpa bottom sheet, then change colour to blue
                textViewName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorVividBlue));
                imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorCuteBlue));
            } else {
                textViewName.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlackShade));
                imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAshHint));
            }


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
