package com.myapp.jlam.motivationalbot.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.myapp.jlam.motivationalbot.LoginActivity;
import com.myapp.jlam.motivationalbot.PrintOut;
import com.myapp.jlam.motivationalbot.R;
import com.myapp.jlam.motivationalbot.SharedPref;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LogoutFragment extends Fragment
{
    private PrintOut printOut;
    private Button buttonLogout;
    private SharedPref sharedPref;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Button buttonSignOut;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        mAuth = FirebaseAuth.getInstance();
        sharedPref = new SharedPref(getContext());
        printOut = new PrintOut(getContext());
        printOut.printThis("Logout Fragment!!!");


        buttonLogout = (Button) view.findViewById(R.id.sign_out_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        buttonLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mAuth.signOut();

                mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                        new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                sharedPref.storeInt("loginStatus", 0);

                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });

                printOut.printThis("You've just logged out!!!");
            }
        });
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //  Intent intent = new Intent(getActivity(), LogoutActivity.class);
        //  startActivity(intent);
    }


}