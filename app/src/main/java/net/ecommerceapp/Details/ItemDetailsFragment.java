package net.ecommerceapp.Details;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.ecommerceapp.R;

import at.blogc.android.views.ExpandableTextView;


public class ItemDetailsFragment extends Fragment {

    private ExpandableTextView mDetailsItemDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_details, container, false);

        init(view);

        mDetailsItemDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailsItemDesc.toggle();
            }
        });

        return view;
    }

    private void init(View view) {

        mDetailsItemDesc = (ExpandableTextView) view.findViewById(R.id.details_item_description);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}