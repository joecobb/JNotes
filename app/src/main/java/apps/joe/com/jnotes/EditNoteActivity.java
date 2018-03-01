package apps.joe.com.jnotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import apps.joe.com.jnotes.models.Note;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class EditNoteActivity extends AppCompatActivity {
    Button btnSave;
    EditText etTitle, etContent;
    String id;
    Realm realm;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        btnSave = findViewById(R.id.btn_save);
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

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                note.setTitle(etTitle.getText().toString());
                                note.setContent(etContent.getText().toString());
                                realm.copyToRealmOrUpdate(note);
                                finish();
                            }
                        });
                    }
                });


            }else{
                new MaterialDialog.Builder(EditNoteActivity.this)
                        .title("Oops")
                        .content("Could not a save note")
                        .positiveText("ok")
                        .show();
            }
        }

    }
}
