package ma.emsi.androidonlinequizapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ma.emsi.androidonlinequizapp.Interface.ItemClickListener;
import ma.emsi.androidonlinequizapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

   public TextView category_name;
   public ImageView category_image;
   private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        category_name=(TextView)itemView.findViewById(R.id.category_name);
        category_image=(ImageView)itemView.findViewById(R.id.category_image);

        itemView.setOnClickListener(this);

    }

    public CategoryViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
