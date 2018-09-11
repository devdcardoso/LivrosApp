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
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    private EditText editTitulo, editPaginas;
    private Spinner spinnerGenero;
    private Button btnSalvar;
    private ProgressBar progressGeneros, progressLivros;
    private ListView mList;

    private DatabaseReference mDatabase;

    private List<String> generos;
    private List<Livro> livros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editPaginas = findViewById(R.id.editPaginas);
        editTitulo = findViewById(R.id.editTitulo);
        spinnerGenero = findViewById(R.id.spinnerGeneros);
        btnSalvar = findViewById(R.id.btnSalvar);
        progressGeneros = findViewById(R.id.progressListaGeneros);
        progressLivros = findViewById(R.id.progressListaCadastrados);
        mList = findViewById(R.id.listLivros);
        btnSalvar.setOnClickListener(this);
        generos = new ArrayList<String>();
        livros = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart(){
        super.onStart();
        carregarListaDeGeneros();
        carregarLivros();
    }

    private void carregarLivros() {
        Query mQuery = mDatabase.child("livros").orderByChild("titulo");
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!livros.isEmpty())
                    livros.clear();
                progressLivros.setVisibility(ProgressBar.VISIBLE);
                for(DataSnapshot livroSnapshot : dataSnapshot.getChildren()){
                    livros.add(livroSnapshot.getValue(Livro.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1);
                for(Livro l: livros){
                    adapter.add(l.getTitulo());
                }
                mList.setAdapter(adapter);
                progressLivros.setVisibility(ProgressBar.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void carregarListaDeGeneros() {
        Query mQuery = mDatabase.child("generos").orderByValue();
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                generos.clear();
                for(DataSnapshot generosSnapShot : dataSnapshot.getChildren()){
                    generos.add(generosSnapShot.getValue(String.class));
                }
                /*GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>(){};
                generos = dataSnapshot.getValue(t);*/
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
        livro.setTitulo(editTitulo.getText().toString());
        livro.setnPaginas(Integer.parseInt(editPaginas.getText().toString()));
        livro.setGenero(spinnerGenero.getSelectedItem().toString());
        salvarNovoLivro(livro);
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
}
