package com.example.ryan.gamenight;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ryan.gamenight.adapter.DiceListViewAdapter;
import com.example.ryan.gamenight.dialog.CreateDiceDialog;
import com.example.ryan.gamenight.listener.ShakeDetector;
import com.example.ryan.gamenight.objects.Die;

import java.util.ArrayList;

public class DiceActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO: make dynamic dice
        dice.add(new Die(6));
        dice.add(new Die(6));
        dice.add(new Die(6));
        dice.add(new Die(6));
        dice.add(new Die(6));
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

        // Initialize the TextView with the dice info
        diceTv = (TextView) findViewById(R.id.dice_tv);

        // Roll the dice and let the TextView update
        rollDice();
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
                rollDice();
            }
        });
    }

    private void rollDice() {
        for (Die d : this.dice){
            d.roll();
        }
        diceListViewAdapter.notifyDataSetChanged();
        updateDiceTv();
    }

    public void updateDiceTv() {
        int sum = 0;
        for (Die d : this.dice){
            sum += d.getCurrentFaceUp();
        }
        diceTv.setText(String.format("%d", sum));
    }

    public void createDice(int dice, int faces){
        for (int i = 0; i < dice; i++){
            this.dice.add(new Die(faces));
        }
        diceListViewAdapter.notifyDataSetChanged();
        updateDiceTv();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
