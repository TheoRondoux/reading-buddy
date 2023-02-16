package com.example.myonlinebookself;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myonlinebookself.utils.Constants;
import com.example.myonlinebookself.utils.PreferenceUtils;
import com.example.myonlinebookself.utils.QuoteResponse;
import com.example.myonlinebookself.utils.QuoteResponseListener;
import com.example.myonlinebookself.utils.RequestManager;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Set;

public class MOBLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mLoggingEditText;
    private EditText mPasswordEditText;
    private TextView mQuoteText;
    private TextView mAuthorText;
    RequestManager manager;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mLoggingEditText = (EditText) findViewById(R.id.usernameInput);
        mPasswordEditText = (EditText) findViewById(R.id.passwordInput);
        mQuoteText = (TextView) findViewById(R.id.quoteTextHolder);
        mAuthorText = (TextView) findViewById(R.id.authorTextHolder);

        manager = new RequestManager(this);
        manager.getAllQuotes(listner);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();

        final String storedLogin = PreferenceUtils.getLogin();
        if(!TextUtils.isEmpty(storedLogin)){
            startActivity(getIntent(storedLogin));
        }
    }

    private final QuoteResponseListener listner = new QuoteResponseListener() {
        @Override
        public void didFetch(List<QuoteResponse> response, String message) {
            showRandomQuote(response, mQuoteText, mAuthorText);
            dialog.dismiss();
        }

        @Override
        public void didError(String message) {
            dialog.dismiss();
            Toast.makeText(MOBLoginActivity.this, message, Toast.LENGTH_SHORT);
        }
    };

    //Function that shows a random quote
    private void showRandomQuote(List<QuoteResponse> response, TextView mQuoteText, TextView mAuthorText) {
        int nbOfQuote = response.size();                                    //Getting the number of elements in the response of the HTTP request
        int randomQuote = (int)Math.floor(Math.random() * nbOfQuote+1);     //Generating a random number to choose a quote
        mQuoteText.setText("\""+response.get(randomQuote).getText()+"\"");  //Setting the quote text to the appropriate TextView
        if (!response.get(randomQuote).getAuthor().isEmpty()){              //If the author is not null...
            mAuthorText.setText(response.get(randomQuote).getAuthor());     //Setting the author of the quote to the appropriate TextView
        }
        else{
            mAuthorText.setText("");                                        //Else, setting the author TextView with no text
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

    public void gotoSignUp(View view){
        startActivity(new Intent(this, MOBSignUpActivity.class));
    }

    private Intent getIntent(String username){
        Intent intent = new Intent(this, MainActivity.class);
        final Bundle extras = new Bundle();
        extras.putString(Constants.Login.EXTRA_LOGIN, username);
        intent.putExtras(extras);
        return intent;
    }

}

