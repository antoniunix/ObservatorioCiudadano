package net.gshp.observatoriociudadano.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dto.DtoPdvPdv;
import net.gshp.observatoriociudadano.listener.OnItemClickListenerRV;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;

import java.util.List;

/**
 * Created by gnu on 15/02/18.
 */

public class RVListStation extends RecyclerView.Adapter<RVListStation.ViewHolder> {

    private List<DtoPdvPdv> listPdv;
    private OnItemClickListenerRV onItemClickListenerRV;

    public RVListStation(List<DtoPdvPdv> listPdv, OnItemClickListenerRV onItemClickListenerRV) {
        this.listPdv = listPdv;
        this.onItemClickListenerRV = onItemClickListenerRV;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_station, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DtoPdvPdv dto = listPdv.get(position);
        holder.txtName.setText(dto.getName());
        holder.txtAddress.setText(dto.getAddress());

        holder.rltMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListenerRV.onItemClickListener(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPdv.size();
    }

    public DtoPdvPdv getItem(int position){
        return listPdv.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtAddress;
        RelativeLayout rltMain;

        ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            rltMain = itemView.findViewById(R.id.rltMain);
            ChangeFontStyle.changeFont(txtName, txtAddress);
        }

    }
}
