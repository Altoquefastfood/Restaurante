package com.app.burger;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText editUser, editPassword;
    private FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);

        Button btnLabelCreate = findViewById(R.id.btnLabelCreate);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLabelCreate.setOnClickListener(this);
        btnLogin.setOnClickListener(this);


        createTables();

        // ...
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
    }

    private void createTables() {
        SQLiteDatabase db;
        db=openOrCreateDatabase("burger.db",MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS order_data");
        db.execSQL("DROP TABLE IF EXISTS user_order");
        db.execSQL("DROP TABLE IF EXISTS plate");
        db.execSQL("CREATE TABLE IF NOT EXISTS order_data" +
                "(id_order_data TEXT PRIMARY KEY,id_user TEXT,state TEXT,date TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS user_order" +
                "(id_order_data TEXT PRIMARY KEY,id_plate TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS plate" +
                "(id_plate TEXT PRIMARY KEY,image TEXT,name TEXT,description TEXT,price TEXT,state TEXT)");

    }

    private boolean validation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String strUser = editUser.getText().toString().trim();
        String strPassword = editPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(strUser) && !TextUtils.isEmpty(strPassword)) {
            return true;
        }else{
        builder.setTitle("Ups!");

        builder.setMessage("Llena todos los campos")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                    // TODO: handle the OK
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
                return false;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
            Log.i("Mensaje", "No user is signed in");
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLabelCreate:
                startActivity(new Intent(Login.this, CreateAccount.class));
                finish();
                break;

            case R.id.btnLogin:
                    //POR AHORA juanes@este.com 123qweasd falta validar antes de presionar el boton
                    mAuth.signInWithEmailAndPassword("juanes@este.com", "123qweasd")
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Mensaje","signInWithEmail:success");
                                    /*FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(Login.this, Menu_bar.class);
                                    //Esto temporalmente estara asi para velocidad
                                   // intent.putExtra("idUser",strUser);
                                    intent.putExtra("idUser",user.getEmail());
                                    startActivity(intent);
                                    finish();*/

                                    startActivity(new Intent(Login.this, Menu_bar.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Mensaje", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
        }
    }
}
