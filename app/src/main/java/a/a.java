package a;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Scanner;
import mpop.revii.webdev.platform.R;
import android.view.View.OnLongClickListener;
public class a extends Activity implements TextWatcher{
	LinearLayout base;
	EditText code;
	WebView output;
	View separator;
	SharedPreferences preferences;
	ValueCallback<Uri> fileAccess;
	ValueCallback<Uri[]> FileAccess;
	String save = "";
	String html = "<!DOCTYPE html>\n<html>\n<head>\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, minimal-ui\">\n<title>This is a sample template</title>\n<link rel=\"stylesheet\" href=\"styles.css\">\n<style type=\"text/css\">\n\n</style>\n</head>\n<body>\n<p>Hello World</p>\n</body>\n<script type=\"text/javascript\" src=\"script.js\">\n</script>\n</html>";
	String css = "*{\nbox-sizing: border-box;\nborder: none;\noutline: 0;\npadding: 0;\nmargin: 0;\n}";
	String js = "console.log(\"Hello World\");";
	String console = "";
	boolean opened = false, feedOpened =  true;
	String[] menu = {
		"Save as file",
		"Open a file",
		"Share as text",
		"See in full view",
		"Feedback",
		"Set as default template",
		"Reset to default",
		"Settings"
	};
	String[] feedback = {
		"Gmail",
		"Facebook",
		"MPOP Feedback Form",
		"See Feedbacks on MPOP Reverse II"
	};
	String[] syntax = {
		"HTML",
		"CSS",
		"JavaScript"
	};
	ShapeDrawable backgroundDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_DeviceDefault_NoActionBar);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		base = new LinearLayout(this);
		code = new EditText(this);
		output = new WebView(this);
		separator = new View(this);
		float radii = 25;
		backgroundDialog = new ShapeDrawable(new RoundRectShape(new float[]{radii, radii, radii, radii, radii, radii, radii, radii}, null, null));
		backgroundDialog.getPaint().setColor(Color.parseColor("#13132a"));
		separator.setBackgroundColor(Color.parseColor("#aaaaaa"));
		separator.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					addedMenu();
				}
			});
		separator.setOnLongClickListener(new OnLongClickListener(){
				@Override
				public boolean onLongClick(View p1) {
					AlertDialog.Builder a = new AlertDialog.Builder(a.this);
					a.setTitle("Console:");
					a.setMessage(console.isEmpty() ? "JavaScript has no error" : console);
					a.setPositiveButton("Done", null);
					AlertDialog b = a.create();
					b.getWindow().setBackgroundDrawable(backgroundDialog);
					b.show();
					b.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
					return false;
				}
			});
		code.setTypeface(font(preferences.getString("font", "DEFAULT")));
		code.setTextSize((preferences.getInt("size", 15) >= 13 && preferences.getInt("size", 15) <= 50) ? preferences.getInt("size", 15) : 19);
		code.setText(preferences.getString(preferences.getString("syntax", "html"), html));
		code.setTextColor(Color.WHITE);
		code.setTextScaleX(1f);
		code.setBackgroundColor(Color.BLACK);
		code.setGravity(Gravity.START);
		code.setPadding(15, 5, 15, 5);
		code.addTextChangedListener(this);
		code.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
		if(preferences.getBoolean("wrap", false)){
			code.setHorizontalFadingEdgeEnabled(false);
			code.setHorizontallyScrolling(false);
		}else{
			code.setHorizontalFadingEdgeEnabled(true);
			code.setHorizontallyScrolling(true);
		}
		WebSettings settings = output.getSettings();
		settings.setSupportZoom(true);
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);
		output.setWebViewClient(new WebViewClient());
		output.setWebChromeClient(new WebChromeClient(){
			public void openFileChooser(ValueCallback<Uri> uri){
				fileAccess = uri;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				startActivityForResult(i, 0);
			}
			public void openFileChooser(ValueCallback<Uri> uri, String str){
				fileAccess = uri;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				startActivityForResult(Intent.createChooser(Intent.createChooser(i, "Choose Media"), "Select file"), 0);
			}
			public void openFileChooser(ValueCallback<Uri> uri, String str, String s){
				fileAccess = uri;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				startActivityForResult(Intent.createChooser(i, "Select File"), 0);
			}
			public boolean onShowFileChooser(WebView v, ValueCallback<Uri[]> uri, FileChooserParams p){
				if(FileAccess != null){
					FileAccess.onReceiveValue(null);
					FileAccess = null;
				}
				FileAccess = uri;
				Intent i = p.createIntent();
				try{
					startActivityForResult(i, 1);
				}catch(ActivityNotFoundException e){
					FileAccess = null;
					return false;
				}
				return true;
			}
			@Override
			public boolean onJsAlert(WebView v, String s, String s2, final JsResult j){
				AlertDialog.Builder b = new AlertDialog.Builder(a.this);
				b.setTitle(v.getTitle());
				b.setMessage(s2);
				b.setPositiveButton("Done", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2) {
							j.confirm();
						}
				});
				b.setCancelable(false);
				AlertDialog d = b.create();
				d.getWindow().setBackgroundDrawable(backgroundDialog);
				d.show();
				d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
				return true;
			}
			@Override
			public boolean onJsConfirm(WebView v, String s, String s2, final JsResult j){
				AlertDialog.Builder b = new AlertDialog.Builder(a.this);
				b.setTitle(v.getTitle());
				b.setMessage(s2);
				b.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2) {
							j.confirm();
						}
					});
				b.setNegativeButton("No", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2) {
							j.cancel();
						}
					});
				b.setCancelable(false);
				AlertDialog d = b.create();
				d.getWindow().setBackgroundDrawable(backgroundDialog);
				d.show();
				d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
				d.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
				return true;
			}
			@Override
			public boolean onJsPrompt(WebView v, String s, String s2, String s3, final JsPromptResult j){
				AlertDialog.Builder b = new AlertDialog.Builder(a.this);
				final EditText e = new EditText(a.this);
				b.setTitle(v.getTitle());
				b.setMessage(s2);
				b.setView(e);
				b.setPositiveButton("Submit", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2) {
							j.confirm(e.getText().toString());
						}
					});
				b.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2) {
							j.cancel();
						}
					});
				b.setCancelable(false);
				final AlertDialog d = b.create();
				d.getWindow().setBackgroundDrawable(backgroundDialog);
				d.show();
				d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
				d.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
				d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
				e.addTextChangedListener(new TextWatcher(){
						@Override
						public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
						@Override
						public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
							d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(e.getText().toString().length() > 0);
						}
						@Override
						public void afterTextChanged(Editable p1) {}
					});
				return true;
			}
			@Override
			public void onConsoleMessage (String s, int l, String i){
				console = s + ((s.toLowerCase().contains("error")) ? " on line " + String.valueOf(l) : "");
			}
		});
		File dir = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor"));
		if(!dir.exists()){
			dir.mkdir();
			dir.mkdirs();
			createHtmlFile();
			createCSS();
			createJS();
		}
		if(preferences.getBoolean("isShown", true)){
			separator.setVisibility(View.GONE);
			code.setVisibility(View.GONE);
			output.loadUrl("file:///android_asset/a");
			preferences.edit().putBoolean("isShown", false).commit();
		}else{
			run();
		}
		if(getWindowManager().getDefaultDisplay().getOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || getWindowManager().getDefaultDisplay().getOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE){
			base.setOrientation(LinearLayout.VERTICAL);
			output.getSettings().setUseWideViewPort(false);
			output.getSettings().setLoadWithOverviewMode(false);
			output.getSettings().setUserAgentString(new WebView(a.this).getSettings().getUserAgentString());
			base.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			output.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.2f));
			code.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, Math.round(getWindow().getWindowManager().getDefaultDisplay().getHeight() / 3f)));
			separator.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10));
			base.addView(output);
			base.addView(separator);
			base.addView(code);
		}else{
			output.getSettings().setUseWideViewPort(true);
			output.getSettings().setLoadWithOverviewMode(true);
			output.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
			base.setOrientation(LinearLayout.HORIZONTAL);
			base.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			output.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 0.2f));
			code.setLayoutParams(new LayoutParams(Math.round(getWindow().getWindowManager().getDefaultDisplay().getWidth() / 3f), LayoutParams.MATCH_PARENT));
			separator.setLayoutParams(new LayoutParams(10, LayoutParams.MATCH_PARENT));
			base.addView(code);
			base.addView(separator);
			base.addView(output);
		}
		registerReceiver(new BroadcastReceiver(){
				@Override
				public void onReceive(Context p1, Intent p2) {
					final AlertDialog.Builder a = new AlertDialog.Builder(a.this);
					try{
						String c = p2.getStringExtra("result").replace("mpopreverseii.eu5.org", "website");
						a.setTitle("Feedbacks");
						ArrayAdapter<String> d = new ArrayAdapter<String>(a.this, android.R.layout.simple_list_item_1, c.split("=/=/=/="));
						a.setAdapter(d, null);
						a.setPositiveButton("Close", null);
						a.setCancelable(false);
						AlertDialog e = a.create();
						e.getWindow().setBackgroundDrawable(backgroundDialog);
						e.show();
						e.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
					}catch(Exception e){
						toast.makeText(a.this, "Something went wrong", 1).show();
					}
				}
			}, new IntentFilter(getPackageName() + ".GET_FEEDS"));
		registerReceiver(new BroadcastReceiver(){
				@Override
				public void onReceive(Context p1, Intent p2) {
					try{
						String[] s = p2.getStringExtra("result").replace("mpopreverseii.eu5.org", "website").split("=/=/=");
						if(s[0].equals("1")){
							preferences.edit().putString("user", s[2]).commit();
							toast.makeText(a.this, s[1], 1).show();
						}else{
							toast.makeText(a.this, s[1], 1).show();
						}
					}catch(Exception e){
						toast.makeText(a.this, "Something went wrong", 1).show();
					}
				}
			}, new IntentFilter(getPackageName() + ".GET_USER_RESPONSE"));
		setContentView(base);
	}
	@Override
	public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
	@Override
	public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
		if(code.getText().toString().length() > 10){
			preferences.edit().putString(preferences.getString("syntax", "html"), code.getText().toString()).commit();
		}else{
			code.setText(preferences.getString(preferences.getString("syntax", "html"), html));
		}
	}
	@Override
	public void afterTextChanged(Editable p1) {
		go();
	}
	void go(){
		String s = preferences.getString("syntax", "html");
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
				requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
			}else{
				switch(s){
					case "html":
						createHtmlFile();
					break;
					case "css":
						createCSS();
					break;
					case "javascript":
						createJS();
					break;
				}
			}
		}else{
			switch(s){
				case "html":
					createHtmlFile();
				break;
				case "css":
					createCSS();
				break;
				case "javascript":
					createJS();
				break;
			}
		}
		run();
	}
	public Typeface font(String g1){
		Typeface p1;
		switch(g1){
			case "SANS-SERIF": 
				p1 = Typeface.SANS_SERIF;
				break;
			case "SERIF":
				p1 = Typeface.SERIF;
				break;
			case "MONOSPACE":
				p1 = Typeface.MONOSPACE;
				break;
			default:
				try{
					p1 = Typeface.createFromFile(new File(g1));
				}catch(Exception e){
					p1 = Typeface.DEFAULT;
					preferences.edit().putString("font", "DEFAULT").commit();
				}
		}
		return p1;
	}
	void createHtmlFile(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
				File f = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor")  , ".nomedia.html");
				if(!f.exists()){
					try {
						f.createNewFile();
					} catch (IOException e) {
						toast.makeText(a.this, "Something went wrong. Please try again", 1).show();
						recreate();
					}
				}
				try {
					FileOutputStream o = new FileOutputStream(f, false);
					String c = preferences.getString("html", html);
					byte[] b = c.getBytes();
					try {
						o.write(b);
						o.close();
					} catch (IOException e) {
						toast.makeText(a.this, "Can't execute code", 1).show();
					}
				} catch (FileNotFoundException e) {
					toast.makeText(a.this, "Error", 1).show();
				}
			}
		}else{
			File f = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor"), ".nomedia.html");
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (IOException e) {
					toast.makeText(a.this, "Something went wrong. Please try again", 1).show();
					recreate();
				}
			}
			try {
				FileOutputStream o = new FileOutputStream(f, false);
				String c = preferences.getString("html", html);
				byte[] b = c.getBytes();
				try {
					o.write(b);
					o.close();
				} catch (IOException e) {
					toast.makeText(a.this, "Can't execute code", 1).show();
				}
			} catch (FileNotFoundException e) {
				toast.makeText(a.this, "Error", 1).show();
			}
		}
	}
	void createCSS(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
				File f = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor"), "styles.css");
				if(!f.exists()){
					try {
						f.createNewFile();
					} catch (IOException e) {
						toast.makeText(a.this, "Something went wrong",1);
					}
				}
				try{
					FileOutputStream o = new FileOutputStream(f, false);
					try {
						o.write(preferences.getString("css", css).getBytes());
						o.close();
					} catch (IOException e) {
						toast.makeText(a.this, "Something went wrong", 1);
					}
				} catch (FileNotFoundException e) {
					toast.makeText(a.this, "File error", 1);
				}
			}
		}else{
			File f = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor"), "styles.css");
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (IOException e) {
					toast.makeText(a.this, "Something went wrong",1);
				}
			}
			try{
				FileOutputStream o = new FileOutputStream(f, false);
				try {
					o.write(preferences.getString("css", css).getBytes());
					o.close();
				} catch (IOException e) {
					toast.makeText(a.this, "Something went wrong", 1);
				}
			} catch (FileNotFoundException e) {
				toast.makeText(a.this, "File error", 1);
			}
		}
	}
	void createJS(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
				File f = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor"), "script.js");
				if(!f.exists()){
					try {
						f.createNewFile();
					} catch (IOException e) {
						toast.makeText(a.this, "Something went wrong",1);
					}
				}
				try{
					FileOutputStream o = new FileOutputStream(f, false);
					try {
						o.write(preferences.getString("javascript", js).getBytes());
						o.close();
					} catch (IOException e) {
						toast.makeText(a.this, "Something went wrong", 1);
					}
				} catch (FileNotFoundException e) {
					toast.makeText(a.this, "File error", 1);
				}
			}
		}else{
			File f = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor"), "script.js");
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (IOException e) {
					toast.makeText(a.this, "Something went wrong",1);
				}
			}
			try{
				FileOutputStream o = new FileOutputStream(f, false);
				try {
					o.write(preferences.getString("javascript", js).getBytes());
					o.close();
				} catch (IOException e) {
					toast.makeText(a.this, "Something went wrong", 1);
				}
			} catch (FileNotFoundException e) {
				toast.makeText(a.this, "File error", 1);
			}
		}
	}
	public void run(){
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				output.clearCache(true);
				output.clearHistory();
				output.clearFormData();
				output.loadUrl("file:///storage/emulated/0/" + preferences.getString("dir", "Front-end Editor")  + "/.nomedia.html");
			}
		}, 100);
	}
	void showMenu(){
		AlertDialog.Builder a = new AlertDialog.Builder(this);
		final ArrayAdapter<String> b = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
		a.setTitle("Menu:");
		a.setIcon(R.drawable.menu);
		a.setAdapter(b, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					String s = new String(b.getItem(p2)).toLowerCase().replace(" ", "");
					switch(s){
						case "saveasfile":
							saveAsFile();
						break;
						case "openafile":
							openAFile();
						break;
						case "shareastext":
							Intent intent = new Intent(Intent.ACTION_SEND);
							String str = "<!--\nFile: index.html\n-->\n" + preferences.getString("html", html);
							if(preferences.getString("html", html).contains("href=\"styles.css\"")){
								str += "/*\nFile: styles.css\n*/\n" + preferences.getString("css", css);
							}
							if(preferences.getString("html", html).contains("src=\"script.js\"")){
								str += "/*\nFile: script.js\n*/\n" + preferences.getString("javascript", js);
							}
							intent.putExtra(Intent.EXTRA_SUBJECT, str);
							intent.putExtra(Intent.EXTRA_TEXT, str);
							intent.setType("text/plain");
							startActivity(intent);
						break;
						case "seeinfullview":
							code.setVisibility(View.GONE);
							separator.setVisibility(View.GONE);
							toast.makeText(a.this, "Press back navigation to show the field again", 1).show();
						break;
						case "feedback":
							feedback();
						break;
						case "setasdefaulttemplate":
							preferences.edit().putString("defaulthtml", preferences.getString("html", html)).commit();
							toast.makeText(a.this, "Default template update", 1).show();
						break;
						case "resettodefault":
							save = "";
							preferences.edit().putString("syntax", "html").commit();
							code.setText(preferences.getString("defaulthtml", html));
							toast.makeText(a.this, "Content back to default", 1).show();
						break;
						case "settings":
							settings();
						break;
					}
				}
			});
		a.setPositiveButton("Close", null);
		a.setCancelable(false);
		AlertDialog d = a.create();
		d.getWindow().setBackgroundDrawable(backgroundDialog);
		d.show();
		d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
	}
	void addedMenu(){
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		final ArrayAdapter<String> l = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, syntax);
		b.setTitle("Syntax:");
		b.setAdapter(l, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					preferences.edit().putString("syntax", String.valueOf(l.getItem(p2).toLowerCase())).commit();
					toast.makeText(a.this, "Syntax is changed to " + String.valueOf(l.getItem(p2)), 1);
					switch(l.getItem(p2).toLowerCase()){
						case "html":
							code.setText(preferences.getString(preferences.getString("syntax", "html"), html));
						break;
						case "css":
							code.setText(preferences.getString(preferences.getString("syntax", "html"), css));
						break;
						case "javascript":
							code.setText(preferences.getString(preferences.getString("syntax", "html"), js));
						break;
					}
				}
			});
		b.setPositiveButton("Cancel", null);
		b.setCancelable(false);
		AlertDialog d = b.create();
		d.getWindow().setBackgroundDrawable(backgroundDialog);
		d.show();
		d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
	}
	void feedback(){
		AlertDialog.Builder a = new AlertDialog.Builder(this);
		final ArrayAdapter<String> b = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, feedback);
		a.setTitle("Send us feedback thru:");
		a.setIcon(R.drawable.send);
		a.setAdapter(b, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					String s = new String(b.getItem(p2)).toLowerCase().replace(" ", "");
					switch(s){
						case "facebook":
							if(checkInternet()){
								output.loadUrl("https://free.facebook.com/messages/thread/101484375409392/");
								code.setVisibility(View.GONE);
								toast.makeText(a.this, "Please wait to load", 1).show();
							}else{
								toast.makeText(a.this, "Internet is not connected", 1).show();
							}
						break;
						case "gmail":
							if(checkInternet()){
								Intent intent = new Intent(Intent.ACTION_SEND);
								intent.putExtra(Intent.EXTRA_SUBJECT, getTitle());
								intent.setType("text/html");
								intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"weryses19@gmail.com"});
								if(checkInstall("com.google.android.gm")){
									intent.setPackage("com.google.android.gm");
									startActivity(intent);
								}else if(checkInstall("com.google.android.gm.lite")){
									intent.setPackage("com.google.android.gm.lite");
									startActivity(intent);
								}else{
									toast.makeText(a.this, "Install Gmail app first", 1).show();
								}
							}else{
								toast.makeText(a.this, "Internet connection is not active", 1).show();
							}
						break;
						case "mpopfeedbackform":
							if(checkInternet()){
								if(preferences.getString("user", "").isEmpty()){
									panel();
								}else{
									AlertDialog.Builder b = new AlertDialog.Builder(a.this);
									LinearLayout l = new LinearLayout(a.this);
									final EditText t = new EditText(a.this);
									final EditText e = new EditText(a.this);
									b.setTitle("Front-end Editor: Feedback");
									b.setMessage("If you we're sending an attachment, better to use facebook or gmail feedback");
									l.setOrientation(LinearLayout.VERTICAL);
									t.setHint("Please enter your screen name here");
									t.setText(preferences.getString("user", ""));
									t.setSingleLine();
									t.setEnabled(false);
									e.setHint("Feedback here, atleast 15 characters excluding whitespaces");
									l.addView(t);
									l.addView(e);
									b.setView(l);
									b.setPositiveButton("Send", new DialogInterface.OnClickListener(){
											@Override
											public void onClick(DialogInterface p1, int p2) {
												http h = new http(a.this, http.DECIDE_SEND_FEED);
												h.execute(t.getText().toString(), e.getText().toString(), getPackageName());
											}
										});
									b.setNegativeButton("Cancel", null);
									b.setCancelable(false);
									final AlertDialog a = b.create();
									a.show();
									a.getWindow().setBackgroundDrawable(backgroundDialog);
									a.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
									a.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
									a.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
									e.addTextChangedListener(new TextWatcher(){
											@Override
											public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
											@Override
											public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
												a.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(e.getText().toString().replace(" ", "").length() >= 10);
											}
											@Override
											public void afterTextChanged(Editable p1) {}
										});
									t.addTextChangedListener(new TextWatcher(){
											@Override
											public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
											@Override
											public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
												a.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(e.getText().toString().replace(" ", "").length() > 10);
											}
											@Override
											public void afterTextChanged(Editable p1) {}
										});
								}
							}else{
								toast.makeText(a.this, "Internet connection is not active", 1).show();
							}
						break;
						case "seefeedbacksonmpopreverseii":
							if(preferences.getString("user", "").isEmpty()){
								panel();
							}else{
								mpopfeed();
							}
						break;
					}
				}
			});
		a.setPositiveButton("Cancel", null);
		a.setCancelable(false);
		AlertDialog d = a.create();
		d.getWindow().setBackgroundDrawable(backgroundDialog);
		d.show();
		d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
	}
	void mpopfeed(){
		http feedbacks = new http(a.this, http.DECIDE_GET_FEED);
		feedbacks.execute(getPackageName());
	}
	void openAFile(){
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Open a file: ");
		String s = "/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor");
		File f = new File(s);
		if(f.exists()){
			if(f.list().length > 0){
				final ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
				for(int i = 0; i < f.list().length; i++){
					if(!f.list()[i].contains(".nomedia.html") && (f.list()[i].endsWith(".html") || f.list()[i].endsWith(".css") || f.list()[i].endsWith(".js"))){
						a.add(f.list()[i]);
						a.notifyDataSetChanged();
					}
				}
				a.sort(new Comparator<String>(){
						@Override
						public int compare(String p1, String p2) {
							return p1.compareToIgnoreCase(p2);
						}
					});
				b.setAdapter(a, new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2) {
							String n = String.valueOf(a.getItem(p2));
							if(n.toLowerCase().endsWith(".css")){
								preferences.edit().putString("syntax", "css").commit();
								toast.makeText(a.this, "Syntax is automatically changed to CSS", 1);
							}else if(n.toLowerCase().endsWith(".js")){
								preferences.edit().putString("syntax", "js").commit();
								toast.makeText(a.this, "Syntax is automatically changed to JavaScript", 1);
							}else{
								preferences.edit().putString("syntax", "html").commit();
								toast.makeText(a.this, "Syntax is automatically changed to HTML", 1);
							}
							openAFile(n);
						}
					});
			}else{
				b.setMessage("There's no saved files here");
			}
		}else{
			b.setMessage("Directory didn't exist");
		}
		b.setPositiveButton("Close", null);
		b.setCancelable(false);
		AlertDialog d = b.create();
		d.getWindow().setBackgroundDrawable(backgroundDialog);
		d.show();
		d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
	}
	void openAFile(String g1){
		code.setText(accessFileContent(g1));
	}
	String accessFileContent(String g1){
		try {
			Scanner s = new Scanner(new FileInputStream(new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor")  + "/" + g1)));
			String c = "";
			while(s.hasNext()){
				c += s.nextLine() + (s.hasNext() ? "\n" : "");
			}
			toast.makeText(a.this, "Done", 1).show();
			save = g1;
			return c;
		} catch (FileNotFoundException e) {
			toast.makeText(a.this, "File not found", 1).show();
			return "";
		}
	}
	void settings(){
		AlertDialog.Builder a = new AlertDialog.Builder(this);
		ScrollView b = new ScrollView(this);
		LinearLayout c = new LinearLayout(this);
		final EditText defaultTemplate = new EditText(this);
		final EditText font = new EditText(this);
		final EditText size = new EditText(this);
		final EditText dir = new EditText(this);	
		final CheckBox wrap = new CheckBox(this);
		a.setTitle("Configurations:");
		a.setIcon(R.drawable.settings);
		c.setOrientation(LinearLayout.VERTICAL);
		c.setPadding(20, 5, 20, 5);
		defaultTemplate.setHint("Enter default template");
		font.setHint("Enter font directory or font type");
		size.setHint("Enter text size");
		dir.setHint("Enter custom folder here");
		defaultTemplate.setText(preferences.getString("defaulthtml", html));
		font.setText(preferences.getString("font", "DEFAULT"));
		size.setText(String.valueOf(preferences.getInt("size", 15)));
		dir.setText(preferences.getString("dir", "Front-end Editor"));
		wrap.setText("Wrap text");
		wrap.setChecked(preferences.getBoolean("wrap", false));
		defaultTemplate.setMaxLines(10);
		font.setSingleLine();
		size.setSingleLine();
		dir.setSingleLine();
		size.setInputType(InputType.TYPE_CLASS_NUMBER);
		c.addView(defaultTemplate);
		c.addView(font);
		c.addView(size);
		c.addView(dir);
		c.addView(wrap);
		b.addView(c);
		a.setView(b);
		a.setPositiveButton("Save all changes", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					int textSize = number(size.getText().toString());
					String fontName = font.getText().toString();
					String defTemp = defaultTemplate.getText().toString();
					preferences.edit().putString("defaulthtml", (defTemp.length() >= 10) ? defTemp : preferences.getString("html", html))
					.putString("font", (fontName.isEmpty()) ? "DEFAULT" : fontName)
					.putInt("size", (textSize >= 13 && textSize <=50) ? textSize : 19)
					.putString("dir", ((dir.getText().toString().replace(" ", "").isEmpty()) ? "Front-end Editor" : dir.getText().toString().replace("/", "")))
					.putBoolean("wrap", wrap.isChecked()).commit();
					code.setTypeface(font(font.getText().toString()));
					code.setTextSize((textSize >= 13 && textSize <= 50) ? textSize : 19);
					if(wrap.isChecked()){
						code.setHorizontalFadingEdgeEnabled(false);
						code.setHorizontallyScrolling(false);
					}else{
						code.setHorizontalFadingEdgeEnabled(true);
						code.setHorizontallyScrolling(true);
					}
					File dir = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor"));
					if(!dir.exists()){
						dir.mkdir();
						dir.mkdirs();
						createHtmlFile();
						createCSS();
						createJS();
					}
					run();
					toast.makeText(a.this, "Done", 1).show();
				}
			});
		a.setNegativeButton("Cancel", null);
		a.setCancelable(false);
		AlertDialog d = a.create();
		d.getWindow().setBackgroundDrawable(backgroundDialog);
		d.show();
		d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
		d.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
	}
	void saveAsFile(){
		AlertDialog.Builder a = new AlertDialog.Builder(this);
		final EditText b = new EditText(this);
		a.setTitle("Save as file: ");
		b.setSingleLine();
		b.setText(save);
		a.setView(b);
		a.setPositiveButton("Save", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					String x = preferences.getString("syntax", "html");
					String y = b.getText().toString();
					switch(x){
						case "html":
							if(y.replace(".css", "").endsWith(".css") && !y.replace(".css", "").isEmpty()){
								y = y.replace(".css", "");
							}
							if(y.replace(".js", "").endsWith(".js") && !y.replace(".js", "").isEmpty()){
								y = y.replace(".js", "");
							}
							if(!y.endsWith(".html")){
								y += ".html";
							}
						break;
						case "css":
							if(y.replace(".html", "").endsWith(".html") && !y.replace(".html", "").isEmpty()){
								y = y.replace(".html", "");
							}
							if(y.replace(".js", "").endsWith(".js") && !y.replace(".js", "").isEmpty()){
								y = y.replace(".js", "");
							}
							if(!y.endsWith(".css")){
								y += ".css";
							}
						break;
						case "javascript":
							if(y.replace(".html", "").endsWith(".html") && !y.replace(".html", "").isEmpty()){
								y = y.replace(".html", "");
							}
							if(y.replace(".css", "").endsWith(".css") && !y.replace(".css", "").isEmpty()){
								y = y.replace(".css", "");
							}
							if(!y.endsWith(".js")){
								y += ".js";
							}
						break;
					}
					saveAsFile(y);
				}
			});
		a.setNegativeButton("Cancel", null);
		a.setCancelable(false);
		final AlertDialog d = a.create();
		d.getWindow().setBackgroundDrawable(backgroundDialog);
		d.show();
		d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
		d.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
		d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b.getText().toString().replace(" ", "").length() > 0);
		b.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b.getText().toString().replace(" ", "").length() > 0);
				}
				@Override
				public void afterTextChanged(Editable p1) {}
			});
	}
	void saveAsFile(final String g1){
		File f = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor"));
		if(!f.exists()){
			f.mkdir();
			f.mkdirs();
		}
		final File f2 = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor"), g1);
		if(f2.exists()){
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("Confirmation:");
			b.setMessage("Are you sure you want to overwrite the file " + g1 + "?");
			b.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2) {
						try {
							FileOutputStream o = new FileOutputStream(f2, false);
							byte[] b = code.getText().toString().getBytes();
							try {
								o.write(b);
								o.close();
								toast.makeText(a.this, "Saved to /storage/emulated/0/" + preferences.getString("dir", "Front-end Editor") + "/" + g1, 1).show();
								save = g1;
							} catch (IOException e) {
								toast.makeText(a.this, "Failed to save as file", 1).show();
							}
						} catch (FileNotFoundException e) {
							toast.makeText(a.this, "File not found", 1).show();
						}
					}
				});
			b.setNegativeButton("No", null);
			b.setCancelable(true);
			AlertDialog d = b.create();
			d.getWindow().setBackgroundDrawable(backgroundDialog);
			d.show();
			d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
			d.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
		}else{
			try {
				f2.createNewFile();
				try {
					FileOutputStream o = new FileOutputStream(f2, false);
					byte[] b = code.getText().toString().getBytes();
					try {
						o.write(b);
						o.close();
						toast.makeText(a.this, "Saved to /storage/emulated/0/" + preferences.getString("dir", "Front-end Editor")  + "/" + g1, 1).show();
						save = g1;
					} catch (IOException e) {
						toast.makeText(a.this, "Failed to save as file", 1).show();
					}
				} catch (FileNotFoundException e) {
					toast.makeText(a.this, "File not found", 1).show();
				}
			} catch (IOException e) {
				toast.makeText(a.this, "Something went wrong", 1).show();
			}
		}
	}
	void panel(){
		AlertDialog.Builder a = new AlertDialog.Builder(a.this);
		LinearLayout b = new LinearLayout(a.this);
		final EditText c = new EditText(a.this);
		final EditText d = new EditText(a.this);
		final EditText e = new EditText(a.this);
		final CheckBox f = new CheckBox(a.this);
		a.setTitle("User's panel");
		c.setSingleLine();
		d.setSingleLine();
		e.setSingleLine();
		c.setHint("Enter username: (Atleast 5 characters and no spaces)");
		d.setHint("Enter password: (Atleast 8 characters");
		e.setHint("Re-enter password");
		f.setText("Login");
		d.setTransformationMethod(new PasswordTransformationMethod());
		e.setTransformationMethod(new PasswordTransformationMethod());
		e.setVisibility(View.GONE);
		b.setOrientation(LinearLayout.VERTICAL);
		b.addView(c);
		b.addView(d);
		b.addView(e);
		b.addView(f);
		a.setView(b);
		a.setPositiveButton("Login", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					http h = new http(a.this, http.DECIDE_USER);
					h.execute(c.getText().toString(), d.getText().toString(), e.getText().toString(), f.isChecked());
				}
			});
		a.setNegativeButton("Cancel", null);
		a.setCancelable(false);
		final AlertDialog g = a.create();
		g.getWindow().setBackgroundDrawable(backgroundDialog);
		g.show();
		g.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
		g.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
		g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
		f.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2) {
					if(f.isChecked()){
						e.setVisibility(View.VISIBLE);
						g.getButton(AlertDialog.BUTTON_POSITIVE).setText("Register");
						f.setText("Register");
						if(e.getText().toString().equals(d.getText().toString())){
							if(c.getText().length() >= 5 && !c.getText().toString().contains(" ") && d.getText().length() >= 8){
								g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
							}else{
								g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
							}
						}else{
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						}
					}else{
						f.setText("Login");
						g.getButton(AlertDialog.BUTTON_POSITIVE).setText("Login");
						e.setVisibility(View.GONE);
						if(c.getText().length() >= 5 && !c.getText().toString().contains(" ") && d.getText().length() >= 8){
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
						}else{
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						}
					}
				}
			});
		c.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					if(f.isChecked()){
						e.setVisibility(View.VISIBLE);
						g.getButton(AlertDialog.BUTTON_POSITIVE).setText("Register");
						f.setText("Register");
						if(e.getText().toString().equals(d.getText().toString())){
							if(c.getText().length() >= 5 && !c.getText().toString().contains(" ") && d.getText().length() >= 8){
								g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
							}else{
								g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
							}
						}else{
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						}
					}else{
						f.setText("Login");
						g.getButton(AlertDialog.BUTTON_POSITIVE).setText("Login");
						e.setVisibility(View.GONE);
						if(c.getText().length() >= 5 && !c.getText().toString().contains(" ") && d.getText().length() >= 8){
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
						}else{
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						}
					}
				}
				@Override
				public void afterTextChanged(Editable p1) {}
			});
		d.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					if(f.isChecked()){
						e.setVisibility(View.VISIBLE);
						g.getButton(AlertDialog.BUTTON_POSITIVE).setText("Register");
						f.setText("Register");
						if(e.getText().toString().equals(d.getText().toString())){
							if(c.getText().length() >= 5 && !c.getText().toString().contains(" ") && d.getText().length() >= 8){
								g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
							}else{
								g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
							}
						}else{
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						}
					}else{
						f.setText("Login");
						g.getButton(AlertDialog.BUTTON_POSITIVE).setText("Login");
						e.setVisibility(View.GONE);
						if(c.getText().length() >= 5 && !c.getText().toString().contains(" ") && d.getText().length() >= 8){
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
						}else{
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						}
					}
				}
				@Override
				public void afterTextChanged(Editable p1) {}
			});
		e.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					if(f.isChecked()){
						e.setVisibility(View.VISIBLE);
						g.getButton(AlertDialog.BUTTON_POSITIVE).setText("Register");
						f.setText("Register");
						if(e.getText().toString().equals(d.getText().toString())){
							if(c.getText().length() >= 5 && !c.getText().toString().contains(" ") && d.getText().length() >= 8){
								g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
							}else{
								g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
							}
						}else{
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						}
					}else{
						f.setText("Login");
						g.getButton(AlertDialog.BUTTON_POSITIVE).setText("Login");
						e.setVisibility(View.GONE);
						if(c.getText().length() >= 5 && !c.getText().toString().contains(" ") && d.getText().length() >= 8){
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
						}else{
							g.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
						}
					}
				}
				@Override
				public void afterTextChanged(Editable p1) {}
			});
	}
	int number(String g1){
		try{
			return Integer.parseInt(g1);
		}catch(NumberFormatException e){
			return 15;
		}
	}
	boolean checkInstall(String app){
		PackageManager pm = getPackageManager();
		try {
			pm.getPackageInfo(app, pm.GET_ACTIVITIES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			ApplicationInfo a = null;
			try {
				a = getPackageManager().getApplicationInfo(app, 0);
			} catch (PackageManager.NameNotFoundException e2) {}
			return a.enabled;
		}
	}
	boolean checkInternet(){
		try{
			ConnectivityManager c = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo n = c.getActiveNetworkInfo();
			return n.isConnected() || n.isAvailable();
		}catch(Exception e){
			return false;
		}
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		File dir = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor"));
		if(!dir.exists()){
			dir.mkdir();
			dir.mkdirs();
		}
		createHtmlFile();
		createCSS();
		createJS();
	}
	@Override
	protected void onResume() {
		super.onResume();
		File dir = new File("/storage/emulated/0/" + preferences.getString("dir", "Front-end Editor"));
		if(!dir.exists()){
			dir.mkdir();
			dir.mkdirs();
		}
		createHtmlFile();
		createCSS();
		createJS();
	}
	@Override
	protected void onDestroy() {
		File a = new File("/storage/emulated/0/"  + preferences.getString("dir", "Front-end Editor")  +  "/.nomedia.html");
		if(a.exists()){
			a.delete();
		}
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		File a = new File("/storage/emulated/0/"  + preferences.getString("dir", "Front-end Editor")  +  "/.nomedia.html");
		if(a.exists()){
			a.delete();
		}
		super.onPause();
	}
	@Override
	protected void onStop() {
		File a = new File("/storage/emulated/0/"  + preferences.getString("dir", "Front-end Editor")  +  "/.nomedia.html");
		if(a.exists()){
			a.delete();
		}
		super.onStop();
	}
	@Override
	public void onBackPressed() {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Confirmation:");
		b.setMessage("Are you sure you want to close the app?");
		b.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					File a = new File("/storage/emulated/0/"  + preferences.getString("dir", "Front-end Editor")  +  "/.nomedia.html");
					if(a.exists()){
						a.delete();
					}
					finishAndRemoveTask();
				}
			});
		b.setNegativeButton("No", null);
		b.setNeutralButton("Show menu", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					showMenu();
				}
			});
		b.setCancelable(true);
		if(code.getVisibility() == View.GONE){
			go();
			separator.setVisibility(View.VISIBLE);
			code.setVisibility(View.VISIBLE);
		}else{
			AlertDialog d = b.create();
			d.getWindow().setBackgroundDrawable(backgroundDialog);
			d.show();
			d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#dedede"));
			d.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#dedede"));
			d.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#dedede"));
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch(requestCode){
			case 0:
				if(grantResults.length > 0 & grantResults[0] == PackageManager.PERMISSION_GRANTED){
					toast.makeText(a.this, "Granted", 1).show();
					run();
				}else{
					toast.makeText(a.this, "Denied", 1).show();
					finishAndRemoveTask();
				}
			break;
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case 0:
				if(fileAccess == null)
					return;
				Uri u = data == null || resultCode != RESULT_OK ? null : data.getData();
				fileAccess.onReceiveValue(u);
				fileAccess = null;
			break;
			case 1:
				if(FileAccess == null)
					return;
				FileAccess.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
				FileAccess = null;
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
