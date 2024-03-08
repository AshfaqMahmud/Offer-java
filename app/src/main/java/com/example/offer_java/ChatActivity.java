package com.example.offer_java;

import android.app.Notification;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;

    private FirebaseFirestore db;
    private CollectionReference messagesRef;
    private String senderId;
    private String receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        messageRecyclerView = findViewById(R.id.messageRecyclerView);

        db = FirebaseFirestore.getInstance();
        messagesRef = db.collection("messages");

        // Assuming senderId and receiverId are passed via intent
        senderId = getIntent().getStringExtra("senderId");
        receiverId = getIntent().getStringExtra("receiverId");

        // Set up RecyclerView
        messageAdapter = new MessageAdapter(this, senderId, receiverId);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(messageAdapter);

        // Set up send button click listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Listen for new messages
        messagesRef
                .whereEqualTo("senderId", senderId)
                .whereEqualTo("receiverId", receiverId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore", "Listen failed", error);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Message message = dc.getDocument().toObject(Message.class);
                                    messageAdapter.addMessage(message);
                                    break;
                                // Handle other types if needed
                            }
                        }
                    }
                });

        // Mark messages as read when activity is resumed
        markMessagesAsRead();
    }

    private void sendMessage() {
        String content = messageEditText.getText().toString().trim();
        if (!content.isEmpty()) {
            Message message = new Message(content, senderId, receiverId, System.currentTimeMillis(), false);
            messagesRef.add(message);
            messageEditText.setText("");
        }
    }

    private void markMessagesAsRead() {
        // Query unread messages sent by the other user and mark them as read
        messagesRef
                .whereEqualTo("senderId", receiverId)
                .whereEqualTo("receiverId", senderId)
                .whereEqualTo("read", false)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            DocumentReference messageRef = documentSnapshot.getReference();
                            messageRef.update("read", true);
                        }
                    }
                });
    }
}