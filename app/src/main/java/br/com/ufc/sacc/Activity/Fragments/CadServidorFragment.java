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

public class CadServidorFragment extends Fragment{

    Spinner spinner;
    String[] spinerListFuncao = {"Assistencia Social", "Psicologia", "Nutrição"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cad_servidor, null);

        spinner = view.findViewById(R.id.edtCadFuncao);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, spinerListFuncao);
        spinner.setAdapter(adapter);

        return view;
    }
}
