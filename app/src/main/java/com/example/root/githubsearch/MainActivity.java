package com.example.root.githubsearch;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements ResultDataAdapter.ResultItemClickListener {

    EditText repoName;
    String baseUrlStr = "https://api.github.com/search/repositories";//?q=android&sort=stars";
    String[] resultData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.repoName = (EditText) findViewById(R.id.repo_name);
        this.repoName.setText("android");
    }

    @Override
    public void onResultItemClickListener(int position) {
        Toast.makeText(this,resultData[position],Toast.LENGTH_LONG).show();
    }

    public class GithubSearch extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL baseURL = urls[0];
            try {
                HttpURLConnection connection = (HttpURLConnection) baseURL.openConnection();
                InputStream input = connection.getInputStream();
                Scanner inputScanner = new Scanner(input);
                inputScanner.useDelimiter("\\A");
                return  inputScanner.next();
            }catch (Exception e){
                return  e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject responseJSON = new JSONObject(s);
                JSONArray resultItems = responseJSON.getJSONArray("items");

                resultData = new String[resultItems.length()];
                for(int i = 0;i < resultItems.length();i++ ){
                    resultData[i] = resultItems.getJSONObject(i).getString("name") ;
                }

                // put the result in recycler view

                RecyclerView resultView = (RecyclerView) findViewById(R.id.result_list);
                ResultDataAdapter dataAdapter = new ResultDataAdapter(resultData,MainActivity.this);
                resultView.setAdapter(dataAdapter);
                resultView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                Toast.makeText(MainActivity.this,resultData[0],Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                Log.e("json_error", "onPostExecute: ", e);

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Uri baseUri = Uri.parse(this.baseUrlStr).buildUpon().appendQueryParameter("q",this.repoName.getText().toString()).appendQueryParameter("sort","stars").build();
        URL baseURL = null;
        try{
            baseURL = new URL(baseUri.toString());
            new GithubSearch().execute(baseURL);
        }catch (Exception e){
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }
}
