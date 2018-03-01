package apps.joe.com.jnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import apps.joe.com.jnotes.adapters.NotesListAdapter;
import apps.joe.com.jnotes.models.Note;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RealmList<Note> notesList = new RealmList<>();
    private Realm realm;
    private NotesListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddNoteActivity.class));
            }
        });
        recyclerView = findViewById(R.id.recyclerView);

        mAdapter = new NotesListAdapter(notesList,getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        prepareNotesData();
    }

    private void prepareNotesData() {
        notesList.clear();
        notesList.addAll(realm.where(Note.class).findAll());
        mAdapter.notifyDataSetChanged();
//        notesList.addChangeListener(new RealmChangeListener<RealmList<Note>>() {
//            @Override
//            public void onChange(RealmList<Note> notes) {
//                if(notesList.size()>notes.size()){
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
//        });

    }
}
