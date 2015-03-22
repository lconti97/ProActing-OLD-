package com.starlightstudios.learnmylines;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.learnmylines.R;

public class SceneEditFragment extends Fragment{
	private static final String TAG = "SceneEditFragment";

	private Button mRecordButton;
	private Button mUndoButton;
	private Button mDoneButton;
	private Button mCharMeButton;
	private Button mCharNotMeButton;
	private SceneAudioRecorder mRecorder;
	private boolean mRecording = false;
	private String mSpeaker;
	private Scene mScene;
	private ListView mLineHistory;
	private ArrayAdapter<Line> mLineHistoryAdapter;
	private EditPlayPagerActivity mActivity;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState)
	{
		Activity c = getActivity();
		int[] sceneIndex = c.getIntent().
				getIntArrayExtra(ProjectListFragment.EXTRA_SCENE_INDEX);
		mScene = ProjectManager.get()
				.getProjects().get(sceneIndex[0])
				.getActs().get(sceneIndex[1])
				.getScenes().get(sceneIndex[2]);

		View v = inflater.inflate(R.layout.fragment_scene_edit, parent, false);

		mLineHistory = (ListView)v.findViewById(R.id.fragment_scene_edit_ListView);
		mActivity = (EditPlayPagerActivity)getActivity();
		mLineHistoryAdapter = mActivity.getLineListAdapter();
		mLineHistory.setAdapter(mLineHistoryAdapter);
		
		mSpeaker = c.getString(R.string.me);

		mRecordButton = (Button)v.findViewById(R.id.fragment_scene_edit_buttonBar)
				.findViewById(R.id.button_center);
		mUndoButton = (Button)v.findViewById(R.id.fragment_scene_edit_buttonBar)
				.findViewById(R.id.button_left);
		mDoneButton = (Button)v.findViewById(R.id.fragment_scene_edit_buttonBar)
				.findViewById(R.id.button_right);
		mCharMeButton = (Button)v.findViewById(
				R.id.fragment_scene_edit_characterButtonMe);
		mCharNotMeButton = (Button)v.findViewById(
				R.id.fragment_scene_edit_characterButtonNotMe);

		mUndoButton.setText(R.string.undo);
		mDoneButton.setText(R.string.done);

		mUndoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO currently does nothing

			}
		});

		mDoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopRecording();
				mActivity.getViewPager().setCurrentItem(1, true);
			}
		});
		
		mCharMeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				v.setSelected(true);
				mSpeaker = getString(R.string.me);
			}
		});
		mCharNotMeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				v.setSelected(true);
				mSpeaker = getString(R.string.not_me);
			}
		});
		
		mCharNotMeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSpeaker = getString(R.string.not_me);
			}
		});
		//if mRecording, the fragment has been rotated
		if(!mRecording)
		{
			mRecordButton.setText(R.string.record);
		}
		else
		{
			mRecordButton.setText(R.string.stop);
		}
		mRecordButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!mRecording)
				{
					mRecording = mRecorder.start(mScene, mSpeaker);
					if(mRecording)
					{
						mRecordButton.setText(R.string.stop);
					}
					else
					{
						Log.i(TAG, "Recording failed to start");
					}
				}
				else
				{
					stopRecording();
				}
			}
		});

		return v;
	}


	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(mRecording)
		{
			stopRecording();
		}
		mRecorder.release();
		mRecorder = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mRecorder = new SceneAudioRecorder();
	}

	private void stopRecording()
	{
		if(mRecorder!=null && mRecording) {
			mRecorder.stop();
			mRecorder.reset();
			mRecordButton.setText(R.string.record);
			mRecording = false;
			mLineHistoryAdapter.notifyDataSetChanged();
		}
	}
}
