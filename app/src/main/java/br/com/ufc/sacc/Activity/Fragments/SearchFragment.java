package br.com.ufc.sacc.Activity.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import br.com.ufc.sacc.Activity.FaqActivity;
import br.com.ufc.sacc.Activity.MarcarConsultaActivity;
import br.com.ufc.sacc.Activity.PrincipalActivity;
import br.com.ufc.sacc.R;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);
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

}
