package com.gwynbleidd.servicemediaplayer;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gwynbleidd.servicemediaplayer.database.ObjectBox;
import com.gwynbleidd.servicemediaplayer.database.entity.MusicObjs;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicListViewHolder> {

    private List<MusicObjs> musicDataset;
    private Context context;

    public MusicListAdapter(Context cont) {
        this.context = cont;
        this.musicDataset = new ArrayList<>();
    }

    public void setMusicDataset(List<MusicObjs> musicDataset) {
        this.musicDataset = musicDataset;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusicListAdapter.MusicListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_recy, parent, false);
        return new MusicListAdapter.MusicListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListAdapter.MusicListViewHolder holder, final int position) {
        MusicObjs musicObj = musicDataset.get(position);
        holder.SongName.setText(musicObj.getMusicName());
        holder.ArtistName.setText(musicObj.getMusicArtist());
        holder.AlbumName.setText(musicObj.getMusicAlbum());
        int dure = musicObj.getMusicDuration();
        int dure_hour = dure/3600;
        int dure_min= (dure%3600)/60;
        int dure_sec=(dure%3600)%60;

        holder.SongDuration.setText(dure_hour+":"+dure_min+":"+dure_sec);
        holder.OtherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, ""+view.getTag() +"--"+position, Toast.LENGTH_SHORT).show();
                ObjectBox.DeleteMusic(musicDataset.get(position).getMusicId());
                setMusicDataset(ObjectBox.get().boxFor(MusicObjs.class).getAll());
                EventBus.getDefault().post(new EventsFromMainActivity.NumOfSongsFromAdapter(musicDataset.size()));
//                notifyItemRemoved(position);
            }
        });

        holder.PlaySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, position+""+musicDataset.get(position).getMusicName(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new EventsFromMainActivity.LoadSelectedFile(musicDataset.get(position)));
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return musicDataset.size();
    }


    class MusicListViewHolder extends RecyclerView.ViewHolder {

        TextView SongName;
        TextView ArtistName;
        TextView AlbumName;
        TextView SongDuration;
        ImageView OtherBtn;
        ImageView PlaySelect;

        public MusicListViewHolder(@NonNull View itemView) {
            super(itemView);

            SongName = itemView.findViewById(R.id.song_name);
            ArtistName = itemView.findViewById(R.id.song_artist);
            AlbumName = itemView.findViewById(R.id.song_album);
            SongDuration = itemView.findViewById(R.id.song_duration);
            OtherBtn = itemView.findViewById(R.id.other_btn);
            PlaySelect = itemView.findViewById(R.id.play_selected);

        }

    }


}
