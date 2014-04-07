package mobi.octodon.TypingTest.Testing;

import android.R;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import mobi.octodon.TypingTest.Common.KeyboardHelper;
import mobi.octodon.TypingTest.Common.TestingText;
import mobi.octodon.TypingTest.Settings.ProfileHelper;
import mobi.octodon.TypingTest.Common.UIConstants;
import mobi.octodon.TypingTest.Settings.Settings;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 27.10.13
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
public class TestingLevelResMan {
	private BaseGameActivity activity;
	private ArrayList<TestingText> TestingTexts = new ArrayList<TestingText>();

	private BitmapTextureAtlas headerFontTexture;
	private BitmapTextureAtlas textFontTexture;
	public Font headerFont;
	public Font textFont;

	public Rectangle introBackground;
	public Rectangle resultBackground;
	public Rectangle textBackground;
	public Rectangle underline;

	public ChangeableText timeText;
	public ChangeableText speedText;

	public void LoadResources()
	{
		textFontTexture = new BitmapTextureAtlas(512, 512, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
		headerFontTexture = new BitmapTextureAtlas(512, 512, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);

		textFont = new Font(textFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL),
			 UIConstants.TEXT_FONT_SIZE, true, Color.WHITE);
		headerFont = new Font(headerFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL),
			 UIConstants.HEADER_FONT_SIZE, true, Color.BLACK);

		activity.getTextureManager().loadTexture(textFontTexture);
		activity.getTextureManager().loadTexture(headerFontTexture);
		activity.getFontManager().loadFont(textFont);
		activity.getFontManager().loadFont(headerFont);

		File dir = new File(Environment.getExternalStorageDirectory().getPath()+ "/Octodon/Texts/");
		if (!(dir.exists() && dir.isDirectory()))
			dir.mkdirs();

		AssetManager am = activity.getAssets();
		if(dir.listFiles().length == 0)
		{
			try{
				InputStream inputStream = am.open("testing_texts.txt");

				File f = new File(Environment.getExternalStorageDirectory().getPath()+ "/Octodon/Texts/testing_texts.txt");
				f.createNewFile();
				OutputStream outputStream = new FileOutputStream(f);
				byte buffer[] = new byte[1024];
				int length = 0;

				while((length=inputStream.read(buffer)) > 0) {
					outputStream.write(buffer,0,length);
				}

				outputStream.close();
				inputStream.close();
			}catch (IOException e) {
				//Logging exception
			}
		}

			for(File f : dir.listFiles())
			{
				try {
					BufferedReader reader = new BufferedReader(new FileReader(f));
					StringBuilder currentText = null;
					String line = reader.readLine();
					while (line != null) {
						if(line.charAt(0)=='#')
						{
							currentText = new StringBuilder();
							TestingTexts.add(new TestingText(line.substring(1), "EN", currentText));
						}
						else
							currentText.append(line);
						line = reader.readLine();
					}
					reader.close();
				}
				catch(IOException ex)
				{		}
			}

		try {
			InputStream is = am.open("Piranhas.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder currentText = null;
			String line = reader.readLine();
			while (line != null) {
				if(line.charAt(0)=='#')
				{
					currentText = new StringBuilder();
					TestingTexts.add(new TestingText(line.substring(1), "EN", currentText));
				}
				else
					currentText.append(line);
				line = reader.readLine();
			}
			reader.close();
		}
		catch(IOException ex){}

	}

	public Scene LoadScene()
	{
		Scene scene = new Scene();

		Rectangle mainBackground = new Rectangle(0, 0, UIConstants.CAMERA_WIDTH, UIConstants.CAMERA_HEIGHT);
		scene.attachChild(mainBackground);

		Rectangle headerBackground = new Rectangle(0, 0, UIConstants.CAMERA_WIDTH,
			 headerFont.getLineHeight() + UIConstants.TESTING_BORDER_WIDTH*2);
		headerBackground.setColor(0.6f,0.6f,0.6f);
		timeText = new ChangeableText(UIConstants.TESTING_BORDER_WIDTH, UIConstants.TESTING_BORDER_WIDTH, headerFont, " ", 10);
		headerBackground.attachChild(timeText);
		speedText = new ChangeableText(UIConstants.TESTING_BORDER_WIDTH, UIConstants.TESTING_BORDER_WIDTH, headerFont, " ", 10);
		headerBackground.attachChild(speedText);

		Rectangle textBackgroundContainer = new Rectangle(
			UIConstants.TESTING_BORDER_WIDTH,
			UIConstants.TESTING_BORDER_WIDTH + headerBackground.getHeight(),
			UIConstants.CAMERA_WIDTH - UIConstants.TESTING_BORDER_WIDTH*2,
			UIConstants.CAMERA_HEIGHT - UIConstants.TESTING_BORDER_WIDTH*2 - headerBackground.getHeight()
		);

		textBackground = new Rectangle(0,0,
			UIConstants.CAMERA_WIDTH - UIConstants.TESTING_BORDER_WIDTH*2,
			UIConstants.CAMERA_HEIGHT - UIConstants.TESTING_BORDER_WIDTH*2 - headerBackground.getHeight()
		);

		underline = new Rectangle(0,0,0,7);
		underline.setColor(0.5f,0.5f,0.5f);
		textBackground.attachChild(underline);

		textBackgroundContainer.attachChild(textBackground);
		scene.attachChild(textBackgroundContainer);
		scene.attachChild(headerBackground);

		introBackground = new Rectangle(0, 0, UIConstants.CAMERA_WIDTH, UIConstants.CAMERA_HEIGHT);
		scene.attachChild(introBackground);
		Text introText = new Text(UIConstants.TESTING_BORDER_WIDTH, UIConstants.TESTING_BORDER_WIDTH, textFont,
			 KeyboardHelper.GetNormalizedText(
					textFont,
					"Hello!\nPlease, fill fields and press button \"Start\". The timer starts as soon as you begin " +
						 "typing!\nGood luck!",
					UIConstants.CAMERA_WIDTH - UIConstants.TESTING_BORDER_WIDTH * 2));
		SetDefault(introText);
		introBackground.attachChild(introText);


		userProfileIntroText = new Text(UIConstants.TESTING_BORDER_WIDTH,
			 introText.getY() + introText.getHeight() + UIConstants.TESTING_BORDER_WIDTH*2,
			 textFont, "Tester:");
		SetDefault(userProfileIntroText);
		introBackground.attachChild(userProfileIntroText);

		keyboardIntroText = new Text(UIConstants.TESTING_BORDER_WIDTH,
			 userProfileIntroText.getY() + userProfileIntroText.getHeight() + UIConstants.TESTING_BORDER_WIDTH*2,
			 textFont, "Keyboard:");
		SetDefault(keyboardIntroText);
		introBackground.attachChild(keyboardIntroText);

		textChooserIntroText = new Text(UIConstants.TESTING_BORDER_WIDTH,
			 keyboardIntroText.getY() + keyboardIntroText.getHeight() + UIConstants.TESTING_BORDER_WIDTH*2,
			 textFont, "Text:");
		SetDefault(textChooserIntroText);
		introBackground.attachChild(textChooserIntroText);

		timeIntroText = new Text(UIConstants.TESTING_BORDER_WIDTH,
			 textChooserIntroText.getY() + textChooserIntroText.getHeight() + UIConstants.TESTING_BORDER_WIDTH*2,
			 textFont, "Time:");
		SetDefault(timeIntroText);
		introBackground.attachChild(timeIntroText);

		languageIntroText = new Text(UIConstants.TESTING_BORDER_WIDTH,
			 timeIntroText.getY() + timeIntroText.getHeight() + UIConstants.TESTING_BORDER_WIDTH*2,
			 textFont, "Language:");
		SetDefault(languageIntroText);
		introBackground.attachChild(languageIntroText);

		resultBackground = new Rectangle(0, 0, UIConstants.CAMERA_WIDTH, UIConstants.CAMERA_HEIGHT);
//		introBackground.setColor(0.8f,1,0.8f);
		scene.attachChild(resultBackground);

		return scene;
	}

	public Text languageIntroText;
	public Text timeIntroText;
	public Text textChooserIntroText;
	public Text keyboardIntroText;
	public Text userProfileIntroText;
	public Spinner keyboardSpinner;
	public Spinner textChooserSpinner;
	public Spinner timeSpinner;
	public Spinner languageSpinner;
	public AutoCompleteTextView userProfileTextEdit;
	public Button startButton;
	public Button retryButton;
	public Button tweetButton;
	public Button screenButton;
	public FrameLayout controlsView;
	public FrameLayout outroControlsView;
	public void LoadControls(FrameLayout root)
	{
		controlsView = new FrameLayout(activity);
		ViewGroup.MarginLayoutParams frameLayoutParams;

		int controlOffset = 65 - (int) userProfileIntroText.getHeight();

		userProfileTextEdit = new AutoCompleteTextView(activity);
		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.TOP | Gravity.RIGHT);
		frameLayoutParams.rightMargin = UIConstants.TESTING_BORDER_WIDTH;
		frameLayoutParams.topMargin = (int) userProfileIntroText.getY() - controlOffset;
		userProfileTextEdit.setLayoutParams(frameLayoutParams);
		controlsView.addView(userProfileTextEdit);

		ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_dropdown_item_1line, ProfileHelper.LoadProfiles());
		userProfileTextEdit.setAdapter(adapter);
		userProfileTextEdit.setThreshold(0);
		userProfileTextEdit.setText(Settings.Profile);

		keyboardSpinner = new Spinner(activity);
		keyboardSpinner.setPrompt("Choose your keyboard:");
		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.TOP | Gravity.RIGHT);
		frameLayoutParams.rightMargin = UIConstants.TESTING_BORDER_WIDTH;
		frameLayoutParams.topMargin = (int)keyboardIntroText.getY() - controlOffset ;
		keyboardSpinner.setLayoutParams(frameLayoutParams);
		controlsView.addView(keyboardSpinner);

		final String[] keyboards = new String[]{"Octodon", "TouchPal", "Qwerty", "Dvorak"};
		adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, keyboards);
		keyboardSpinner.setAdapter(adapter);
		for(int i = 0; i < keyboards.length; i++)
			if(keyboards[i].contentEquals(Settings.Keyboard)) {
				keyboardSpinner.setSelection(i);
				break;
			}

		textChooserSpinner = new Spinner(activity);
		textChooserSpinner.setPrompt("Choose testing text:");
		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.TOP | Gravity.RIGHT);
		frameLayoutParams.rightMargin = UIConstants.TESTING_BORDER_WIDTH;
		frameLayoutParams.topMargin = (int)textChooserIntroText.getY() - controlOffset ;
		textChooserSpinner.setLayoutParams(frameLayoutParams);
		controlsView.addView(textChooserSpinner);

		ArrayList<String> texts = new ArrayList<String>();
		texts.add("Random");
		for(int i = 0; i < TestingTexts.size(); i++)
			texts.add(TestingTexts.get(i).Name);
		adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, texts);
		textChooserSpinner.setAdapter(adapter);
		int index = Settings.PiranhaRun ? texts.indexOf("Piranhas") : texts.indexOf(Settings.TestingText);
		if(index >= 0)
			textChooserSpinner.setSelection(index);


		timeSpinner = new Spinner(activity);
		timeSpinner.setPrompt("Choose time of testing:");
		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.TOP | Gravity.RIGHT);
		frameLayoutParams.rightMargin = UIConstants.TESTING_BORDER_WIDTH;
		frameLayoutParams.topMargin = (int)timeIntroText.getY() - controlOffset;
		timeSpinner.setLayoutParams(frameLayoutParams);
		controlsView.addView(timeSpinner);

		final String[] times = new String[]{"1 min.", "2 min.", "3 min.", "4 min.", "5 min."};
		adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, times);
		timeSpinner.setAdapter(adapter);
		timeSpinner.setSelection(2);
		for(int i = 0; i < times.length; i++)
			if(Integer.parseInt(times[i].substring(0,1)) == Settings.LevelTime) {
				timeSpinner.setSelection(i);
				break;
			}

		languageSpinner = new Spinner(activity);
		languageSpinner.setPrompt("Choose your language:");
		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.TOP | Gravity.RIGHT);
		frameLayoutParams.rightMargin = UIConstants.TESTING_BORDER_WIDTH;
		frameLayoutParams.topMargin = (int)languageIntroText.getY() - controlOffset;
		languageSpinner.setLayoutParams(frameLayoutParams);
		controlsView.addView(languageSpinner);

		final String[] langs = new String[]{"English", "Русский"};
		adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, langs);
		languageSpinner.setAdapter(adapter);
		for(int i = 0; i < langs.length; i++)
			if(langs[i].contentEquals(Settings.Language)) {
				languageSpinner.setSelection(i);
				break;
			}

		startButton = new Button(activity);
		startButton.setText("Start");
		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		frameLayoutParams.bottomMargin = UIConstants.TESTING_BORDER_WIDTH;
		startButton.setLayoutParams(frameLayoutParams);
		controlsView.addView(startButton);

		controlsView.setVisibility(View.GONE);


		outroControlsView = new FrameLayout(activity);

		retryButton = new Button(activity);
		retryButton.setText("Try again!");
		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		frameLayoutParams.bottomMargin = UIConstants.TESTING_BORDER_WIDTH;
		retryButton.setLayoutParams(frameLayoutParams);
		outroControlsView.addView(retryButton);

		tweetButton = new Button(activity);
		tweetButton.setText("Tweet");
		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		frameLayoutParams.bottomMargin = 65 + UIConstants.TESTING_BORDER_WIDTH;
		tweetButton.setLayoutParams(frameLayoutParams);
		outroControlsView.addView(tweetButton);

		screenButton = new Button(activity);
		screenButton.setText("Screenshot");
		frameLayoutParams = new FrameLayout.LayoutParams(250, 65, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		frameLayoutParams.bottomMargin = 130 + UIConstants.TESTING_BORDER_WIDTH;
		screenButton.setLayoutParams(frameLayoutParams);
		outroControlsView.addView(screenButton);

		outroControlsView.setVisibility(View.GONE);

		FixControlLayouts(root);
		root.addView(controlsView);
		root.addView(outroControlsView);
	}

	public void FixControlLayouts(View root)
	{
		Rect r = new Rect();
		root.getWindowVisibleDisplayFrame(r);

		double coefW = r.width()/480d;
		double offsetH = (r.height() - 800*coefW)/2; //r.height()/800d;

		FixLayout(coefW, offsetH, (FrameLayout.LayoutParams) userProfileTextEdit.getLayoutParams());
		FixLayout(coefW, offsetH, (FrameLayout.LayoutParams) keyboardSpinner.getLayoutParams());
		FixLayout(coefW, offsetH, (FrameLayout.LayoutParams) textChooserSpinner.getLayoutParams());
		FixLayout(coefW, offsetH, (FrameLayout.LayoutParams) timeSpinner.getLayoutParams());
		FixLayout(coefW, offsetH, (FrameLayout.LayoutParams) languageSpinner.getLayoutParams());
		FixLayout(coefW, offsetH, (FrameLayout.LayoutParams) startButton.getLayoutParams());
		FixLayout(coefW, offsetH, (FrameLayout.LayoutParams) retryButton.getLayoutParams());
		FixLayout(coefW, offsetH, (FrameLayout.LayoutParams) tweetButton.getLayoutParams());
		FixLayout(coefW, offsetH, (FrameLayout.LayoutParams) screenButton.getLayoutParams());
	}

	private void FixLayout(double coefW, double offsetH, FrameLayout.LayoutParams layoutParams)
	{
		layoutParams.width = (int)(layoutParams.width*coefW);
		layoutParams.height = (int)(layoutParams.height*coefW);
		layoutParams.leftMargin = (int)(layoutParams.leftMargin*coefW);
		layoutParams.topMargin = (int)(layoutParams.topMargin*coefW) + (int)offsetH;
		layoutParams.rightMargin = (int)(layoutParams.rightMargin*coefW);
		layoutParams.bottomMargin = (int)(layoutParams.bottomMargin*coefW) + (int)offsetH;
	}

	public int textIndex;
	public String GetTestText()
	{
		Random rand = new Random();
		int textItemPosition = textChooserSpinner.getSelectedItemPosition();
		String textForSelect = textChooserSpinner.getSelectedItem().toString();

		if(textItemPosition == 0)
			textIndex = rand.nextInt(TestingTexts.size()-1); //-1 because last text is piranhas
		else if(textItemPosition == TestingTexts.size()) //mean last in spinner array
			textIndex = TestingTexts.size() - 1;
		else
			textIndex = textChooserSpinner.getSelectedItemPosition() - 1;

//		if(languageSpinner.getSelectedItemPosition() == 0)
			return TestingTexts.get(textIndex).Text.toString();
//		return TestingTextsRU.get(textIndex).toString();

//			return "Lorem ipsum dolor sit amet!";
//
//        return "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt" +
//                " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
//                "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in" +
//                " voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat" +
//                " non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
//		return "Hello, i'm a man who seems the eaglehorn";
	}

	public void SetDefault(Text text)
	{
		text.setColor(0,0,0,1);
//		text.setColor(204f/255f,204f/255f, 51f/255f);
	}

	public void SetRight(Text text)
	{
		text.setColor(0, 1, 50f / 255f, 1);
	}

	public void SetWrong(Text text)
	{
		text.setColor(1f, 80f/255f, 0, 1);
	}

	public boolean GetWrong(Text text)
	{
		return text.getRed() == 1 && text.getGreen() == 80f/255f && text.getBlue() == 0;
	}

	public TestingLevelResMan(BaseGameActivity activity)
	{
		this.activity = activity;
	}

}
