package arshsingh93.una;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Student on 7/16/2015.
 */
public class UnaApplication extends Application { //the application class is the entry point for when an app starts.


    @Override
    public void onCreate(){
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "2i0CNaI5uozJ7BCGXMpfr1yucrJkYlPpg207wM4N",
                "WtMn4uEQKXobFnNYasRsPKtiADfiyS9AVtE5gKQ6");
    }



}
