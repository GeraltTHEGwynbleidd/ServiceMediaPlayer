package com.gwynbleidd.servicemediaplayer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.gwynbleidd.servicemediaplayer.database.ObjectBox;
import com.gwynbleidd.servicemediaplayer.database.entity.MusicObjs;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicListViewHolder>  {

    private List<MusicObjs> musicDataset;
    private Context context;
    int playingPos;


    public MusicListAdapter(Context cont) {
        this.context = cont;
        this.musicDataset = new ArrayList<>();
    }


    public void setMusicDataset(List<MusicObjs> musicDataset) {
        this.musicDataset = musicDataset;
        notifyDataSetChanged();
    }

    public void GetDataSetFromAdapter(){
        EventBus.getDefault().post(new EventsFromMainActivity.DataSetFromAdapter(musicDataset));
        this.playingPos++;
    }





    @NonNull
    @Override
    public MusicListAdapter.MusicListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_recy, parent, false);
        return new MusicListAdapter.MusicListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListAdapter.MusicListViewHolder holder, int position) {
        MusicObjs musicObj = musicDataset.get(position);
        holder.SongName.setText(musicObj.getMusicName());
        holder.ArtistName.setText(musicObj.getMusicArtist());
        holder.AlbumName.setText(musicObj.getMusicAlbum());
        int dure = musicObj.getMusicDuration();
        int dure_hour = dure / 3600;
        int dure_min = (dure % 3600) / 60;
        int dure_sec = (dure % 3600) % 60;

        holder.SongDuration.setText(dure_hour + ":" + dure_min + ":" + dure_sec);


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return musicDataset.size();
    }

    class MusicListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        TextView SongName;
        TextView ArtistName;
        TextView AlbumName;
        TextView SongDuration;
        ImageView OtherBtn;





        public MusicListViewHolder(@NonNull View itemView) {
            super(itemView);

            SongName = itemView.findViewById(R.id.song_name);
            ArtistName = itemView.findViewById(R.id.song_artist);
            AlbumName = itemView.findViewById(R.id.song_album);
            SongDuration = itemView.findViewById(R.id.song_duration);
            OtherBtn = itemView.findViewById(R.id.other_btn);

            OtherBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, view);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.options_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_delete:
                                    final AlertDialog dialog = utils.warningDialog(context,
                                            "Are you sure you want to delete " + musicDataset.get(getAdapterPosition()).getMusicName() + "?")
                                            .create();
                                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(context,
                                                    "Delete " + musicDataset.get(getAdapterPosition()).getMusicName(),
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                            if(playingPos==getAdapterPosition()){
                                                Toast.makeText(context, "this Song is playing ,  You Can not delete it", Toast.LENGTH_SHORT).show();
                                            }else {
                                                if (playingPos > getAdapterPosition()) {
                                                    playingPos--;
                                                    EventBus.getDefault().post(new EventsFromMainActivity.UpdatePlayingposition(playingPos));
                                                }
                                                ObjectBox.get().boxFor(MusicObjs.class).remove(musicDataset.get(getAdapterPosition()).getMusicId());
                                                setMusicDataset(ObjectBox.get().boxFor(MusicObjs.class).getAll());
                                                EventBus.getDefault().post(new EventsFromMainActivity.NumOfSongsFromAdapter(musicDataset.size()));
                                            }
                                        }
                                    });
                                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();


                                    //handle menu1 click
//                                    final AlertDialog.Builder adb = new AlertDialog.Builder(context);
//                                    adb.setTitle("Warning");
//                                    adb.setMessage("Are you sure you want to delete "+musicDataset.get(getAdapterPosition()).getMusicName()+"?");
//                                    adb.setIcon(android.R.drawable.ic_dialog_alert);
//                                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toast.makeText(context, "Delete " + musicDataset.get(getAdapterPosition()).getMusicName(), Toast.LENGTH_SHORT).show();
//                                            ObjectBox.get().boxFor(MusicObjs.class).remove(musicDataset.get(getAdapterPosition()).getMusicId());
//                                            setMusicDataset(ObjectBox.get().boxFor(MusicObjs.class).getAll());
//                                            EventBus.getDefault().post(new EventsFromMainActivity.NumOfSongsFromAdapter(musicDataset.size()));
//                                        }
//                                    });
//                                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            adb.create().cancel();
//                                        }
//                                    });
//                                    adb.show();
//                                    ObjectBox.DeleteMusic(context,musicDataset.get(getAdapterPosition()).getMusicName(),musicDataset.get(getAdapterPosition()).getMusicId());
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }




        @Override
        public void onClick(View view) {
            Toast.makeText(context, "Play " + musicDataset.get(getAdapterPosition()).getMusicName(), Toast.LENGTH_SHORT).show();
            playingPos = getAdapterPosition();
            EventBus.getDefault().post(new EventsFromMainActivity.LoadSelectedFile(musicDataset.get(getAdapterPosition()), playingPos));
        }

        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(context, "onLongClick " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
            return true;
        }


    }


}
