package com.example.ryan.gamenight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;

import com.example.ryan.gamenight.DiceActivity;
import com.example.ryan.gamenight.R;

public class DiceSelectFragment extends Fragment implements View.OnClickListener {

    View view;
    private ImageButton diceImageButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dice_select, container, false);
        diceImageButton = (ImageButton)view.findViewById(R.id.diceImage);
        diceImageButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.diceImage:
                Intent intent = new Intent(getActivity(), DiceActivity.class);
                startActivity(intent);
                break;
        }
    }
}
