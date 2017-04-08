package com.example.ryan.gamenight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.ryan.gamenight.R;

public class CreateDiceDialog extends Dialog {

    private DiceCustomDialogEventListener listener;

    private NumberPicker dice;
    private NumberPicker faces;

    public interface DiceCustomDialogEventListener {
        void onClickCancel();
        void onClickCreate(int numberOfDice, int numberOfFaces);
    }

    public CreateDiceDialog(@NonNull Context context, DiceCustomDialogEventListener listener) {
        super(context, R.style.dialog);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_dice_dialog);

        // Setup the NumberPickers
        initializeNumberPickers();

        // Set Cancel Button Listener
        Button btCancel = (Button) findViewById(R.id.dice_dialog_cancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickCancel();
                dismiss();
            }
        });

        // Set Create Button Listener
        Button btCreate = (Button) findViewById(R.id.dice_dialog_create);
        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the NumberPicker values and pass them
                dice = (NumberPicker) findViewById(R.id.dice_dialog_numberPicker_dice);
                faces = (NumberPicker) findViewById(R.id.dice_dialog_numberPicker_faces);
                listener.onClickCreate(dice.getValue(), faces.getValue());
                dismiss();
            }
        });
    }

    private void initializeNumberPickers() {
        dice = (NumberPicker) findViewById(R.id.dice_dialog_numberPicker_dice);
        faces = (NumberPicker) findViewById(R.id.dice_dialog_numberPicker_faces);

        dice.setMinValue(1); dice.setMaxValue(99); dice.setValue(2);
        faces.setMinValue(1); faces.setMaxValue(99); faces.setValue(6);
    }
}