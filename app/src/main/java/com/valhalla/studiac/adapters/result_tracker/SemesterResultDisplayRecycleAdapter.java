package com.valhalla.studiac.adapters.result_tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.models.Result;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SemesterResultDisplayRecycleAdapter extends RecyclerView.Adapter<SemesterResultDisplayRecycleAdapter.SemesterResultDisplayViewHolder>{

    private Result mResult;
    private Iterator iterator;

    public static class SemesterResultDisplayViewHolder extends RecyclerView.ViewHolder{
        public TextView mCourseText;
        public TextView mGradeText;
        public SemesterResultDisplayViewHolder(View itemView){
            super(itemView);
            mCourseText = itemView.findViewById(R.id.item_dialog_semester_result_display_course_text);
            mGradeText = itemView.findViewById(R.id.item_dialog_semester_result_display_gpa_text);
        }
    }

    public SemesterResultDisplayRecycleAdapter(Result mResult){
        this.mResult = mResult;
        HashMap<String ,String> hashMap = mResult.getGradesObtained();
        Set<Map.Entry<String, String>> set = hashMap.entrySet();
        iterator = set.iterator();
    }

    @NonNull
    @Override
    public SemesterResultDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_semester_result_display,parent,false);
        // Toast.makeText(parent.getContext(), getItemCount()+"", Toast.LENGTH_SHORT).show();
        return new SemesterResultDisplayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SemesterResultDisplayViewHolder holder, int position) {
        //ItemDialogSemesterResultDisplay currentItem = mResultList.get(position);
        if(iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry)iterator.next();
            holder.mCourseText.setText(mapEntry.getKey().toString());
            holder.mGradeText.setText(mapEntry.getValue().toString());
        }
    }

    @Override
    public int getItemCount() {
        return mResult.getGradesObtained().size();
    }

}
