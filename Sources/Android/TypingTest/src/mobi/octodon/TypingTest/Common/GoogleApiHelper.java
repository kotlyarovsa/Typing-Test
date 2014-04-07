package mobi.octodon.TypingTest.Common;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * Created with IntelliJ IDEA.
 * User: kotlyarovsa
 * Date: 02.04.14
 * Time: 0:16
 * To change this template use File | Settings | File Templates.
 */
public class GoogleApiHelper {

	public static GoogleApiClient Get(Activity activity)
	{
		return new GoogleApiClient.Builder(activity)
			 .addApi(Games.API).addScope(Games.SCOPE_GAMES)
			 .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
				 @Override
				 public void onConnected(Bundle bundle) {
					 //To change body of implemented methods use File | Settings | File Templates.
				 }

				 @Override
				 public void onConnectionSuspended(int i) {
					 //To change body of implemented methods use File | Settings | File Templates.
				 }
			 })
			 .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
				 @Override
				 public void onConnectionFailed(ConnectionResult connectionResult) {
					 //To change body of implemented methods use File | Settings | File Templates.
				 }
			 })
			 .build();
	}

}
