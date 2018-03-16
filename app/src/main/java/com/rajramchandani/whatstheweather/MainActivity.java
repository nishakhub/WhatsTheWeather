package com.rajramchandani.whatstheweather;

import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText city;
    TextView final1;

    public void click(View view)
    {
        DownloadTask task=new DownloadTask();
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(city.getWindowToken(),0);
        if(city.getText().toString()!=null) {
            try {
                String encoded = URLEncoder.encode(city.getText().toString(), "UTF-8");
                String result = task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encoded + "&appid=7027fd4b33f31e998e35e41e422b5b91").get();

            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Invalid City name", Toast.LENGTH_LONG).show();

            } catch (ExecutionException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Invalid City name", Toast.LENGTH_LONG).show();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Invalid City name", Toast.LENGTH_LONG).show();

            }
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Invalid City name",Toast.LENGTH_LONG).show();

        }

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        String result = "";

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Invalid City name",Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Invalid City name",Toast.LENGTH_LONG).show();

            }

            return "failed";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String message="";
            try {
                JSONObject jsonObject=new JSONObject(result);
                String whether=jsonObject.getString("weather");
                JSONArray array=new JSONArray(whether);
                for(int i=0;i<array.length();i++)
                {
                    JSONObject part=array.getJSONObject(i);
                    //Log.i("main",part.getString("main"));
                    String main=part.getString("main");
                    String desc=part.getString("description");
                    if(main!="" && desc!="")
                    {
                        message+=main+" : "+desc;

                    }

                }

                if(message!=null)
                {
                    final1.setText(message);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid City name",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Invalid City name",Toast.LENGTH_LONG).show();

            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = (EditText) findViewById(R.id.city);
        final1=(TextView)findViewById(R.id.data);



    }
}
