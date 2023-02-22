package com.example.myonlinebookself;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MOBSignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
        {
            currentUser.reload();
        }
    }

    public void createAccount(View view)
    {
        final EditText mUsernameInput = (EditText) findViewById(R.id.emailInputRegister);
        final EditText mPasswordInput = (EditText) findViewById(R.id.passwordInputRegister);
        final EditText mPasswordConfirmInput = (EditText) findViewById(R.id.passwordConfirmInputRegister);
        if (TextUtils.isEmpty(mUsernameInput.getText()))
        {
            Toast.makeText(this, "Email field is empty!", Toast.LENGTH_LONG).show();
            return;
        }
        if (mPasswordInput.getText().length() < 8)
        {
            Toast.makeText(this, "Password must contains at least 8 characters", Toast.LENGTH_LONG).show();
            return;
        }
        if (!TextUtils.equals(mPasswordInput.getText(), mPasswordConfirmInput.getText()))
        {
            Toast.makeText(this, "Password does not match!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(String.valueOf(mUsernameInput.getText()), String.valueOf(mPasswordInput.getText()))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MOBSignUpActivity.this, "Account created !", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MOBSignUpActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
