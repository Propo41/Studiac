package com.valhalla.studiac.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.valhalla.studiac.R;

public class WalkThroughFragment extends Fragment implements View.OnClickListener {

    private int mPosition;
    private TextView skipText;
    private Button continueButton;
    private WalkThroughFragmentListener listener;


    public WalkThroughFragment(int position) {
        mPosition = position;
    }

    public interface WalkThroughFragmentListener {
        void onSkipClick();

        void onContinueClick(int pos);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof WalkThroughFragmentListener) {
            listener = (WalkThroughFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.walkthrough_routine, container, false);
        switch (mPosition) {
            case 1:
                rootView = (ViewGroup) inflater.inflate(R.layout.walkthrough_result, container, false);
                break;
            case 2:
                rootView = (ViewGroup) inflater.inflate(R.layout.walkthrough_todo, container, false);
                break;
            case 3:
                rootView = (ViewGroup) inflater.inflate(R.layout.walkthrough_bulletin, container, false);
                break;
            case 4:
                rootView = (ViewGroup) inflater.inflate(R.layout.walkthrough_messenger, container, false);
                break;

        }

        skipText = rootView.findViewById(R.id.skip_text);
        continueButton = rootView.findViewById(R.id.walkthrough_continue_button);

        skipText.setOnClickListener(this);
        continueButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        if (skipText.getId() == v.getId()) {
            listener.onSkipClick();
        }

        if (continueButton.getId() == v.getId()) {
            listener.onContinueClick(mPosition);

        }
    }


}

