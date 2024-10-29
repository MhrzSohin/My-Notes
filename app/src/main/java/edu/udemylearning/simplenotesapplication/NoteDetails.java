package edu.udemylearning.simplenotesapplication;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;



public class NoteDetails extends AppCompatActivity {
    EditText notesTitle,notesContent;
    ImageButton saveNotesBtn;
    TextView titleAddNewNote;
    String title,docId,content;
    boolean isEditMode = false;
    Button deleteNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        titleAddNewNote= findViewById(R.id.page_title);
        notesTitle = findViewById(R.id.notes_title_content);
        notesContent = findViewById(R.id.notes_context_text);
        saveNotesBtn = findViewById(R.id.save_notesBtn);
        deleteNoteBtn = findViewById(R.id.deleteBtn);
        title = getIntent().getStringExtra("title");
        docId = getIntent().getStringExtra("docId");
        content = getIntent().getStringExtra("content");
        if (docId!=null && !docId.isEmpty()){
            isEditMode=true;
        }
        notesTitle.setText(title);
        notesContent.setText(content);
        if (isEditMode){
            titleAddNewNote.setText("Edit your note");
            deleteNoteBtn.setVisibility(View.VISIBLE);
        }

        saveNotesBtn.setOnClickListener((V)->saveNotes());

        deleteNoteBtn.setOnClickListener((V)->deleteNoteFromFireBase());

    }
    void saveNotes(){
        String noteTitle = notesTitle.getText().toString();
        String noteContext = notesContent.getText().toString();
        if (noteTitle == null || noteTitle.isEmpty()){
            notesTitle.setError("Title Required.");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContext);
        note.setTimestamp(Timestamp.now());
        saveNotesToFireBase(note);

    }
    void saveNotesToFireBase(Note note) {
        DocumentReference documentReference;
        if (isEditMode) {
            //update the note
             documentReference = Utility.getCollectionReferenceForNote().document(docId);
        } else {
            //create a new note
            documentReference = Utility.getCollectionReferenceForNote().document();
        }
            documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //note added
                        Utility.showToast(NoteDetails.this, "Notes added succesfully.");
                        finish();
                    } else {
                        Utility.showToast(NoteDetails.this, "Failed while adding notes.");
                    }
                }
            });
        }
        void deleteNoteFromFireBase(){
            DocumentReference documentReference;
                documentReference = Utility.getCollectionReferenceForNote().document(docId);
                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //note added
                        Utility.showToast(NoteDetails.this, "Notes deleted succesfully.");
                        finish();
                    } else {
                        Utility.showToast(NoteDetails.this, "Failed while deleting notes.");
                    }
                }
            });
        }
    }
