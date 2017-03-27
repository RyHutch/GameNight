package com.example.ryan.gamenight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ryan.gamenight.DiceActivity;
import com.example.ryan.gamenight.R;
import com.example.ryan.gamenight.objects.Die;

import java.util.ArrayList;

public class DiceListViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Die> dice;

    public DiceListViewAdapter(Context context, ArrayList<Die> dice) {
        this.mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dice = dice;
    }

    @Override
    public int getCount() {
        return dice.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // Set up the ViewHolder
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dice_item, parent, false);
            holder = new ViewHolder();
            holder.textView1 = (TextView) convertView.findViewById(R.id.dice_item_tv1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.dice_item_tv2);
            holder.buttonReroll = (Button) convertView.findViewById(R.id.dice_item_bt_reroll);
            holder.buttonRemove = (Button) convertView.findViewById(R.id.dice_item_bt_remove);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set the text in each dice_item
        holder.textView1.setText(String.format("d%d", dice.get(position).getNumberOfFaces()));
        holder.textView2.setText(String.format("%d", dice.get(position).getCurrentFaceUp()));

        // Create a listener for the reroll button
        holder.buttonReroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reroll the die at the current position and update the text
                dice.get(position).roll();
                View parentView = (View)v.getParent();
                TextView tv2 = (TextView)parentView.findViewById(R.id.dice_item_tv2);
                tv2.setText(String.format("%d", dice.get(position).getCurrentFaceUp()));

                //  Use the update method in the DiceActivity
                if(mContext instanceof DiceActivity){
                    ((DiceActivity)mContext).updateDiceTv();
                }

            }
        });

        // Create a listener for the remove button
        holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Remove the die at the current position
                dice.remove(position);
                notifyDataSetChanged();

                //  Use the update method in the DiceActivity
                if(mContext instanceof DiceActivity){
                    ((DiceActivity)mContext).updateDiceTv();
                }
            }
        });
        return convertView;
    }
}


class ViewHolder {
    TextView textView1;
    TextView textView2;
    Button buttonReroll;
    Button buttonRemove;
    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
}