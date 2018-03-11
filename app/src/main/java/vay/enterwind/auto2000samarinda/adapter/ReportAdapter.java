package vay.enterwind.auto2000samarinda.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.models.Sales;
import vay.enterwind.auto2000samarinda.module.supervisor.report.PlanActivity;
import vay.enterwind.auto2000samarinda.session.AuthManagement;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {

    private List<Sales> salesList;
    private Activity mContext;
    private ReportAdapter adapter;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtNama) TextView txtNama;
        @BindView(R.id.txtEmail) TextView txtEmail;

        @BindView(R.id.imgPhoto) CircleImageView imgPhoto;

        @BindView(R.id.txtTaken) TextView txtTaken;
        @BindView(R.id.txtReported) TextView txtReported;
        @BindView(R.id.txtRemain) TextView txtRemain;

        @BindView(R.id.item) CardView item;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ReportAdapter(Activity mContext, List<Sales> salesList) {
        this.salesList = salesList;
        this.mContext = mContext;
        this.adapter = this;
    }

    @Override
    public ReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_report, parent, false);

        return new ReportAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ReportAdapter.MyViewHolder holder, int position) {
        final Sales sales = salesList.get(position);
        holder.txtNama.setText(sales.getNama());
        holder.txtEmail.setText(sales.getEmail());
        Picasso.with(mContext).load(sales.getFoto())
                .error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher)
                .into(holder.imgPhoto);

        holder.txtTaken.setText("Taken " + sales.getPlanning() + " Plan");
        holder.txtReported.setText("Reported " + sales.getReported() + " Plan");
        holder.txtRemain.setText("Remains " + sales.getRemains() + " Plan");

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, PlanActivity.class);
                i.putExtra("EMAIL", sales.getEmail());
                i.putExtra("NAMA", sales.getNama());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

}
