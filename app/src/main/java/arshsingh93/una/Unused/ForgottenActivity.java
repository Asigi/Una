package arshsingh93.una;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgottenActivity extends AppCompatActivity {

    protected EditText myEmail;
    protected Button myResetButton;

    protected ProgressBar myProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten);

        myProgressBar = (ProgressBar) findViewById(R.id.forgottenProgressBar);
        myProgressBar.setVisibility(View.INVISIBLE);

        myEmail = (EditText) findViewById(R.id.forgottenEmailField);
        myResetButton = (Button) findViewById(R.id.forgottenResetButton);
        myResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = myEmail.getText().toString();
                email = email.trim().toLowerCase();

                if (email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgottenActivity.this);
                    builder.setMessage("Please enter an email address")
                            .setTitle("Oops")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    toggleRefresh();

                    ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                        public void done(ParseException e) {
                            toggleRefresh();
                            if (e == null) {
                                // An email was successfully sent with reset instructions.

                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgottenActivity.this);
                                builder.setMessage("A reset link will be sent to your email shortly.")
                                        .setTitle("Reset Link")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //navigate to login screen
                                                Intent intent = new Intent(ForgottenActivity.this, LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();


                            } else {
                                // Something went wrong. Look at the ParseException to see what's up.

                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgottenActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle("Oops, something went wrong!")
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
        getMenuInflater().inflate(R.menu.menu_forgotten, menu);
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
