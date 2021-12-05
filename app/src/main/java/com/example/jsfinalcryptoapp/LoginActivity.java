package com.example.jsfinalcryptoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity  {

    TextView create_user, forgot_User;
    EditText  email,password;

    FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //declaration of variables
        email = (EditText) findViewById(R.id.email);
        password = (EditText)  findViewById(R.id.password);
        create_user = (TextView) findViewById(R.id.create_user);
        forgot_User = (TextView) findViewById(R.id.forgotPASS);
        ImageView google_login = (ImageView) findViewById(R.id.googleBTN);
        ImageView facebook_login = (ImageView) findViewById(R.id.githubBTN);
        MaterialButton loginButton = (MaterialButton) findViewById(R.id.LoginBTN);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


    } // end of on create

    @Override
    protected void onStart() {
        super.onStart();
        //initialize firebase user
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    public void login_User(){
        String S_email = email.getText().toString();
        String S_passwd = password.getText().toString();
        if(S_email.isEmpty()){
            Toast.makeText(getApplicationContext(),"Email can't be empty!",Toast.LENGTH_LONG).show();
            email.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(S_email).matches()){
            //check if real email format
            Toast.makeText(getApplicationContext(),"Invalid email. Please enter a valid one!",Toast.LENGTH_LONG).show();
            email.requestFocus();
            return;
        } else if(S_passwd.isEmpty()){
            Toast.makeText(getApplicationContext(),"Password can't be empty!",Toast.LENGTH_LONG).show();
            password.requestFocus();
            return;
        }else{
            //if fields correctly formated OR not empty login
            mAuth.signInWithEmailAndPassword(S_email,S_passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"User Successfully logged in!", Toast.LENGTH_LONG).show();
                        //send user to the corresponding page
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else{
                        //loggin Error.
                        Toast.makeText(getApplicationContext(),"Error loggin in. Please try again", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    } //end of login_User


    public void Google_login(){
        Log.e("TAG","in google login ");
        //initialize sign in intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    } //end of google login

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("TAG", "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]//end of onActivityResult


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    // [END auth_with_google]

    public void github_login(){
        Intent intent = new Intent(getApplicationContext(), GithubLogin.class);
        startActivity(intent);
    }//end of github login

    private void updateUI(FirebaseUser user) {
        if (user != null){
            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
            //send user to the corresponding page
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else{
            Log.e("TAG", "Error Logging in!");

        }
    }


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_user:
                startActivity(new Intent(this, Register_User.class));
                break;

            case R.id.LoginBTN:
                login_User();
                break;

            case R.id.googleBTN:
                Google_login();
                break;

            case R.id.githubBTN:
                github_login();
                break;
        }
    } //end of on click


} //end login
