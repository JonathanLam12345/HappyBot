package com.myapp.jlam.motivationalbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myapp.jlam.motivationalbot.database.UserInfoDatabase;
import com.myapp.jlam.motivationalbot.ui.home.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;

public class AdditionalInfo extends AppCompatActivity
{
    PrintOut printOut;

    // Default values. If the user doesn't select anything if the number picker, it means use these values.
    String age = "12-17";
    String gender = "Male";
    UserInfoDatabase userInfoDatabase;

    //when counter reaches three, the screen will scroll down automatically.
    int counter = 0;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additional_info);

        userInfoDatabase = new UserInfoDatabase(getApplicationContext());
        final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollview));

        sharedPref = new SharedPref(getApplicationContext());



//        if (!getIntent().getExtras().getBoolean("New User"))
//        {
//            Intent currentIntent = getIntent();
//
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtras(currentIntent.getExtras());
//            startActivity(intent);
//        }
//        else
        {
            printOut = new PrintOut(getApplicationContext());
            Button buttonSubmit = findViewById((R.id.buttonSubmit));
            final EditText editText = findViewById(R.id.editTextName);
            NumberPicker numberPickerAge = findViewById((R.id.numberPickerAge));
            NumberPicker numberPickerGender = findViewById((R.id.numberPickerGender));

            final String[] ageArray = new String[]{"12-17", "18-24", "25-34", "35-44", "45-54", "55-64", "65-74", "75-84", "85-94", "105-114"};
            numberPickerAge.setMinValue(0);
            numberPickerAge.setMaxValue(ageArray.length - 1);
            numberPickerAge.setDisplayedValues(ageArray);
            numberPickerAge.setWrapSelectorWheel(false);
            numberPickerAge.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
            {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal)
                {
                    printOut.printThis(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + ageArray[newVal]);
                    age = ageArray[newVal] + "";
                }
            });

            final String[] genderArray = new String[]{"Male", "Female", "Prefer Not To Say"};
            numberPickerGender.setMinValue(0);
            numberPickerGender.setMaxValue(genderArray.length - 1);
            numberPickerGender.setDisplayedValues(genderArray);
            numberPickerGender.setWrapSelectorWheel(false);
            numberPickerGender.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
            {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal)
                {
                    printOut.printThis(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + genderArray[newVal] + "");
                    gender = genderArray[newVal];
                }
            });


            buttonSubmit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(editText.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please enter your first name.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        userInfoDatabase.setFirstName(editText.getText().toString());

                        new Analytics(getApplicationContext(), "Gender: " + gender, "gender_event");
                        new Analytics(getApplicationContext(), "Age: " + age, "age_event");
                        //get the current intent
                        Intent currentIntent = getIntent();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtras(currentIntent.getExtras());
                        intent.putExtra("firstName", editText.getText());
                        startActivity(intent);
                    }
                }
            });


            scrollview.post(new Runnable()
            {
                @Override
                public void run()
                {
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });

        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }
}
