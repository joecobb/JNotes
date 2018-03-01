package apps.joe.com.jnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

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


                    new MaterialDialog.Builder(AddNoteActivity.this)
                            .title("Success")
                            .content("Note Successfully added")
                            .positiveText("ok")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    startActivity(new Intent(AddNoteActivity.this,MainActivity.class));
                                    finish();
                                }
                            })
                            .show();
                }
            }
        });

    }
}
