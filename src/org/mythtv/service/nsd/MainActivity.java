package org.mythtv.service.nsd;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements MythNsdHelper.MythServiceDiscoveryListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	
    private TextView mMasterBackendsView, mSecondaryBackendsView, mFrontendsView;
	
	private MythBackendNsdHelper mBackendNsdHelper;
	private MythFrontendNsdHelper mFrontendNsdHelper;

    private static Handler mBackendUpdateHandler;
    private static Handler mFrontendUpdateHandler;

    private MythConnection mBackendConnection;
    private MythConnection mFrontendConnection;
    
    private List<String> masterBackends = new ArrayList<String>();
    private List<String> secondaryBackends = new ArrayList<String>();
    private List<String> frontends = new ArrayList<String>();
    
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		Log.i( TAG, "onCreate : enter" );
		super.onCreate( savedInstanceState );
		
		setContentView( R.layout.activity_main );

        mMasterBackendsView = (TextView) findViewById( R.id.master_backends );
        mSecondaryBackendsView = (TextView) findViewById( R.id.secondary_backends );
        mFrontendsView = (TextView) findViewById( R.id.frontends );
		
        mBackendUpdateHandler = new Handler() {
            
        	/* (non-Javadoc)
        	 * @see android.os.Handler#handleMessage(android.os.Message)
        	 */
        	@Override
            public void handleMessage( Message msg ) {
        		String name = msg.getData().getString( "name" );
        		
        		if( !masterBackends.contains( name ) ) {
        			masterBackends.add( name );
        		}
        		
        		updateMasterBackends();
        	}
            
        };

        mFrontendUpdateHandler = new Handler() {
            
        	/* (non-Javadoc)
        	 * @see android.os.Handler#handleMessage(android.os.Message)
        	 */
        	@Override
            public void handleMessage( Message msg ) {
        		String name = msg.getData().getString( "name" );
        		
        		if( !frontends.contains( name ) ) {
        			frontends.add( name );
        		}
        		
        		updateFrontends();
        	}
            
        };

        mBackendConnection = new MythConnection( mBackendUpdateHandler );
        mFrontendConnection = new MythConnection( mFrontendUpdateHandler );
        
        mBackendNsdHelper = new MythBackendNsdHelper( this, this );
        mBackendNsdHelper.initializeNsd();

        mFrontendNsdHelper = new MythFrontendNsdHelper( this, this );
        mFrontendNsdHelper.initializeNsd();

        Log.i( TAG, "onCreate : exit" );
	}

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
		Log.d( TAG, "onPause : enter" );

    	if( null != mBackendNsdHelper ) {
    		mBackendNsdHelper.stopDiscovery();
        }
        
    	if( null != mFrontendNsdHelper ) {
    		mFrontendNsdHelper.stopDiscovery();
        }

    	super.onPause();

    	Log.d( TAG, "onPause : exit" );
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
		Log.d( TAG, "onResume : enter" );
        super.onResume();
        
//        if( null != mBackendNsdHelper ) {
//            mBackendNsdHelper.discoverServices();
//        }
//    
//        if( null != mFrontendNsdHelper ) {
//            mFrontendNsdHelper.discoverServices();
//        }

		Log.d( TAG, "onResume : exit" );
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
		Log.d( TAG, "onDestroy : enter" );

    	if( null != mBackendNsdHelper ) {
        	mBackendNsdHelper.tearDown();
        }
        
    	if( null != mFrontendNsdHelper ) {
        	mFrontendNsdHelper.tearDown();
        }
        
        super.onDestroy();

        Log.d( TAG, "onDestroy : exit" );
    }

    /* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		Log.d( TAG, "onCreateOptionsMenu : enter" );

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.main, menu );
		
		Log.d( TAG, "onCreateOptionsMenu : exit" );
		return true;
	}

    public void clickDiscover( View v ) {
		Log.v( TAG, "clickDiscover : enter" );

		mBackendNsdHelper.discoverServices();
        mFrontendNsdHelper.discoverServices();

        Log.v( TAG, "clickDiscover : exit" );
    }

    public void stopDiscover( View v ) {
		Log.v( TAG, "stopDiscover : enter" );

		mBackendNsdHelper.stopDiscovery();
        mFrontendNsdHelper.stopDiscovery();

        Log.v( TAG, "stopDiscover : exit" );
    }

    private void updateMasterBackends() {
		Log.v( TAG, "updateMasterBackends : enter" );

    	if( !masterBackends.isEmpty() ) {
    	
    		mMasterBackendsView.setText( "" );
    		
    		for( String backend : masterBackends ) {
    			mMasterBackendsView.append( backend + "\n" );
    		}
    		
    	}
    	
		Log.v( TAG, "updateMasterBackends : exit" );
    }
    
    private void updateSecondaryBackends() {
		Log.v( TAG, "updateSecondaryBackends : enter" );

    	if( !secondaryBackends.isEmpty() ) {
    	
    		mSecondaryBackendsView.setText( "" );
    		
    		for( String backend : secondaryBackends ) {
    			mSecondaryBackendsView.append( backend + "\n" );
    		}
    		
    	}
    	
		Log.v( TAG, "updateSecondaryBackends : exit" );
    }

    private void updateFrontends() {
		Log.v( TAG, "updateFrontends : enter" );

    	if( !frontends.isEmpty() ) {
    	
    		mFrontendsView.setText( "" );
    		
    		for( String frontend : frontends ) {
    			mFrontendsView.append( frontend + "\n" );
    		}
    		
    	}
    	
		Log.v( TAG, "updateFrontends : exit" );
    }

	/* (non-Javadoc)
	 * @see org.mythtv.service.nsd.MythNsdHelper.MythServiceDiscoveryListener#onBackendDiscovered(android.net.nsd.NsdServiceInfo)
	 */
	@Override
	public void onBackendDiscovered() {
		Log.d( TAG, "onBackendDiscovered : enter" );
		
		NsdServiceInfo service = mBackendNsdHelper.getChosenServiceInfo();
		mBackendConnection.updateMessages( service );

		Log.d( TAG, "onBackendDiscovered : exit" );
	}

	/* (non-Javadoc)
	 * @see org.mythtv.service.nsd.MythNsdHelper.MythServiceDiscoveryListener#onFrontendDiscovered(android.net.nsd.NsdServiceInfo)
	 */
	@Override
	public void onFrontendDiscovered() {
		Log.d( TAG, "onFrontendDiscovered : enter" );
		
		NsdServiceInfo service = mFrontendNsdHelper.getChosenServiceInfo();
		mFrontendConnection.updateMessages( service );

		Log.d( TAG, "onFrontendDiscovered : exit" );
	}

}
