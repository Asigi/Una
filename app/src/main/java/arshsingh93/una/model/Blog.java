package arshsingh93.una.model;

import android.support.annotation.NonNull;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Student on 7/30/2015.
 */
public class Blog {
    private ParseObject blog;
    private String myId;

    public Blog(String theTitle, String theBody, ParseUser theWriter) {
        blog = new ParseObject("Blog");
        blog.put("Title", theTitle);
        blog.put("LikeList", Arrays.asList(new ArrayList<ParseUser>())); //creates an array in parse
        blog.get("LikeList");
        blog.put("User", theWriter);
        blog.put("Writer", theWriter.get("origName"));
        blog.put("Vote", 0);
        String spaceString = getSpaceString(theBody);
        blog.put("Body", spaceString + theBody); //added a tab to the front of this

        //set this blog to be editable by current user and readable by everyone.
        ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
        postACL.setPublicReadAccess(true);
        blog.setACL(postACL);
    }

    public Blog(ParseObject theObject) {
        blog = theObject;
        myId = theObject.getObjectId();
    }

    //this is code for indenting the body of the blog.
    private String getSpaceString(String theBody) {
        int lesser = -1;
        if (theBody.length() < 5) {
            lesser = theBody.length();
        } else {
            lesser = 5;
        }
        int spaceCount = 0;
        for (int i = 0; i < lesser; i++) {
            if (theBody.charAt(i) == ' ') {
                spaceCount ++;
            } else if (theBody.charAt(i) != ' ') {
                break;
            }
        }
        int putSpace = 5 - spaceCount;
        String spaceString = "";
        while (putSpace > 0) {
            spaceString += " ";
            putSpace --;
        }
        return spaceString;
    }




    public String toString() {
        return (String) blog.get("Title");
    }


    public ParseObject getBlog() {
       return blog;
   }


    public String getId() {
        return myId;
    }

    public boolean equalsBlog(Blog theBlog) {
        if (this.myId == theBlog.getId()) {
            return true;
        }
        return false;
    }


}
