package a;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
public class http extends AsyncTask {
	private Context con;
	private int decide;
    private String url = "https://mpopreverseii.eu5.org/";
	public static int DECIDE_SEND_FEED = 0 ;
	public static int DECIDE_GET_FEED = 1;
	public static int DECIDE_USER = 2;
	public static int DECIDE_SEND_CODE = 3;
	public http(Context a, int decision){
		con = a;
		decide = decision;
	}
	@Override
	protected Object doInBackground(Object[] p1) {
		String a = null;
		switch(decide){
			case 0:
				a = sendFeed(p1);
			break;
			case 1:
				a = getFeed(p1);
			break;
			case 2:
				a = getUser(p1);
			break;
			case 3:
				a = sendCode(p1);
			break;
			default:
				a = "No Command added";
		}
		return a;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	@Override
	protected void onPostExecute(Object result) {
		final String r = result.toString();
		switch(decide){
			case 0:
				toast.makeText(con, r, 1).show();
			break;
			case 1:
				Intent i = new Intent(con.getPackageName() + ".GET_FEEDS");
				i.setAction(con.getPackageName() + ".GET_FEEDS");
				i.putExtra("result", r);
				con.sendBroadcast(i);
			break;
			case 2:
				Intent j = new Intent(con.getPackageName() + ".GET_USER_RESPONSE");
				j.setAction(con.getPackageName() + ".GET_USER_RESPONSE");
				j.putExtra("result", r);
				con.sendBroadcast(j);
			break;
			case 3:
			    toast.makeText(con, r, 1).show();
			break;
		}
	}
	String sendFeed(Object[] p1){
		try{
			String a = p1[0].toString();
			String b1 = p1[1].toString();
			String d = p1[2].toString();
			String s = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(a, "UTF-8");
			s += "&" + URLEncoder.encode("feed", "UTF-8") + "=" + URLEncoder.encode(b1, "UTF-8");
			s += "&" + URLEncoder.encode("packageName", "UTF-8") + "=" + URLEncoder.encode(d, "UTF--8");
			URL u = new URL(url + "MPOPSendFeedback12345.php");
			URLConnection c = u.openConnection();
			c.setDoOutput(true);
			OutputStreamWriter w = new OutputStreamWriter(c.getOutputStream());
			w.write(s);
			w.flush();
			BufferedReader b = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String x = "", l;
			while(( l = b.readLine()) != null){
				x += l;
				break;
			}
			return x;
		}catch(Exception e){
			return e.getMessage();
		}
	}
	String getFeed(Object[] p1){
		try{
			String a = p1[0].toString();
			String s = URLEncoder.encode("package", "UTF-8") + "=" + URLEncoder.encode(a, "UTF-8");
			URL u = new URL(url + "MPOPGetFeedbacks54321.php");
			URLConnection c = u.openConnection();
			c.setDoOutput(true);
			OutputStreamWriter w = new OutputStreamWriter(c.getOutputStream());
			w.write(s);
			w.flush();
			BufferedReader b = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String x = "", y;
			while((y = b.readLine()) != null){
				x += y;
				break;
			}
			return x;
		}catch(Exception e){
			return e.getMessage();
		}
	}
	String getUser(Object[] p1){
		try{
			String u = p1[0].toString();
			String p = p1[1].toString();
			String r = p1[2].toString();
			boolean a = p1[3];
			String s = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(u, "UTF-8");
			s += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(p, "UTF-8");
			s += "&" + URLEncoder.encode("isReg", "UTF-8") + "=" + URLEncoder.encode((a) ? "reg" : "in", "UTF-8");
			s += "&" + URLEncoder.encode("pash", "UTF-8") + "=" + URLEncoder.encode(r, "UTF-8");
			URL l = new URL(url + "MPOPUsersPanel.php");
			URLConnection c = l.openConnection();
			c.setDoOutput(true);
			OutputStreamWriter w = new OutputStreamWriter(c.getOutputStream());
			w.write(s);
			w.flush();
			BufferedReader b = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String o = "", x;
			while((x = b.readLine()) != null){
				o += x;
				break;
			}
			return o;
		}catch(Exception e){
			return e.getMessage();
		}
	}
	String sendCode(Object[] p1){
		try{
			String name = p1[0].toString();
			String file = p1[1].toString();
			String code = p1[2].toString();
			String send = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
			send += "&" + URLEncoder.encode("file", "UTF-8") + "=" + URLEncoder.encode(file, "UTF-8");
			send += "&" + URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(code, "UTF-8");
			URL link = new URL(url + "MPOPCodeSend.php");
			URLConnection con = link.openConnection();
			con.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
			writer.write(send);
			writer.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String o = "", e;
			while((e = reader.readLine()) != null){
				o += e;
				break;
			}
			return o;
		}catch(Exception e){
			return e.getMessage();
		}
	}
}
