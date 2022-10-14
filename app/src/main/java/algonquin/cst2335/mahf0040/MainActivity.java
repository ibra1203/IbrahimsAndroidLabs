package algonquin.cst2335.mahf0040;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity<TAG> extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );


        Button loginButton =findViewById(R.id.loginButton);
        EditText email = findViewById(R.id.etEmailIn);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        String emailAddress = prefs.getString("LoginName", "sds");
        email.setText(emailAddress);


        loginButton.setOnClickListener(e -> {

            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);

            nextPage.putExtra( "EmailAddress", email.getText().toString() );

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString( "LoginName", String.valueOf(email.getText()));
            editor.apply();


            startActivity(nextPage);
        });



    }

}

//    private static String TAG = "MainActivity";
// //   Log.d ( TAG, "Message");
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.w( "MainActivity", "In onStart() - The application is now visible on screen" );
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.w( "MainActivity", "In onStop() - The application is no longer visible" );
//
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        Log.w( "MainActivity", "In onDestroy() - Any memory used by the application is freed" );
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        Log.w( "MainActivity", "In onPause() - The application no longer responds to user input" );
//
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.w( "MainActivity", "In onResume() - The application is now responding to user input" );
//
//    }

