package edu.udemylearning.simplenotesapplication;

import static java.util.regex.Pattern.matches;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class CreateAccountActivity extends AppCompatActivity {
    EditText signupEmail,signupPassword,signupConfirmPass;
    TextView loginBtnText;
    ProgressBar progressBar;
    Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupConfirmPass = findViewById(R.id.signup_confirm_password);
        loginBtnText = findViewById(R.id.login_btn_signin);
        progressBar = findViewById(R.id.progress_bar);
        createButton = findViewById(R.id.signup_button);

        createButton.setOnClickListener(V-> createAccount());
        loginBtnText.setOnClickListener(V-> finish());
    }
    void createAccount(){
        String email =  signupEmail.getText().toString();
        String password = signupPassword.getText().toString();
        String confirmPassword = signupConfirmPass.getText().toString();
        boolean isValidated = validateData(email,password,confirmPassword);
        if (!isValidated){
            return;
        }
            createAccountInfireBase(email,password);
    }
    void createAccountInfireBase(String email , String password){
        changeInProgressBar(true);//shpwing the progress bar while creating account
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgressBar(false);//hiding the progress bar after completion of creating account
                        if (task.isSuccessful()){
                            //creating account is done
                            Utility.showToast(CreateAccountActivity.this,"Account Created Successfully. Check Email To Verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else {
                            // failure
                            Utility.showToast(CreateAccountActivity.this,task.getException().getLocalizedMessage());

                        }
                    }
                });


    }
    void changeInProgressBar(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createButton.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            createButton.setVisibility(View.VISIBLE);
        }

    }
    boolean validateData(String email , String password , String confirmPassword){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signupEmail.setError("Invalid Email");
            return  false;
        }
        if(password.length() < 6){
            signupPassword.setError("Password length invalid");
            return false;
        }
        if(!password.equals(confirmPassword)){
            signupConfirmPass.setError("Password didn't  matched");
            return false;
        }
        return true;
    }
}