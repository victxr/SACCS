package br.com.ufc.sacc.Activity.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import br.com.ufc.sacc.Activity.Adapter.ExpandableListAdapter;
import br.com.ufc.sacc.Activity.FaqActivity;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.ItemFaq;
import br.com.ufc.sacc.R;
import br.com.ufc.sacc.ServicesBroadcasts.ServiceDownloadFaq;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class PsicologaFragment extends Fragment {

     int selected;
     ArrayList<ItemFaq> listaItens;

     ExpandableListAdapter adapter;
     ExpandableListView expandableListViewItens;
     FirebaseDatabase fireBaseDatabase;
     DatabaseReference databaseReference;

    Context context;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_psicologa, null);

        context = view.getContext();

        inicializarComponentes();
        iniciarFirebase();
        dispararAtualizacao();

        expandableListViewItens.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                selected = groupPosition;
                return false;
            }
        });

        return view;
    }

    private void inicializarComponentes() {
        selected = -1;

        expandableListViewItens = view.findViewById(R.id.listViewPsicologa);
        expandableListViewItens.setSelector( android.R.color.holo_green_light);

        listaItens = new ArrayList<ItemFaq>();
    }

    private void dispararAtualizacao() {

        databaseReference.child("ItemFaq").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaItens.clear();
                for(DataSnapshot objSnap: dataSnapshot.getChildren()){
                    ItemFaq itemFaq = objSnap.getValue(ItemFaq.class);

                    if(itemFaq.getTipo().equals("Psicologia")) listaItens.add(itemFaq);

                }
                adapter = new ExpandableListAdapter(context, listaItens);
                expandableListViewItens.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void iniciarFirebase() {
        fireBaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference = ConfiguracaoFirebase.getFirebase();
    }
}
