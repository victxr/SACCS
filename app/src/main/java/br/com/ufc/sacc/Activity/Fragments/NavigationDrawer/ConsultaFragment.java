package br.com.ufc.sacc.Activity.Fragments.NavigationDrawer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.com.ufc.sacc.Activity.Activities.MarcarConsultaActivity;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.ItemConsultaMarcada;
import br.com.ufc.sacc.Model.Usuario;
import br.com.ufc.sacc.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class ConsultaFragment extends Fragment {

    private FirebaseDatabase fireBaseDatabase;
    private DatabaseReference databaseReference;
    private ListView listViewConsultaMarcada;
    private ArrayAdapter<String> arrayAdapterItemConsultaMarcada;
    private ArrayList<String> listaMinhasConsultas = new ArrayList<>();
    private String itemSelecionado;
    private Context context;
    private int selected;
    private Usuario usuarioLogado = new Usuario();
    private FirebaseAuth autenticacao;
    private String emailAlunoLogado;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta, null);

        iniciarFirebase();
        inicializarComponentes(view);
        pegarUsuarioLogado();
        dispararAtualizacao();

        listViewConsultaMarcada.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelecionado = (String) parent.getItemAtPosition(position);

                selected = position;
            }
        });

        FloatingActionButton btnMarcar =  view.findViewById(R.id.btnMarcarConsulta);
        btnMarcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_marcar = new Intent (getActivity(), MarcarConsultaActivity.class);
                startActivity(intent_marcar);
            }
        });
        return view;

    }

    private void inicializarComponentes(View view){
        context = view.getContext();
        listViewConsultaMarcada = view.findViewById(R.id.listViewConsultaMarcada);
        listViewConsultaMarcada.setSelector(android.R.color.holo_purple);
    }

    private void dispararAtualizacao() {
        databaseReference.child("ItemConsultaMarcada").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaMinhasConsultas.clear();
                for (DataSnapshot objSnap : dataSnapshot.getChildren()) {
                    ItemConsultaMarcada itemConsultaMarcada = objSnap.getValue(ItemConsultaMarcada.class);

                    if(usuarioLogado.getRegistro() != null) {
                        if (itemConsultaMarcada.getMatriculaAluno().equals(usuarioLogado.getRegistro())) {
                            String item = itemConsultaMarcada.getData()+" - "+itemConsultaMarcada.getTipo();
                            listaMinhasConsultas.add(item);
                        }
                    }
                }
                arrayAdapterItemConsultaMarcada = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, listaMinhasConsultas);
                listViewConsultaMarcada.setAdapter(arrayAdapterItemConsultaMarcada);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void iniciarFirebase() {
        FirebaseApp.initializeApp(ConsultaFragment.this.getContext());
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
                    Usuario usuario = objSnap.getValue(Usuario.class);

                    if(usuario.getEmail().equals(emailAlunoLogado)) {
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
}
