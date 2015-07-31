package arshsingh93.una.model;

import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Student on 7/30/2015.
 */
public class Blog {
    public ParseObject blog;

    public Blog(String theTitle, String theBody, ParseUser theWriter) {
        blog = new ParseObject("Blog");
        blog.put("Title", theTitle);
        blog.put("Body", theBody);
        blog.put("User", theWriter);
        blog.put("Writer", theWriter.get("origName"));
    }


   public ParseObject getBlog() {
       return blog;
   }

}
