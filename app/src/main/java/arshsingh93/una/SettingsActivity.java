package arshsingh93.una;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SettingsActivity extends AppCompatActivity {

    @InjectView(R.id.settingBlueButton) Button myBlueButton;
    @InjectView(R.id.settingGreenButton) Button myGreenButton;
    @InjectView(R.id.settingRedButton) Button myRedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(this.getClass().getSimpleName(), "onCreate");
        //getSupportActionBar().hide();
        TheUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick (R.id.settingBlueButton)
    public void changeToBlue(View view) {
        Log.e(this.getClass().getSimpleName(), "Clicked blue button");
        TheUtils.changeToTheme(this, TheUtils.THEME_BLUE);

    }

    @OnClick (R.id.settingGreenButton)
    public void changeToGreen(View view) {
        Log.e(this.getClass().getSimpleName(), "Clicked green button");
        TheUtils.changeToTheme(this, TheUtils.THEME_GREEN);
    }

    @OnClick (R.id.settingRedButton)
    public void changeToRed(View view) {
        Log.e(this.getClass().getSimpleName(), "Clicked red button");
        TheUtils.changeToTheme(this, TheUtils.THEME_RED);
    }


    // @InjectView(R.id.settingUpdateProfileButton) Button myProfUpdateButton;
    // @InjectView(R.id.settingSelectColorButton) Button myColorButton;

    //public static final String SHOW = "show";
    //public static final String SHOW_COLOR_OPTIONS = "show color options";
    //public static final String SHOW_UPDATE_PROFILE = "show update profile screen";


/**
    @OnClick (R.id.settingSelectColorButton)
    public void startSettings(View view) {
        Intent intent = new Intent(this, NoTabActivity.class);
        intent.putExtra(SHOW, SHOW_COLOR_OPTIONS);
        startActivity(intent);
    }
**/

}
