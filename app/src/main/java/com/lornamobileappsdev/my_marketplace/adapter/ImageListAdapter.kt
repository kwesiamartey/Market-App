package com.example.imageproject.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lornamobileappsdev.my_marketplace.R
import java.io.File

/*public class ImageListAdapter  extends BaseAdapter {

    Context context;
    List<Uri> list;

    public ImageListAdapter(Context context, List<Uri> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_image, null);

        ImageView imageView = view.findViewById(R.id.image);

        File file = new File(list.get(position).getPath());
        Picasso.get().load(file).into(imageView);


        return view;
    }
}*/

class ImageListAdapter (val context: Context, val list:MutableList<Uri>) :RecyclerView.Adapter<ImageListAdapter.MyViewHolder>(){
   inner class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        fun setData(featuredList: Uri, position: Int){
            val imageView = itemView.findViewById<ImageView>(R.id.image)

            var file =  File(list[position].path)
            Glide.with(context).load(file).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hold = list[position]
        holder.setData(hold, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
