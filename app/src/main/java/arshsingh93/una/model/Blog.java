package arshsingh93.una.model;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Student on 7/30/2015.
 */
public class Blog {
    public ParseObject blog;
    public String myID;

    public Blog(String theTitle, String theBody, ParseUser theWriter) {
        blog = new ParseObject("Blog");
        blog.put("Title", theTitle);
        blog.put("Body", theBody);
        blog.put("User", theWriter);
        blog.put("Writer", theWriter.get("origName"));

        //set this blog to be editable by current user and readable by everyone.
        ParseACL postACL = new ParseACL(ParseUser.getCurrentUser());
        postACL.setPublicReadAccess(true);
        blog.setACL(postACL);

        myID = blog.getObjectId();
    }




   public ParseObject getBlog() {
       return blog;
   }

}
