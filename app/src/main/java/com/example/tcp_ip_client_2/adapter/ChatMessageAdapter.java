package com.example.tcp_ip_client_2.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcp_ip_client_2.classs.ChatModel;
import com.example.tcp_ip_client_2.R;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemSelected(int position,View view, ChatModel chatModel);
    }

    public interface OnItemLongClickListener {
        boolean onItemSelected(int position,View view, ChatModel chatModel);
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        final TextView messageInView,timestampInView,messageOutView,timestampOutView;
        final View root_in,root_out,llIn,llOut;

        public ViewHolder(@NonNull View viewInOut) {
            super(viewInOut);
            llIn = viewInOut.findViewById(R.id.llIn);
            llOut = viewInOut.findViewById(R.id.llOut);
            root_out = viewInOut.findViewById(R.id.root_out);
            root_in = viewInOut.findViewById(R.id.root_in);
            messageInView = viewInOut.findViewById(R.id.messageIn);
            timestampInView = viewInOut.findViewById(R.id.time_stampIn);
            messageOutView = viewInOut.findViewById(R.id.messageOut);
            timestampOutView = viewInOut.findViewById(R.id.time_stampOut);

            viewInOut.setOnClickListener(view -> {
                if(mOnItemClickListener!=null){
                    int position = getLayoutPosition();
                    //Log.d("MessageAdapterClick", "click "+ position);
                    mOnItemClickListener.onItemSelected(position,view,mDataSet.get(position));
                }
            });

            viewInOut.setOnLongClickListener(view -> {
                if(mOnItemLongClickListener!=null){
                    int position = getLayoutPosition();
                    //Log.d("MessageAdapterLongClick", "LongClick "+ position);
                    return mOnItemLongClickListener.onItemSelected(position,view,mDataSet.get(position));
                }
                return true;
            });
        }
    }

    private final List<ChatModel> mDataSet;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    @SuppressLint("NotifyDataSetChanged")
    public ChatMessageAdapter(List<ChatModel> msg) {
        this.mDataSet = msg;
        notifyDataSetChanged();
    }

    //вызывается один раз
    //создает и возвращает для каждого элемента из источника данных ссылку на представление
    @NonNull
    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_in_out, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageAdapter.ViewHolder holder, int position) {
        ChatModel chatModel = mDataSet.get(position);
        if (chatModel.isOutgoing()) {
            holder.llIn.setVisibility(View.GONE);
            holder.llOut.setVisibility(View.VISIBLE);
            holder.root_in.setVisibility(View.GONE);
            holder.root_out.setVisibility(View.VISIBLE);
            holder.messageInView.setVisibility(View.GONE);
            holder.timestampInView.setVisibility(View.GONE);
            holder.messageOutView.setVisibility(View.VISIBLE);
            holder.timestampOutView.setVisibility(View.VISIBLE);
            holder.messageOutView.setText(chatModel.getMessage_out());
            holder.timestampOutView.setText(chatModel.getMessage_time());
        } else {
            holder.llIn.setVisibility(View.VISIBLE);
            holder.llOut.setVisibility(View.GONE);
            holder.root_in.setVisibility(View.VISIBLE);
            holder.root_out.setVisibility(View.GONE);
            holder.messageOutView.setVisibility(View.GONE);
            holder.timestampOutView.setVisibility(View.GONE);
            holder.messageInView.setVisibility(View.VISIBLE);
            holder.timestampInView.setVisibility(View.VISIBLE);
            holder.messageInView.setText(chatModel.getMessage_in());
            holder.timestampInView.setText(chatModel.getMessage_time());
        }
        //ViewHolder viewHolder = (ViewHolder) holder;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateAdapter(List<ChatModel> list){
        this.mDataSet.clear();
        this.mDataSet.addAll(list);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteItemByPosition(int position){
        mDataSet.remove(position);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteItem(ChatModel chatModel){
        mDataSet.remove(chatModel);
        notifyDataSetChanged();
    }

}

