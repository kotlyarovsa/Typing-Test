package mobi.octodon.TypingTest.Common;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 20.01.14
 * Time: 2:25
 * To change this template use File | Settings | File Templates.
 */
public class CharStatistic {
	public char ActualChar;
	public char ExpectedChar;
	public Date TimeStamp;

	public CharStatistic(char actualChar, char expectedChar, Date timeStamp) {
		ActualChar = actualChar;
		ExpectedChar = expectedChar;
		TimeStamp = timeStamp;
	}
}
