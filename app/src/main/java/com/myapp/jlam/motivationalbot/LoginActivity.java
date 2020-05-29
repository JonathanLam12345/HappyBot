package com.myapp.jlam.motivationalbot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.jlam.motivationalbot.database.UserInfoDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class LoginActivity extends AppCompatActivity
{
    private PrintOut printOut;
    private SignInButton buttonSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    private int RC_SIGN_IN = 1;

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPref sharedPref;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        printOut = new PrintOut(getApplicationContext());
        sharedPref = new SharedPref(getApplicationContext());
        buttonSignIn = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();


        // Exit app where appropriate
        sharedPref.storeBoolean("Exit", false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        buttonSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signIn();
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        int loginStatus = sharedPref.retrieveInt("loginStatus");

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser;

        //user logged out of the app already
        if (loginStatus == 0)
        {
            currentUser = null;
        }
        else
        {
            currentUser = mAuth.getCurrentUser();
        }
        updateUI(currentUser);
    }


    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                printOut.printThis("Google Sign in was successful");
                firebaseAuthWithGoogle(account);
            } catch (ApiException e)
            {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                printOut.printThis("Google Sign in was NOT successful" + e);
                updateUI(null);
            }
        }
    }

    //Sign in successful. Now, authenticate with Firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        printOut.printThis("firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            printOut.printThis("signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        //  hideProgressBar();
                    }
                });
    }


    private void updateUI(FirebaseUser user)
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        //successful
        if (user != null)
        {
            final String displayName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            final String personEmail = account.getEmail();
            String personId = account.getId();
            final Uri personPhoto = account.getPhotoUrl();

            // login success
            sharedPref.storeInt("loginStatus", 1);

            String displayInfo = displayName + "\n" + personGivenName + "\n" + personFamilyName + "\n" + personEmail + "\n" + personId + "\n" + personPhoto + "\n";
            final String id = personEmail.replace(".", "").replace("@", "").replace("@", "").replace("#", "");
            final Bundle bundle = new Bundle();

            mUserRef.child(id).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot snapshot)
                {
                    // new user
                    if (snapshot.getValue() == null)
                    {
                        bundle.putBoolean("New User", true);
                        UserProfile userProfile = new UserProfile();
                        userProfile.setDisplayName(displayName);
                        userProfile.setEmail(personEmail);
                        userProfile.setImage(personPhoto.toString());
                        mUserRef.child(id).setValue(userProfile);
                    }
                    // existing user
                    // The only field that should be updating is the unix time.
                    // update all fields since user might update their personal info.
                    else
                    {
                        bundle.putBoolean("New User", false);
                        printOut.printThis("Welcome Back!");
                        mUserRef.child(id).child("displayName").setValue(displayName);
                        mUserRef.child(id).child("image").setValue(personPhoto.toString());
                    }

                    UserInfoDatabase userInfoDatabase = new UserInfoDatabase(getApplicationContext());
                    userInfoDatabase.setDisplayName(displayName);
                    userInfoDatabase.setEmail(personEmail);
                    userInfoDatabase.setImageURL(personPhoto.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                }
            });

            printOut.printThis("User Info: " + displayInfo);
            Intent intent = new Intent(getApplicationContext(), AdditionalInfo.class);

            bundle.putString("Name", displayName);
            bundle.putString("Email", personEmail);
            bundle.putString("Image", personPhoto.toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
        {
            sharedPref.storeInt("loginStatus", 0);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        printOut.printThis("onSupportNavigateUp");
        // new HomeFragment().saveCurrentPageURL();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    long back_pressed;

    @Override
    public void onBackPressed()
    {
        printOut.printThis("onBackPressed");

        if (back_pressed + 2000 > System.currentTimeMillis())
        {
            super.onBackPressed();
            Toast.makeText(getApplicationContext(), "Bye", Toast.LENGTH_SHORT).show();

            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }


}
