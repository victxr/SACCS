package br.com.ufc.sacc.Activity.Fragments.FAQ;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import br.com.ufc.sacc.Activity.Adapter.ExpandableListAdapter;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.ItemFaq;
import br.com.ufc.sacc.R;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class FaqNutricionistaFragment extends Fragment {

    private int selected;
    private ArrayList<ItemFaq> listaItens;

    private ExpandableListAdapter adapter;
    private ExpandableListView expandableListViewItens;
    private FirebaseDatabase fireBaseDatabase;
    private DatabaseReference databaseReference;

    private Context context;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nutricionista, null);

        inicializarComponentes(view);
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

    private void inicializarComponentes(View view) {
        context = view.getContext();
        selected = -1;

        expandableListViewItens = view.findViewById(R.id.listViewNutricionista);
        expandableListViewItens.setSelector( android.R.color.holo_purple);
        listaItens = new ArrayList<>();
    }

    private void dispararAtualizacao() {
        databaseReference.child("ItemFaq").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaItens.clear();
                for(DataSnapshot objSnap: dataSnapshot.getChildren()){
                    ItemFaq itemFaq = objSnap.getValue(ItemFaq.class);

                    if(itemFaq.getTipo().equals("Nutrição")) listaItens.add(itemFaq);

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
