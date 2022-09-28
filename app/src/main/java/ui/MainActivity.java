package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.mahf0040.R;
import algonquin.cst2335.mahf0040.databinding.ActivityMainBinding;
import data.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());


      //  variableBinding.textview.setText(model.editString.toString());
        variableBinding.mybutton.setOnClickListener(click -> {

         model.editString.postValue(variableBinding.myedittext.getText().toString());
            // model.editString = variableBinding.myedittext.getText().toString();
//            variableBinding.textview.setText("Your edit text has: " + model.editString);

            model.editString.observe(this, s -> {
                variableBinding.textview.setText("Your edit text has: " + s);

            });

        });

        model.selected.observe(this, selected -> {
            variableBinding.checkbox.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
            variableBinding.switch1.setChecked(selected);

            Context context = getApplicationContext();
            CharSequence text = "You drink coffee!";

            int duration = Toast.LENGTH_SHORT;
            Toast toast= Toast.makeText(context, text, duration);
            toast.show();

        });

        variableBinding.checkbox.setOnCheckedChangeListener((checkbox, isChecked) -> {
            model.selected.postValue(variableBinding.checkbox.isChecked());
        });

        variableBinding.radioButton.setOnCheckedChangeListener((radioButton, isChecked) -> {
            model.selected.postValue(variableBinding.radioButton.isChecked());
        });

        variableBinding.switch1.setOnCheckedChangeListener((switch1, isChecked) -> {
            model.selected.postValue(variableBinding.switch1.isChecked());
        });

        variableBinding.myimagebutton.setOnClickListener(click -> {

            ImageButton imageButton = (ImageButton)findViewById(R.id.myimagebutton);
            int width = imageButton.getMeasuredWidth();
            int   height = imageButton.getMeasuredHeight();

            Context context = getApplicationContext();
            CharSequence text = "The width = " + width + " and height ="  + height;

            int duration = Toast.LENGTH_LONG;
            Toast toast1= Toast.makeText(context, text, duration);
            toast1.show();
        });
    }
}
