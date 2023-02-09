package com.example.myonlinebookself;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myonlinebookself.utils.Constants;
import com.example.myonlinebookself.utils.PreferenceUtils;

public class MOBLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mLoggingEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mLoggingEditText = (EditText) findViewById(R.id.usernameInput);
        mPasswordEditText = (EditText) findViewById(R.id.passwordInput);

        final String storedLogin = PreferenceUtils.getLogin();
        if(!TextUtils.isEmpty(storedLogin)){
            startActivity(getIntent(storedLogin));
        }
    }

    @Override
    public void onClick(View view){
        if(TextUtils.isEmpty(mLoggingEditText.getText())){
            Toast.makeText(this, "Empty login!", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(mPasswordEditText.getText())){
            Toast.makeText(this, "Empty password!", Toast.LENGTH_LONG).show();
            return;
        }

        PreferenceUtils.setLogin(mLoggingEditText.getText().toString());
        //PreferenceUtils.setPassword(mPasswordEditText.getText().toString());
        startActivity(getIntent(PreferenceUtils.getLogin()));
    }

    private Intent getIntent(String username){
        Intent intent = new Intent(this, MainActivity.class);
        final Bundle extras = new Bundle();
        extras.putString(Constants.Login.EXTRA_LOGIN, username);
        intent.putExtras(extras);
        return intent;
    }
}

