package com.valhalla.studiac.adapters.result_tracker;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Weight;

import java.util.ArrayList;

public class EditGradeWeightRecycleAdapter extends RecyclerView.Adapter<EditGradeWeightRecycleAdapter.EditWeightViewHolder> {

    private ArrayList<Weight> mWeightList;
    private boolean mEditEnabled;

    public static int TYPE_HEADER = 0;
    public static int TYPE_BODY = 1;

    public EditGradeWeightRecycleAdapter(ArrayList<Weight> weightList, Boolean editEnabled) {
        this.mWeightList = weightList;
        mEditEnabled = editEnabled;
    }

    @NonNull
    @Override
    public EditWeightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EditWeightViewHolder evh;
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_edit_weight, parent, false);
            evh = new EditWeightViewHolder(v, mWeightList, TYPE_HEADER);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_weight_bottomsheet, parent, false);
            evh = new EditWeightViewHolder(v, mWeightList, TYPE_BODY);
        }
        return evh;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_BODY;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull EditWeightViewHolder holder, final int position) {
        // we can use position but using count for testing
        if (position != 0) {
            holder.gradeTextView.setText(mWeightList.get(position - 1).getGrade());
            String gradePoint = mWeightList.get(position - 1).getWeight() + "";
            holder.gradePointTextView.setText(gradePoint.trim());
            if (mEditEnabled) {
                holder.gradePointTextView.setEnabled(true);
            } else {
                holder.gradePointTextView.setEnabled(false);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mWeightList.size() + 1;
    }

    static class EditWeightViewHolder extends RecyclerView.ViewHolder {
        private EditText gradeTextView;
        private EditText gradePointTextView;

        EditWeightViewHolder(View itemView, final ArrayList<Weight> mWeightList, int viewType) {
            super(itemView);

            if (viewType == TYPE_BODY) {
                gradeTextView = itemView.findViewById(R.id.item_edit_weight_bottom_sheet_grade_text);
                gradePointTextView = itemView.findViewById(R.id.item_edit_weight_bottom_sheet_grade_point);
                gradePointTextView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            mWeightList.get(getAdapterPosition() - 1).setWeight(Double.parseDouble(s.toString()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }
                });

            }

        }

    }


}