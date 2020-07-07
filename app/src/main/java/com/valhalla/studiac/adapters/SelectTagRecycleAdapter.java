package com.valhalla.studiac.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.Common;

import java.util.ArrayList;

/*
 * adapter class for the recycler view to implement select course
 */

public class SelectTagRecycleAdapter extends RecyclerView.Adapter<SelectTagRecycleAdapter.SelectTagViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<String> mTagItems;
    private RadioButton mCheckedButton = null;
    private Context mContext;


    public SelectTagRecycleAdapter(ArrayList<String> tagItems, Context context) {
        mTagItems = tagItems;
        mContext = context;
    }

    public interface OnItemClickListener {
        void onItemChecked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public SelectTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelectTagViewHolder selectTagViewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_select_tag, parent, false);
        selectTagViewHolder = new SelectTagViewHolder(view); // view holder

        return selectTagViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final SelectTagViewHolder holder, final int position) {
        if (position == 0) {
            holder.mRadioButton.setChecked(true);
            mCheckedButton = holder.mRadioButton;
        }


        String tag = mTagItems.get(position);
        holder.mRadioButton.setText(tag);
        holder.mContainer.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), Common.TASK_TAG_COLOURS.get(tag), null));

        holder.mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // if any of the radio button is not selected then check the selected button
                if (mCheckedButton != null) {
                    mCheckedButton.setChecked(false);
                }
                mCheckedButton = (RadioButton) buttonView;

                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemChecked(position);
                    }
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return mTagItems.size();
    }

    static class SelectTagViewHolder extends RecyclerView.ViewHolder {
        RadioButton mRadioButton;
        ConstraintLayout mContainer;

        SelectTagViewHolder(View itemView) {
            super(itemView);
            mRadioButton = itemView.findViewById(R.id.item_dialog_select_tag_radio_button_id);
            mContainer = itemView.findViewById(R.id.item_dialog_select_tag_root_id);

        }
    }


}
