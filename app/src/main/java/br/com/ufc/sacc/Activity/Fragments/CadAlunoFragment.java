package br.com.ufc.sacc.Activity.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import br.com.ufc.sacc.R;

public class CadAlunoFragment extends Fragment {
    Spinner spinner;
    String[] spinerListCurso = {"Engenharia de Software", "Engenharia da Computação",
            "Sistemas de Informação", "Ciência da Computação",
            "Design Digital", "Redes de Computadores"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cad_aluno, null);

        spinner = view.findViewById(R.id.edtCadCurso);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, spinerListCurso);
        spinner.setAdapter(adapter);

        return view;
    }
}
