package apps.joe.com.jnotes.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import apps.joe.com.jnotes.AddNoteActivity;
import apps.joe.com.jnotes.NoteDetailActivity;
import apps.joe.com.jnotes.R;
import apps.joe.com.jnotes.models.Note;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.MyViewHolder> {


    private RealmList<Note> notesList;
    private Context context;
    private AppCompatActivity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, date, content;
        public CardView cardView;
        public ImageView btnDelete;
        public CheckBox fav;

        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            title = view.findViewById(R.id.title);
            btnDelete = view.findViewById(R.id.btn_delete);
            content = view.findViewById(R.id.content);
            date = view.findViewById(R.id.date);
            fav = view.findViewById(R.id.star);


        }
    }


    public NotesListAdapter(final RealmList<Note> notesList, Context ctx, AppCompatActivity activity) {
        this.notesList = notesList;
        this.context = ctx;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Note note = notesList.get(position);
        final Realm mRealm = Realm.getDefaultInstance();
        if(note.isFavorite()){
            holder.fav.setChecked(true);
        }
        if(note.getTitle().length()>30) {
            holder.title.setText(note.getTitle().substring(0,30)+"...");
        }else{
            holder.title.setText(note.getTitle());
        }
        if(note.getContent().length()>82){
            holder.content.setText(note.getContent().substring(0,75)+"...");
        }else{
            holder.content.setText(note.getContent());
        }
        holder.fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {


                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            try {

                                if (b) {
                                    note.setFavorite(true);
                                } else {
                                    note.setFavorite(false);
                                }
                                realm.copyToRealmOrUpdate(note);
                                notifyItemChanged(position,note);
                            }catch(Exception ex){
                                new MaterialDialog.Builder(context)
                                        .title("Oops")
                                        .content("Something went wrong, please try again")
                                        .positiveText("ok")
                                        .show();
                            }


                        }
                    });

            }
        });
        holder.date.setText(note.getDateCreated());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(activity)
                        .title("Warning")
                        .content("Are you sure you want to delete note?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mRealm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        notesList.remove(note);
                                        note.deleteFromRealm();
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,notesList.size());
                                    }
                                });
                            }
                        })
                        .show();
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    Pair<View, String> p1 = Pair.create((View)holder.cardView, "card");
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(activity,p1);
                    activity.startActivity(new Intent(context, NoteDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id",note.getId()), options.toBundle());
                }else {
                    context.startActivity(new Intent(context, NoteDetailActivity.class).putExtra("id", note.getId()));
                }
            }
        });
        note.addChangeListener(new RealmChangeListener<Note>() {
            @Override
            public void onChange(Note realmModel) {
                try {
                    if(realmModel.getTitle().length()>30) {
                        holder.title.setText(realmModel.getTitle().substring(0,30)+"...");
                    }else{
                        holder.title.setText(realmModel.getTitle());
                    }
                    if(realmModel.getContent().length()>82){
                        holder.content.setText(realmModel.getContent().substring(0,75)+"...");
                    }else{
                        holder.content.setText(realmModel.getContent());
                    }
                    holder.date.setText(realmModel.getDateCreated());
                }catch(Exception ex){

                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return notesList.size();

    }


}