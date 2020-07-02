package com.valhalla.studiac.adapters.result_tracker;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Weight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EditGradeWeightRecycleAdapter extends RecyclerView.Adapter<EditGradeWeightRecycleAdapter.EditWeightViewHolder> {

    private ArrayList<Weight> mWeightList;
    private EditWeightViewHolder mEditWeightViewHolder;
    private static boolean mEditEnabled;

    public boolean isEditEnabled() {
        return mEditEnabled;
    }

    public void setEditEnabled(boolean mEditEnabled) {
        EditGradeWeightRecycleAdapter.mEditEnabled = mEditEnabled;
    }

    static class EditWeightViewHolder extends RecyclerView.ViewHolder {
        private EditText gradeTextView;
        private EditText gradePointTextView;

        EditWeightViewHolder(View itemView, final ArrayList<Weight> mWeightList) {
            super(itemView);
            gradeTextView = itemView.findViewById(R.id.item_edit_weight_bottom_sheet_grade_text);
            gradePointTextView = itemView.findViewById(R.id.item_edit_weight_bottom_sheet_grade_point);
            gradePointTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        if (mEditEnabled) {
                            gradePointTextView.setEnabled(true);
                        } else {
                            gradePointTextView.setEnabled(false);

                        }
                    }else{
                        gradePointTextView.setEnabled(false);
                    }
                }
            });

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
                        mWeightList.get(getAdapterPosition()).setWeight(Double.parseDouble(s.toString()));
                    } catch (Exception ex) {
                        System.out.println("Error in Conversion");
                        ex.printStackTrace();

                    }


                }
            });


        }
    }

    public EditGradeWeightRecycleAdapter(ArrayList<Weight> weightList, Context mContext, Boolean mEditEnabled) {
        this.mWeightList = weightList;
        this.mEditEnabled = mEditEnabled;
    }


    @NonNull
    @Override
    public EditWeightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_weight_bottomsheet, parent, false);
        EditWeightViewHolder evh = new EditWeightViewHolder(v, mWeightList);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull EditWeightViewHolder holder, final int position) {
        mEditWeightViewHolder = holder;
        // HashMap<String, Double> currentItem = mWeights.get(position);
        // we can use position but using count for testing
        mEditWeightViewHolder.gradeTextView.setText(mWeightList.get(position).getGrade());
        String gradePoint = mWeightList.get(position).getWeight() + "";
        mEditWeightViewHolder.gradePointTextView.setText(gradePoint.trim());
        if(mEditEnabled){
            mEditWeightViewHolder.gradePointTextView.setEnabled(true);
        }else{
            mEditWeightViewHolder.gradePointTextView.setEnabled(false);
        }

    }

    @Override
    public int getItemCount() {
        return mWeightList.size();
    }

}