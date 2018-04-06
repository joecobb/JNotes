package apps.joe.com.jnotes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.util.UUID;

import apps.joe.com.jnotes.models.Note;
import io.realm.Realm;

public class AddNoteActivity extends AppCompatActivity implements ColorPickerDialogListener{

    EditText etTitle;
    private static final int DIALOG_TEXT_BACK_COLOR_ID = 1;
    Realm realm;
    private MenuItem mSave;
    private RTextEditorView editor;
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
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Note");
        etTitle = findViewById(R.id.title);
        editor = findViewById(R.id.editor_view);
        RTextEditorToolbar editorToolbar = findViewById(R.id.editor_toolbar);
        realm = Realm.getDefaultInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        editorToolbar.setEditorView(editor);


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
                        .show(AddNoteActivity.this);
            }
        });
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
                if(Html.fromHtml(editor.getHtml()).toString().trim().isEmpty() ){
                    new MaterialDialog.Builder(AddNoteActivity.this)
                            .title("Notice")
                            .content("Please enter content")
                            .positiveText("ok")
                            .show();
                }else{
                    Note note = new Note();
                    note.setTitle(etTitle.getText().toString());
                    note.setContent(editor.getHtml());
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
