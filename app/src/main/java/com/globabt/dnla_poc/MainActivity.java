package com.globabt.dnla_poc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.android.FixedAndroidLogHandler;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.transport.Router;
import org.fourthline.cling.transport.RouterException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity{

    private final BrowseRegistryListener registryListener = new BrowseRegistryListener(this);
    private RecyclerView devicesRecycler;
    private RecyclerView directoriesRecycler;
    private DevicesRVAdapter RVAdapterDevices;
    private DirectoriesRVAdapter RVAdapterDirectories;

    private Context mContext = this;
    //Layouts
    private SwipeRefreshLayout mContentLayout;
    private LinearLayout mBrowserLayout;
    private LinearLayout mControlLayout;
    //Constants
    private final int BROWSER = 0;
    private final int MULTIMEDIA_CONTROLLER = 1;
    //Browser usage
    private int parentID = -1;
    private int actualID = 0;

    private AndroidUpnpService upnpService;
    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            upnpService = (AndroidUpnpService) service;

            // Clear the list
            RVAdapterDevices.clear();

            // Get ready for future device advertisements
            upnpService.getRegistry().addListener(registryListener);

            // Now add all devices to the list we already know about
            for (Device device : upnpService.getRegistry().getDevices()) {
                registryListener.addDeviceInList(device);
            }

            // Search asynchronously for all devices, they will respond soon
            upnpService.getControlPoint().search();
        }

        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        //Layouts
        mContentLayout = (SwipeRefreshLayout) findViewById(R.id.content_layout);
        mBrowserLayout = (LinearLayout) findViewById(R.id.content_browser_layout);
        mControlLayout = (LinearLayout) findViewById(R.id.multimedia_control_layout);
        mContentLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mContentLayout.setRefreshing(false);
            }
        });

        //recycler
        devicesRecycler = (RecyclerView) findViewById(R.id.recycler_list_devices);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        devicesRecycler.setHasFixedSize(true);
        devicesRecycler.setLayoutManager(llm);
        RVAdapterDevices = new DevicesRVAdapter(this, new ArrayList<DeviceDisplay>());
        RVAdapterDevices.setListener(new RecyclerViewListener() {
            @Override
            public void recyclerViewOnItemClickListener(View view, int position) {
                browsingBehavior(RVAdapterDevices.getItemAt(position));
            }
        });
        devicesRecycler.setAdapter(RVAdapterDevices);

        directoriesRecycler = (RecyclerView) findViewById(R.id.recycler_directories);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        directoriesRecycler.setHasFixedSize(true);
        directoriesRecycler.setLayoutManager(llm2);
        RVAdapterDirectories = new DirectoriesRVAdapter(mContext, new ArrayList<Container>());
        directoriesRecycler.setAdapter(RVAdapterDirectories);

        // Fix the logging integration between java.util.logging and Android internal logging
        org.seamless.util.logging.LoggingUtil.resetRootHandler(new FixedAndroidLogHandler());
        // Now you can enable logging as needed for various categories of Cling:
        //Logger.getLogger("org.fourthline.cling").setLevel(Level.FINEST);

        // This will start the UPnP service if it wasn't already started
        bindService(
                new Intent(this, AndroidUpnpServiceImpl.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (upnpService != null) {
            upnpService.getRegistry().removeListener(registryListener);
        }
        // This will stop the UPnP service if nobody else is bound to it
        unbindService(serviceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, R.string.searchLAN).setIcon(android.R.drawable.ic_menu_search);

        menu.add(0, 1, 0, R.string.switchRouter).setIcon(android.R.drawable.ic_menu_revert);
        menu.add(0, 2, 0, R.string.toggleDebugLogging).setIcon(android.R.drawable.ic_menu_info_details);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                if (upnpService == null)
                    break;
                Toast.makeText(this, R.string.searchingLAN, Toast.LENGTH_SHORT).show();
                upnpService.getRegistry().removeAllRemoteDevices();
                upnpService.getControlPoint().search();
                break;

            case 1:
                if (upnpService != null) {
                    Router router = upnpService.get().getRouter();
                    try {
                        if (router.isEnabled()) {
                            Toast.makeText(this, R.string.disablingRouter, Toast.LENGTH_SHORT).show();
                            router.disable();
                        } else {
                            Toast.makeText(this, R.string.enablingRouter, Toast.LENGTH_SHORT).show();
                            router.enable();
                        }
                    } catch (RouterException ex) {
                        Toast.makeText(this, getText(R.string.errorSwitchingRouter) + ex.toString(), Toast.LENGTH_LONG).show();
                        ex.printStackTrace(System.err);
                    }
                }
                break;
            case 2:
                Logger logger = Logger.getLogger("org.fourthline.cling");
                if (logger.getLevel() != null && !logger.getLevel().equals(Level.INFO)) {
                    Toast.makeText(this, R.string.disablingDebugLogging, Toast.LENGTH_SHORT).show();
                    logger.setLevel(Level.INFO);
                } else {
                    Toast.makeText(this, R.string.enablingDebugLogging, Toast.LENGTH_SHORT).show();
                    logger.setLevel(Level.FINEST);
                }
                break;
        }
        return false;
    }

    protected class BrowseRegistryListener extends DefaultRegistryListener {

        protected Context context;

        public BrowseRegistryListener(Context context){ this.context = context; }

        /* Discovery performance optimization for very slow Android devices! */
        @Override
        public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
            if(acceptableToShow(device)) addDeviceInList(device);
        }

        @Override
        public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(
                            MainActivity.this,
                            "Discovery failed of '" + device.getDisplayString() + "': "
                                    + (ex != null ? ex.toString() : "Couldn't retrieve device/service descriptors"),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
            deviceRemoved(device);
        }
        /* End of optimization, you can remove the whole block if your Android handset is fast (>= 600 Mhz) */

        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            if(acceptableToShow(device)) addDeviceInList(device);
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            deviceRemoved(device);
        }

        @Override
        public void localDeviceAdded(Registry registry, LocalDevice device) {
            if(acceptableToShow(device)) addDeviceInList(device);
        }

        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice device) {
            deviceRemoved(device);
        }

        public void addDeviceInList(final Device device) {
            runOnUiThread(new Runnable() {
                public void run() {
                    DeviceDisplay d = new DeviceDisplay(context,device);
                    if(RVAdapterDevices.contains(d)) {
                        // Device already in the list, re-set new value at same position
                        int position = RVAdapterDevices.indexOf(d);
                        RVAdapterDevices.remove(d);
                        RVAdapterDevices.add(position, d);
                    }else{
                        RVAdapterDevices.add(d);
                    }
                }
            });
        }

        public void deviceRemoved(final Device device) {
            runOnUiThread(new Runnable() {
                public void run() {
                    RVAdapterDevices.remove(new DeviceDisplay(context,device));
                }
            });
        }

        private boolean acceptableToShow(Device device){
            return (device.getType().getType().equals(Constants.TYPE_DEVICE_MEDIA_RENDER) ||
                    device.getType().getType().equals(Constants.TYPE_DEVICE_MEDIA_SERVER) );
        }
    }

    public void browsingBehavior(final DeviceDisplay device){
        if(device.getDevice().getType().getType().equals(Constants.TYPE_DEVICE_MEDIA_SERVER)){
            for (Service service : device.getDevice().getServices()){
                if(service.getServiceType().getType().equals(Constants.SERVICE_TYPE_CONTENT_DIRECTORY)){

                    executeBrowsing(upnpService, service, "0", new BrowseCustomListener() { //Main root
                        @Override
                        public void received(ActionInvocation actionInvocation, final DIDLContent didl) {
                            RVAdapterDirectories.clear();
                            RVAdapterDirectories.addAll(new ArrayList<Container>(didl.getContainers()));
                            RVAdapterDirectories.setListener(new RecyclerViewListener() {
                                @Override
                                public void recyclerViewOnItemClickListener(View view, int position) {
                                    //TODO
                                }
                            });
                            mBrowserLayout.setVisibility(View.VISIBLE);
                            mControlLayout.setVisibility(View.GONE);
                            mContentLayout.setRefreshing(false);
                        }

                        @Override
                        public void updateStatus(Browse.Status status) {}

                        @Override
                        public void failure(ActionInvocation invocation, UpnpResponse operation, final String defaultMsg) {
                            Toast.makeText(mContext, defaultMsg, Toast.LENGTH_LONG).show();
                            mContentLayout.setRefreshing(false);
                        }
                    });
                }
            }
        }else{
            RVAdapterDirectories.clear();
        }
    }

   public void executeBrowsing(AndroidUpnpService upnpService, Service service, String directoryID, final BrowseCustomListener browseListener) {
        mContentLayout.setRefreshing(true);
        upnpService.getControlPoint().execute(new Browse(service, directoryID, BrowseFlag.DIRECT_CHILDREN) {
            @Override
            public void received(final ActionInvocation actionInvocation, final DIDLContent didl) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        browseListener.received(actionInvocation,didl);
                    }
                });
            }

            @Override
            public void updateStatus(final Status status) { /*Nothing here*/}

            @Override
            public void failure(final ActionInvocation invocation, final UpnpResponse operation, final String defaultMsg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        failure(invocation,operation,defaultMsg);
                    }
                });
            }
        });
    }

}