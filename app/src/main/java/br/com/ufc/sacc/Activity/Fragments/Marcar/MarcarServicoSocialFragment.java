package br.com.ufc.sacc.Activity.Fragments.Marcar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.ItemConsulta;
import br.com.ufc.sacc.Model.ItemConsultaMarcada;
import br.com.ufc.sacc.Model.Usuario;
import br.com.ufc.sacc.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.UUID;

public class MarcarServicoSocialFragment extends Fragment {

    private  FirebaseDatabase fireBaseDatabase;
    private  DatabaseReference databaseReference;
    private ListView listViewConsulta;
    private Context context;
    private EditText edtMotivo;
    private Button btnConfirmarConsultaSocial;
    private ItemConsulta itemSelecionado;
    private ItemConsultaMarcada itemConsultaMarcada;
    private int selected;

    private FirebaseAuth autenticacao;

    private String emailAlunoLogado;

    private Usuario usuarioLogado = new Usuario();
    private ArrayList<ItemConsulta> listaItensSocial = new ArrayList<>();
    private ArrayAdapter<ItemConsulta> arrayAdapterItemConsulta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_servico_social_marcar, null);

        iniciarFirebase();
        inicializarComponentes(view);
        pegarUsuarioLogado();
        dispararAtualizacao();

        listViewConsulta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelecionado = (ItemConsulta) parent.getItemAtPosition(position);

                selected = position;
            }
        });

        btnConfirmarConsultaSocial.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pegarUsuarioLogado();

                String data, tipo = "Serviço Social", uid;
                uid = UUID.randomUUID().toString();
                data = itemSelecionado.getDiaDaSemana() + " " + itemSelecionado.getHorario();
                Log.d("Marcada: ", data);
                itemConsultaMarcada = new ItemConsultaMarcada(uid, data, tipo, edtMotivo.getText().toString(),
                        usuarioLogado.getNome(), usuarioLogado.getRegistro());

                databaseReference.child("ItemConsultaMarcada").child(itemConsultaMarcada.getUid()).setValue(itemConsultaMarcada);
                alert("Consulta marcada.");

                limparCamposTexto();
            }
        });
        return view;
    }
    private void inicializarComponentes(View view){
        context = view.getContext();
        listViewConsulta = view.findViewById(R.id.listViewConsultaSocial);
        listViewConsulta.setSelector(android.R.color.holo_purple);
        edtMotivo = view.findViewById(R.id.motivo);
        btnConfirmarConsultaSocial = view.findViewById(R.id.btnConfirmarConsultaSocial);
    }

    private void iniciarFirebase() {
        FirebaseApp.initializeApp(MarcarServicoSocialFragment.this.getContext());
        fireBaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference = ConfiguracaoFirebase.getFirebase();
    }


    private void pegarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        emailAlunoLogado = autenticacao.getCurrentUser().getEmail();
        Log.d("Email do cara logado:", emailAlunoLogado);

        databaseReference.child("usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot objSnap : dataSnapshot.getChildren()) {
                    Usuario usuario= objSnap.getValue(Usuario.class);

                    if(usuario.getEmail().equals(emailAlunoLogado)) {
                        usuarioLogado.setNome(usuario.getNome());
                        usuarioLogado.setRegistro(usuario.getRegistro());
                        Log.d("Email do cara do banco:", usuario.getEmail());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void dispararAtualizacao() {
        databaseReference.child("ItemConsulta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaItensSocial.clear();
                for (DataSnapshot objSnap : dataSnapshot.getChildren()) {
                    ItemConsulta itemConsulta = objSnap.getValue(ItemConsulta.class);

                    if(itemConsulta.getTipo().equals("Assistente Social")) listaItensSocial.add(itemConsulta);
                }
                arrayAdapterItemConsulta = new ArrayAdapter<ItemConsulta>(context, android.R.layout.simple_list_item_1, listaItensSocial);
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
