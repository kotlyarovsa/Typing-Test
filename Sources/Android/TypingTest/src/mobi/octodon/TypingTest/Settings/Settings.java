package mobi.octodon.TypingTest.Settings;

import android.os.Environment;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 27.10.13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public class Settings {
	public static boolean PiranhaRun = false;

	//text settings
	public static String Profile ="";
	public static String Keyboard = "";
	public static String TestingText = "";
	public static int LevelTime = -1;
	public static String Language = "";

	//sequence
	public static int SeqLength = -1;
	public static int SeqRepeat = -1;


	public static boolean LoadSettings()
	{
		BufferedReader reader = null;
		try {
			File dir = new File(Environment.getExternalStorageDirectory().getPath()+ "/Octodon/");
			if (!(dir.exists() && dir.isDirectory()))
				dir.mkdirs();
			File file = new File(dir, "settings.txt");
			if(!file.exists())
				return false;

			reader = new BufferedReader(new FileReader(file));
			Profile = reader.readLine();
			Keyboard = reader.readLine();
			TestingText = reader.readLine();
			LevelTime = Integer.parseInt(reader.readLine());
			Language = reader.readLine();
			SeqLength = Integer.parseInt(reader.readLine());
			SeqRepeat = Integer.parseInt(reader.readLine());
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				reader.close();
			}
			catch (Exception e) {}
		}
		return true;
	}

	public static void SaveSettings() {
		OutputStreamWriter writer = null;
		FileOutputStream fileOut = null;
		try {
			File dir = new File(Environment.getExternalStorageDirectory().getPath()+ "/Octodon/");
			if (!(dir.exists() && dir.isDirectory()))
				dir.mkdirs();
			File file = new File(dir, "settings.txt");
			boolean success = file.createNewFile();

			fileOut = new FileOutputStream(file);
			writer = new OutputStreamWriter(fileOut);

			writer.write(Profile + "\n");
			writer.write(Keyboard + "\n");
			writer.write(TestingText + "\n");
			writer.write(String.format("%d\n", LevelTime));
			writer.write(Language + "\n");
			writer.write(String.format("%d\n", SeqLength));
			writer.write(String.format("%d\n", SeqRepeat));
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				fileOut.close();
			} catch (Exception e) {
			}
		}
	}
}
