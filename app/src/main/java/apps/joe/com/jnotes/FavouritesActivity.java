package apps.joe.com.jnotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import apps.joe.com.jnotes.adapters.FavoriteNotesListAdapter;
import apps.joe.com.jnotes.models.Note;
import io.realm.Realm;
import io.realm.RealmList;

public class FavouritesActivity extends AppCompatActivity {

    private Realm realm;
    private LinearLayout nodata;
    private Toolbar toolbar;
    private EmptyRecyclerView recyclerView;
    private FavoriteNotesListAdapter mAdapter;
    private RealmList<Note> notesList = new RealmList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        realm = Realm.getDefaultInstance();
        toolbar = findViewById(R.id.toolbar);
        nodata = findViewById(R.id.nonote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);


        mAdapter = new FavoriteNotesListAdapter(notesList,getApplicationContext(),this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setEmptyView(nodata);
        prepareNotesData();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void prepareNotesData() {
        notesList.clear();
        notesList.addAll(realm.where(Note.class).equalTo("isFavorite",true).findAll());
        mAdapter.notifyDataSetChanged();

    }
}
