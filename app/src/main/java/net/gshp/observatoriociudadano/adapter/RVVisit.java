package net.gshp.observatoriociudadano.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoPdvPdv;
import net.gshp.observatoriociudadano.dto.DtoReportVisit;
import net.gshp.observatoriociudadano.listener.OnItemClickListenerRV;
import net.gshp.observatoriociudadano.model.ModelMenuReport;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;
import net.gshp.observatoriociudadano.util.Config;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by gnu on 16/02/18.
 */

public class RVVisit extends RecyclerView.Adapter<RVVisit.ViewHolder> {

    private List<DtoReportVisit> listVisit;
    private OnItemClickListenerRV onItemClickListenerRV;

    public RVVisit(List<DtoReportVisit> listVisit, OnItemClickListenerRV onItemClickListenerRV) {
        this.listVisit = listVisit;
        this.onItemClickListenerRV = onItemClickListenerRV;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_visit, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DtoReportVisit dto = listVisit.get(position);
        holder.txtName.setText(dto.getName());
        holder.txtCreated.setText(Config.formatDateFromCurrentMillis(dto.getDateCheckIn(), "dd MMMM yyyy"));
        holder.txtInit.setText(Config.formatDateFromCurrentMillis(dto.getDateCheckIn(), "hh:mm aa"));
        holder.txtFinish.setText(Config.formatDateFromCurrentMillis(dto.getDateCheckOut(), "hh:mm aa"));

        if (dto.getTypePoll() == ContextApp.context.getResources().getInteger(R.integer.idPollSupervisor)) {
            holder.txtUserPass.setVisibility(View.GONE);
        } else if (dto.getTypePoll() == ContextApp.context.getResources().getInteger(R.integer.idPollRepresentanteCasilla)) {
            holder.txtUserPass.setVisibility(View.VISIBLE);
            String msg = "Error al obtener datos";

            msg = new ModelMenuReport(new DtoBundle()).getUserPassword(dto.getId());


            holder.txtUserPass.setText(msg);
        }

        if (dto.getDateCheckOut() == 0) {
            holder.viewStatus.setBackgroundResource(R.color.colorScheduleNotDone);
            holder.txtStatusText.setText("INCOMPLETO");
            holder.txtFinish.setText("--");
        } else if (dto.getDateCheckOut() != 0 && dto.getSend() == 0) {
            holder.viewStatus.setBackgroundResource(R.color.colorScheduleIncomplete);
            holder.txtStatusText.setText("POR ENVIAR");
        } else if (dto.getSend() == 1) {
            holder.viewStatus.setBackgroundResource(R.color.colorScheduleComplete);
            holder.txtStatusText.setText("ENVIADO");
        } else {
            holder.viewStatus.setBackgroundResource(R.color.colorScheduleComplete);
            holder.txtStatusText.setText("");
        }

        holder.rltMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListenerRV.onItemClickListener(view, position);
            }
        });
        holder.imgTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListenerRV.onItemClickListener(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listVisit.size();
    }

    public DtoReportVisit getItem(int position) {
        return listVisit.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        View viewStatus;
        TextView txtName, txtAddress, txtPassword, txtCreated, txtLabelInit, txtInit, txtLabelFinish,
                txtFinish, txtStatusText, txtUserPass;
        ImageView imgTrash;
        RelativeLayout rltMain;

        ViewHolder(View itemView) {
            super(itemView);
            viewStatus = itemView.findViewById(R.id.viewStatus);
            txtName = itemView.findViewById(R.id.txtName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtPassword = itemView.findViewById(R.id.txtPassword);
            txtCreated = itemView.findViewById(R.id.txtCreated);
            txtLabelInit = itemView.findViewById(R.id.txtLabelInit);
            txtInit = itemView.findViewById(R.id.txtInit);
            txtLabelFinish = itemView.findViewById(R.id.txtLabelFinish);
            txtFinish = itemView.findViewById(R.id.txtFinish);
            txtStatusText = itemView.findViewById(R.id.txtStatusText);
            rltMain = itemView.findViewById(R.id.rltMain);
            imgTrash = itemView.findViewById(R.id.imgTrash);
            txtUserPass = itemView.findViewById(R.id.txtUserPass);
            ChangeFontStyle.changeFont(txtName, txtAddress, txtUserPass);
        }

    }
}
