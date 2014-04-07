package mobi.octodon.TypingTest.Common;

/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 17.07.13
 * Time: 1:06
 * To change this template use File | Settings | File Templates.
 */
public class IntContainer {
    public int Value;

    public IntContainer(int value)
    {
        Value = value;
    }

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof IntContainer)) return false;

		IntContainer that = (IntContainer) o;
		return Value == that.Value;
	}

	@Override
	public int hashCode() {
		return Value;
	}
}
