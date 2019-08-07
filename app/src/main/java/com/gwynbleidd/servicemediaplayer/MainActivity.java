package com.gwynbleidd.servicemediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gwynbleidd.servicemediaplayer.database.ObjectBox;
import com.gwynbleidd.servicemediaplayer.database.entity.MusicObjs;
import com.gwynbleidd.servicemediaplayer.database.entity.MusicObjs_;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonPlay, buttonPause, buttonStop, buttonLoad;
    CardView buttonSort;
    TextView playingSongInf, totalSongNumber;
    SeekBar seekbarPlay;
    boolean isUserSeeking;
    MediaPlayerReciver mediaPlayerReciver;
    RecyclerView musicsRecyclerView;
    Box<MusicObjs> musicObjsBox;
    MusicListAdapter musicListAdapter;
    List<MusicObjs> musicObjsList = new ArrayList<>();

    String sortType = "";
    String sortOrder = "";
    String sortResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayerReciver = new MediaPlayerReciver();
        registerReceiver(mediaPlayerReciver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
        initViews();
        EventBus.getDefault().register(this);
        startService(new Intent(this, MediaPlayerService.class));


        //
        musicObjsList = musicObjsBox.getAll();
        //
        musicListAdapter = new MusicListAdapter(this);
        musicListAdapter.setMusicDataset(musicObjsList);
        musicsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        musicsRecyclerView.setAdapter(musicListAdapter);

        //

//        buttonPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EventBus.getDefault().post(new EventsFromMainActivity.PlayEvent());
//            }
//        });
//
//        buttonPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EventBus.getDefault().post(new EventsFromMainActivity.PauseEvent());
//            }
//        });

        seekbarPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int userSelectedPosition = 0;


            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    userSelectedPosition = i;
                    isUserSeeking = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserSeeking = false;
                EventBus.getDefault().post(new EventsFromMainActivity.SeekToEvent(userSelectedPosition));
            }
        });
    }

    private void initViews() {
        buttonPlay = findViewById(R.id.button_play);
        buttonPause = findViewById(R.id.button_pause);
        buttonStop = findViewById(R.id.button_stop);
        buttonLoad = findViewById(R.id.button_load);
        buttonSort = findViewById(R.id.sort_card);

        buttonPlay.setOnClickListener(this);
        buttonPause.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonLoad.setOnClickListener(this);
        buttonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortDialog();
            }
        });


        playingSongInf = findViewById(R.id.info_song_name);
        totalSongNumber = findViewById(R.id.info_total_song);

        seekbarPlay = findViewById(R.id.seekbar_player);
        musicsRecyclerView = findViewById(R.id.music_recycler);

        musicObjsBox = ObjectBox.get().boxFor(MusicObjs.class);

    }

    public void SortSongs(String sortType) {
        QueryBuilder<MusicObjs> musicObjsQueryBuilder = musicObjsBox.query();
        List<MusicObjs> musicObjs;
        switch (sortType) {
            case "Name_ASC":
                musicObjs = musicObjsQueryBuilder.order(MusicObjs_.musicName).build().find();
                musicListAdapter.setMusicDataset(musicObjs);
                break;
            case "Name_DES":
                musicObjs = musicObjsQueryBuilder.order(MusicObjs_.musicName, QueryBuilder.DESCENDING).build().find();
                musicListAdapter.setMusicDataset(musicObjs);
                break;
            case "Album_ASC":
                musicObjs = musicObjsQueryBuilder.order(MusicObjs_.musicAlbum).build().find();
                musicListAdapter.setMusicDataset(musicObjs);
                break;
            case "Album_DES":
                musicObjs = musicObjsQueryBuilder.order(MusicObjs_.musicAlbum, QueryBuilder.DESCENDING).build().find();
                musicListAdapter.setMusicDataset(musicObjs);
                break;
            case "Artist_ASC":
                musicObjs = musicObjsQueryBuilder.order(MusicObjs_.musicArtist).build().find();
                musicListAdapter.setMusicDataset(musicObjs);
                break;
            case "Artist_DES":
                musicObjs = musicObjsQueryBuilder.order(MusicObjs_.musicArtist, QueryBuilder.DESCENDING).build().find();
                musicListAdapter.setMusicDataset(musicObjs);
                break;
            case "Dure_ASC":
                musicObjs = musicObjsQueryBuilder.order(MusicObjs_.musicDuration).build().find();
                musicListAdapter.setMusicDataset(musicObjs);
                break;
            case "Dure_DES":
                musicObjs = musicObjsQueryBuilder.order(MusicObjs_.musicDuration, QueryBuilder.DESCENDING).build().find();
                musicListAdapter.setMusicDataset(musicObjs);
                break;
            case "Date_ASC":
                musicObjs = musicObjsQueryBuilder.order(MusicObjs_.musicDatemodified).build().find();
                musicListAdapter.setMusicDataset(musicObjs);
                break;
            case "Date_DES":
                musicObjs = musicObjsQueryBuilder.order(MusicObjs_.musicDatemodified, QueryBuilder.DESCENDING).build().find();
                musicListAdapter.setMusicDataset(musicObjs);
                break;
        }

    }


    public void SortDialog() {
        final Dialog sortDialog = new Dialog(this, R.style.ThemeDialog);
        sortDialog.setContentView(R.layout.dialog_sort_lay);

        RadioGroup sortTypeGroup = sortDialog.findViewById(R.id.dialog_sort_items);
        RadioGroup sortOrderGroup = sortDialog.findViewById(R.id.dialog_sort_asc_dsc);
        Button sortOkBtn = sortDialog.findViewById(R.id.sort_dialog_ok_btn);
        Button sortCancelBtn = sortDialog.findViewById(R.id.sort_dialog_cancel_btn);


        switch (sortTypeGroup.getCheckedRadioButtonId()) {
            case R.id.dialog_sort_name:
                sortType = "Name";
                break;
            case R.id.dialog_sort_artist:
                sortType = "Artist";
                break;
            case R.id.dialog_sort_album:
                sortType = "Album";
                break;
            case R.id.dialog_sort_duration:
                sortType = "Dure";
                break;
            case R.id.dialog_sort_modified:
                sortType = "Date";
                break;
        }

        switch (sortOrderGroup.getCheckedRadioButtonId()) {
            case R.id.dialog_sort_asc:
                sortOrder = "ASC";
                break;
            case R.id.dialog_sort_des:
                sortOrder = "DES";
                break;
        }


        sortTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.dialog_sort_name:
                        sortType = "Name";
                        break;
                    case R.id.dialog_sort_artist:
                        sortType = "Artist";
                        break;
                    case R.id.dialog_sort_album:
                        sortType = "Album";
                        break;
                    case R.id.dialog_sort_duration:
                        sortType = "Dure";
                        break;
                    case R.id.dialog_sort_modified:
                        sortType = "Date";
                        break;
                }
            }
        });
        sortOrderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.dialog_sort_asc:
                        sortOrder = "ASC";
                        break;
                    case R.id.dialog_sort_des:
                        sortOrder = "DES";
                        break;
                }
            }
        });

        sortOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortResult = sortType + "_" + sortOrder;
//                Toast.makeText(MainActivity.this, sortResult, Toast.LENGTH_SHORT).show();
                SortSongs(sortResult);
                sortDialog.dismiss();
            }
        });

        sortCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortDialog.dismiss();
            }
        });

        sortDialog.show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mediaPlayerReciver);
    }

    @Subscribe
    public void onMessageState(EventsFromService.StateEvent stateEvent) {
//        Toast.makeText(this, stateEvent.currentState + "", Toast.LENGTH_SHORT).show();
    }

    @Subscribe()
    public void onMessageSetSeekbarMax(EventsFromService.PlaybackDuration event) {
        seekbarPlay.setMax(event.duration);
    }

    @Subscribe()
    public void onMessageSeekbarPosition(EventsFromService.PlaybackPosition playbackPosition) {
        if (!isUserSeeking) {
            seekbarPlay.setProgress(playbackPosition.position);
        }
    }

    @Subscribe()
    public void onMessageLoadSongsUpdateAdapter(EventsFromService.DatabaseIsUpdate databaseIsUpdate) {
        musicListAdapter.setMusicDataset(databaseIsUpdate.musicObjsList);
        totalSongNumber.setText(String.valueOf(databaseIsUpdate.musicObjsList.size()));
    }

    @Subscribe
    public void onMessageNumOfSongsChange(EventsFromMainActivity.NumOfSongsFromAdapter numOfSongsFromAdapter) {
        totalSongNumber.setText(String.valueOf(numOfSongsFromAdapter.numOfSongs));
    }

    @Subscribe
    public void onMessagePlayingMusicobjGet(EventsFromService.SongInfoSend songInfo) {
        playingSongInf.setText(songInfo.musicObjs.getMusicName());
    }

    @Subscribe
    public void playbackCompleteEvent(EventsFromService.PlaybackCompleted playbackCompleted) {
        musicListAdapter.GetDataSetFromAdapter();
//        Toast.makeText(this, "completed "+playbackCompleted.musicObjs.getMusicName(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_play:
                if (!musicObjsList.isEmpty()) {
                    EventBus.getDefault().post(new EventsFromMainActivity.LoadSelectedFile(musicObjsList.get(0), 0));
                }
                break;
            case R.id.button_pause:
                EventBus.getDefault().post(new EventsFromMainActivity.PauseEvent());
                break;
            case R.id.button_stop:
                EventBus.getDefault().post(new EventsFromMainActivity.StopEvent());
                break;
            case R.id.button_load:
                EventBus.getDefault().post(new EventsFromMainActivity.LoadEvent());
                break;
        }
    }


//    private class MusicIntentReceiver extends BroadcastReceiver {
//        String TAG = "Receiverlog";
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
//                int state = intent.getIntExtra("state", -1);
//                switch (state) {
//                    case 0:
//                        Log.d(TAG, "Headset is unplugged");
//                        break;
//                    case 1:
//                        Log.d(TAG, "Headset is plugged");
//                        break;
//                    default:
//                        Log.d(TAG, "I have no idea what the headset state is");
//                }
//            }
//        }
//    }

}
