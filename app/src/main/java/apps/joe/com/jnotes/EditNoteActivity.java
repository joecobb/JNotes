package apps.joe.com.jnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
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
    private MenuItem mDelete;
    private MenuItem mFav;

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
        super.onBackPressed();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        mSave = menu.findItem(R.id.miSave);
        mDelete = menu.findItem(R.id.miDelete);
        mDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                new MaterialDialog.Builder(EditNoteActivity.this)
                        .title("Warning")
                        .content("Are you sure you want to delete note?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        note.deleteFromRealm();
                                        finish();
                                    }
                                });
                            }
                        })
                        .show();
                return true;
            }
        });
        mFav = menu.findItem(R.id.miFav);
        if(note.isFavorite()){
            mFav.setIcon(R.drawable.ic_favorite);
        }
        mFav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if(note.isFavorite()) {
                                note.setFavorite(false);
                                mFav.setIcon(R.drawable.ic_favorite_border);
                            }else{
                                note.setFavorite(true);
                                mFav.setIcon(R.drawable.ic_favorite);
                            }
                            realm.copyToRealmOrUpdate(note);
                        }
                    });

                return true;
            }
        });
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
                        EditNoteActivity.super.onBackPressed();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
