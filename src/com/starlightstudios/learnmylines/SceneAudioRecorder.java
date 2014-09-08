package com.starlightstudios.learnmylines;

import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class SceneAudioRecorder extends MediaRecorder {
	private static String TAG = "SceneAudioRecorder";

	private Scene mScene;
	private String mFileName;
	private int mLineNumber = 0;
	
	public boolean start(Scene scene)
	{
		try
		{
			mScene = scene;
			setAudioSource(MediaRecorder.AudioSource.MIC);
			setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mFileName = createFileName();			
			setOutputFile(mFileName);
			prepare();
		}
		catch (IOException e)
		{
			Log.e(TAG, "prepare() failed", e);
			return false;
		}
		start();
		return true;
	}
	
	private String createFileName()
	{
		String fn = Environment.getExternalStorageDirectory().getAbsolutePath();
		fn += "/learnmylines";
		fn += UUID.randomUUID().toString();
		fn += ".3gp";
		return fn;
	}

	@Override
	public void stop()
	{
		super.stop();
		mScene.addLine(new Line(mFileName, mScene, "Line " + mLineNumber));
		mLineNumber++;
	}

}
