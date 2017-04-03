package com.example.ryan.gamenight.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.ryan.gamenight.R;
import com.example.ryan.gamenight.TimerActivity;

public class TimerSelectFragment extends Fragment implements View.OnClickListener {

    View view;
    private ImageButton timerImageButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timer_select, container, false);
        timerImageButton = (ImageButton)view.findViewById(R.id.timerImage);
        timerImageButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timerImage:
                Intent intent = new Intent(getActivity(), TimerActivity.class);
                startActivity(intent);
                break;
        }
    }
}
