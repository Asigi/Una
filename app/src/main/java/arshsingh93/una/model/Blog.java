package arshsingh93.una.model;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Student on 7/30/2015.
 */
public class Blog {
    private ParseObject blog;

    public Blog(String theTitle, String theBody, ParseUser theWriter) {
        blog = new ParseObject("Blog");
        blog.put("Title", theTitle);
        if (theBody.substring(0,5) != "     ") {
            blog.put("Body", "     " + theBody); //added a tab to the front of this
        } else {
            blog.put("Body", theBody); //dont add the indent if it is already present
        }
        blog.put("User", theWriter);
        blog.put("Writer", theWriter.get("origName"));
        blog.put("Vote", 0);

        //set this blog to be editable by current user and readable by everyone.
        ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
        postACL.setPublicReadAccess(true);
        blog.setACL(postACL);
    }

    public Blog(ParseObject theObject) {
        blog = theObject;
    }


    public String toString() {
        return (String) blog.get("Title");
    }


   public ParseObject getBlog() {
       return blog;
   }

}
