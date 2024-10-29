package edu.udemylearning.simplenotesapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmail, loginPassword;
    Button loginBtn;
    ProgressBar progressBar;
    TextView signupAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        signupAccount = findViewById(R.id.signup_account);

        loginBtn.setOnClickListener((V)->loginAccount());
        signupAccount.setOnClickListener((V)->startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class)));

    }
        void loginAccount(){
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();
        boolean isValidateData = validateData(email,password);
        if (!isValidateData){
            return;
        }
             loginAccountInFirebase(email,password);
    }

    void loginAccountInFirebase(String email ,String password){
        changeInprogress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInprogress(false);
                if (task.isSuccessful()){
                    //login is success
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else {
                        Utility.showToast(LoginActivity.this,"Email not verified, Please verify the email.");
                    }
                }else {
                    //login failed
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInprogress(boolean progress){
        if(progress){
            loginBtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            loginBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
    boolean validateData(String email , String password){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmail.setError("Invalid Email");
            return false;
        }
        if (password.length()<6){
            loginPassword.setError("Invalid Password");
            return false;
        }
        return true;
    }
}