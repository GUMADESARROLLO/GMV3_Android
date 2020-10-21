package com.app.gmv3.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.gmv3.R;

import com.app.gmv3.models.Comentarios;
import com.app.gmv3.utilities.Utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerAdapterInteligenciaMercado extends RecyclerView.Adapter<RecyclerAdapterInteligenciaMercado.MyViewHolder> implements Filterable {

    private Context context;
    private List<Comentarios> productList;
    private List<Comentarios> productListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_title, product_date,product_comentario,product_autor;
        public ImageView product_image;

        public MyViewHolder(View view) {
            super(view);
            product_title = (TextView) view.findViewById(R.id.id_title);
            product_date = (TextView) view.findViewById(R.id.id_date);
            product_comentario = (TextView) view.findViewById(R.id.id_comentario);
            product_autor = (TextView) view.findViewById(R.id.id_autor);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onContactSelected(productListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public RecyclerAdapterInteligenciaMercado(Context context, List<Comentarios> productList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.productList = productList;
        this.productListFiltered = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news_horizontal, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Comentarios product = productListFiltered.get(position);

        PrettyTime prettyTime = new PrettyTime();
        long timeAgo = Utils.timeStringtoMilis(product.getFecha());
        holder.product_title.setText(product.getTitulo());
        holder.product_date.setText(prettyTime.format(new Date(timeAgo)));
        holder.product_comentario.setText(product.getContenido());
        holder.product_autor.setText(product.getAutor());

    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Comentarios> filteredList = new ArrayList<>();
                    for (Comentarios row : productList) {
                        if (row.getTitulo().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<Comentarios>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Comentarios product);
    }
}