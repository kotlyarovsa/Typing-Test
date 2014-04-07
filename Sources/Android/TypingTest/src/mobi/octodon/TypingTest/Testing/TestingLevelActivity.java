package mobi.octodon.TypingTest.Testing;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import mobi.octodon.TypingTest.Common.KeyboardHelper;
import mobi.octodon.TypingTest.Settings.ProfileHelper;
import mobi.octodon.TypingTest.Common.UIConstants;
import mobi.octodon.TypingTest.Settings.Settings;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveByModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.entity.util.ScreenCapture;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 19.09.13
 * Time: 0:13
 * To change this template use File | Settings | File Templates.
 */
public class TestingLevelActivity extends BaseGameActivity
	 implements Scene.IOnSceneTouchListener, IUpdateHandler {

	private Camera mCamera;
	private TestingLevelResMan resMan;

	private KeyboardHelper keyboardHelper;
	public TestingStatistic statistic;

	private boolean isRunning;
	private float pastTime;
	private int gameTime;
	private int currentPosition;
	public String testingText;

	private int editorTextOffset = 0;
	public ArrayList<Text> charSprites = new ArrayList<Text>();

	public View GetView()
	{
		return mRenderSurfaceView.getRootView();
	}

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, UIConstants.CAMERA_WIDTH, UIConstants.CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(UIConstants.CAMERA_WIDTH, UIConstants.CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		resMan = new TestingLevelResMan(this);
		resMan.LoadResources();
	}

	ScreenCapture screenCapture = new ScreenCapture();
	@Override
	public Scene onLoadScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		mEngine.registerUpdateHandler(this);

		Scene scene = resMan.LoadScene();
		scene.setOnSceneTouchListener(this);

//		resMan.resultBackground.attachChild(screenGrabber);
		scene.attachChild(screenCapture);
		keyboardHelper = new KeyboardHelper(this);
//		String lang = getApplicationContext().getResources().getConfiguration().locale.getLanguage();
//		testingModel.ChangeLang(lang);

		return scene;
	}

	@Override
	public void onLoadComplete() {
		Settings.LoadSettings();

		FrameLayout root = (FrameLayout)GetView();
		resMan.LoadControls(root);
		SetPiranhaControlSettings();

		resMan.startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SetCurrentSettings();
				ProfileHelper.TryAddProfile(Settings.Profile);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						LoadTestingText();
						statistic.TotalTime = Settings.LevelTime;
						gameTime = Settings.LevelTime * 60;
						if(Settings.PiranhaRun)
							resMan.timeText.setText(String.format("0"));
						else
							resMan.timeText.setText(String.format("0/%d", gameTime));

						resMan.speedText.setText("0");
						resMan.speedText.setPosition(
							 UIConstants.CAMERA_WIDTH - resMan.speedText.getWidth() - UIConstants.TESTING_BORDER_WIDTH,
							 UIConstants.TESTING_BORDER_WIDTH);

						resMan.controlsView.setVisibility(View.GONE);
						resMan.introBackground.setVisible(false);
						TestingLevelActivity.this.mRenderSurfaceView.getRootView().setFocusableInTouchMode(true);
						TestingLevelActivity.this.mRenderSurfaceView.getRootView().requestFocusFromTouch();
//						keyboardHelper.GetInputManager().showSoftInput(
//							 TestingLevelActivity.this.mRenderSurfaceView.getRootView(), InputMethodManager.SHOW_FORCED);
					}
				});
			}
		});

		resMan.retryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PrepareCurrentSubLevel();
			}
		});

		resMan.tweetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				TweetResult();
			}
		});

		resMan.screenButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ScreenResult(null);
			}
		});

		PrepareCurrentSubLevel();
	}

	private void SetCurrentSettings()
	{
		Settings.Profile = resMan.userProfileTextEdit.getText().toString();
		Settings.Keyboard = resMan.keyboardSpinner.getSelectedItem().toString();
		if(!Settings.PiranhaRun)
			Settings.TestingText = resMan.textChooserSpinner.getSelectedItem().toString();
		Settings.LevelTime = Integer.parseInt(resMan.timeSpinner.getSelectedItem().toString().substring(0, 1));
		Settings.Language = resMan.languageSpinner.getSelectedItem().toString();
		Settings.SaveSettings();
	}

	private void SetPiranhaControlSettings()
	{
		if(Settings.PiranhaRun)
		{
			resMan.textChooserSpinner.setEnabled(false);
			resMan.timeIntroText.setVisible(false);
			resMan.timeSpinner.setVisibility(View.INVISIBLE);
			resMan.languageIntroText.setVisible(false);
			resMan.languageSpinner.setVisibility(View.INVISIBLE);
		}
		else
		{
			resMan.textChooserSpinner.setEnabled(true);
			resMan.timeIntroText.setVisible(true);
			resMan.timeSpinner.setVisibility(View.VISIBLE);
			resMan.languageIntroText.setVisible(true);
			resMan.languageSpinner.setVisibility(View.VISIBLE);
		}
	}

	public void PrepareCurrentSubLevel()
	{
		runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				Reset();
				resMan.resultBackground.setVisible(false);
				resMan.introBackground.setVisible(true);
			}
		});
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				resMan.outroControlsView.setVisibility(View.GONE);
				resMan.controlsView.setVisibility(View.VISIBLE);
			}
		});
	}

	public void LoadTestingText()
	{
		testingText = resMan.GetTestText();

		int charLeftMargin = 0, charTopMargin = 0;

		String[] words = testingText.split(" ");
		statistic.WordsCount = words.length;

		for(String word : words)
		{
			int wordWidth = resMan.textFont.getStringWidth(word);
			if(charLeftMargin + wordWidth + UIConstants.TEXT_SPACE_WIDTH > resMan.textBackground.getWidth())
			{
				charLeftMargin = 0;
				charTopMargin += resMan.textFont.getLineHeight();
			}
			for (int i = 0; i < word.length(); ++i)
			{
				String ch = word.substring(i, i+1);
				final Text charSprite = new Text(0,0,resMan.textFont,ch);
				resMan.SetDefault(charSprite);

				charSprite.setPosition(charLeftMargin, charTopMargin);
				charLeftMargin += charSprite.getWidth() + 2;

				charSprites.add(charSprite);
				resMan.textBackground.attachChild(charSprite);
			}
			final Text spaceSprite = new Text(charLeftMargin,charTopMargin,resMan.textFont, "_");
			spaceSprite.setColor(0,0,0,0);
			charLeftMargin += UIConstants.TEXT_SPACE_WIDTH;
			charSprites.add(spaceSprite);
			resMan.textBackground.attachChild(spaceSprite);
		}
		resMan.textBackground.setHeight(charTopMargin + resMan.textFont.getLineHeight());

		Text first = charSprites.get(0);
		resMan.underline.detachSelf();
		resMan.textBackground.attachChild(resMan.underline);
		ActualUnderline(first);

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(!resMan.resultBackground.isVisible())
			keyboardHelper.GetInputManager().toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		return false;
	}

	private boolean isShiftPressed;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		ProcessInput(event);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		ProcessInput(event);
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}

	private void ProcessInput(KeyEvent event) {
		if(resMan.introBackground.isVisible() || resMan.resultBackground.isVisible())
			return;
		if(!isRunning)
			isRunning = true;

		if(event.getKeyCode() == KeyEvent.KEYCODE_SHIFT_LEFT)
		{
			isShiftPressed = true;
			return;
		}

		if(event.getKeyCode() == KeyEvent.KEYCODE_DEL)
		{
			if(currentPosition == 0)
				return;
			if(currentPosition != testingText.length()
				 && charSprites.get(currentPosition).getText().contentEquals("_"))
				statistic.CompleteWords--;

			currentPosition--;
			Text prevText = charSprites.get(currentPosition);
			if(resMan.GetWrong(prevText))
				statistic.FixedMistakes++;
			if(prevText.getText().contentEquals("_"))
				prevText.setColor(0,0,0,0);
			else
				resMan.SetDefault(prevText);

			ActualUnderline(prevText);

			if(editorTextOffset != 0 &&
				 prevText.getY() + editorTextOffset < UIConstants.CAMERA_HEIGHT/4 - resMan.textFont.getLineHeight()) {
				editorTextOffset += resMan.textFont.getLineHeight();
				resMan.textBackground.registerEntityModifier(
					 new MoveByModifier(UIConstants.TEXT_SCROLL_DURATION, 0, resMan.textFont.getLineHeight()));
			}
			return;
		}
		if(KeyEvent.isModifierKey(event.getKeyCode()) || event.getKeyCode() == -1)
			return;
		if(currentPosition == testingText.length())
			return;

		final Text charText = charSprites.get(currentPosition);
		final char typedCh = keyboardHelper.GetTypedChar(event);
		final char originalCh = testingText.charAt(currentPosition);

//		if(Character.toLowerCase(typedCh) == Character.toLowerCase(originalCh))
		if((isShiftPressed ? Character.toUpperCase(typedCh) : typedCh) == originalCh)
		{
			statistic.CorrectEntries++;
			resMan.SetRight(charText);
		}
		else
		{
			statistic.IncorrectEntries++;
			resMan.SetWrong(charText);
		}
		isShiftPressed = false;

		currentPosition++;
		if(currentPosition == testingText.length()) {
			statistic.CompleteWords++;
			LevelComplete(true);
			return;
		}
		ActualUnderline(charSprites.get(currentPosition));
		char nextCh = testingText.charAt(currentPosition);

		boolean wordThe = currentPosition > 2 &&
			 testingText.substring(currentPosition-3, currentPosition).contentEquals("the");
		if(nextCh == ' ' && !wordThe)
			statistic.CompleteWords++;
		if(nextCh == '\n')
			currentPosition++;

		if(charText.getY() + editorTextOffset > UIConstants.CAMERA_HEIGHT/4) {
			editorTextOffset -= resMan.textFont.getLineHeight();
			resMan.textBackground.registerEntityModifier(
				 new MoveByModifier(UIConstants.TEXT_SCROLL_DURATION, 0, -resMan.textFont.getLineHeight()));
		}
		return;
	}

	private void ActualUnderline(Text charText)
	{
		resMan.underline.setPosition(charText.getX(), charText.getY() + charText.getHeight() - 5);
		resMan.underline.setWidth(charText.getWidth() + 4);
	}

	private int lastUpdate;
	@Override
	public void onUpdate(final float pSecondsElapsed) {
		if(!isRunning)
			return;
		pastTime += pSecondsElapsed;
		final float currentTime = pastTime;

		if(lastUpdate == (int)currentTime)
			return;
		lastUpdate = (int)currentTime;
		runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				resMan.speedText.setText(String.format("%.1f", statistic.GetWPM(currentTime)));
				resMan.speedText.setPosition(
					 UIConstants.CAMERA_WIDTH - resMan.speedText.getWidth() - UIConstants.TESTING_BORDER_WIDTH,
					 UIConstants.TESTING_BORDER_WIDTH);

				if(Settings.PiranhaRun) {
					resMan.timeText.setText(String.format("%.0f", currentTime));
					return;
				}
				resMan.timeText.setText(String.format("%.0f/%d", currentTime, gameTime));

				if(currentTime > gameTime)
					LevelComplete(false);
			}
		});
	}

	@Override
	public void reset() {
//		pastTime = 0;
//		resMan.timeText.setText(String.format("Time: %f", pastTime));
	}


	public void LevelComplete(boolean beforeEnding) {
		isRunning = false;


			keyboardHelper.GetInputManager().hideSoftInputFromWindow(this.mRenderSurfaceView.getRootView().getWindowToken(), 0);
			int topMargin = UIConstants.TESTING_BORDER_WIDTH;
			statistic.FactTime = pastTime/60f;
			statistic.CalculateMetrics();

			String fileName = String.format("%s_%s_%s_#%s.txt",
				 resMan.userProfileTextEdit.getText(),
				 resMan.keyboardSpinner.getSelectedItem().toString(),
				 new SimpleDateFormat("yyyyMMdd-HHmmss").format(Calendar.getInstance().getTime()),
				 Settings.PiranhaRun ? "Piranhas" : String.valueOf(resMan.textIndex)
			);
			statistic.SaveToFile(fileName);

			resMan.resultBackground.detachChildren();

			PastResultLabel(topMargin, resMan.headerFont, String.format("%s, %s:",
				 resMan.userProfileTextEdit.getText(), resMan.textChooserSpinner.getSelectedItem().toString()));
			topMargin += resMan.headerFont.getLineHeight() + UIConstants.RESULT_LINE_INDENT;

			PastResultLabel(topMargin, resMan.headerFont, "WPM:");
			PastResultValue(topMargin, resMan.headerFont, String.format("%.1f", Math.max(statistic.Speed, 0)));
			topMargin += resMan.headerFont.getLineHeight() + UIConstants.RESULT_LINE_INDENT;

			PastResultLabel(topMargin, resMan.headerFont, "CPM:");
			PastResultValue(topMargin, resMan.headerFont, String.format("%.1f", Math.max(statistic.KeySpeed, 0)));
			topMargin += resMan.headerFont.getLineHeight() + UIConstants.RESULT_LINE_INDENT;

			PastResultLabel(topMargin, resMan.headerFont, "Accuracy:");
			PastResultValue(topMargin, resMan.headerFont, String.format("%.1f%%", statistic.Accuracy));
			topMargin += resMan.headerFont.getLineHeight() + UIConstants.RESULT_LINE_INDENT*2;

			PastResultLabel(topMargin, resMan.textFont, "Correct Entries:");
			PastResultValue(topMargin, resMan.textFont, String.format("%d", statistic.CorrectEntries));
			topMargin += resMan.textFont.getLineHeight() + UIConstants.RESULT_LINE_INDENT;

			PastResultLabel(topMargin, resMan.textFont, "Incorrect Entries:");
			PastResultValue(topMargin, resMan.textFont, String.format("%d", statistic.IncorrectEntries));
			topMargin += resMan.textFont.getLineHeight() + UIConstants.RESULT_LINE_INDENT;

			PastResultLabel(topMargin, resMan.textFont, "Fixed Mistakes:");
			PastResultValue(topMargin, resMan.textFont, String.format("%d", statistic.FixedMistakes));
			topMargin += resMan.textFont.getLineHeight() + UIConstants.RESULT_LINE_INDENT;

			PastResultLabel(topMargin, resMan.textFont, "Total Entries:");
			PastResultValue(topMargin, resMan.textFont, String.format("%d", statistic.TotalEntries));
			topMargin += resMan.textFont.getLineHeight() + UIConstants.RESULT_LINE_INDENT;

			PastResultLabel(topMargin, resMan.textFont, "Complete Words:");
			PastResultValue(topMargin, resMan.textFont, String.format("%d", statistic.CompleteWords));
			topMargin += resMan.textFont.getLineHeight() + UIConstants.RESULT_LINE_INDENT;

			PastResultLabel(topMargin, resMan.textFont, "Total Time:");
		if(!beforeEnding)
			PastResultValue(topMargin, resMan.textFont, String.format("%d min.", statistic.TotalTime));
		else
			PastResultValue(topMargin, resMan.textFont, String.format("%.2f sec.", pastTime));
			topMargin += resMan.textFont.getLineHeight() + UIConstants.RESULT_LINE_INDENT;

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					resMan.resultBackground.setVisible(true);
					resMan.outroControlsView.setVisibility(View.VISIBLE);
				}
			});

	}

	private void PastResultLabel(int topMargin, Font font, String label)	{
		Text text = new Text(UIConstants.TESTING_BORDER_WIDTH, topMargin, font, label);
		text.setColor(0,0,0);
		resMan.resultBackground.attachChild(text);
	}

	private void PastResultValue(int topMargin, Font font, String label)	{
		Text text = new Text(0,0, font, label);
		text.setColor(0, 0, 0);
		text.setPosition(UIConstants.CAMERA_WIDTH - text.getWidth() - UIConstants.TESTING_BORDER_WIDTH * 2, topMargin);
		resMan.resultBackground.attachChild(text);
	}

	private void Reset()
	{
		isRunning = false;
		pastTime = 0;
		currentPosition = 0;
		testingText = null;

		editorTextOffset = 0;

		for(Text t : charSprites) {
			t.reset();
			t.detachSelf();
		}
		resMan.textBackground.detachChildren();
		resMan.textBackground.setPosition(0, 0);

		charSprites.clear();
		statistic = new TestingStatistic();
	}

	private void TweetResult() {
		String nameApp = "twi";
		final List<Intent> targetedShareIntents = new ArrayList<Intent>();
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
		if (!resInfo.isEmpty()){
			for (ResolveInfo info : resInfo) {
				final ResolveInfo currentInfo = info;
				final Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
				targetedShare.setType("text/plain"); // put here your mime type

				if (info.activityInfo.packageName.toLowerCase().contains(nameApp) ||
					 info.activityInfo.name.toLowerCase().contains(nameApp)) {
							targetedShare.putExtra(Intent.EXTRA_TEXT, "My Octodon testing text result:");
							targetedShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(ScreenResult(null))) );
							targetedShare.setPackage(currentInfo.activityInfo.packageName);
							targetedShareIntents.add(targetedShare);
				}
			}

			Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
			startActivity(chooserIntent);
		}
	}



	private String ScreenResult(ScreenCapture.IScreenCaptureCallback callback)
	{
		String dirName = Environment.getExternalStorageDirectory().getPath()+ "/Octodon/Screenshots/";
		String fileName = dirName + String.format("%s_%s_%s_#%s.png",
			 resMan.userProfileTextEdit.getText(),
			 resMan.keyboardSpinner.getSelectedItem().toString(),
			 new SimpleDateFormat("yyyyMMdd-HHmmss").format(Calendar.getInstance().getTime()),
			 Settings.PiranhaRun ? "Piranhas" : String.valueOf(resMan.textIndex)
		);

		File dir = new File(dirName);
		if (!(dir.exists() && dir.isDirectory()))
			dir.mkdirs();

		final int viewWidth = mRenderSurfaceView.getWidth();
		final int viewHeight = mRenderSurfaceView.getHeight();

		final ScreenCapture.IScreenCaptureCallback screenCaputureCallback = callback != null
			 ? callback
			 : new ScreenCapture.IScreenCaptureCallback() {
				@Override
				public void onScreenCaptured(final String pFilePath) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(TestingLevelActivity.this, "Screenshot: " + pFilePath + " taken!", Toast.LENGTH_SHORT).show();
						}
					});
				}

				@Override
				public void onScreenCaptureFailed(final String pFilePath, final Exception pException) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(TestingLevelActivity.this, "FAILED capturing Screenshot: " + pFilePath + " !", Toast.LENGTH_SHORT).show();
						}
					});
				}
		};

		screenCapture.capture(viewWidth, viewHeight, fileName, screenCaputureCallback);
		return fileName;
	}
}