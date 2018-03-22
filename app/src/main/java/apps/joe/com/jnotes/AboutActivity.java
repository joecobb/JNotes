package apps.joe.com.jnotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        Element versionElement = new Element();
//        versionElement.setTitle("Version 1.0");
//        Element linkedInElement = new Element();
//        linkedInElement.setTitle("LinkedIn");
//        linkedInElement.setValue("https://www.linkedin.com/in/joseph-cobbinah-788291b5/");
//        View aboutPage = new AboutPage(this)
//                .isRTL(false)
//                .setImage(R.drawable.pp)
//                .addItem(versionElement)
//                .setDescription("Joseph Ofori Cobbinah\nSoftware Engineer\nThis is just a simple note-taking app\nThanks so much for downloading")
//                .addGroup("Let's Connect")
//                .addItem(linkedInElement)
//                .addEmail("josephcobbinah.jc@gmail.com")
//                .create();

    }
    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }
}
