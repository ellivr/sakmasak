package com.github.ellivr.sakmasak;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ellivr.sakmasak.utils.GlobalVar;
import com.github.ellivr.sakmasak.utils.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {

    ArrayList<Steps> mSteps;
    SimpleExoPlayer simpleExoPlayer;
    BandwidthMeter bandwidthMeter;
    int mPos;
    Bundle args;
    int MIN_POS = 0;
    int MAX_POS;


    @BindView(R.id.step_short_description)
    TextView stepShortDescriptionView;
    @BindView(R.id.step_description)
    TextView stepDescriptionView;
    @BindView(R.id.btn_next)
    Button buttonNext;
    @BindView(R.id.btn_prev)
    Button buttonPrev;
    @BindView(R.id.exoplayerview)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.imageview)
    ImageView imageView;
    @BindView(R.id.dummyimageview)
    ImageView dummyimageView;


    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mPos = savedInstanceState.getInt(GlobalVar.Const.EXTRA_STEPS_POS,0);
            mSteps = savedInstanceState.getParcelableArrayList(GlobalVar.Const.EXTRA_STEPS);
            MAX_POS = mSteps.size()-1;
        }
        else {
            mPos = getArguments().getInt(GlobalVar.Const.EXTRA_STEPS_POS);
            mSteps = getArguments().getParcelableArrayList(GlobalVar.Const.EXTRA_STEPS);
            MAX_POS = mSteps.size()-1;
        }


        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        rootView.setVisibility(View.VISIBLE);

        initializeButtons();
        syncUI();

        return rootView;
    }

    private void syncUI() {
        if(mSteps.size() > 0 && mSteps != null){
            stepShortDescriptionView.setText(mSteps.get(mPos).getShortDescription());
            stepDescriptionView.setText(mSteps.get(mPos).getDescription());

            String thumbnailUrl = mSteps.get(mPos).getThumbnailURL();
            String videoUrl = mSteps.get(mPos).getVideoURL();

            if(thumbnailUrl.isEmpty()){
                imageView.setVisibility(View.GONE);
            }else{
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(thumbnailUrl).into(imageView);
            }

            if(!videoUrl.isEmpty()){
                simpleExoPlayerView.setVisibility(View.VISIBLE);
              //  simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                initializeExoPlayer(Uri.parse(videoUrl));
            }
            else{
                simpleExoPlayerView.setVisibility(View.GONE);
                simpleExoPlayer =null;
            }

            if(imageView.getVisibility() == View.GONE && simpleExoPlayerView.getVisibility() == View.GONE){
                dummyimageView.setVisibility(View.VISIBLE);
            }else{
                dummyimageView.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GlobalVar.Const.EXTRA_STEPS_POS, mPos);
        outState.putParcelableArrayList(GlobalVar.Const.EXTRA_STEPS,mSteps);
    }

    void initializeExoPlayer(Uri uri){
        if(simpleExoPlayer != null){
            return;
        }

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(new Handler(), videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        simpleExoPlayerView.setPlayer(simpleExoPlayer);

        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
    }

    void initializeButtons(){
        buttonPrev.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPos <= MIN_POS){
                    Toast.makeText(getContext(), R.string.reached_first_step, Toast.LENGTH_SHORT).show();
                    return;
                }
                mPos--;

                exoPlayerStopAndRelease();
                syncUI();
            }
        });

        buttonNext.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPos >= MAX_POS){
                    Toast.makeText(getContext(), R.string.reached_last_step, Toast.LENGTH_SHORT).show();
                    return;
                }
                mPos++;

                exoPlayerStopAndRelease();
                syncUI();
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        exoPlayerStopAndRelease();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        exoPlayerStopAndRelease();
    }

    @Override
    public void onStop() {
        super.onStop();
        exoPlayerStopAndRelease();
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayerStopAndRelease();
    }

    void exoPlayerStopAndRelease(){
        if (simpleExoPlayer!=null) {
            try{
                simpleExoPlayer.stop();
                simpleExoPlayer.release();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        }
    }
}
