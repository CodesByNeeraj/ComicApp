package com.example.norman_lee.comicapp;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.norman_lee.comicapp.utils.Container;
import com.example.norman_lee.comicapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    EditText editTextComicNo;
    Button buttonGetComic;
    TextView textViewTitle;
    ImageView imageViewComic;

    String comicNo;
    public static final String TAG = "Logcat";

    @Override
    protected void onCreate(Bundle savedInstanceState) { //onCreate is called when activity starts, sets layout from activity_main.xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //findviewbyID links to java variables in xml file
        editTextComicNo = findViewById(R.id.editTextComicNo);
        buttonGetComic = findViewById(R.id.buttonGetComic);
        textViewTitle = findViewById(R.id.textViewTitle);
        imageViewComic = findViewById(R.id.imageViewComic);

        buttonGetComic.setOnClickListener(new View.OnClickListener() { //when button is clicked
            @Override
            public void onClick(View v) {
                comicNo = editTextComicNo.getText().toString(); //gets comic number inputted by user
                if (Utils.isNetworkAvailable(MainActivity.this)) { //method in utils
                    getComic(comicNo); //if got internet
                } else {
                    Toast.makeText(MainActivity.this, "No Network Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //TODO 6.6 - 6.15 Modify GetComic Below *************
    //TODO 6.6 - 6.15 ****************
    //TODO you are reminded that this is NOT inside onCreate()
    //TODO 6.6 Make sure an executor and a handler are instantiated
    //TODO 6.7 (background work) create a final Container<Bitmap> cBitmap object which will be used for communication between the main thread and the child thread
    //TODO 6.8 Call Utils.buildURL to get the URL based on the comic number from userInput
    //TODO 6.9 Call Utils.getJSON to get the String response of the URL
    //TODO 6.10 If the response is null, write a Toast message in main thread, otherwise:
    //TODO 6.11 Inside a try/catch, get JSON object from the String response
    //TODO 6.12 Extract the image string url with key "img" from the JSON object
    //TODO 6.13 Extract the title with key "safe_title"
    //TODO 6.14 Download the Bitmap using Utils.getBitmap
    //TODO 6.15 (main thread) Assign the bitmap downloaded to imageView and set the title to textViewTitle
    void getComic(final String userInput) {
        ExecutorService executor = Executors.newSingleThreadExecutor(); //run only 1 thread,and ExecutorService creates a background thread
        final Handler handler = new Handler(Looper.getMainLooper()); //handler allows communication back to main thread (UI thread)

        executor.execute(new Runnable() { //start executing operations in separate thread
            @Override //run is an abstract method defined in runnable interface
            public void run() {
                final Container<Bitmap> cBitmap = new Container<>(); //generic class that stores a single object. here it is used to hold a Bitmap object(comic image)
                try {
                    URL url = Utils.buildURL(userInput);
                    String json = Utils.getJson(url); //gets json response as a string

                    if (json == null) { //if json is null, means request failed so show a toast message in main thread
                        handler.post(new Runnable() { //in the main thread
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "No Comic Found", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return; // Stop execution here if JSON is null
                    }
                    JSONObject jsonObject = new JSONObject(json); //once u have a json response, you parse it using this
                    String imgUrl = jsonObject.getString("img"); //comic image url
                    String title = jsonObject.getString("safe_title"); //comic title
                    cBitmap.set(Utils.getBitmap(new URL(imgUrl))); //utils.getBitmap downloads bitmap from url and sets it to cBitmap container

                    handler.post(new Runnable() { //run UI updates on main thread
                        @Override
                        public void run() {
                            imageViewComic.setImageBitmap(cBitmap.get()); //sets the comic image
                            textViewTitle.setText(title); //updates title
                        }
                    });

                } catch (MalformedURLException e) { //if the url is invalid
                    Log.e(TAG, "Invalid URL", e);
                } catch (JSONException e) { //if JSON parsing fails
                    Log.e(TAG, "Error parsing JSON", e);
                } catch (Exception e) { //any other unexpected errors
                    Log.e(TAG, "Unexpected error", e);
                }
            } //run ends here
        }); //executor ends here
    }
}