/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mythtv.service.nsd;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public abstract class MythNsdHelper {

	private static final String TAG = MythNsdHelper.class.getSimpleName();

	protected Context mContext;

	protected NsdManager mNsdManager;
	protected NsdManager.ResolveListener mResolveListener;
	protected NsdManager.DiscoveryListener mDiscoveryListener;
	protected NsdManager.RegistrationListener mRegistrationListener;

	protected NsdServiceInfo mService;

    public interface MythServiceDiscoveryListener {
    	
    	void onBackendDiscovered();
    	
    	void onFrontendDiscovered();

    }

    protected MythServiceDiscoveryListener listener;
    
	public MythNsdHelper( Context context, MythServiceDiscoveryListener listener ) {
		Log.v( TAG, "initialize : enter" );
		
		mContext = context;
		mNsdManager = (NsdManager) context.getSystemService( Context.NSD_SERVICE );
		this.listener = listener;

		Log.v( TAG, "initialize : exit" );
	}

	public void initializeNsd() {
		initializeResolveListener();
		initializeDiscoveryListener();
		initializeRegistrationListener();

	}

	public abstract void initializeDiscoveryListener();
	
	public abstract void initializeResolveListener();
	
	public abstract void initializeRegistrationListener();
	
	public abstract void discoverServices();

	public void stopDiscovery() {
		Log.i( TAG, "Stopping Discovery Service" );
		
		try {
			mNsdManager.stopServiceDiscovery( mDiscoveryListener );
		} catch( IllegalArgumentException e ) {
			Log.w( TAG, "Stopping Discovery Service error : " + e.getMessage() );
		}
	}

	public NsdServiceInfo getChosenServiceInfo() {
		return mService;
	}

	public void tearDown() {
		Log.i( TAG, "Tearing Down Discovery Service" );

		try {
			mNsdManager.unregisterService( mRegistrationListener );
		} catch( IllegalArgumentException e ) {
			Log.w( TAG, "Tearing Down Discovery Service error : " + e.getMessage() );
		}
	}

}
