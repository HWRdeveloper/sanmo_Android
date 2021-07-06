package com.ruehyeon.sanmo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruehyeon.sanmo.models.ChatEntity;

import java.util.ArrayList;
import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {
    Activity activity;
    Context context;

    int lastPosition;
        private List<ChatEntity> entities = new ArrayList<>();

    public ChatRecyclerAdapter(Context context) {
            this.context = context;
            //this.activity =  (Activity)context;
    }
    @Override
    public int getItemCount() {
        Log.d("준영_갱신", "getItemCount: 실행됨");
        return entities.size();
        //return notiData.size();
    }
    @Override
    public long getItemId(int position) {
        //NotiData data = notiData.get(position);
        //return data.getNoti_id();
        return  entities.get(position).id;
    }
    public void setEntities(List<ChatEntity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
        notifyItemInserted(entities.size() - 1);
    }
    public void setSampleEntities(List<ChatEntity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        switch(viewType) {
            case Constants.OTHER_MESSAGE:
                layout = R.layout.item_other;
                break;
            case Constants.USER_MESSAGE:
                layout = R.layout.item_user;
                break;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
  /*    else if(false) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_date_item, parent, false);
            DateViewHolder dateViewHolder = new DateViewHolder(view);        return dateViewHolder;
        }*/
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        TextView date;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //setAnimation(holder.itemView, position);
        if(getItemCount() == 0){
            Log.d("준영", "앱 리스트의 사이즈가 0입니다.");
            return;


        }

        ChatEntity data = entities.get(position);
        holder.message.setText(data.message);




    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.popup);
            animation.setInterpolator(new DecelerateInterpolator());
            if(position < 4) animation.setStartOffset(position * 200);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }



    public void removeItemView(int position, String uid) {
        //db에서도 값을 지웁니다.
        Log.d("지웁니다", "noti_idx2: "+position);
        entities.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, entities.size()); // 지워진 만큼 다시 채워넣기.
    }




    class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;




        public ViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message);





            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent("remove");
                    intent.putExtra("position", getAdapterPosition());
                    System.out.println("getAdpaterPosition은 " + getAdapterPosition());
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                    return true;
                }
            });
        }
    }


    public void clicked() {

    }

    @Override
    public int getItemViewType(int pos) {
        return entities.get(pos).type;
    }

}
