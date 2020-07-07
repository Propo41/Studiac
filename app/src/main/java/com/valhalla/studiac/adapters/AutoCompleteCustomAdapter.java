package com.valhalla.studiac.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.valhalla.studiac.R;
import com.valhalla.studiac.holders.TextItem;

import java.util.ArrayList;
import java.util.List;


public class AutoCompleteCustomAdapter extends ArrayAdapter<TextItem> {

    private List<TextItem> itemList;

    public AutoCompleteCustomAdapter(@NonNull Context context, @NonNull List<TextItem> itemList) {
        super(context, 0, itemList);
        this.itemList = new ArrayList<>(itemList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return textFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_autocomplete_row,
                    parent,
                    false
            );
        }
        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        textViewName.setTypeface(
                ResourcesCompat.getFont(getContext(),
                        R.font.montserrat_regular));
        TextItem textItem = getItem(position);
        if (textItem != null) {
            textViewName.setText(textItem.getText());
        }

        return convertView;
    }

    private Filter textFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<TextItem> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(itemList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TextItem item : itemList) {
                    if (item.getText().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((TextItem) resultValue).getText();
        }
    };


}