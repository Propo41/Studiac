package com.valhalla.studiac.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.valhalla.studiac.R;
import com.valhalla.studiac.holders.HeaderItem;
import com.valhalla.studiac.holders.ImageItem;
import com.valhalla.studiac.holders.ListItem;

import java.util.ArrayList;

/*
 * adapter class for the recycler view to implement view courses ui
 */

public class SettingsRecycleAdapter extends RecyclerView.Adapter<SettingsRecycleAdapter.SettingsViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<ListItem> mListItems;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 1;
    private boolean mGoogleUser;
    private Context mContext;

    public SettingsRecycleAdapter(ArrayList<ListItem> listItems, boolean googleUser, Context context) {
        mListItems = listItems;
        mGoogleUser = googleUser;
        mContext = context;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    @Override
    public int getItemViewType(int position) {
        //if position = 0, then show the header, else show the list
        if (mListItems.get(position) instanceof HeaderItem) {
            return TYPE_HEADER; // The macro that we defined
        } else {
            return TYPE_LIST;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SettingsViewHolder settingsViewHolder;

        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_settings, parent, false);
            settingsViewHolder = new SettingsViewHolder(view, mListener, viewType); // view holder

        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_settings, parent, false);
            settingsViewHolder = new SettingsViewHolder(view, mListener, viewType); // view holder

        }
        return settingsViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
        if (holder.view_type == TYPE_HEADER) {
            holder.mHeader.setText(((HeaderItem) mListItems.get(position)).getHeader());
        } else {
            // if it's a google user, then user cannot change password
            if (mGoogleUser && position == 2) {
                holder.mCardView.setFocusable(false);
                holder.mCardView.setClickable(false);
                holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAshLight));
            }
            holder.mName.setText(((ImageItem) mListItems.get(position)).getName());
            holder.mIcon.setImageResource(((ImageItem) mListItems.get(position)).getImageId());


        }

    }


    @Override
    public int getItemCount() {
        return mListItems.size();
    }


    static class SettingsViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        ImageView mIcon;
        TextView mHeader;
        CardView mCardView;
        int view_type;

        SettingsViewHolder(final View itemView, final OnItemClickListener customListener, int view_type) {
            super(itemView);
            if (view_type == TYPE_LIST) {
                mName = itemView.findViewById(R.id.item_settings_name_id);
                mIcon = itemView.findViewById(R.id.item_settings_image_id);
                mCardView = itemView.findViewById(R.id.item_settings_card_view_id);

                mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                customListener.onItemClick(position);
                            }
                        }
                    }
                });
            } else {
                mHeader = itemView.findViewById(R.id.item_settings_header_id);
            }

            this.view_type = view_type;
        }
    }


}
