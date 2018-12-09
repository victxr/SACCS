package br.com.ufc.sacc.Activity.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.ufc.sacc.Model.ItemConsultaMarcada;
import br.com.ufc.sacc.Model.ItemFaq;
import br.com.ufc.sacc.R;

import java.util.List;

public class ExpandableListAdapterVisualizarConsulta extends BaseExpandableListAdapter {

    private Context context;
    private List<ItemConsultaMarcada> itemConsultaMarcadas;

    public ExpandableListAdapterVisualizarConsulta(Context context, List<ItemConsultaMarcada> itemConsultaMarcadas) {
        this.context = context;
        this.itemConsultaMarcadas = itemConsultaMarcadas;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ItemConsultaMarcada item = (ItemConsultaMarcada) getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_visualizar_consulta, null);
        }

        ImageView imageViewGroup = convertView.findViewById( R.id.imageViewGroupVisualizarConsulta );

        if(isExpanded){
            imageViewGroup.setImageResource(R.drawable.ic_expand_less_black_24dp);
        } else{
            imageViewGroup.setImageResource(R.drawable.ic_expand_more_black_18dp);
        }

        TextView txViewNomeItem = convertView.findViewById( R.id.listHeaderVisualizarConsulta );
        txViewNomeItem.setTypeface(null, Typeface.BOLD);
        txViewNomeItem.setText( item.getTipo() + " - " + item.getData());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemConsultaMarcada itemConsultaMarcada = (ItemConsultaMarcada) getChild( groupPosition, childPosition );

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rowlayout_visualizar_consulta, null);
        }

        TextView txViewContentResposta = convertView.findViewById(R.id.txtRespostaVisualizarConsulta);
        txViewContentResposta.setText("Nome: " + itemConsultaMarcada.getNomeAluno() +
                                     "\nMatrícula: " + itemConsultaMarcada.getMatriculaAluno() +
                                     "\nMotivo: " + itemConsultaMarcada.getMotivo());
        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return itemConsultaMarcadas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemConsultaMarcadas.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return itemConsultaMarcadas.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getGroupCount() {
        return itemConsultaMarcadas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
}
