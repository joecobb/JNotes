package apps.joe.com.jnotes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import apps.joe.com.jnotes.models.Note;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class NoteDetailActivity extends AppCompatActivity {
    String id;
    Realm realm;
    Note note;
    TextView title, content;
    Button btnEdit, btnShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        //creating handles to views
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        btnEdit = findViewById(R.id.btn_edit);
        btnShare = findViewById(R.id.btn_share);


        //getting id from MainActivity
        id = getIntent().getStringExtra("id");

        //getting instance of realm
        realm = Realm.getDefaultInstance();

        if(id != null){
            note = realm.where(Note.class)
                    .equalTo("id",id)
                    .findFirst();

            note.addChangeListener(new RealmChangeListener<Note>() {
                @Override
                public void onChange(Note realmModel) {
                    try {
                        title.setText(realmModel.getTitle());
                        content.setText(realmModel.getContent());
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
            if(note != null) {
                title.setText(note.getTitle());
                content.setText(note.getContent());

                btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "*Shared via JNotes* "+"\n\n"+"http://www.apps/playstore.com"+"\n\n"+note.getTitle()+"\n\n"+note.getContent()+"\n\n");
                        startActivity(Intent.createChooser(shareIntent, "Share note using"));
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(NoteDetailActivity.this,EditNoteActivity.class).putExtra("id",id));
                    }
                });


            }else{
                new MaterialDialog.Builder(NoteDetailActivity.this)
                        .title("Oops")
                        .content("Could not a load note")
                        .positiveText("ok")
                        .show();
            }
        }




    }
}
