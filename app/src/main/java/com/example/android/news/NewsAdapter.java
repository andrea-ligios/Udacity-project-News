package com.example.android.news;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ana on 14/06/2017.
 */

class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<News> News;
    private static OnItemClickListener Listener;
    private NewsActivity Context;

    NewsAdapter(NewsActivity context, ArrayList<News> news, OnItemClickListener listener) {
        Context = context;
        News = news;
        Listener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(News news);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {

        News news = News.get(position);

        holder.newsTitleTextView.setText(news.getTitle());
        holder.newsAuthorTextView.setText(news.getAuthor());
        holder.newsDateTextView.setText(news.getDate());
        holder.newsSectionTextView.setText(news.getSection());

        //Picasso Library to convert the url from JSONObject imageLinks to an image(@thumbnail)
        Picasso.with(Context).load(news.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.newsImageView);

        holder.bind(News.get(position), Listener);
    }

    @Override
    public int getItemCount() {
        return News.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView newsImageView;
        TextView newsTitleTextView;
        TextView newsAuthorTextView;
        TextView newsDateTextView;
        TextView newsSectionTextView;


        ViewHolder(View itemView) {
            super(itemView);

            newsImageView = (ImageView) itemView.findViewById(R.id.news_image);
            newsTitleTextView = (TextView) itemView.findViewById(R.id.news_title);
            newsAuthorTextView = (TextView) itemView.findViewById(R.id.news_author);
            newsDateTextView = (TextView) itemView.findViewById(R.id.news_date);
            newsSectionTextView = (TextView) itemView.findViewById(R.id.news_section);
        }

        void bind(final News news, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(news);
                }
            });
        }

    }

    void clear() {
        News.clear();
        notifyDataSetChanged();
    }

    void addAll(List<News> news) {
        News.addAll(news);
        notifyDataSetChanged();
    }
}

