//////////////////////////////
// Project Checklist        //
//                          //
// ListView: Complete       //
// ViewPager: Complete      //
// Animation:               //
// Gesture: -- Complete --  //
// Custom Dialog: Complete  //
// Interface: Complete      //
// Broadcast:               //
// Service:                 //
// RadioGroup: Complete     //
// Handler:                 //
// Bundle:                  //
//////////////////////////////

package com.example.ryan.gamenight;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ryan.gamenight.adapter.ViewPagerAdapter;
import com.example.ryan.gamenight.fragment.DiceSelectFragment;
import com.example.ryan.gamenight.fragment.TimerSelectFragment;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Setup the array of fragments
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new DiceSelectFragment());
        fragments.add(new TimerSelectFragment());

        // Load the fragments into the ViewPager
        ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        viewPagerAdapter.setFragments(fragments);
        viewPager.setAdapter(viewPagerAdapter);
    }
}
