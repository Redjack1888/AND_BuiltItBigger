package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.myandroidlibrary.JokeDisplayActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.Objects;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    private ProgressBar progressBar = null;
    public String loadedJoke = null;

    public boolean testFlag = false;
    private PublisherInterstitialAd mPublisherInterstitialAd = null;
    private final String LOG_TAG = "FREEDEBUG";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        AdView mAdView = root.findViewById(R.id.adView);

        //Set up for pre-fetching interstitial ad request
        mPublisherInterstitialAd = new PublisherInterstitialAd(Objects.requireNonNull(getContext()));
        mPublisherInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        mPublisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //process the joke Request
                progressBar.setVisibility(View.VISIBLE);
                tellJoke();

                //pre-fetch the next ad
                requestNewInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);

                Log.i(LOG_TAG, "onAdFailedToLoad: ad Failed to load. Reloading...");

                //prefetch the next ad
                requestNewInterstitial();

            }

            @Override
            public void onAdLoaded() {
                Log.i(LOG_TAG, "onAdLoaded: interstitial is ready!");
                super.onAdLoaded();
            }
        });

        //Kick off the fetch
        requestNewInterstitial();

        // Set onClickListener for the button
        Button button = root.findViewById(R.id.joke_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mPublisherInterstitialAd.isLoaded()) {
                    Log.i(LOG_TAG, "onClick: interstitial was ready");
                    mPublisherInterstitialAd.show();
                } else {
                    Log.i(LOG_TAG, "onClick: interstitial was not ready.");
                    progressBar.setVisibility(View.VISIBLE);
                    tellJoke();
                }
            }
        });

        progressBar = root.findViewById(R.id.joke_progressbar);
        progressBar.setVisibility(View.GONE);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return root;
    }

    public void tellJoke(){
        new EndpointsAsyncTask().execute(this);
    }

    public void launchDisplayJokeActivity(){
        if (!testFlag) {
            Context context = getActivity();
            Intent intent = new Intent(context, JokeDisplayActivity.class);
            assert context != null;
            intent.putExtra(context.getString(R.string.jokeEnvelope), loadedJoke);
            context.startActivity(intent);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void requestNewInterstitial() {
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("EA27D37DF5448BF42AA5F7A6D4F11A9B")
                .build();

        mPublisherInterstitialAd.loadAd(adRequest);
    }
}
