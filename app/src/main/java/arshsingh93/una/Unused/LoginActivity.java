package arshsingh93.una;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    protected EditText myUsername;
    protected EditText myPassword;
    protected Button myLoginButton;
    protected ProgressBar myProgressBar;

    protected TextView mySignUpTextView;
    protected TextView myForgottenView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.e(TAG, "started onCreate");
        super.onCreate(savedInstanceState);
        //Log.e(TAG, "called super");
        setContentView(R.layout.activity_login);

        //Log.e(TAG, "going to initialize textview for signup");
        mySignUpTextView = (TextView) findViewById(R.id.signupText);
        mySignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e(TAG, "sign up text clicked, headed to sign up activity");
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        //Log.e(TAG, "going to initialize textview for forgotten");
        myForgottenView = (TextView) findViewById(R.id.forgotLabel);
        myForgottenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e(TAG, "forgotten text clicked, headed to forgotten activity");
                Intent intent = new Intent(LoginActivity.this, ForgottenActivity.class);
                startActivity(intent);
            }
        });

        //Log.e(TAG, "going to initialize progressbar and edittexts and button");
        myProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
        myProgressBar.setVisibility(View.INVISIBLE); //originally not seen

        myUsername = (EditText) findViewById(R.id.usernameField);
        myPassword = (EditText) findViewById(R.id.passwordField);
        myLoginButton = (Button) findViewById(R.id.loginButton);
        myLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = myUsername.getText().toString();
                String password = myPassword.getText().toString();

                username = username.trim().toLowerCase();
                password = password.trim();

                if (username.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.login_error_message)
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    //login
                    toggleRefresh();
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            toggleRefresh();
                            if (e == null) {
                                //success
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.login_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void toggleRefresh() {
        if(myProgressBar.getVisibility() == View.INVISIBLE) {
            myProgressBar.setVisibility(View.VISIBLE);
        } else {
            myProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
