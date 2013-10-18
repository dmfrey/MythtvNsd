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

public class MythFrontendNsdHelper extends MythNsdHelper {

	private static final String TAG = MythFrontendNsdHelper.class.getSimpleName();

	private static final String SERVICE_NAME_PREFIX = "Mythfrontend on ";

	public static final String SERVICE_TYPE = "_mythfrontend._tcp.";

	private String mServiceName = "";
	
	public MythFrontendNsdHelper( Context context, MythServiceDiscoveryListener listener ) {
		super( context, listener );
	}

	public void initializeDiscoveryListener() {

		mDiscoveryListener = new NsdManager.DiscoveryListener() {

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.DiscoveryListener#onDiscoveryStarted(java.lang.String)
			 */
			@Override
			public void onDiscoveryStarted( String regType ) {
				Log.d( TAG, "Service discovery started" );
			}

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.DiscoveryListener#onServiceFound(android.net.nsd.NsdServiceInfo)
			 */
			@Override
			public void onServiceFound( NsdServiceInfo service ) {
				Log.d( TAG, "Service discovery success" + service );
				
				if( !service.getServiceType().equals( SERVICE_TYPE ) ) {
					Log.d( TAG, "Unknown Service Type: " + service.getServiceType() );
				} else if( service.getServiceName().startsWith( SERVICE_NAME_PREFIX ) ) {
					mNsdManager.resolveService( service, mResolveListener );
				}
			
			}

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.DiscoveryListener#onServiceLost(android.net.nsd.NsdServiceInfo)
			 */
			@Override
			public void onServiceLost( NsdServiceInfo service ) {
				Log.e( TAG, "service lost" + service );
				
				if( mService == service ) {
					mService = null;
				}
				
			}

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.DiscoveryListener#onDiscoveryStopped(java.lang.String)
			 */
			@Override
			public void onDiscoveryStopped( String serviceType ) {
				Log.i( TAG, "Discovery stopped: " + serviceType );
			}

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.DiscoveryListener#onStartDiscoveryFailed(java.lang.String, int)
			 */
			@Override
			public void onStartDiscoveryFailed( String serviceType, int errorCode ) {
				Log.e( TAG, "Discovery failed: Error code:" + errorCode );
				
				mNsdManager.stopServiceDiscovery( this );
			}

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.DiscoveryListener#onStopDiscoveryFailed(java.lang.String, int)
			 */
			@Override
			public void onStopDiscoveryFailed( String serviceType, int errorCode ) {
				Log.e( TAG, "Discovery failed: Error code:" + errorCode );
				
				mNsdManager.stopServiceDiscovery( this );
			}
			
		};
		
	}

	public void initializeResolveListener() {
		
		mResolveListener = new NsdManager.ResolveListener() {

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.ResolveListener#onResolveFailed(android.net.nsd.NsdServiceInfo, int)
			 */
			@Override
			public void onResolveFailed( NsdServiceInfo serviceInfo, int errorCode ) {
				Log.e( TAG, "Resolve failed" + errorCode );
			}

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.ResolveListener#onServiceResolved(android.net.nsd.NsdServiceInfo)
			 */
			@Override
			public void onServiceResolved( NsdServiceInfo serviceInfo ) {
				Log.e( TAG, "Resolve Succeeded. " + serviceInfo );
				
				mService = serviceInfo;
				
				listener.onFrontendDiscovered();
			}
			
		};
		
	}

	public void initializeRegistrationListener() {
	
		mRegistrationListener = new NsdManager.RegistrationListener() {

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.RegistrationListener#onServiceRegistered(android.net.nsd.NsdServiceInfo)
			 */
			@Override
			public void onServiceRegistered( NsdServiceInfo NsdServiceInfo ) {
				mServiceName = NsdServiceInfo.getServiceName();
			}

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.RegistrationListener#onRegistrationFailed(android.net.nsd.NsdServiceInfo, int)
			 */
			@Override
			public void onRegistrationFailed( NsdServiceInfo arg0, int arg1 ) {
			}

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.RegistrationListener#onServiceUnregistered(android.net.nsd.NsdServiceInfo)
			 */
			@Override
			public void onServiceUnregistered( NsdServiceInfo arg0 ) {
			}

			/* (non-Javadoc)
			 * @see android.net.nsd.NsdManager.RegistrationListener#onUnregistrationFailed(android.net.nsd.NsdServiceInfo, int)
			 */
			@Override
			public void onUnregistrationFailed( NsdServiceInfo serviceInfo, int errorCode ) {
			}

		};
	
	}

	public void discoverServices() {
		mNsdManager.discoverServices( SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener );
	}

}
