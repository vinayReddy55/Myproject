package com.example.myownapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myownapp.Models.PostModel;
import com.example.myownapp.R;
import com.example.myownapp.Viewactivity;
import com.example.myownapp.myinterfaces.RefreshList;
import com.example.myownapp.postactivity;
import com.example.myownapp.utilitis.FirebaseUtilitis;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.postViewholder> {
    List<PostModel> postModelList;
    Context context;
    FirebaseUtilitis firebaseUtilities;
    OutputStream outputStream;
  private int mcounter=0;


    public PostAdapter(List<PostModel> postModelList, Context context) {
        this.postModelList = postModelList;
        this.context = context;
        firebaseUtilities = new FirebaseUtilitis(context);

    }

    @NonNull
    @Override
    public postViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        postViewholder postViewholder1=new postViewholder(LayoutInflater.from(context)
                .inflate(R.layout.postitem,parent,false));
        return postViewholder1;
    }

    @Override
    public void onBindViewHolder(@NonNull postViewholder holder, int position) {
        holder.title.setText(postModelList.get(position).getTitle());
        holder.description.setText(postModelList.get(position).getDescription());
        Glide.with(context).load(postModelList.get(position).getImageUrl()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Viewactivity.class);

                intent.putExtra("image_id",postModelList.get(position).getImageUrl());
                intent.putExtra("Title",postModelList.get(position).getTitle());
                intent.putExtra("Description",postModelList.get(position).getDescription());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    class postViewholder extends RecyclerView.ViewHolder{

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.description)
     TextView description;



        public postViewholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context).setMessage("Action")
                            .setMessage("Choose Action to perform")
                            .setPositiveButton("Close",null)

                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {@Override
                                public void onClick(DialogInterface dialog, int which) {
                                   firebaseUtilities .deleteImageByUrl(null,postModelList.get(getAdapterPosition()),false);

                                }
                            }).create().show();
                    return false;
                }
            });
        }
    }
}
