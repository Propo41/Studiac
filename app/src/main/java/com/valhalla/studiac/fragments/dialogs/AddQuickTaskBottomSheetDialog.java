package com.valhalla.studiac.fragments.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.common.Common;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddQuickTaskBottomSheetDialog extends BottomSheetDialogFragment {
    private String mDescription;
    private String mAdditionalNotes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_todo_quick_add, container, false);
        Button addButton = view.findViewById(R.id.view_courses_add_button_id);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid(view)) {

                    Bundle bundle = new Bundle();
                    bundle.putString("description", mDescription);
                    bundle.putString("additionalNotes", mAdditionalNotes);

                    getTargetFragment().onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_OK,
                            new Intent().putExtras(bundle)
                    );
                    dismiss();
                }
            }
        });

        return view;
    }

    private boolean isInputValid(View view) {
        EditText taskDescriptionView = view.findViewById(R.id.view_courses_add_name_id);
        EditText additionalNotesView = view.findViewById(R.id.currenttasks_quickadd_notes_id);

        mDescription = taskDescriptionView.getText().toString();
        mAdditionalNotes = additionalNotesView.getText().toString();

        if (mDescription.equals("")) {
            Common.addStroke(taskDescriptionView, 5);
            return false;
        } else {
            Common.addStroke(additionalNotesView, 0);
        }

        return true;

    }

    @Override
    public int getTheme() {
        return R.style.BaseBottomSheetDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), getTheme());
    }
}
