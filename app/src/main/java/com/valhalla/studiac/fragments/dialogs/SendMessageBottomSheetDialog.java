package com.valhalla.studiac.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.valhalla.studiac.R;
import com.valhalla.studiac.utility.ErrorStyle;

public class SendMessageBottomSheetDialog extends BottomSheetDialogFragment {
    private String mMessage;
    private OnButtonClickListener mListener;
    private EditText mMessageTextView;
    private ErrorStyle errorStyle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_send_message, container, false);
        errorStyle =ErrorStyle.getInstance(getContext());
        Button addButton = view.findViewById(R.id.send_message_button_id);
        mMessageTextView = view.findViewById(R.id.send_message_input_id);
        errorStyle.resetError(mMessageTextView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    // send the message to parent activity using interface
                    mListener.sendMessage(mMessage);
                    dismiss();
                }
            }
        });

        return view;
    }


    public interface OnButtonClickListener {
        void sendMessage(String message);
    }


    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonClickListener) {
            mListener = (OnButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    private boolean isInputValid() {
        mMessage = mMessageTextView.getText().toString();
        if (mMessage.equals("")) {
            errorStyle.setError(getString(R.string.Error_EmptyField),mMessageTextView);
            return false;
        } else {
            errorStyle.resetError(mMessageTextView);
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
        return new BottomSheetDialog(requireContext(), getTheme());
    }
}
