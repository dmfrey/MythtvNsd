package org.mythtv.service.nsd;

import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MythConnection {

	private static final String TAG = MythConnection.class.getSimpleName();
	
	private Handler mUpdateHandler;
	
	public MythConnection( Handler handler ) {
		mUpdateHandler = handler;
	}
	
    public synchronized void updateMessages( NsdServiceInfo serviceInfo ) {
        Log.v( TAG, "Updating message: " + serviceInfo );

        Bundle messageBundle = new Bundle();
        messageBundle.putString( "name", clean( serviceInfo.getServiceName() ) );
        messageBundle.putString( "host", serviceInfo.getHost().getHostAddress() );
        messageBundle.putInt( "port", serviceInfo.getPort() );
        messageBundle.putString( "type", serviceInfo.getServiceType() );

        Message message = new Message();
        message.setData( messageBundle );
        mUpdateHandler.sendMessage( message );

    }

    private String clean( String value ) {
    	Log.v( TAG, "clean : enter" );
    	
    	if( value.indexOf( "032" ) != -1 ) {
    		value = value.replaceAll( "\\\\032", " " );
    	}

    	Log.d( TAG, "clean : value=" + value );
    	
    	Log.v( TAG, "clean : exit" );
    	return value;
    }
    
}
