package com.ruehyeon.sanmo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;



public class AdapterRe extends RecyclerView.Adapter<AdapterRe.MyViewHolder> {

    private LayoutInflater inflater;
    private List<String> myNameList = new ArrayList<String>();
    private List<String> myImageList = new ArrayList<String>();
    private RequestManager requestManager;

    public AdapterRe(Context ctx, List<String> myNameList, List<String> myImageList, RequestManager requestManager){
        inflater = LayoutInflater.from(ctx);
        this.myNameList = myNameList;
        this.requestManager = requestManager;
        this.myImageList = myImageList;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @Override
    public AdapterRe.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.rv_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterRe.MyViewHolder holder, int position) {

        holder.name.setText(myNameList.get(position));
        requestManager.load(myImageList.get(position)).placeholder(R.drawable.ic_action_name).into(holder.pf_image);
    }



    @Override
    public int getItemCount() {
        return myNameList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        CircleImageView pf_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            pf_image = (CircleImageView) itemView.findViewById(R.id.profile_pic);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });

        }

    }
}