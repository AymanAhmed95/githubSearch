package com.example.root.githubsearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by root on 2/14/18.
 */

public class ResultDataAdapter extends RecyclerView.Adapter<ResultDataAdapter.ResultViewHolder> {

    private String[] resultDataSRC;
    final private ResultItemClickListener listener;

    public interface ResultItemClickListener{

        void onResultItemClickListener(int position);

    }


    public class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView resultItemView;
        public ResultViewHolder(View itemView) {
            super(itemView);
            resultItemView = (TextView) itemView.findViewById(R.id.result_item_name);
            itemView.setOnClickListener(this);

        }

        public void bind(String resultItemStr){
            resultItemView.setText(resultItemStr);
        }

        @Override
        public void onClick(View view) {
            listener.onResultItemClickListener(getAdapterPosition());
        }
    }






    public  ResultDataAdapter(String[] resultData,ResultItemClickListener listener){
        this.resultDataSRC = resultData;
        this.listener = listener;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View resultViewInflated = inflater.inflate(R.layout.result_item,parent,false);
        ResultViewHolder resultViewHolder = new ResultViewHolder(resultViewInflated);
        return resultViewHolder;
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        holder.bind(this.resultDataSRC[position]);
    }

    @Override
    public int getItemCount() {
        return this.resultDataSRC.length;
    }
}
