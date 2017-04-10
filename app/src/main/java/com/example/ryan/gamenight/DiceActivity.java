package com.example.ryan.gamenight;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ryan.gamenight.adapter.DiceListViewAdapter;
import com.example.ryan.gamenight.dialog.CreateDiceDialog;
import com.example.ryan.gamenight.listener.ShakeDetector;
import com.example.ryan.gamenight.objects.Die;

import java.util.ArrayList;

public class DiceActivity extends BaseActivity {

    // This context
    final Context diceActivity = this;

    // The Array of Dice
    private ArrayList<Die> dice = new ArrayList<>();

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    // The ListViewAdapter is used for .notifyDataSetChanged()
    private DiceListViewAdapter diceListViewAdapter;

    // The TextView that displays the sum of the dice rolled
    private TextView diceTv;

    // The ID of the currently checked radio button
    private int selectedRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Start with 2 d6
        dice.add(new Die(6));
        dice.add(new Die(6));

        // Get state and set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        // ShakeDetector initialization
        createShakeDetector();

        // ListView initialization
        createListView();

        // Set Roll Button Listener
        Button btRoll = (Button) findViewById(R.id.dice_bt_roll);
        btRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });

        // Set the Create Dice Button Listener
        Button btCreate = (Button) findViewById(R.id.dice_bt_create);
        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDiceDialog dialog = new CreateDiceDialog(diceActivity, new CreateDiceDialog.DiceCustomDialogEventListener() {
                    @Override
                    public void onClickCancel() {
                        // Do nothing
                    }

                    @Override
                    public void onClickCreate(int dice, int faces) {
                        createDice(dice, faces);
                    }
                });
                dialog.show();
            }
        });

        // Set the Dice Radio Group listener
        RadioGroup rgDice = (RadioGroup) findViewById(R.id.dice_rg);
        rgDice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                selectedRb = checkedId;
                updateDiceTv();
            }
        });

        // Initialize the TextView with the dice info
        diceTv = (TextView) findViewById(R.id.dice_tv);

        // Roll the dice and let the TextView update
        rollDice();

        // Set the Sum radio button to be checked by default
        RadioButton r = (RadioButton)findViewById(R.id.dice_rb_sum);
        r.setChecked(true);
    }

    private void createListView() {
        ListView listView = (ListView) findViewById(R.id.diceListView);
        diceListViewAdapter = new DiceListViewAdapter(this, dice);
        listView.setAdapter(diceListViewAdapter);
    }

    private void createShakeDetector() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                toastShort("Roll Dice");
                rollDice();
            }
        });
    }

    private void rollDice() {
        for (Die d : this.dice) {
            d.roll();
        }
        diceListViewAdapter.notifyDataSetChanged();
        updateDiceTv();
    }

    public void updateDiceTv() {

        switch (selectedRb) {

            // Sum the dice
            case R.id.dice_rb_sum:
                int sum = 0;
                for (Die d : this.dice) {
                    sum += d.getCurrentFaceUp();
                }
                diceTv.setTextSize(128);
                diceTv.setText(String.format("%d", sum));
                break;

            // Find the highest die
            case R.id.dice_rb_high:
                int high = 0;
                for (Die d : this.dice) {
                    if (d.getCurrentFaceUp() > high) {
                        high = d.getCurrentFaceUp();
                    }
                }
                diceTv.setTextSize(128);
                diceTv.setText(String.format("%d", high));
                break;

            // Find the face with the most occurrences
            case R.id.dice_rb_mode:
                SparseIntArray counts = new SparseIntArray();
                for (Die d : this.dice) {
                    // Check if the mapping exists
                    if (counts.get(d.getCurrentFaceUp()) == 0) {
                        // If not, add the mapping
                        counts.put(d.getCurrentFaceUp(), 1);
                    } else {
                        // If so, add to the current count
                        counts.put(d.getCurrentFaceUp(), (counts.get(d.getCurrentFaceUp()) + 1));
                    }
                }

                // Find the max die
                int max = 0;
                for (Die d : this.dice) {
                    if (d.getCurrentFaceUp() > max) {
                        max = d.getCurrentFaceUp();
                    }
                }

                // Check for the most occurrences of a face
                int occurrences = 0;
                for (int i = 0; i <= max; i++) {
                    if (counts.get(i) > occurrences) {
                        occurrences = counts.get(i);
                    }
                }

                // Add all faces with the most occurrences to the mostCommon list
                ArrayList<Integer> mostCommon = new ArrayList<>();
                for (int i = 0; i <= max; i++) {
                    if (counts.get(i) == occurrences) {
                        mostCommon.add(i);
                    }
                }

                // Display all the faces with the most occurrences
                String s = "";
                for (int j = 0; j < mostCommon.size(); j++) {
                    if (j == mostCommon.size() - 1){
                        s += mostCommon.get(j);
                    } else {
                        s += mostCommon.get(j) + ", ";
                    }
                }

                // Change the text size depending on the number of characters
                diceTv.setTextSize(128/(int)Math.ceil(0.5 * Math.sqrt(s.length())));

                // Put the values into the TextView
                diceTv.setText(String.format("%s", s));
                break;

            // Currently Unused
            default:
                break;
        }
    }

    public void createDice(int dice, int faces) {
        for (int i = 0; i < dice; i++) {
            this.dice.add(new Die(faces));
        }
        diceListViewAdapter.notifyDataSetChanged();
        updateDiceTv();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}