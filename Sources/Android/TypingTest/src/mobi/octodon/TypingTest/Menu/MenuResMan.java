package mobi.octodon.TypingTest.Menu;

import android.graphics.Color;
import android.graphics.Typeface;
import mobi.octodon.TypingTest.Common.UIConstants;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 27.10.13
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
public class MenuResMan {
	private BaseGameActivity activity;

	private BitmapTextureAtlas textFontTexture;
	public Font textFont;

	private BitmapTextureAtlas backgroundTexture;

	public TextureRegion mainBackgroundRegion;
	public TextureRegion aboutBackgroundRegion;
	public TextureRegion buttonRegion;

	public Scene aboutScene;
	public Scene donateScene;
	public Scene recordsScene;

	public Sprite mainBackground;
	public Sprite aboutBackground;

	public TouchSprite bigTextButton;
	public TouchSprite piranhasButton;
	public TouchSprite recordsButton;
	public TouchSprite aboutButton;
	public TouchSprite donateButton;

	public void LoadResources()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("Menu/");
		backgroundTexture = new BitmapTextureAtlas(2048, 1024, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
		textFontTexture = new BitmapTextureAtlas(512, 512, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);

		textFont = new Font(textFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL),
			 UIConstants.TEXT_FONT_SIZE, true, Color.BLACK);

		activity.getTextureManager().loadTexture(backgroundTexture);
		activity.getTextureManager().loadTexture(textFontTexture);
		activity.getFontManager().loadFont(textFont);

		mainBackgroundRegion = BitmapTextureAtlasTextureRegionFactory
			 .createFromAsset(backgroundTexture, activity, "mymenu.png", 0, 0); //480x800
//			 .createFromAsset(backgroundTexture, activity, "mainmenu.png", 0, 0); //480x800
//			 .createFromAsset(backgroundTexture, activity, "apple.jpg", 0, 0); //480x800
		aboutBackgroundRegion = BitmapTextureAtlasTextureRegionFactory
			 .createFromAsset(backgroundTexture, activity, "about.png", 480, 0);
		buttonRegion = BitmapTextureAtlasTextureRegionFactory
			 .createFromAsset(backgroundTexture, activity, "menubutton.png", 0, 800); //221x80
	}

	public Scene LoadScene()
	{
		Scene scene = new Scene();
		mainBackground = new Sprite(0,0,mainBackgroundRegion);
		scene.attachChild(mainBackground);

		aboutScene = new Scene();
		aboutBackground = new Sprite(0,0,aboutBackgroundRegion);
		aboutScene.attachChild(aboutBackground);

		aboutButton = CreateTouchButton(scene, mainBackground, 220, 210, 230, 35);
		bigTextButton = CreateTouchButton(scene, mainBackground, 280, 500, 180, 60);
		piranhasButton = CreateTouchButton(scene, mainBackground, 280, 570, 180, 60);
		recordsButton = CreateTouchButton(scene, mainBackground, 290, 640, 170, 60);
		donateButton = CreateTouchButton(scene, mainBackground, 350, 750, 130, 50);

		return scene;
	}

	private TouchSprite CreateTouchButton(Scene scene, Entity parent, int x, int y, int width, int height)
	{
		TouchSprite sprite = new TouchSprite(x,y, width,height);
		sprite.setColor(1f, 0.4f, 0.7f);
		sprite.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		sprite.setAlpha(0.0f);

		parent.attachChild(sprite);
		scene.registerTouchArea(sprite);
		return sprite;
	}

//	public Button startButton;
//	public Button retryButton;
//	public Button tweetButton;
//	public FrameLayout controlsView;
//	public FrameLayout outroControlsView;
//	public void LoadControls(FrameLayout root)
//	{
//		controlsView = new FrameLayout(activity);
//		ViewGroup.MarginLayoutParams frameLayoutParams;
//
//
//		startButton = new Button(activity);
//		startButton.setText("Start");
//		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//		frameLayoutParams.bottomMargin = UIConstants.TESTING_BORDER_WIDTH;
//		startButton.setLayoutParams(frameLayoutParams);
//		controlsView.addView(startButton);
//
//		controlsView.setVisibility(View.GONE);
//
//
//		outroControlsView = new FrameLayout(activity);
//
//		retryButton = new Button(activity);
//		retryButton.setText("Try again!");
//		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//		frameLayoutParams.bottomMargin = UIConstants.TESTING_BORDER_WIDTH;
//		retryButton.setLayoutParams(frameLayoutParams);
//		outroControlsView.addView(retryButton);
//
//		tweetButton = new Button(activity);
//		tweetButton.setText("Tweet");
//		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
//		frameLayoutParams.bottomMargin = 65 + UIConstants.TESTING_BORDER_WIDTH*2;
//		tweetButton.setLayoutParams(frameLayoutParams);
//		outroControlsView.addView(tweetButton);
//
//		outroControlsView.setVisibility(View.GONE);
//
//		FixControlLayouts(root);
//		root.addView(controlsView);
//		root.addView(outroControlsView);
//	}

	public MenuResMan(BaseGameActivity activity)
	{
		this.activity = activity;
	}

}
