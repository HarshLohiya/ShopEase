package com.example.newEcom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newEcom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {
    ProgressBar progressBar;
    EditText nameEditText, emailEditText, passEditText;
    ImageView nextBtn;
    TextView loginPageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressBar = findViewById(R.id.progress_bar);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passEditText = findViewById(R.id.passEditText);
        nextBtn = findViewById(R.id.nextBtn);
        loginPageBtn = findViewById(R.id.loginPageBtn);

        nextBtn.setOnClickListener(v -> createAccount());

        loginPageBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void createAccount() {
        String email = emailEditText.getText().toString();
        String pass = passEditText.getText().toString();
//        String confPass = confPassEditText.getText().toString();

        boolean isValidated = validate(email, pass);
        if (!isValidated)
            return;

        createAccountInFirebase(email,pass);
    }

    void createAccountInFirebase(String email, String pass) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "Successfully created account, check email to verify", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(nameEditText.getText().toString()).build();
                            user.updateProfile(profileUpdates);
                            firebaseAuth.signOut();
                            finish();
                        }
                        else {
                            Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validate(String email, String pass){
        int flag=0;
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            flag=1;
        }
        if (pass.length() < 6){
            passEditText.setError("Password must be of six characters");
            flag=1;
        }
//        if (!pass.equals(confPass)){
//            confPassEditText.setError("Confirm Password not matched");
//            flag=1;
//        }
        return flag == 0;
    }
}