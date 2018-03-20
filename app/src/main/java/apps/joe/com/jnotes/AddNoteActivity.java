package apps.joe.com.jnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.util.UUID;

import apps.joe.com.jnotes.models.Note;
import io.realm.Realm;

public class AddNoteActivity extends AppCompatActivity {
    Button btnAdd;
    EditText etTitle,etContent;
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Note");
        etTitle = findViewById(R.id.title);
        etContent = findViewById(R.id.content);
        btnAdd = findViewById(R.id.btn_add);
        realm = Realm.getDefaultInstance();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    startActivity(new Intent(AddNoteActivity.this,MainActivity.class));
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddNoteActivity.this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(AddNoteActivity.this,MainActivity.class));
        finish();
        return true;
    }
}
