package edu.udemylearning.simplenotesapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.zip.Inflater;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.NoteViewHolder>{

Context context;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timeStampTextView.setText(Utility.timeStampToString(note.timestamp));
        holder.itemView.setOnClickListener((V)->{
            Intent intent = new Intent(context,NoteDetails.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);

        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,contentTextView,timeStampTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_recycler_view);
            contentTextView = itemView.findViewById(R.id.content_recycler_view);
            timeStampTextView = itemView.findViewById(R.id.timestamp_recycler_view);
        }


    }
}
