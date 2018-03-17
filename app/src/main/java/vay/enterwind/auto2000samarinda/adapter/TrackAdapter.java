package vay.enterwind.auto2000samarinda.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNGetStateResult;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.models.Sales;
import vay.enterwind.auto2000samarinda.module.supervisor.track.SalesMapActivity;
import vay.enterwind.auto2000samarinda.pubnub.Constants;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.MyViewHolder> {

    private List<Sales> salesList;
    private Activity mContext;
    private TrackAdapter adapter;
    private PubNub pubNub;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtNama) TextView txtNama;
        @BindView(R.id.txtEmail) TextView txtEmail;

        @BindView(R.id.imgPhoto) CircleImageView imgPhoto;
        @BindView(R.id.imgOnline) TextView imgOnline;
        @BindView(R.id.imgOffline) TextView imgOffline;

        @BindView(R.id.item) CardView item;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public TrackAdapter(Activity mContext, List<Sales> salesList, PubNub pubNub) {
        this.salesList = salesList;
        this.mContext = mContext;
        this.adapter = this;
        this.pubNub = pubNub;
    }

    @Override
    public TrackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_track, parent, false);

        return new TrackAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final TrackAdapter.MyViewHolder holder, int position) {
        final Sales sales = salesList.get(position);
        holder.txtNama.setText(sales.getNama());
        holder.txtEmail.setText(sales.getEmail());
        Picasso.with(mContext).load(sales.getFoto())
                .error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher)
                .into(holder.imgPhoto);

        this.pubNub.getPresenceState()
                .channels(Arrays.asList(Constants.PUBLISH_CHANNEL_NAME))
                .uuid(sales.getEmail())
                .async(new PNCallback<PNGetStateResult>() {
                    @Override
                    public void onResponse(PNGetStateResult result, PNStatus status) {

                        JsonElement shit = result.getStateByUUID().get(Constants.PUBLISH_CHANNEL_NAME);
                        JsonObject fuck = shit.getAsJsonObject();
                        JsonElement data = fuck.get("nameValuePairs");
                        if(data!= null) {
                            // Online
                            holder.imgOnline.setVisibility(View.VISIBLE);
                            holder.imgOffline.setVisibility(View.GONE);
                        } else {
                            // Offline
                            holder.imgOnline.setVisibility(View.GONE);
                            holder.imgOffline.setVisibility(View.VISIBLE);
                        }

                    }
                });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, SalesMapActivity.class);
                i.putExtra("EMAIL", sales.getEmail());
                i.putExtra("TELEPON", sales.getTelepon());
                i.putExtra("NAMA", sales.getNama());
                i.putExtra("PHOTO", sales.getFoto());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

}
