package mobi.octodon.TypingTest.Testing;

import android.os.Environment;
import mobi.octodon.TypingTest.Settings.Settings;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 27.10.13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public class TestingStatistic {
	public float Speed;
	public float Accuracy;

	public int CorrectEntries;
	public int IncorrectEntries;
	public int FixedMistakes;
	public int TotalEntries;
	public float ErrorRate;
	public float RawSpeed;
	public float KeySpeed;
	public int CompleteWords;
	public int TotalTime;
	public float FactTime;

	public int WordsCount;

	public float GetWPM(float time)
	{
		return 12*(CorrectEntries + FixedMistakes)/time;
	}

	public void CalculateMetrics() {
		TotalEntries = CorrectEntries + IncorrectEntries;
		int wrongEntries = IncorrectEntries - FixedMistakes;
		int rightEntries = TotalEntries - wrongEntries;

		ErrorRate = wrongEntries / FactTime;
		RawSpeed = (TotalEntries/FactTime)/5f;
		KeySpeed = rightEntries/FactTime;
		Speed = KeySpeed/5f;
		Accuracy = TotalEntries == 0 ? 0 : ((float)CorrectEntries / (float)TotalEntries)*100;
	}

	public void SaveToFile(String fileName) {
		OutputStreamWriter writer = null;
		FileOutputStream fileOut = null;
		try {

			File dir = new File(Environment.getExternalStorageDirectory().getPath()+ "/Octodon/Keyboard Testing Logs/");
			if (!(dir.exists() && dir.isDirectory()))
				dir.mkdirs();
			File file = new File(dir, fileName);
			boolean success = file.createNewFile();

			fileOut = new FileOutputStream(file);
			writer = new OutputStreamWriter(fileOut);
			writer.write(String.format("Speed: %.1f\n", Speed));
			writer.write(String.format("Accuracy: %.1f%%\n\n", Accuracy));
			writer.write(String.format("CorrectEntries: %d\n", CorrectEntries));
			writer.write(String.format("IncorrectEntries: %d\n", IncorrectEntries));
			writer.write(String.format("FixedMistakes: %d\n", FixedMistakes));
			writer.write(String.format("TotalEntries: %d\n", TotalEntries));
			writer.write(String.format("ErrorRate: %.1f\n", ErrorRate));
			writer.write(String.format("RawSpeed: %\n", RawSpeed));
			writer.write(String.format("KeySpeed: %.1f\n", KeySpeed));
			writer.write(String.format("CompleteWords: %d\n", CompleteWords));
			if(!Settings.PiranhaRun)
				writer.write(String.format("TotalTime: %d min.", TotalTime));
			else
				writer.write(String.format("TotalTime: %.2f min.\n", FactTime));
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
