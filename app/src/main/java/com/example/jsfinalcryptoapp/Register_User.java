package com.example.jsfinalcryptoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register_User extends AppCompatActivity {

    private FirebaseAuth mAuth;
    //Declaration of varibles
    private EditText fullName, age, Email, Password;
    private MaterialButton registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        fullName = (EditText) findViewById(R.id.full_username);
        age = (EditText) findViewById(R.id.age);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.passwd);
        registerButton = (MaterialButton) findViewById(R.id.registerBTN);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "Register user button pressed");
                //get text from the EditText variables
                String S_fullName = fullName.getText().toString();
                String S_age = age.getText().toString();
                String S_email = Email.getText().toString();
                String S_password = Password.getText().toString();


                //check if fields are empty
                if(S_fullName.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Full Name can't be empty!",Toast.LENGTH_LONG).show();
                    fullName.requestFocus();
                    return;
                }

                if(S_age.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Age can't be empty!",Toast.LENGTH_LONG).show();
                    age.requestFocus();
                    return;
                }

                if(S_email.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Email can't be empty!",Toast.LENGTH_LONG).show();
                    Email.requestFocus();
                    return;
                }

                //check if real email format
                if(!Patterns.EMAIL_ADDRESS.matcher(S_email).matches()){
                    Toast.makeText(getApplicationContext(),"Invalid email. Please enter a valid one!",Toast.LENGTH_LONG).show();
                    Email.requestFocus();
                    return;
                }

                if(S_password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Password can't be empty!",Toast.LENGTH_LONG).show();
                    Password.requestFocus();
                    return;
                }
                //check password length
                if(S_password.length() < 6 ){
                    Toast.makeText(getApplicationContext(),"Min password length should be 6 characters",Toast.LENGTH_LONG).show();
                    Password.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(S_email, S_password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    User user =  new User(S_fullName,S_age, S_email);

                                    FirebaseDatabase.getInstance().getReference("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                Toast.makeText(Register_User.this, "User has been registered", Toast.LENGTH_LONG).show();
                                                //redirect to next login page
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                                            } else{
                                                Toast.makeText(Register_User.this, "Failed to register new User! Try again!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(Register_User.this, "Failed to register new User! Try again!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }//end of onClick
        }); // end onClickListener register button

    } //end of onCreate
}