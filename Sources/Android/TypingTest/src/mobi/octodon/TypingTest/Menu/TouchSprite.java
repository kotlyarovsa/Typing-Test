package mobi.octodon.TypingTest.Menu;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;

/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 06.01.14
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
public class TouchSprite extends Rectangle
{
	private Scene.IOnAreaTouchListener listener;

	public void setOnAreaTouchListener(Scene.IOnAreaTouchListener listener)
	{
		this.listener = listener;
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if(listener != null)
			listener.onAreaTouched(pSceneTouchEvent, this, pTouchAreaLocalX, pTouchAreaLocalY);
		return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);    //To change body of overridden methods use File | Settings | File Templates.
	}

	public TouchSprite(float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight);
	}
}
