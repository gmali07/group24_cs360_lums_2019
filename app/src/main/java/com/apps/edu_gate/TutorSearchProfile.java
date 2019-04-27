package com.apps.edu_gate;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TutorSearchProfile extends AppCompatActivity {

    Button forcall;
    TextView fname;
    TextView gender;
    TextView lastname;
    TextView instituion;
    TextView location;
    TextView grades;
    TextView subject;
    TextView rating;
    ImageView imageTutor;

    View itemView;

    private RecyclerView recyclerView;
    private List<String> adminNumbers = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ListView listView = null;
//    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = new ListView(this);
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
        Tutorinfo x = (Tutorinfo) getIntent().getSerializableExtra("result");
        setContentView(R.layout.activity_tutor_search_profile);
        fname = (TextView) findViewById(R.id.fname);
        fname.setText(x.firstName);
        gender = (TextView) findViewById(R.id.gender);
        gender.setText(x.gender);
        lastname = (TextView) findViewById(R.id.lname);
        lastname.setText(x.lastName);
        instituion = (TextView) findViewById(R.id.institution);
        instituion.setText(x.recentInstitution);
        location = (TextView) findViewById(R.id.location);
        location.setText(x.tuitionLocation);
        rating = (TextView) findViewById(R.id.rating);
        rating.setText(String.valueOf(x.tempr));
        subject = (TextView) findViewById(R.id.subject);
        subject.setText(x.subject);
        grades = (TextView) findViewById(R.id.grades);
        grades.setText(x.grade);

//        mAuth.signInAnonymously()
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//                        }
//                    }
//                });
//
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference httpsReference = storage.getReferenceFromUrl(x.profileImage);
//        final long ONE_MEGABYTE = 1024 * 1024;
//        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                imageTutor.setImageBitmap(bmp);
//                // Data for "images/island.jpg" is returns, use this as needed
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

        forcall = findViewById(R.id.callbutton);
        Query q = FirebaseDatabase.getInstance().getReference("Admin")
                .orderByChild("number");
        q.addListenerForSingleValueEvent(valueEventListener);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                R.layout.listitem, R.id.txtitem,adminNumbers);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int
                    position, long id) {
                ViewGroup vg=(ViewGroup)view;
                TextView txt=(TextView)vg.findViewById(R.id.txtitem);
                Uri u = Uri.parse("tel:" + txt.getText().toString());
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                startActivity(i);
//                Toast.makeText(TutorSearchProfile.this,txt.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public void showDialogListView(View view) {

        AlertDialog.Builder builder = new
                AlertDialog.Builder(TutorSearchProfile.this);

        builder.setCancelable(true);

        builder.setPositiveButton("OK", null);

        builder.setView(listView);

        AlertDialog dialog = builder.create();

        dialog.show();
    }


//        forcall.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(TutorSearchProfile.this);
//                dialog.setContentView(R.layout.dialogbox_call);
//                recyclerView = dialog.findViewById(R.id.rv);
//                recyclerView.setHasFixedSize(true);
//                mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                recyclerView.setLayoutManager(mLayoutManager);
//                adminList = new ArrayList<>();
//                mAdapter = new CallerAdapter(getApplicationContext(), adminList);
//                recyclerView.setAdapter(mAdapter);
//                dialog.show();
//
//            }
//        });


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            adminNumbers.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Admininfo admin = snapshot.getValue(Admininfo.class);
                    String number = snapshot.child("number").getValue(String.class);
                    adminNumbers.add(number);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
