package br.com.ufc.sacc.Activity.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.ufc.sacc.Model.ItemFaq;
import br.com.ufc.sacc.R;

import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<ItemFaq> itensDeFaq;

    public ExpandableListAdapter(Context context, List<ItemFaq> itensDeFaq) {
        this.context = context;
        this.itensDeFaq = itensDeFaq;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ItemFaq item = (ItemFaq) getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        ImageView imageViewGroup = ( ImageView )convertView.findViewById( R.id.imageViewGroup );

        if(isExpanded){
            imageViewGroup.setImageResource( R.drawable.ic_expand_less_black_24dp);
        } else{
            imageViewGroup.setImageResource( R.drawable.ic_expand_more_black_18dp);
        }

        TextView txViewNomeItem = ( TextView )convertView.findViewById( R.id.listHeader );
        txViewNomeItem.setTypeface(null, Typeface.BOLD);
        txViewNomeItem.setText( item.getPergunta());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemFaq itemFaq = (ItemFaq) getChild( groupPosition, childPosition );

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rowlayout, null);
        }

        TextView txViewContentResposta = convertView.findViewById(R.id.txtReposta);

        txViewContentResposta.setText(itemFaq.getResposta());

        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return itensDeFaq.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itensDeFaq.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return itensDeFaq.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return itensDeFaq.get(groupPosition).getId();
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
        return itensDeFaq.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return -1;
    }
}
