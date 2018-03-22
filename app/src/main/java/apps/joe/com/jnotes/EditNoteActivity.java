package apps.joe.com.jnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import apps.joe.com.jnotes.models.Note;
import io.realm.Realm;

public class EditNoteActivity extends AppCompatActivity {
    EditText etTitle, etContent;
    String id;
    Realm realm;
    private Note note;
    private MenuItem mSave;
    private MenuItem mShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Note");
        etTitle = findViewById(R.id.title);
        etContent = findViewById(R.id.content);
        realm = Realm.getDefaultInstance();
        id = getIntent().getStringExtra("id");
        if(id != null){
            note = realm.where(Note.class)
                    .equalTo("id",id)
                    .findFirst();

            if(note != null) {
                etTitle.setText(note.getTitle());
                etContent.setText(note.getContent());
            }else{
                new MaterialDialog.Builder(EditNoteActivity.this)
                        .title("Oops")
                        .content("Could not a save note")
                        .positiveText("ok")
                        .show();
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        mSave = menu.findItem(R.id.miSave);
        mShare = menu.findItem(R.id.miShare);
        mSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        note.setTitle(etTitle.getText().toString());
                        note.setContent(etContent.getText().toString());
                        realm.copyToRealmOrUpdate(note);
                        finish();
                    }
                });
                return true;
            }
        });
        mShare.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Shared via JNotes"+"\n"+note.getTitle()+"\n"+note.getContent()+"\n\n");
                startActivity(Intent.createChooser(shareIntent, "Share note using"));
                return true;
            }
        });

        return true;

    }

}
