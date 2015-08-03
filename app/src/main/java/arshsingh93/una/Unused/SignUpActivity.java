package arshsingh93.una;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    protected EditText myUsername;
    protected EditText myPassword;
    protected EditText myEmail;
    protected Button mySignUpButton;

    protected ProgressBar mySignUpProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mySignUpProgressBar = (ProgressBar) findViewById(R.id.signUpProgressBar);
        mySignUpProgressBar.setVisibility(View.INVISIBLE);
        myUsername = (EditText) findViewById(R.id.usernameField);
        myPassword = (EditText) findViewById(R.id.passwordField);
        myEmail = (EditText) findViewById(R.id.emailField);
        mySignUpButton = (Button) findViewById(R.id.signupButton);
        mySignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String originalUsername = myUsername.getText().toString().trim();
                String password = myPassword.getText().toString();
                String email = myEmail.getText().toString();

                String lowerUsername = originalUsername.toLowerCase(); //this is the username is all lowercase letters.
                password = password.trim();
                email = email.trim().toLowerCase();


                if (originalUsername.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    toggleRefresh();
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(lowerUsername); //sign up with lowercase username for uniqueness
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.put("origName", originalUsername); //keep the original username intact for display
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            toggleRefresh();
                            if (e == null) {
                                //success
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.signup_error_title)
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
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
        if(mySignUpProgressBar.getVisibility() == View.INVISIBLE) {
            mySignUpProgressBar.setVisibility(View.VISIBLE);
        } else {
            mySignUpProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
