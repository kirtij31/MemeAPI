package com.example.memeapi;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    ImageView memeImageView ;
    ProgressBar progressBar;
    TextView errorTextView;

    String currentImageUrl = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memeImageView = findViewById(R.id.memeImage);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.error_text);

        loadMeme();
    }

    private void loadMeme(){
        progressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://meme-api.herokuapp.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                currentImageUrl = response.getString("url");
                Glide.with(MainActivity.this).load(currentImageUrl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        errorTextView.setText(R.string.speed_check);
                        errorTextView.setVisibility(View.VISIBLE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(memeImageView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> { progressBar.setVisibility(View.GONE);
             errorTextView.setText(R.string.connection_check);
             errorTextView.setVisibility(View.VISIBLE); });

        requestQueue.add(jsonObjectRequest);
    }

    public void next(View view) {
        loadMeme();
    }

    public void share(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, currentImageUrl);
        sendIntent.setType("text/plain");

       // Intent shareIntent = Intent.createChooser(sendIntent, null);
        //startActivity(shareIntent);

        startActivity(sendIntent);
    }
}