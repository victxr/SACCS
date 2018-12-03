package br.com.ufc.sacc.Activity.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import br.com.ufc.sacc.Activity.Adapter.ExpandableListAdapter;
import br.com.ufc.sacc.Activity.CadastroConsultaActivity;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.ItemConsulta;
import br.com.ufc.sacc.Model.ItemConsultaMarcada;
import br.com.ufc.sacc.Model.ItemFaq;
import br.com.ufc.sacc.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.UUID;

public class MarcarPsicologaFragment extends Fragment {

    FirebaseDatabase fireBaseDatabase;
    DatabaseReference databaseReference;
    ListView listViewConsulta;
    Context context;
    EditText edtMotivo;
    Button btnConfirmarConsultaPsicologa;
    ItemConsulta itemSelecionado;
    int selected;

    private ArrayList<ItemConsulta> listaItensPsicologa = new ArrayList<>();
    private ArrayAdapter<ItemConsulta> arrayAdapterItemConsulta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_psicologa_marcar, null);
        context = view.getContext();
        listViewConsulta = view.findViewById(R.id.listViewConsultaPsicologa);
        listViewConsulta.setSelector(android.R.color.holo_green_light);
        edtMotivo = view.findViewById(R.id.motivo);

        btnConfirmarConsultaPsicologa = view.findViewById(R.id.btnConfirmarConsultaPsicologa);
        iniciarFirebase();
        dispararAtualizacao();

        listViewConsulta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelecionado = (ItemConsulta) parent.getItemAtPosition(position);

                selected = position;
            }
        });
        btnConfirmarConsultaPsicologa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ItemConsultaMarcada itemConsultaMarcada;
                String marcada, tipo;
                String uid;
                uid = UUID.randomUUID().toString();
                marcada = itemSelecionado.getDiaDaSemana() + " " + itemSelecionado.getHorario();
                itemConsultaMarcada = new ItemConsultaMarcada(uid, marcada, "Psicologa", edtMotivo.getText().toString());
                databaseReference.child("ItemConsultaMarcada").child(itemConsultaMarcada.getUid()).setValue(itemConsultaMarcada);
                alert("Item adicionado.");

                limparCamposTexto();
            }
        });
        return view;
    }
    private void inicializarComponentes(){

    }
    private void iniciarFirebase() {
        FirebaseApp.initializeApp(MarcarPsicologaFragment.this.getContext());
        fireBaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference = ConfiguracaoFirebase.getFirebase();
    }

    private void dispararAtualizacao() {
        databaseReference.child("ItemConsulta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaItensPsicologa.clear();
                for (DataSnapshot objSnap : dataSnapshot.getChildren()) {
                    ItemConsulta itemConsulta = objSnap.getValue(ItemConsulta.class);

                    if(itemConsulta.getTipo().equals("Psicologia")) listaItensPsicologa.add(itemConsulta);
                }
                arrayAdapterItemConsulta = new ArrayAdapter<ItemConsulta>(context, android.R.layout.simple_list_item_1, listaItensPsicologa);
                listViewConsulta.setAdapter(arrayAdapterItemConsulta);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private boolean validarConsulta(EditText edtMotivo){
        if(edtMotivo.getText().toString() == null || edtMotivo.getText().toString() == "" || edtMotivo.getText().toString().equals("")) return false;
        return true;
    }

    public void alert(String mensagem) {
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }
    private void limparCamposTexto() {
        edtMotivo.setText("");
    }
}
