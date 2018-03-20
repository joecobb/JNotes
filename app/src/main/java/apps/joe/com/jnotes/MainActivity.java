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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import apps.joe.com.jnotes.adapters.NotesListAdapter;
import apps.joe.com.jnotes.models.Note;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;

public class MainActivity extends AppCompatActivity {
    private MenuItem mSearchItem;
    private FloatingActionButton fab;
    private EmptyRecyclerView recyclerView;
    private RealmList<Note> notesList = new RealmList<>();
    private Realm realm;
    private LinearLayout nodata;
    private NotesListAdapter mAdapter;
    private Toolbar toolbar;
    private MenuItem mFav;
    private MenuItem mDevInfo;
    private MaterialSearchView searchView;
    private MenuItem mRestore;
    private MenuItem mBackup;
    private final String TAG = "MyBackups";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nodata= findViewById(R.id.nonote);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddNoteActivity.class));
                finish();
            }
        });
        recyclerView = findViewById(R.id.recyclerView);

        mAdapter = new NotesListAdapter(notesList,getApplicationContext(),this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setEmptyView(nodata);
        prepareNotesData();

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("query##", query);
                returnResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                returnResults(newText);
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    private void returnResults(String query) {
        if(mAdapter!=null) {
            notesList.clear();
            notesList.addAll(realm.where(Note.class)
                    .contains("title", query,Case.INSENSITIVE)
                    .or()
                    .contains("content",query, Case.INSENSITIVE)
                    .findAll());
            mAdapter.notifyDataSetChanged();
        }
    }


    private void prepareNotesData() {
        notesList.clear();
        notesList.addAll(realm.where(Note.class).findAll());
        mAdapter.notifyDataSetChanged();
//        if(mAdapter.getItemCount()==0){
//            nodata.setVisibility(View.VISIBLE);
//        }else{
//            nodata.setVisibility(View.GONE);
//        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

//        mBackup = menu.findItem(R.id.backup);
//        mBackup.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
////                DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddhhmmss");
////                dateFormatter.setLenient(false);
////                Date today = new Date();
////                String datetime = dateFormatter.format(today);
////                String filename = "backup"+datetime;
////                Realm orgRealm = Realm.getDefaultInstance();
////                File destination = new File(getFilesDir(), filename);
////                orgRealm.writeCopyTo(destination);
////                new MaterialDialog.Builder(MainActivity.this)
////                        .title("Notice")
////                        .content("Successfully backed up data here:\n"+ destination)
////                        .positiveText("ok")
////                        .show();
////                orgRealm.close();
//                backup();
//                return true;
//            }
//        });
//        mRestore = menu.findItem(R.id.restore);
//        mRestore.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//
//                return true;
//            }
//        });
        mFav = menu.findItem(R.id.miFav);
        mFav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
                return false;
            }
        });

        mDevInfo = menu.findItem(R.id.miDeveloper);
            MenuItem item = menu.findItem(R.id.action_search);
            searchView.setMenuItem(item);
            mDevInfo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    startActivity(new Intent(MainActivity.this,AboutActivity.class));
                    return true;
                }
            });
            return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        Realm mRealm = Realm.getDefaultInstance();
        if(mRealm.where(Note.class).findAll().size()<notesList.size()){
            prepareNotesData();
        }
    }
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    public void backup() {

        File exportRealmFile = null;

        File exportRealmPATH = getExternalFilesDir(null);
        String exportRealmFileName = "default.realm";

        Log.d(TAG, "Realm DB Path = "+realm.getPath());

        try {
            // create a backup file
            exportRealmFile = new File(exportRealmPATH, exportRealmFileName);

            // if backup file already exists, delete it
            exportRealmFile.delete();

            // copy current realm to backup file
            realm.writeCopyTo(exportRealmFile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String msg =  "File exported to Path: "+ getExternalFilesDir(null);
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        Log.d(TAG, msg);


        realm.close();

    }

    public void restore() {

        //Restore
        File exportRealmPATH = getExternalFilesDir(null);
        String FileName = "default.realm";

        String restoreFilePath = getExternalFilesDir(null) + "/"+FileName;

        Log.d(TAG, "oldFilePath = " + restoreFilePath);

        copyBundledRealmFile(restoreFilePath, FileName);
        Log.d(TAG, "Data restore is done");

    }

    private String copyBundledRealmFile(String oldFilePath, String outFileName) {
        try {
            File file = new File(getFilesDir(), outFileName);

            Log.d(TAG, "context.getFilesDir() = " + getFilesDir().toString());
            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(new File(oldFilePath));

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String dbPath(){

        return realm.getPath();
    }
}
