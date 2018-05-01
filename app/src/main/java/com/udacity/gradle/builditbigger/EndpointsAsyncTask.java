package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import com.example.myandroidlibrary.JokeDisplayActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

    private static MyApi myApiService = null;

    public View mView;

    public Context mContext;
    private ProgressBar progressBar;

    private static final String LOCALHOST_IP_ADDRESS = "http://10.0.2.2:8080/_ah/api/";

//    public interface TaskCompleteListener {
//
//        void onTaskComplete(String result);
//    }
//

public EndpointsAsyncTask(Context context, View view) {
    this.mContext = context;
    this.mView = view;

}

    @Override
    protected String doInBackground(Pair<Context, String>... params) {

        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl(LOCALHOST_IP_ADDRESS)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

//        mainActivityFragment = params[0];
//        context = mainActivityFragment.getActivity();
////        String name = params[0].second;

        try {
            return String.valueOf(myApiService.showJoke().execute().getData());
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Create Intent to launch JokeFactory Activity
        Intent intent = new Intent(mContext, JokeDisplayActivity.class);
        // Put the string in the envelope
        intent.putExtra(JokeDisplayActivity.JOKE_KEY,result);
        mContext.startActivity(intent);
//
//        Toast.makeText(context, result, Toast.LENGTH_LONG).show();

//        mainActivityFragment.loadedJoke = result;
//        mainActivityFragment.launchDisplayJokeActivity();
    }

    public interface AsyncTaskCallback {
        void callBack(String joke);
    }
}