package apps.joe.com.jnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

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
                finish();
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
        Log.d("hmm",notesList.size()+"");
//         notesList.addChangeListener(new RealmChangeListener<RealmList<Note>>() {
//            @Override
//            public void onChange(RealmList<Note> notes) {
//                try {
//
//
//                if(notesList.size()<notes.size()){
//                    notesList.clear();
//                    notesList.addAll(realm.where(Note.class).findAll());
//                    mAdapter.notifyDataSetChanged();
//                }
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        });

    }

}
