package com.example.admin.nihongo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.nihongo.model.Word;

import java.util.List;


/**
 * Created by Admin on 2017/8/3.
 */

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {
    private List<Word> list;

    // 点击监听，长按监听事件
    public interface OnItemOnClickListener{
        void onItemOnClick(View view,int pos);
        void onItemOnTouch(View view,int pos);
        void onItemLongOnClick(View view ,int pos);
    }
    // 接口的内部变量
    private OnItemOnClickListener mOnItemOnClickListener;
    // setter
    public void setItemOnClickListener(OnItemOnClickListener listener){
        this.mOnItemOnClickListener = listener;
    }

    public WordListAdapter(List<Word> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Word item = list.get(position);
        holder.wordId.setText(item.getId() + ""); // 傻X函数不能自动把int转换为String
        holder.wordPos.setText(position + ""); // 傻X函数不能自动把int转换为String
        holder.japanese.setText(item.getJapanese());
        holder.kanJi.setText(item.getKanJi());
        holder.nominal.setText(item.getNominal());
        holder.chinese.setText(item.getChinese());

        // 给每个ItemView分别添加监听
        if(mOnItemOnClickListener!=null) {
            // 点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                // 执行接口中定义的函数, 函数实现在HomeFragment中
                mOnItemOnClickListener.onItemOnClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    mOnItemOnClickListener.onItemOnTouch(holder.itemView, position);
                    return false;
                }
            });
            // 长按事件
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                mOnItemOnClickListener.onItemLongOnClick(holder.itemView, position);
                return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView wordId;
        public TextView wordPos;
        public TextView japanese;
        public TextView kanJi;
        public TextView nominal;
        public TextView chinese;

        public ViewHolder(View itemView) {
            super(itemView);
            wordId = itemView.findViewById(R.id.wordId);
            wordPos = itemView.findViewById(R.id.wordPos);
            japanese = itemView.findViewById(R.id.wordJapanese);
            kanJi = itemView.findViewById(R.id.wordKanJi);
            nominal = itemView.findViewById(R.id.wordNominal);
            chinese = itemView.findViewById(R.id.wordChinese);
        }
    }

    public void removeItem(int pos){
        list.remove(pos);
        notifyItemRemoved(pos);
        // 删除后重新为每个item绑定position
        notifyItemRangeChanged(0,list.size());
    }


}
