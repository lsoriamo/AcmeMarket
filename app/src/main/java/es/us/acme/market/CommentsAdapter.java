package es.us.acme.market;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.us.acme.market.entities.Comment;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Comment> dataSet;

    public CommentsAdapter() {
        dataSet = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        dataSet.add(comment);
        notifyItemInserted(dataSet.indexOf(comment));
    }

    public void removeComment(Comment comment) {
        dataSet.remove(comment);
        int index = dataSet.indexOf(comment);
        if (index > -1) {
            dataSet.remove(comment);
            notifyItemRemoved(index);
        }
    }

    public void clearComments() {
        dataSet.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return dataSet.isEmpty();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int listPosition) {
        CommentViewHolder trainingViewHolder = (CommentViewHolder) holder;
        Comment comment = dataSet.get(listPosition);
        trainingViewHolder.comment_title.setText(comment.getTitle());
        trainingViewHolder.comment_text.setText(comment.getText());

        trainingViewHolder.comment_rating_bar.setRating(comment.getStars());
    }
    private class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView comment_title;
        RatingBar comment_rating_bar;
        TextView comment_text;

        CommentViewHolder(View itemView) {
            super(itemView);
            comment_title = itemView.findViewById(R.id.comment_title);
            comment_rating_bar = itemView.findViewById(R.id.comment_rating_bar);
            comment_text = itemView.findViewById(R.id.comment_text);
        }
    }
}
