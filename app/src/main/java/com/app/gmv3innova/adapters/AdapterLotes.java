package com.app.gmv3innova.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.gmv3innova.R;
import com.app.gmv3innova.activities.ActivityProductDetail;
import com.app.gmv3innova.models.Lotes;

import java.util.List;
import java.util.Locale;

public class AdapterLotes extends RecyclerView.Adapter<AdapterLotes.ViewHolder> {

    private Context context;
    private List<Lotes> arrayItemCart;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_id_lote;
        TextView txt_date_lote;
        TextView txt_cant_lote;

        public ViewHolder(View view) {
            super(view);
            txt_id_lote = view.findViewById(R.id.id_name_lote);
            txt_date_lote = view.findViewById(R.id.id_date_lote);
            txt_cant_lote = view.findViewById(R.id.id_cant_lote);
        }

    }

    public AdapterLotes(Context context, List<Lotes> arrayItemCart) {
        this.context = context;
        this.arrayItemCart = arrayItemCart;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lotes, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txt_id_lote.setText(ActivityProductDetail.lote_name.get(position));
        holder.txt_date_lote.setText(("Exp. ").concat(ActivityProductDetail.lote_date.get(position)));



        String lote_cantidad = String.format(Locale.ENGLISH, "%1$,.2f", Double.parseDouble(ActivityProductDetail.lote_cant.get(position)));

        holder.txt_cant_lote.setText(lote_cantidad);


    }

    @Override
    public int getItemCount() {
        return ActivityProductDetail.lote_name.size();
    }

}
