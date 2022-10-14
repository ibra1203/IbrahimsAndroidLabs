package algonquin.cst2335.mahf0040;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        TextView tvWelcome = findViewById(R.id.tvWelcomeMsg);
        tvWelcome.setText("Welcome  " + emailAddress);


        Button phoneNumber = findViewById(R.id.btnCall);

        EditText etPhone = findViewById(R.id.editTextPhone);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String storedPhoneNumber = prefs.getString("PhoneNumber", "000");
        etPhone.setText(storedPhoneNumber);

        phoneNumber.setOnClickListener(e -> {

            Intent call = new Intent(Intent.ACTION_DIAL);
            String input = etPhone.getText().toString();
            call.setData(Uri.parse("tel:" + input));

            startActivity(call);
        });



        ImageView pic = findViewById(R.id.imageView);
        Button changePic = findViewById(R.id.btnChange);

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            pic.setImageBitmap(thumbnail);


                            // new part
                            FileOutputStream fOut = null;
                            try { fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            }
                            catch (IOException e)
                            { e.printStackTrace();
                            }
                        }
                    }
                });
        File file = new File(getFilesDir(),"Picture.png" );
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(getFilesDir() +"/Picture.png");
            pic.setImageBitmap(theImage);
        }

        changePic.setOnClickListener(p -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResult.launch(cameraIntent);

        });

    }

    @Override
    protected void onPause() {

        super.onPause();
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        EditText etPhone = findViewById(R.id.editTextPhone);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString( "PhoneNumber", String.valueOf(etPhone.getText()));
        editor.apply();

    }
}








