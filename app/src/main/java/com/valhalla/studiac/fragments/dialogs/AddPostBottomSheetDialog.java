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
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.valhalla.studiac.R;
import com.valhalla.studiac.adapters.bulletinboard.BulletinSpinnerAdapter;
import com.valhalla.studiac.utility.Common;
import com.valhalla.studiac.models.SpinnerItem;

import java.util.ArrayList;

/*
 * called from fragment
 */
public class AddPostBottomSheetDialog extends BottomSheetDialogFragment {

    private SpinnerAdapter mAdapter;
    private int mTabIndex;
    private ArrayList<SpinnerItem> mSpinnerItemsTopic;
    private ArrayList<SpinnerItem> mSpinnerItemsCategory;

    private EditText mDescription;
    private String mDescriptionText;
    private String mTopic;
    private String mCategory;
    private OnAddPostListener mOnAddPostListener;
    private BulletinSpinnerAdapter mAdapterTopic;
    private View mView;

    public AddPostBottomSheetDialog() {
        initListCategory();
        initListTopic(Common.POST_CATEGORY_1, true);
    }

    private void initListCategory() {
        mSpinnerItemsCategory = new ArrayList<>();
        mSpinnerItemsCategory.add(new SpinnerItem(Common.POST_CATEGORY_1, R.drawable.bulletin_ic_official));
        mSpinnerItemsCategory.add(new SpinnerItem(Common.POST_CATEGORY_3, R.drawable.bulletin_ic_advertisement));
        mSpinnerItemsCategory.add(new SpinnerItem(Common.POST_CATEGORY_2, R.drawable.bulletin_ic_help));
        mSpinnerItemsCategory.add(new SpinnerItem(Common.POST_CATEGORY_4, R.drawable.bulletin_ic_others));

    }

    public interface OnAddPostListener {
        void writePost(String category, String topic, String description);
    }


    private void initListTopic(String category, boolean isFirstTime) {

        if (isFirstTime) {
            mSpinnerItemsTopic = new ArrayList<>();
        }
        if (category.equals(Common.POST_CATEGORY_1)) {
            mTopic = Common.POST_TOPIC_1; // the default topic selected
            mSpinnerItemsTopic.add(new SpinnerItem(Common.POST_TOPIC_1, R.drawable.bulletin_ic_notice));
            mSpinnerItemsTopic.add(new SpinnerItem(Common.POST_TOPIC_2, R.drawable.bulletin_ic_seminar));
            mSpinnerItemsTopic.add(new SpinnerItem(Common.POST_TOPIC_3, R.drawable.bulletin_ic_job_opportunities));
            mSpinnerItemsTopic.add(new SpinnerItem(Common.POST_TOPIC_4, R.drawable.bulletin_ic_club_recruitment));
            mSpinnerItemsTopic.add(new SpinnerItem(Common.POST_TOPIC_5, R.drawable.bulletin_ic_workshop));

        } else if (category.equals(Common.POST_CATEGORY_3)) {
            mTopic = Common.POST_TOPIC_6;
            mSpinnerItemsTopic.add(new SpinnerItem(Common.POST_TOPIC_6, R.drawable.bulletin_ic_giveaway));
            mSpinnerItemsTopic.add(new SpinnerItem(Common.POST_TOPIC_7, R.drawable.bulletin_ic_sale_post));
            mSpinnerItemsTopic.add(new SpinnerItem(Common.POST_TOPIC_8, R.drawable.bulletin_ic_roommate));

        } else if (category.equals(Common.POST_CATEGORY_2)) {
            mTopic = Common.POST_TOPIC_9;
            mSpinnerItemsTopic.add(new SpinnerItem(Common.POST_TOPIC_9, R.drawable.bulletin_ic_lost_items));
            mSpinnerItemsTopic.add(new SpinnerItem(Common.POST_TOPIC_10, R.drawable.bulletin_ic_blood_donation));
        } else if (category.equals(Common.POST_CATEGORY_4)) {
            // do nothing
            mTopic = Common.POST_TOPIC_11;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_bulletin_add_post, container, false);
        mView = view;
        initViews(view);

        handleAddEvent(view);
        Spinner spinnerTopic = view.findViewById(R.id.bulletin_add_post_spinner_select_topic_id);
        Spinner spinnerCategory = view.findViewById(R.id.bulletin_add_post_spinner_select_category_id);

        populateSpinner("category", spinnerCategory);
        populateSpinner("topic", spinnerTopic);

        return view;
    }

    private void initViews(View view) {
        mDescription = view.findViewById(R.id.bulletin_add_post_description_id);
    }

    private void populateSpinner(String type, Spinner spinner) {
        if (type.equals("category")) {
            BulletinSpinnerAdapter adapter = new BulletinSpinnerAdapter(getContext(), mSpinnerItemsCategory);
            spinner.setAdapter(adapter);
            handleSpinnerCategoryEvents(adapter, spinner);
        } else {
            mAdapterTopic = new BulletinSpinnerAdapter(getContext(), mSpinnerItemsTopic);
            spinner.setAdapter(mAdapterTopic);
            handleSpinnerTopicEvents(mAdapterTopic, spinner);
        }

    }

    /*
     * if others category is selected, then set visibility of SelectTopic spinner to GONE.
     */
    private void handleSpinnerCategoryEvents(final BulletinSpinnerAdapter adapter, Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedIndex(position);
                SpinnerItem clickedItem = (SpinnerItem) parent.getItemAtPosition(position);
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
                Toast.makeText(getContext(), mCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void handleSpinnerTopicEvents(final BulletinSpinnerAdapter adapter, Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedIndex(position);
                SpinnerItem clickedItem = (SpinnerItem) parent.getItemAtPosition(position);
                mTopic = clickedItem.getName();
                Toast.makeText(getContext(), mTopic, Toast.LENGTH_SHORT).show();
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
                    mOnAddPostListener.writePost(mCategory, mTopic, mDescription.getText().toString());
                    dismiss();
                }
            }
        });
    }

    private boolean isInputValid() {
        if (mDescription.getText().toString().equals("")) {
            return false;
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
