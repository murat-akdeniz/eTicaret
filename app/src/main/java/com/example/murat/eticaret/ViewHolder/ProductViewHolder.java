package com.example.murat.eticaret.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.murat.eticaret.Interface.ItemClickListener;
import com.example.murat.eticaret.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtUrunAdi,txtUrunTanimi,txtUrunFiyati;
    public ImageView imageView;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=(ImageView)itemView.findViewById(R.id.urun_resmi);
        txtUrunAdi=(TextView) itemView.findViewById(R.id.urun_adi);
        txtUrunTanimi=(TextView) itemView.findViewById(R.id.urun_tanimi);
        txtUrunFiyati=(TextView)itemView.findViewById(R.id.urun_fiyati);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);

    }
}
