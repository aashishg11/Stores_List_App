package com.aashishgodambe.android.brstores.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aashishgodambe.android.brstores.Model.Stores;
import com.aashishgodambe.android.brstores.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Aashish on 1/15/2016.
 */
public class AdapterStoreList extends RecyclerView.Adapter<AdapterStoreList.ViewHolderStoreList> {

    private List<Stores> storeList;
    private final String LOG_TAG = AdapterStoreList.class.getSimpleName();

    // Constructor to initialize data fed to adapter
    public AdapterStoreList(List<Stores> storeList) {
        this.storeList = storeList;
        Log.v(LOG_TAG,"Constructor created list items = "+storeList.size());
    }

    @Override
    public ViewHolderStoreList onCreateViewHolder(ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

        return new ViewHolderStoreList(row);
    }

    @Override
    public void onBindViewHolder(ViewHolderStoreList holder, int position) {

        Stores currentStore = storeList.get(position);
        holder.storeAddress.setText(currentStore.getAddress());
        holder.storePhone.setText("Ph: "+currentStore.getPhone());
        Log.v("AdapterStoreList", position + "");
        if(!currentStore.getStoreLogoURL().equals("") ) {
            Picasso.with(holder.itemView.getContext()).load(currentStore.getStoreLogoURL()).into(holder.storeImage);
        }
        // Item Animation
        YoYo.with(Techniques.FadeInUp)
                .duration(1000)
                .playOn(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    static class ViewHolderStoreList extends RecyclerView.ViewHolder{

        private ImageView storeImage;
        private TextView storeAddress;
        private TextView storePhone;

        public ViewHolderStoreList(View itemView) {
            super(itemView);

            storeAddress = (TextView) itemView.findViewById(R.id.textview_address);
            storePhone = (TextView) itemView.findViewById(R.id.textview_phone);
            storeImage = (ImageView) itemView.findViewById(R.id.image_view);

        }
    }
}
