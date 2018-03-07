package apps.joe.com.jnotes.models;

import java.text.DateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by APPUSER1 on 17/10/2017.
 */

public class Note extends RealmObject{
    @PrimaryKey
    private String id;
    private String title;
    private String content;
    private String dateCreated;
    private Boolean isFavorite;

    public Note(String title, String content, String dateCreated) {
        this.title = title;
        this.content = content;
        this.dateCreated = dateCreated;
        this.isFavorite = false;
    }
    public Note(){
        Date date = new Date();
        android.text.format.DateFormat df = new android.text.format.DateFormat();
//        this.dateCreated = String.valueOf(df.format("mmm d, yyyy  MM:HH", date));
        this.dateCreated= DateFormat.getDateTimeInstance().format(date);
        this.isFavorite = false;
    }

    public Boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
