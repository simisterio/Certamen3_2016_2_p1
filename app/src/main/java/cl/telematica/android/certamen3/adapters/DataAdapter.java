package cl.telematica.android.certamen3.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cl.telematica.android.certamen3.DataBase;
import cl.telematica.android.certamen3.Feed;
import cl.telematica.android.certamen3.R;

/**
 * Created by franciscocabezas on 11/18/16.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<Feed> mDataset;
    private Context mContext;
    private DataBase dbinstance;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public Button mAddBtn;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.textTitle);
            mImageView = (ImageView) v.findViewById(R.id.imgBackground);
            mAddBtn = (Button) v.findViewById(R.id.add_btn);
        }
    }

    public DataAdapter(Context mContext, List<Feed> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
        dbinstance = new DataBase(mContext);
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Feed feed = mDataset.get(position);

        holder.mTextView.setText(feed.getTitle());
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = feed.getLink();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
            }
        });

        Glide.with(mContext).load(feed.getImage()).into(holder.mImageView);

        if(feed.isFavorite()) {
            holder.mAddBtn.setText(mContext.getString(R.string.added));
        } else {
            holder.mAddBtn.setText(mContext.getString(R.string.like));
        }
        holder.mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * In this section, you have to manage the add behavior on local database
                 */

                SQLiteDatabase database = dbinstance.getWritableDatabase();

                feed.setFavorite(!feed.isFavorite());

                if(feed.isFavorite()) {
                    holder.mAddBtn.setText(mContext.getString(R.string.added));
                    //feed.setFavorite(!feed.isFavorite());
                    if (database != null ) {
                        database.execSQL("INSERT INTO favoritos(title, id, link, author,publishedDate,content, img, isFavorite)" +
                                "VALUES ('" + feed.getTitle() +
                                        "', '"+ feed.getId() +
                                        "', '"+ feed.getLink() +
                                        "', '"+ feed.getAuthor() +
                                        "', '"+ feed.getPublishedDate()+
                                        "', '"+ feed.getContent()+
                                        "','"+ feed.getImage()+
                                        "','"+ feed.isFavorite()+"')");
                        Toast.makeText(mContext,"se agrego", Toast.LENGTH_SHORT).show();
                    }
                    database.close();
                } else {
                    holder.mAddBtn.setText(mContext.getString(R.string.like));
                    //feed.setFavorite(!feed.isFavorite());
                    if(database != null) {
                       database.execSQL("DELETE FROM favoritos WHERE id = " + feed.getId());
                        //database.delete("favoritos", "title ="+feed.getTitle(), null);
                        Toast.makeText(mContext,"se elimino ", Toast.LENGTH_SHORT).show();
                    }
                    database.close();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
