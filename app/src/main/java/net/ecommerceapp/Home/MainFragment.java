package net.ecommerceapp.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;

import net.ecommerceapp.R;

public class MainFragment extends Fragment {

    private MaterialCardView mNikeCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
    
        init(view);

        mNikeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_mainFragment_to_detailsFragment);
            }
        });
        
        return view;
    }

    private void init(View view) {

        mNikeCard = (MaterialCardView) view.findViewById(R.id.main_nike_card);

    }
}