package apps.joe.com.jnotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.UUID;

import apps.joe.com.jnotes.models.Note;
import io.realm.Realm;

public class AddNoteActivity extends AppCompatActivity {

    EditText etTitle,etContent;
    Realm realm;
    private MenuItem mSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Note");
        etTitle = findViewById(R.id.title);
        etContent = findViewById(R.id.content);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        mSave = menu.findItem(R.id.miSave);
        mSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(etTitle.getText().toString().isEmpty() || etContent.getText().toString().isEmpty()){
                    new MaterialDialog.Builder(AddNoteActivity.this)
                            .title("Notice")
                            .content("Please enter title and content")
                            .positiveText("ok")
                            .show();
                }else{
                    Note note = new Note();
                    note.setTitle(etTitle.getText().toString());
                    note.setContent(etContent.getText().toString());
                    note.setId(UUID.randomUUID().toString());
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(note);
                    realm.commitTransaction();
                    finish();
                }
                return true;
            }
        });
        return true;

    }


}
