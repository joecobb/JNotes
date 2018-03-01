package apps.joe.com.jnotes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import apps.joe.com.jnotes.NoteDetailActivity;
import apps.joe.com.jnotes.R;
import apps.joe.com.jnotes.models.Note;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.MyViewHolder> {


    private RealmList<Note> notesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, content;
        public CardView cardView;
        public ImageView btnDelete;

        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            title = view.findViewById(R.id.title);
            btnDelete = view.findViewById(R.id.btn_delete);
            content = view.findViewById(R.id.content);
            date = view.findViewById(R.id.date);
        }
    }


    public NotesListAdapter(final RealmList<Note> notesList, Context ctx) {
        this.notesList = notesList;
        this.context = ctx;
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

        holder.date.setText(note.getDateCreated());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        notesList.remove(note);
                        note.deleteFromRealm();
                        notifyItemRemoved(position);
                    }
                });
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, NoteDetailActivity.class).putExtra("id",note.getId()));
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