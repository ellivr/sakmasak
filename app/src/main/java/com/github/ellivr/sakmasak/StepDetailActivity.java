package com.github.ellivr.sakmasak;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.ellivr.sakmasak.utils.GlobalVar;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class StepDetailActivity extends AppCompatActivity {

    Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if(savedInstanceState == null){
            arguments = new Bundle();
            arguments.putInt(GlobalVar.Const.EXTRA_STEPS_POS, getIntent().getIntExtra(GlobalVar.Const.EXTRA_STEPS_POS,0));
            arguments.putParcelableArrayList(GlobalVar.Const.EXTRA_STEPS, getIntent().getParcelableArrayListExtra(GlobalVar.Const.EXTRA_STEPS));

            //Place the StepDetail Fragment
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);

            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.stepdetailactivity_container, fragment, GlobalVar.Const.TAG_FRAGMENT_STEP);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
