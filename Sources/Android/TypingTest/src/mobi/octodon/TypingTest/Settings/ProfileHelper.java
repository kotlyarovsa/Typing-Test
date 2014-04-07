package mobi.octodon.TypingTest.Settings;

import android.os.Environment;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 27.10.13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public class ProfileHelper {
	private static ArrayList<String> profiles = new ArrayList<String>();

	public static void TryAddProfile(String profile)
	{
		if(!profiles.contains(profile))
			profiles.add(profile);
		SaveProfiles();
	}

	public static ArrayList<String> LoadProfiles()
	{
		profiles.clear();
		BufferedReader reader = null;
		try {
			File dir = new File(Environment.getExternalStorageDirectory().getPath()+ "/Octodon/");
			if (!(dir.exists() && dir.isDirectory()))
				dir.mkdirs();
			File file = new File(dir, "profiles.txt");
			if(!file.exists())
				return profiles;

			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				profiles.add(line);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			}
			catch (Exception e) {}
		}
		return profiles;
	}

	public static void SaveProfiles() {
		OutputStreamWriter writer = null;
		FileOutputStream fileOut = null;
		try {
			File dir = new File(Environment.getExternalStorageDirectory().getPath()+ "/Octodon/");
			if (!(dir.exists() && dir.isDirectory()))
				dir.mkdirs();
			File file = new File(dir, "profiles.txt");
			boolean success = file.createNewFile();

			fileOut = new FileOutputStream(file);
			writer = new OutputStreamWriter(fileOut);
			for(String profile : profiles)
				writer.write(profile+"\n");
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
