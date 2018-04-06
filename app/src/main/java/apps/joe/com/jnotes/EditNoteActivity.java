package apps.joe.com.jnotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import apps.joe.com.jnotes.models.Note;
import io.realm.Realm;

public class EditNoteActivity extends AppCompatActivity implements ColorPickerDialogListener{
    EditText etTitle;
    private static final int DIALOG_TEXT_BACK_COLOR_ID = 1;
    String id;
    Realm realm;
    private Note note;
    private MenuItem mSave;
    private MenuItem mShare;
    private MenuItem mDelete;
    private MenuItem mFav;
    RTextEditorView editor;
    private Toolbar toolbar;
    private final InsertTableDialogFragment.OnInsertClickListener onInsertTableClickListener =
            new InsertTableDialogFragment.OnInsertClickListener() {
                @Override
                public void onInsertClick(int colCount, int rowCount) {
                    editor.insertTable(colCount, rowCount);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        editor = findViewById(R.id.editor_view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert));
        setSupportActionBar(toolbar);
        RTextEditorToolbar editorToolbar = findViewById(R.id.editor_toolbar);



        editorToolbar.setEditorView(editor);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Note");
        etTitle = findViewById(R.id.title);
        realm = Realm.getDefaultInstance();
        id = getIntent().getStringExtra("id");
        if(id != null){
            note = realm.where(Note.class)
                    .equalTo("id",id)
                    .findFirst();

            if(note != null) {
                etTitle.setText(note.getTitle());
                editor.setHtml(note.getContent());

//                editor.setFontSize(30);

            }else{
                new MaterialDialog.Builder(EditNoteActivity.this)
                        .title("Oops")
                        .content("Could not a load note")
                        .positiveText("ok")
                        .show();
            }
        }


        RTextEditorButton insertTableButton = findViewById(R.id.table);
        insertTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertTableDialogFragment dialog = InsertTableDialogFragment.newInstance();
                dialog.setOnInsertClickListener(onInsertTableClickListener);
                dialog.show(getSupportFragmentManager(), "insert-table-dialog");
            }
        });

        RTextEditorButton textBackColorButton = findViewById(R.id.text_back_color);
        textBackColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                ColorPickerDialog.newBuilder()
                        .setDialogId(DIALOG_TEXT_BACK_COLOR_ID)
                        .setDialogTitle(R.string.dialog_title_text_back_color)
                        .setShowAlphaSlider(false)
                        .setAllowCustom(true)
                        .show(EditNoteActivity.this);
            }
        });


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
                if(Html.fromHtml(editor.getHtml()).toString().trim().isEmpty()){
                    new MaterialDialog.Builder(EditNoteActivity.this)
                            .title("Oops")
                            .content("Please content cannot be empty")
                            .positiveText("ok")
                            .show();
                }else{
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            note.setTitle(etTitle.getText().toString());
                            note.setContent(editor.getHtml());
                            realm.copyToRealmOrUpdate(note);
                            EditNoteActivity.super.onBackPressed();
                        }
                    });
                }
                return true;
            }
        });
        mShare.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, "Shared via JNotes"+"\n"+note.getTitle()+"\n"+note.getContent()+"\n\n");
//                startActivity(Intent.createChooser(shareIntent, "Share note using"));

                if(note.getContent().toLowerCase().contains("</table>")){
                    new MaterialDialog.Builder(EditNoteActivity.this)
                            .title("Oops")
                            .content("Sorry you cannot share tables")
                            .positiveText("ok")
                            .show();
                }else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "Shared via JNotes"+"\nhttps://play.google.com/store/apps/details?id=apps.joe.com.jnotes&hl=en-GB\n\n"+note.getTitle()+"\n\n"+Html.fromHtml(note.getContent().toString()));
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("Shared via JNotes"+"<br><h3>"+note.getTitle()+"</h3><br>"+note.getContent()));
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));


                }
                return true;
            }
        });

        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        if (dialogId == DIALOG_TEXT_BACK_COLOR_ID) {
            editor.setTextBackgroundColor(color);
        }
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }


}
