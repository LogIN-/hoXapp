package com.android.hoxapp;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class AndroidHTTPD extends Activity {
	
  private static final int PORT = 8765;
  private TextView server_request;
  private MyHTTPD server;
  private Handler handler = new Handler();
  private File wwwroot = Environment.getExternalStorageDirectory();
  private String formatedIpAddress = "";
  private static String PHP_LOCATION = "NOLOC";
  // private static String VNC_LOCATION = "NOLOC";
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.server);
    server_request = (TextView) findViewById(R.id.server_request);
    
    SharedPreferences settings = this.getSharedPreferences(MainActivity.class.getName(), 0);	
	PHP_LOCATION = settings.getString("PHP_LOCATION", null);
	// VNC_LOCATION = settings.getString("VNC_LOCATION", null);
		
  }
  public String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Log.e("ERROR", ex.toString());
	    }
	    return null;
	}

  @Override
  protected void onResume() {
    super.onResume();
    formatedIpAddress = getLocalIpAddress();
    
    TextView textIpaddr = (TextView) findViewById(R.id.ipaddr);    
    String msg_ip_adress = getResources().getString(R.string.hoxapp_server_ip_addr);
    textIpaddr.setText(msg_ip_adress + formatedIpAddress + ":" + PORT);

    try {
      server = new MyHTTPD();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (server != null)
      server.stop();
  }

  private class MyHTTPD extends NanoHTTPD {
    public MyHTTPD() throws IOException {
      super(PORT, wwwroot, PHP_LOCATION);
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
    	Log.i("SERVER_TAG",
    			new StringBuilder().append("#serve called with uri=")
    			.append(uri).append(", method=").append(method)
    			.append(", header=")
    			.append(null == header ? null : header.toString())
    			.append(", parms=")
    			.append(null == parms ? null : parms.toString())
    			.toString());
    	
	final StringBuilder buf = new StringBuilder();
	  for (Entry<Object, Object> kv : header.entrySet())
	    buf.append(kv.getKey() + " : " + kv.getValue() + "\n");
	  handler.post(new Runnable() {
	    public void run() {
	    	server_request.setText(buf);
	    }
	  });
      return super.serve(uri, method, header, parms, files);
    }
  }
  /*
	public void startServer() {
		// Lets see if i need to boot daemon...
		try {
		String files_dir = getFilesDir().getAbsolutePath();

		String password = "password";
		String password_check = "";
		if (!password.equals(""))
		password_check = "-p " + password;

		String rotation = "0";
		if (!rotation.equals(""))
		rotation = "-r " + rotation;

		String scaling = "100";

		String scaling_string = "";
		if (!scaling.equals(""))
		scaling_string = "-s " + scaling;

		String port = "5901";
		try {
		int port1 = Integer.parseInt(port);
		port = String.valueOf(port1);
		} catch (NumberFormatException e) {
		port = "5901";
		}
		String port_string = "";
		if (!port.equals(""))
		port_string = "-P " + port;

		String display_method = "";
		display_method = "-m auto";

		String display_zte="";
		display_zte = "-z";

		String droidvncserver_exec=getFilesDir().getParent() + "/lib/libandroidvncserver.so";
		File f=new File (droidvncserver_exec);
		if (!f.exists())
		{
		return;
		}
		
		Runtime.getRuntime().exec("chmod 777 " + droidvncserver_exec);		 
		String permission_string="chmod 777 " + droidvncserver_exec;
		String server_string= droidvncserver_exec + " " + password_check + " " + rotation+ " " + scaling_string + " " + port_string + " " + display_method + " " + display_zte;
		 

		Runtime.getRuntime().exec(permission_string);
		Runtime.getRuntime().exec(server_string,null,new File(files_dir));
		
		// dont show password on logcat
		Log.e("VNC_START", "Starting " + droidvncserver_exec + " " + rotation+ " " + scaling_string + " " + port_string + " " + display_method + " " + display_zte);

		} catch (IOException e) {
			Log.e("VNC_START","startServer():" + e.getMessage());
		} catch (Exception e) {
			Log.e("VNC_START","startServer():" + e.getMessage());
		}

		}
		*/
}