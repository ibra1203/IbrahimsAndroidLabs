package algonquin.cst2335.mahf0040;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.mahf0040.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected String cityName;
    protected RequestQueue queue = null;
    String iconName = null;
    ImageRequest imgReq;
    Bitmap image;
    Button btnForcast;
    EditText cityEdit;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(this);
        binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView(binding.getRoot());

//        setContentView(R.layout.activity_main);
        cityEdit = (EditText) findViewById(R.id.cityEdit);
        btnForcast = (Button) findViewById(R.id.getForecastBtn);
        btnForcast.setOnClickListener(click -> {
            cityName = cityEdit.getText().toString();
            String stringURL = "https://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(cityName) + "&appid=bcb9e1005c8e8039af48034c6e69b046&units=metric";

            //this goes in the button click handler:
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {

                        try {
                            JSONObject coord = response.getJSONObject( "coord" );
                            iconName = response.getJSONArray("weather").getJSONObject(0).getString("icon");


                            String pathname = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(pathname);
//                            if(file.exists())
//                            {
//                                image = BitmapFactory.decodeFile(pathname);
//                            }
//                            else {
                                imgReq = new ImageRequest("https://openweathermap.org/img/w/" + iconName + ".png", new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        try {
                                            //
                                            image = bitmap;
                                            image.compress(Bitmap.CompressFormat.PNG, 100, MainActivity.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                        }
                                        catch(Exception e){
                                            System.out.println("Err " + e.getMessage());
                                        }
                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {   });
//                            }

                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);
                            String description = position0.getString("description");

                            JSONObject mainObject = response.getJSONObject("main");
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");


                            binding.temperatureTV.setText("The current temperature is " + current + " Degree Celsius");
                            binding.temperatureTV.setVisibility(View.VISIBLE);

                            binding.maxTempTV.setText("The max temperature is " + max + " Degree Celsius");
                            binding.maxTempTV.setVisibility(View.VISIBLE);

                            binding.minTempTV.setText("The min temperature is " + min + " Degree Celsius");
                            binding.minTempTV.setVisibility(View.VISIBLE);

                            binding.humidityTV.setText("Humitidy is " + humidity + " % ");
                            binding.humidityTV.setVisibility(View.VISIBLE);

                            binding.weatherIcon.setImageBitmap(image);
                            binding.weatherIcon.setVisibility(View.VISIBLE);

                            binding.descriptionTextView.setText(description);
                            binding.descriptionTextView.setVisibility(View.VISIBLE);

                            queue.add(imgReq);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    },
                    (error) -> {
                        Log.e("Error", "error");
                    } );
            queue.add(request);

        });
    }
}

