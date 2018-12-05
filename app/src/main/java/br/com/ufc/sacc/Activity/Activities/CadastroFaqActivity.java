package br.com.ufc.sacc.Activity.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.ItemFaq;
import br.com.ufc.sacc.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.UUID;

public class CadastroFaqActivity extends AppCompatActivity {

    private int selected;
    private int posicao;
    private EditText edtPergunta, edtResposta;
    private ListView listView;
    private Spinner spinner;
    private final String[] spinerListFuncao = {"Assistente Social", "Psicologia", "Nutrição"};

    private FirebaseDatabase fireBaseDatabase;
    private DatabaseReference databaseReference;

    private ArrayList<ItemFaq> listaItensFaq = new ArrayList<>();
    private ArrayAdapter<ItemFaq> arrayAdapterItemFaq;

    private ItemFaq itemSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_faq_activity);

        inicializarComponentes();
        iniciarFirebase();
        dispararAtualizacao();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelecionado = (ItemFaq) parent.getItemAtPosition(position);
                edtPergunta.setText(itemSelecionado.getPergunta());
                edtResposta.setText(itemSelecionado.getResposta());

                selected = position;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posicao = spinner.getSelectedItemPosition();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void inicializarComponentes() {
        selected = -1;

        edtPergunta = findViewById(R.id.edtPergunta);
        edtResposta = findViewById(R.id.edtResposta);

        listView = findViewById(R.id.listView);
        listView.setSelector(android.R.color.holo_purple);

        spinner = findViewById(R.id.edtFuncao);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CadastroFaqActivity.this, android.R.layout.simple_dropdown_item_1line, spinerListFuncao);
        spinner.setAdapter(adapter);

    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(CadastroFaqActivity.this);
        fireBaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference = ConfiguracaoFirebase.getFirebase();
    }

    private void dispararAtualizacao() {
        databaseReference.child("ItemFaq").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaItensFaq.clear();
                for(DataSnapshot objSnap: dataSnapshot.getChildren()){
                    ItemFaq itemFaq = objSnap.getValue(ItemFaq.class);

                    listaItensFaq.add(itemFaq);
                }
                arrayAdapterItemFaq = new ArrayAdapter<>(CadastroFaqActivity.this, android.R.layout.simple_list_item_1, listaItensFaq);
                listView.setAdapter(arrayAdapterItemFaq);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_faq, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ItemFaq itemFaq;
        String pergunta = edtPergunta.getText().toString();;
        String resposta = edtResposta.getText().toString();;
        String tipo = spinerListFuncao[posicao];
        String uid;

        switch (item.getItemId()) {
            case R.id.addFaq:
                uid = UUID.randomUUID().toString();
                itemFaq = new ItemFaq(uid, pergunta, resposta, tipo);

                if(validaItemFaq(itemFaq)){
                    databaseReference.child("ItemFaq").child(itemFaq.getUid()).setValue(itemFaq);
                    alert("Item de FAQ adicionado.");

                    limparCamposTexto();
                }else{
                    alert("Preencha todos os campos para finalizar!");
                }
                break;

            case R.id.editFaq:
                uid = itemSelecionado.getUid();

                itemFaq = new ItemFaq(uid, pergunta, resposta, tipo);

                if(validaItemFaq(itemFaq)){
                    databaseReference.child("ItemFaq").child(itemFaq.getUid()).setValue(itemFaq);
                    alert("Item de FAQ editado.");

                    limparCamposTexto();
                }else{
                    alert("Preencha todos os campos para finalizar!");
                }

                break;

            case R.id.delFaq:
                String remover =  itemSelecionado.getUid();
                alert("Item removido!");
                databaseReference.child("ItemFaq").child(remover).removeValue();
                break;
        }
        return true;
    }

    private boolean validaItemFaq(ItemFaq itemFaq) {
        if((itemFaq.getPergunta() == null || itemFaq.getPergunta() == "" || itemFaq.getPergunta().equals("")) ||
                (itemFaq.getResposta() == null || itemFaq.getResposta() == "" || itemFaq.getResposta().equals(""))){
            return false;
        }else{
            return true;
        }
    }

    private void limparCamposTexto() {
        edtPergunta.setText("");
        edtResposta.setText("");
    }

    public void alert(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
