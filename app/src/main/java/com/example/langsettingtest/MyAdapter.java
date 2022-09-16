package com.example.langsettingtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<SnackFood> snackfoods;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        ImageView tvImage;
        private CardView cardView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            //tvId = itemView.findViewById(R.id.id);
            tvName = itemView.findViewById(R.id.name);
            tvImage = itemView.findViewById(R.id.image);

            //System.out.println(tvId.getText());
            System.out.println(tvName.getText());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAbsoluteAdapterPosition();;

                    Context context = view.getContext();
                    Intent SnackinfoActivity = new Intent(mContext, SnackinfoActivity.class);
                    SnackinfoActivity.putExtra("foodName", snackfoods.get(pos).getFood());
                    SnackinfoActivity.putExtra("ingredient", snackfoods.get(pos).getIngredient());
                    SnackinfoActivity.putExtra("flavor", snackfoods.get(pos).getFlavor());
                    SnackinfoActivity.putExtra("spicy", snackfoods.get(pos).getSpicy());
                    SnackinfoActivity.putExtra("image", snackfoods.get(pos).getImage());

                    (mContext).startActivity(SnackinfoActivity);
                    notifyItemChanged(pos);

                }
            });

        }
    }

    public MyAdapter(ArrayList<SnackFood> snackFoods){
        this.snackfoods = snackFoods;
    }

    public MyAdapter(ArrayList<SnackFood> snackFoods, Context mContext){
        this.snackfoods = snackFoods;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // holder에 데이터 담기
        //holder.tvId.setText(snackfoods.get(position).getId());
        holder.tvName.setText(String.valueOf(snackfoods.get(position).getFood()));
        System.out.println(snackfoods.get(position).getImage());

        byte[] image = snackfoods.get(position).getImage();
        try{
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length);
            holder.tvImage.setImageBitmap(bitmap);
        }catch(Exception e){
            throw e;
        }

    }

    @Override
    public int getItemCount() {
        return snackfoods.size();
    }
}
