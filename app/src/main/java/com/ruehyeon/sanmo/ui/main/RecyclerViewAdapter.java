package com.ruehyeon.sanmo.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruehyeon.sanmo.R;
import com.ruehyeon.sanmo.models.RulesEntity;


import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Activity activity;
    Context context;
    String rule_name;
    int lastPosition;
        private List<RulesEntity> entities = new ArrayList<>();
    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    // 리스너 객체 참조를 저장하는 변수
    private RecyclerViewAdapter.OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(RecyclerViewAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }
    public RecyclerViewAdapter(Context context) {
            this.context = context;
            this.activity =  (Activity)context;
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
    public void setEntities(List<RulesEntity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
        notifyItemInserted(entities.size() - 1);
    }
    public void setSampleEntities(List<RulesEntity> entities) {
        this.entities = entities;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);        return viewHolder;

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
        //entities.get(position).this_user_real_evaluation

        Log.d("준영", "앱 리스트의 사이즈는 " + getItemCount() + "입니다.");
        //NotiData data = notiData.get(position);
        RulesEntity data = entities.get(position);

        // 데이터 결합
//      holder.notiTitle.setText(data.getNotiTitle());
        Log.d("준영", position + " 번째 알림의 extra_Title은 " + data.rule + " 입니다.");

        switch( data.rule ) {
            case 0:
                rule_name = "단일치환암호";
                break;
            case 1:
                rule_name = "RSA";
                break;
            default:
                rule_name = "해당없음";
                break;
        }


        holder.rule.setText(rule_name);
            Log.d("준영", position + " 번째 알림의 extra_text은 " + data.profile_url + " 입니다.");
//        holder.extra_info_text.setText("extra_info_text : " + data.getExtra_info_text());
//        Log.d("준영", position + " 번째 알림의 extra_info_text은 " + data.getExtra_info_text() + " 입니다.");
//        holder.extra_people_list.setText("extra_people_text : " + data.getExtra_people_list());
//        Log.d("준영", position + " 번째 알림의 extra_people_text은 " + data.getExtra_people_list() + " 입니다.");
//        holder.extra_picture.setText("extra_picture : " + data.getExtra_picture());
//        Log.d("준영", position + " 번째 알림의 extra_picture은 " + data.getExtra_picture() + " 입니다.");
//        holder.extra_sub_text.setText(data.getExtra_sub_text());
//        Log.d("준영", position + " 번째 알림의 extra_sub_text은 " + data.getExtra_sub_text() + " 입니다.");
//        holder.extra_summary_text.setText("extra_summary_text : " + data.getExtra_summary_text());
//        Log.d("준영", position + " 번째 알림의 extra_summary_text은 " + data.getExtra_summary_text() + " 입니다.");
//        holder.extra_massage.setText("extra_massage : " + data.getExtra_massage());
//        Log.d("준영", position + " 번째 알림의 extra_massage은 " + data.getExtra_massage() + " 입니다.");
//        holder.group_name.setText("group_name : " + data.getGroup_name());
//        Log.d("준영", position + " 번째 알림의 group_name은 " + data.getGroup_name() + " 입니다.");
//        holder.app_string.setText("app_string : " + data.getApp_string());
//        Log.d("준영", position + " 번째 알림의 app_string은 " + data.getApp_string() + " 입니다.");

            holder.date.setText(data.created_time.toString());
            Log.d("준영", position + " 번째 알림의 noti_date은 " + data.created_time.toString() + " 입니다.");

            try{
            Drawable icon = activity.getPackageManager().getApplicationIcon("com.kakao.talk");
            holder.icon.setImageDrawable(icon);

        }
        catch (PackageManager.NameNotFoundException e)
        {

        }

        final PackageManager pm = activity.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = activity.getPackageManager().getApplicationInfo(data.profile_url, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
        holder.app_name.setText(data.app_name);
//        holder.package_name.setText(data.getPkg_name());
        //holder.noti_id.setText(Integer.toString((int) data.id));
        holder.counterpart.setText(data.counterpart);
        //holder.noti_category.setText(data.category);


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
        TextView noti_id;
        TextView counterpart;
        TextView rule;
        TextView app_name;
        ImageView icon;
//        TextView package_name;
//        TextView extra_info_text;
//        TextView extra_people_list;
//        TextView extra_picture;
//        TextView extra_sub_text;
//        TextView extra_summary_text;
//        TextView extra_massage;
//        TextView group_name;
//        TextView app_string;
        TextView date;
        TextView noti_category;


        public ViewHolder(View itemView) {
            super(itemView);
            counterpart = (TextView) itemView.findViewById(R.id.counterpart);
            rule = (TextView) itemView.findViewById(R.id.rule);
            icon = (ImageView) itemView.findViewById(R.id.app_icon);
            app_name = (TextView) itemView.findViewById(R.id.app_name);
            date = (TextView) itemView.findViewById(R.id.date);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(view, pos);
                        }
                    }



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
}
