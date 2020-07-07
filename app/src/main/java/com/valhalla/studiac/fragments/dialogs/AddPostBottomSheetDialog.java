package com.valhalla.studiac.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.SpinnerAdapter;
import com.valhalla.studiac.holders.ImageItem;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.utility.ErrorStyle;

import java.util.ArrayList;

/*
 * called from fragment
 */
public class AddPostBottomSheetDialog extends BottomSheetDialogFragment {

    private ArrayList<ImageItem> mSpinnerItemsTopic;
    private ArrayList<ImageItem> mSpinnerItemsCategory;

    private EditText mDescription;
    private String mTopic;
    private String mCategory;
    private OnAddPostListener mOnAddPostListener;
    private SpinnerAdapter mAdapterTopic;
    private View mView;
    private ErrorStyle errorStyle;

    public AddPostBottomSheetDialog() {
        initListCategory();
        initListTopic(Common.POST_CATEGORY_1, true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_bulletin_add_post, container, false);
        errorStyle = ErrorStyle.getInstance(getContext());
        mView = view;
        initViews(view);

        handleAddEvent(view);
        Spinner spinnerTopic = view.findViewById(R.id.bulletin_add_post_spinner_select_topic_id);
        Spinner spinnerCategory = view.findViewById(R.id.bulletin_add_post_spinner_select_category_id);

        populateSpinner("category", spinnerCategory);
        populateSpinner("topic", spinnerTopic);

        return view;
    }

    private void initListCategory() {
        mSpinnerItemsCategory = new ArrayList<>();
        mSpinnerItemsCategory.add(new ImageItem(Common.POST_CATEGORY_1, R.drawable.bulletin_ic_official));
        mSpinnerItemsCategory.add(new ImageItem(Common.POST_CATEGORY_3, R.drawable.bulletin_ic_advertisement));
        mSpinnerItemsCategory.add(new ImageItem(Common.POST_CATEGORY_2, R.drawable.bulletin_ic_help));
        mSpinnerItemsCategory.add(new ImageItem(Common.POST_CATEGORY_4, R.drawable.bulletin_ic_others));
    }

    public interface OnAddPostListener {
        void writePost(String category, String topic, String description);
    }


    private void initListTopic(String category, boolean isFirstTime) {

        if (isFirstTime) {
            mSpinnerItemsTopic = new ArrayList<>();
        }
        switch (category) {
            case Common.POST_CATEGORY_1:
                mTopic = Common.POST_TOPIC_1; // the default topic selected
                mSpinnerItemsTopic.add(new ImageItem(Common.POST_TOPIC_1, R.drawable.bulletin_ic_notice));
                mSpinnerItemsTopic.add(new ImageItem(Common.POST_TOPIC_2, R.drawable.bulletin_ic_seminar));
                mSpinnerItemsTopic.add(new ImageItem(Common.POST_TOPIC_3, R.drawable.bulletin_ic_job_opportunities));
                mSpinnerItemsTopic.add(new ImageItem(Common.POST_TOPIC_4, R.drawable.bulletin_ic_club_recruitment));
                mSpinnerItemsTopic.add(new ImageItem(Common.POST_TOPIC_5, R.drawable.bulletin_ic_workshop));
                break;
            case Common.POST_CATEGORY_3:
                mTopic = Common.POST_TOPIC_6;
                mSpinnerItemsTopic.add(new ImageItem(Common.POST_TOPIC_6, R.drawable.bulletin_ic_giveaway));
                mSpinnerItemsTopic.add(new ImageItem(Common.POST_TOPIC_7, R.drawable.bulletin_ic_sale_post));
                mSpinnerItemsTopic.add(new ImageItem(Common.POST_TOPIC_8, R.drawable.bulletin_ic_roommate));
                break;
            case Common.POST_CATEGORY_2:
                mTopic = Common.POST_TOPIC_9;
                mSpinnerItemsTopic.add(new ImageItem(Common.POST_TOPIC_9, R.drawable.bulletin_ic_lost_items));
                mSpinnerItemsTopic.add(new ImageItem(Common.POST_TOPIC_10, R.drawable.bulletin_ic_blood_donation));
                break;
            case Common.POST_CATEGORY_4:
                // do nothing
                mTopic = Common.POST_TOPIC_11;
                break;
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAddPostListener) {
            mOnAddPostListener = (OnAddPostListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    private void initViews(View view) {
        mDescription = view.findViewById(R.id.bulletin_add_post_description_id);
        errorStyle.resetError(mDescription);
    }

    private void populateSpinner(String type, Spinner spinner) {
        if (type.equals("category")) {
            SpinnerAdapter adapter = new SpinnerAdapter(requireContext(), mSpinnerItemsCategory, AddPostBottomSheetDialog.class.getSimpleName());
            spinner.setAdapter(adapter);
            handleSpinnerCategoryEvents(spinner);
        } else {
            mAdapterTopic = new SpinnerAdapter(requireContext(), mSpinnerItemsTopic, AddPostBottomSheetDialog.class.getSimpleName());
            spinner.setAdapter(mAdapterTopic);
            handleSpinnerTopicEvents(spinner);
        }

    }

    /*
     * if others category is selected, then set visibility of SelectTopic spinner to GONE.
     */
    private void handleSpinnerCategoryEvents(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //adapter.setSelectedIndex(position);
                ImageItem clickedItem = (ImageItem) parent.getItemAtPosition(position);
                mCategory = clickedItem.getName();

                Spinner spinnerTopic = mView.findViewById(R.id.bulletin_add_post_spinner_select_topic_id);
                TextView spinnerTopicText = mView.findViewById(R.id.bulletin_add_post_select_topic_text_id);

                if (mCategory.equals(Common.POST_CATEGORY_4)) {
                    spinnerTopic.setVisibility(View.GONE);
                    spinnerTopicText.setVisibility(View.GONE);
                    mTopic = null;

                } else {
                    mSpinnerItemsTopic.clear();
                    initListTopic(mCategory, false);
                    mAdapterTopic.notifyDataSetChanged();


                    spinnerTopic.setVisibility(View.VISIBLE);
                    spinnerTopicText.setVisibility(View.VISIBLE);
                }
                //Toast.makeText(getContext(), mCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void handleSpinnerTopicEvents(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // adapter.setSelectedIndex(position);
                ImageItem clickedItem = (ImageItem) parent.getItemAtPosition(position);
                mTopic = clickedItem.getName();
                //    Toast.makeText(getContext(), mTopic, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void handleAddEvent(final View view) {
        Button addButton = view.findViewById(R.id.bulletin_add_post_add_button_id);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    // pass the topic name and description to underlying parent activity using interface
                    if (mCategory.equals(Common.POST_CATEGORY_4)) {
                        mTopic = Common.POST_CATEGORY_4;
                    }
                    mOnAddPostListener.writePost(mCategory, mTopic, mDescription.getText().toString());
                    dismiss();
                }
            }
        });

    }

    private boolean isInputValid() {
        if (mDescription.getText().toString().equals("")) {
            errorStyle.setError(getString(R.string.Error_EmptyField), mDescription);
            return false;
        } else {
            errorStyle.resetError(mDescription);
        }

        if (mDescription.getText().toString().length() < 25) {
            errorStyle.setError(getString(R.string.Error_Meaningful_description),mDescription);
            return  false;
        } else {
            errorStyle.resetError(mDescription);
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