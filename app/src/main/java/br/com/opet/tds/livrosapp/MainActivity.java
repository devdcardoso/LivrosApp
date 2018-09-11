package br.com.opet.tds.livrosapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener{

    private EditText editTitulo, editPaginas
//            ,editGenero
            ;
    private Spinner spinnerGenero;
    private Button btnSalvar;
    private ProgressBar progressGeneros, progressLivros;
    private ListView listLivros;

    private DatabaseReference mDatabase;

    private List<String> generos , livros ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editPaginas = findViewById(R.id.editPaginas);
//        editGenero = findViewById(R.id.editGenero);
        editTitulo = findViewById(R.id.editTitulo);
        spinnerGenero = findViewById(R.id.spinnerGeneros);
        btnSalvar = findViewById(R.id.btnSalvar);
        progressGeneros = findViewById(R.id.progressListaGeneros);
        progressLivros = findViewById(R.id.progressListaCadastrados);
        listLivros = findViewById(R.id.listLivros);
        btnSalvar.setOnClickListener(this);
        generos = new ArrayList<String>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        livros =  new ArrayList<String>();
    }

    @Override
    protected void onStart(){
        super.onStart();
        carregarListaDeGeneros();
        carregarListaDeLivros();
    }

    private void carregarListaDeLivros(){
        Query mQuery = mDatabase.child("livros").orderByChild("livros");
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Livro>> t = new GenericTypeIndicator<HashMap<String, Livro>>(){};
                HashMap<String, Livro> wawa = dataSnapshot.getValue(t);

                livros = new ArrayList<String>();
                Iterator<Livro> iteratorLivro = wawa.values().iterator();

                while (iteratorLivro.hasNext()){
                    livros.add(iteratorLivro.next().getTitulo());
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,livros);
                listLivros.setAdapter(adapter);
                progressGeneros.setVisibility(ProgressBar.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void carregarListaDeGeneros() {
        Query mQuery = mDatabase.child("generos").orderByChild("generos");
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>(){};
                generos = dataSnapshot.getValue(t);
                generos.remove(0);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,generos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerGenero.setAdapter(adapter);
                progressGeneros.setVisibility(ProgressBar.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Livro livro = new Livro();
//        Genero genero = new Genero();
        livro.setTitulo(editTitulo.getText().toString());
        livro.setnPaginas(Integer.parseInt(editPaginas.getText().toString()));
//        livro.setGenero(editGenero.getText().toString());
        salvarNovoLivro(livro);
//        salvarNovoGenero(genero);
    }

    private void salvarNovoLivro(Livro livro) {
        mDatabase.child("livros").child(String.valueOf(livro.generateTimeStamp())).setValue(livro)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Livro Salvo!", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Erro ao Salvar o livro...", Toast.LENGTH_SHORT).show();
            }
        });
    }
//    private void salvarNovoGenero(Genero genero) {
//        mDatabase.child("generos").child(String.valueOf(genero.generateTimeStamp())).setValue(genero)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(MainActivity.this, "Livro Salvo!", Toast.LENGTH_SHORT).show();
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MainActivity.this, "Erro ao Salvar o livro...", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
