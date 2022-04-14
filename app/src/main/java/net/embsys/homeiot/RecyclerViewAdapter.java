package net.embsys.homeiot;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private List<Items> items;
    MainActivity mainActivity;

    public RecyclerViewAdapter(Activity activity, List<Items> items) {
        this.activity = activity;
        //MainActivity의 recyclerViewAdapter = new RecyclerViewAdapter(this,person); person 연관
        this.items = items;
        mainActivity = (MainActivity)activity;
    }

    //data 갯수 반환
    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        TextView count;
        ImageView image;
        Button addbtn;

        public ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            count = (TextView) itemView.findViewById(R.id.count);
            image = (ImageView) itemView.findViewById(R.id.image);
            addbtn = (Button) itemView.findViewById(R.id.addbtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "click " +
                            items.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    mainActivity.clientThread.sendDataCheck("[" + ClientThread.devId + "]" + "PURCHASE@" + items.get(getAdapterPosition()).getName());
      //              mainActivity.clientThread.sendDataCheck("[TEST]TEST");
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(activity, "remove " +
                            items.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    removeItemView(getAdapterPosition());
                    return false;
                }
            });
            addbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity, "addbtn " +
                            items.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //뷰 홀더를 생성하고 뷰를 붙여주는 부분
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Items data = items.get(position);

        // 데이터 결합
        holder.name.setText(data.getName());
        holder.price.setText(data.getPrice());
        holder.count.setText(data.getCount());
        //Glide.with(mainActivity).load(R.drawable.coke).into(holder.image);
        Glide.with(mainActivity).load("http://192.168.1.139/image/coke.jpg").into(holder.image);
    }

    private void removeItemView(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size()); // 지워진 만큼 다시 채워넣기.
    }

    public void setFriendList(List<Items> list){
        this.items = list;
        notifyDataSetChanged();
    }


}
