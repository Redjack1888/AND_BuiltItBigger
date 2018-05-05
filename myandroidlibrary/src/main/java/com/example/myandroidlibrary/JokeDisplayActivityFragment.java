package com.example.myandroidlibrary;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class JokeDisplayActivityFragment extends Fragment {

    public JokeDisplayActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_joke, container, false);

        String joke;
        Intent intent = getActivity().getIntent();
        joke = intent.getStringExtra(getString(R.string.jokeEnvelope));

        TextView jokeTextView = root.findViewById(R.id.joke_text_view);
        if (!TextUtils.isEmpty(joke)) {
            jokeTextView.setText(joke);
        }else {
            jokeTextView.setText(R.string.dig_deeper);
        }

        return root;
    }
}