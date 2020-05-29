package com.myapp.jlam.motivationalbot.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.jlam.motivationalbot.Analytics;
import com.myapp.jlam.motivationalbot.R;
import com.myapp.jlam.motivationalbot.UserProfile;
import com.myapp.jlam.motivationalbot.adapter.MyCustomAdapter;
import com.myapp.jlam.motivationalbot.database.UserInfoDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment
{

    private HomeViewModel homeViewModel;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    private ListView mList;
    private ArrayList<String> arrayList;
    private MyCustomAdapter mAdapter;
    private ImageButton mbtSpeak;
    UserInfoDatabase userInfoDatabase;
    String firstName;
    String message;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        userInfoDatabase = new UserInfoDatabase(getContext());
        arrayList = new ArrayList<String>();

        arrayList.add("Bot: Welcome " + userInfoDatabase.getFirstName() + " to the Happy App!!!");
        final EditText editText = root.findViewById(R.id.editText);
        ImageButton send = root.findViewById(R.id.send_button);

        // relate the listView from java to the one created in xml
        mList = root.findViewById(R.id.list);
        mbtSpeak = root.findViewById(R.id.btSpeak);
        checkVoiceRecognition();

        mAdapter = new MyCustomAdapter(getContext(), arrayList);
        mList.setAdapter(mAdapter);

        send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!editText.getText().toString().equals(""))
                {
                    String message = editText.getText().toString();

                    storeMessage(message);

                    // add the text in the arrayList
                    arrayList.add("You: " + message);

                    mAdapter.notifyDataSetChanged();
                    editText.setText("");
                    new Analytics(getContext(), "Message sent by text", "sent_type_event");
                    hideKeyboard(view);
                }
            }
        });

        mbtSpeak.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                speak();
                new Analytics(getContext(), "Message sent by voice.", "sent_type_event");
            }
        });

        return root;
    }

    // Stores user's sent message to database
    public void storeMessage(final String message)
    {
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mUserRef = mRootRef.child("messages");

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                long unixTime = System.currentTimeMillis() / 1000L;
                int randomNum = 1 + (int) (Math.random() * 99);   // 1-99
                mUserRef.child(unixTime + "-" + randomNum).setValue(message + "/// " + userInfoDatabase.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


    public void checkVoiceRecognition()
    {
        // Check if voice recognition is present
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            mbtSpeak.setEnabled(false);
            showToastMessage("Voice recognizer not present");
        }
    }

    public void speak()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

            // If Voice recognition is successful then it returns RESULT_OK
            if (resultCode == RESULT_OK)
            {
                ArrayList<String> textMatchList = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (!textMatchList.isEmpty())
                {
                    String searchQuery = textMatchList.get(0);

                    // add the text in the arrayList
                    arrayList.add("You: " + searchQuery);
                    storeMessage(searchQuery);
                    mAdapter.notifyDataSetChanged();

                }
                // Result code for various error.
            }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void hideKeyboard(View view)
    {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    void showToastMessage(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


}
