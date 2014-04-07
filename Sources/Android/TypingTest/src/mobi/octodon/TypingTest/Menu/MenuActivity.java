package mobi.octodon.TypingTest.Menu;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import mobi.octodon.TypingTest.Common.GoogleApiHelper;
import mobi.octodon.TypingTest.Common.UIConstants;
import mobi.octodon.TypingTest.R;
import mobi.octodon.TypingTest.Settings.Settings;
import mobi.octodon.TypingTest.Testing.TestingLevelActivity;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.ui.activity.BaseGameActivity;

/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 19.09.13
 * Time: 0:13
 * To change this template use File | Settings | File Templates.
 */
public class MenuActivity extends BaseGameActivity
	 implements Scene.IOnSceneTouchListener {
	private Camera mCamera;
	private MenuResMan resMan;

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
		resMan = new MenuResMan(this);
		resMan.LoadResources();
	}

	@Override
	public Scene onLoadScene() {
		mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = resMan.LoadScene();
		scene.setOnSceneTouchListener(this);

		resMan.bigTextButton.setOnAreaTouchListener(new Scene.IOnAreaTouchListener() {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, Scene.ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Settings.PiranhaRun = false;
				startActivity(new Intent(getApplicationContext(), TestingLevelActivity.class));
				return false;  //To change body of implemented methods use File | Settings | File Templates.
			}
		});
		resMan.piranhasButton.setOnAreaTouchListener(new Scene.IOnAreaTouchListener() {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, Scene.ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Settings.PiranhaRun = true;
				startActivity(new Intent(getApplicationContext(), TestingLevelActivity.class));
				return false;  //To change body of implemented methods use File | Settings | File Templates.
			}
		});
		resMan.recordsButton.setOnAreaTouchListener(new Scene.IOnAreaTouchListener() {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, Scene.ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
//				Intent intent = Games.Leaderboards.getLeaderboardIntent(googleApiClient,
//					 getResources().getString(R.string.leaderboard_words_per_minute_rating));
//				startActivity(intent);
//				scene.setChildSceneModal(resMan.recordsScene);
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MenuActivity.this);
				dlgAlert.setMessage("Coming soon in next release");
				dlgAlert.setTitle("Records");
				dlgAlert.setPositiveButton("OK", null);
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();
				return false;  //To change body of implemented methods use File | Settings | File Templates.
			}
		});
		resMan.aboutButton.setOnAreaTouchListener(new Scene.IOnAreaTouchListener() {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, Scene.ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				scene.setChildSceneModal(resMan.aboutScene);
				return false;  //To change body of implemented methods use File | Settings | File Templates.
			}
		});
		resMan.donateButton.setOnAreaTouchListener(new Scene.IOnAreaTouchListener() {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, Scene.ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MenuActivity.this);
				dlgAlert.setMessage("Yandex.Money: 410012245231838");
				dlgAlert.setTitle("Donate");
				dlgAlert.setPositiveButton("OK", null);
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();
//				scene.setChildSceneModal(resMan.donateScene);
				return false;  //To change body of implemented methods use File | Settings | File Templates.
			}
		});

//		String lang = getApplicationContext().getResources().getConfiguration().locale.getLanguage();
//		testingModel.ChangeLang(lang);

		return scene;
	}

	@Override
	public void onLoadComplete() {
	}

	private GoogleApiClient googleApiClient;

	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
//		googleApiClient = GoogleApiHelper.Get(this);
//		googleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
//		googleApiClient.disconnect();
	}

	@Override
	public void onBackPressed()
	{
		Scene scene = this.mEngine.getScene();
		if(scene.hasChildScene()){
			scene.back();
		}
		else{
			this.finish();
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}
}