package com.example.android.pogoda;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView cisnienie, temperatura, natezenieSwiatla, wilgotnosc;
    Button button;
    ImageView ikona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute();
            }
        });
        
        cisnienie = (TextView) findViewById(R.id.cisnienie);
        wilgotnosc = (TextView) findViewById(R.id.wilgotnosc);
        temperatura = (TextView) findViewById(R.id.temperatura);
        natezenieSwiatla = (TextView) findViewById(R.id.natezenieSwiatla);
        ikona = (ImageView) findViewById(R.id.ikona);
        new MyTask().execute();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        String textResult;
        String[] parts;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                StringBuilder result = new StringBuilder();
                URL url = new URL("https://liquid-journal-193218.appspot.com/demo");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                textResult = result.toString();
                parts = textResult.split(" ");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            
            if (parts[4].equals("0")) {
                ikona.setImageResource(R.drawable.deszcz);
            } else if (Float.parseFloat(parts[3]) < 15) {
                ikona.setImageResource(R.drawable.zachmurzenie);
            } else {
                ikona.setImageResource(R.drawable.slonce);
            }


            temperatura.setText( (Math.round(Float.parseFloat(parts[0])* 10.0) / 10.0) + " °C");
            cisnienie.setText((int)(Float.parseFloat(parts[1]) / 100) + " HPa");
            wilgotnosc.setText(Float.parseFloat(parts[2]) + " %");
            natezenieSwiatla.setText((int) Float.parseFloat(parts[3]) + " lx");
            super.

                    onPostExecute(result);
        }
    }


}
