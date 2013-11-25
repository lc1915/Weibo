package lll.weibo;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.util.Pair;

public class API {

	public final static int USER_ID_UNKNOWN = -1;
	public final static String USER_USERNAME_UNKNOWN = "";
	public final static String USER_PASSWORD_UNKNOWN = "";
	public final static String USER_NICKNAME_UNKNOWN = "";
	public final static String USER_ICON_UNKNOWN = "";
	public final static String USER_DESCRIPTION_UNKNOWN = "";
	public final static int USER_COUNTRY_UNKNOWN = -1;
	public final static int USER_COUNTRY_CHINA = 0;
	public final static int USER_COUNTRY_AMERICA = 1;
	public final static int USER_AGE_UNKNOWN = -1;
	public final static int USER_SEX_UNKNOWN = -1;
	public final static int USER_SEX_MALE = 0;
	public final static int USER_SEX_FEMALE = 1;
	public final static String USER_EMAIL_UNKNOWN = "";
	
	public final static int TWEET_ID_UNKNOWN = -1;
	public final static String TWEET_CONTENT_UNKNOWN = "";
	
	static String PATH_JSON = Environment.getExternalStorageDirectory().getPath() + "/uniqueTask2.json";
	
	static API mInstance = null;
	
	public static API GetInstance(Context context) {
		
		if(mInstance != null) {
			return mInstance;
		}
		else {
			mInstance = new API(context);
			return mInstance;
		}
		
	}
	
	Context mContext = null;
	
	JSONObject mJSON = null;
	JSONArray mUsers = null;
	JSONArray mTweets = null;
	
	ArrayList<Pair<String, Integer>> mSessions = null;
	
	API(Context context) {
		
		mContext = context;
		Load();
		mSessions = new ArrayList<Pair<String, Integer>>();
		
	}
	
	JSONObject createUser() {
		
		try {
			JSONObject json = new JSONObject();
			json.put("id", mUsers.length())
				.put("username", USER_USERNAME_UNKNOWN)
				.put("password", USER_PASSWORD_UNKNOWN)
				.put("backgroundUris", new JSONArray())
				.put("iconUri", USER_ICON_UNKNOWN)
				.put("nickname", USER_NICKNAME_UNKNOWN)
				.put("description", USER_DESCRIPTION_UNKNOWN)
				.put("country", USER_COUNTRY_UNKNOWN)
				.put("sex", USER_SEX_UNKNOWN)
				.put("age", USER_AGE_UNKNOWN)
				.put("email", USER_EMAIL_UNKNOWN)
				.put("tweets", new JSONArray())
				.put("followers", new JSONArray());
			return json;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	ArrayList<JSONObject> findUsers(ArrayList<Pair<String, String>> pairs) {
		
		ArrayList<JSONObject> users = new ArrayList<JSONObject>();
		
		try {
			for(int i = 0; i < mUsers.length(); i++) {
				
				JSONObject user = mUsers.getJSONObject(i);
				int index = 0;
				for(int j = 0; j < pairs.size(); j++) {
					Pair<String, String> pair = pairs.get(j);
					if(user.getString(pair.first).equals(pair.second)) {
						index++;
					}
				}
				if(index == pairs.size()) {
					users.add(user);
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	JSONObject findUserById(int userId) {
		
		ArrayList<Pair<String, String>> pairs = new ArrayList<Pair<String, String>>();
		pairs.add(new Pair<String, String>("id", userId + ""));
		ArrayList<JSONObject> users = findUsers(pairs);
		if(users.size() == 1) {
			return users.get(0);
		}
		else {
			return null;
		}
		
	}
	
	JSONObject createTweet() {
		
		try {
			JSONObject json = new JSONObject();
			json.put("id", mTweets.length())
				.put("content", TWEET_CONTENT_UNKNOWN)
				.put("userId", USER_ID_UNKNOWN);
			return json;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	JSONObject makeProfile(JSONObject user) {
		
		try {
			JSONObject profile = new JSONObject();
			
			profile.put("id", user.get("id"));
			profile.put("backgroundUris", user.getJSONArray("backgroundUris"));
			profile.put("iconUri", user.get("iconUri"));
			profile.put("nickname", user.get("nickname"));
			profile.put("description", user.get("description"));
			profile.put("country", user.get("country"));
			profile.put("sex", user.get("sex"));
			profile.put("age", user.get("age"));
			profile.put("email", user.get("email"));
			profile.put("tweets", user.getJSONArray("tweets"));
			profile.put("followers", user.getJSONArray("followers"));
			
			return profile;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	void Load() {
		
		try {
			String json = null;
			File file = new File(PATH_JSON);
			if(file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				json = Utils.ReadTextFromStream(fis);
			}
			else {
				json = "{ 'Users': [], 'Tweets': [] }";
			}
			mJSON = new JSONObject(json);
			mUsers = mJSON.getJSONArray("Users");
			mTweets = mJSON.getJSONArray("Tweets");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void Save() {
		
		try {
			Utils.SaveToFile(PATH_JSON, mJSON.toString(4));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	boolean GetIsReady() {
		return mJSON != null;
	}
	
	String makeSession() {
		
		String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
		
		StringBuilder sb = new StringBuilder("");
		Random rand = new Random();
		for(int i = 0; i < 16; i++) {
			sb.append(chars.charAt(Math.abs(rand.nextInt()) % chars.length()));
		}
		return sb.toString();
		
	}
	
	void registerSession(String session, int userId) {
		
		mSessions.add(new Pair<String, Integer>(session, userId));
		
	}
	
	int findUserIdBySession(String session) {
		
		for(int i = 0; i < mSessions.size(); i++) {
			
			Pair<String, Integer> pair = mSessions.get(i);
			if(pair.first.equals(session)) {
				return pair.second;
			}
			
		}
		return -1;
		
	}
	
	JSONObject findUserByUsername(String username) {
		
		ArrayList<Pair<String, String>> pairs = new ArrayList<Pair<String, String>>();
		pairs.add(new Pair<String, String>("username", username));
		ArrayList<JSONObject> users = findUsers(pairs);
		if(users.size() == 1) {
			return users.get(0);
		}
		else {
			return null;
		}
		
	}
	
	
	String replaceFuck(String content) {
		
		return content.replaceAll("我操", "你好").replaceAll("妈的", "么么哒");
		
	}
	
	String makeAtUserSpan(String content) {
		
		try {
			Pattern p = Pattern.compile("@(.+?) ");
			Matcher m = p.matcher(content);
			StringBuffer sb = new StringBuffer();
			while(m.find()) {
				
				int userId = findUserByUsername(m.group(1)).getInt("id");
				m.appendReplacement(sb, "<at userId=\"" + userId + "\">@$1</at> ");
				
			}
			return m.appendTail(sb).toString();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
		
	}
	
	Response Register(String username, String password) {
		
		ArrayList<Pair<String, String>> pairs = new ArrayList<Pair<String,String>>();
		pairs.add(new Pair<String, String>("username", username));
		
		ArrayList<JSONObject> users = findUsers(pairs);
		
		if(users.size() == 0) {
			try {
				JSONObject user = createUser();
				user.put("username", username)
					.put("password", password);
				mUsers.put(user);
				Save();
				String session = makeSession();
				registerSession(session, user.getInt("id"));
				return Response.Success().SetSession(session).SetExtra(user.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.Error().SetExtra("EXCEPTION");
			}
		}
		else {
			return Response.Error().SetExtra("USER_EXISTING");
		}
		
	}
	
	Response Login(String username, String password) {
		
		ArrayList<Pair<String, String>> pairs = new ArrayList<Pair<String,String>>();
		pairs.add(new Pair<String, String>("username", username));
		pairs.add(new Pair<String, String>("password", password));
		
		ArrayList<JSONObject> users = findUsers(pairs);
		
		if(users.size() == 1) {
			try {
				Save();
				String session = makeSession();
				registerSession(session, users.get(0).getInt("id"));
				return Response.Success().SetSession(session).SetExtra(users.get(0).toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.Error().SetExtra("EXCEPTION");
			}
		}
		else {
			return Response.Error().SetExtra("LOGIN_FAILURE");
		}
		
	}
	
	Response FetchProfile(String session, int userId) {
		
		int userId0 = findUserIdBySession(session);
		if(userId0 != USER_ID_UNKNOWN) {
			
			JSONObject user = findUserById(userId == -1 ? userId0 : userId);
			
			JSONObject profile = makeProfile(user);
			
			return Response.Success().SetExtra(profile.toString());
			
		}
		
		return Response.Error().SetExtra("UNKNOWN_SESSION");
		
	}
	
	Response UpdateProfile(String session, String profileJSON) {
		
		int userId = findUserIdBySession(session);
		if(userId != -1) {
			try {
				
				JSONObject user = findUserById(userId);
				JSONObject profile = new JSONObject(profileJSON);
				
				user.put("backgroundUri", profile.get("backgroundUri"));
				user.put("iconUri", profile.get("iconUri"));
				user.put("nickname", profile.get("nickname"));
				user.put("sex", profile.get("sex"));
				user.put("followings", profile.get("followings"));
				user.put("followers", profile.getJSONArray("followers"));
				user.put("nianling", profile.get("nianling"));
				user.put("description", profile.get("description"));

				
				
				
				Save();
				return Response.Success();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Response.Error().SetExtra("EXCEPTION");
			}
		}
		
		return Response.Error().SetExtra("UNKNOWN_SESSION");
		
	}
	
	Response FetchTweets(String session, int userId) {
		
		int userId0 = findUserIdBySession(session);
		if(userId0 != -1) {
			try {
				
				JSONArray tweets = new JSONArray();
				
				for(int i = 0; i < mTweets.length(); i++) {
					
					JSONObject tweet = mTweets.getJSONObject(i);
					if(tweet.getInt("userId") == (userId == -1 ? userId0 : userId)) {
						tweets.put(tweet);
					}
					
				}
				
				return Response.Success().SetExtra(tweets.toString());
				
			} catch(JSONException e) {
				e.printStackTrace();
				return Response.Error().SetExtra("EXCEPTION");
			}
		}
		
		return Response.Error().SetExtra("UNKNOWN_SESSION");
		
	}
	
	Response FetchFollowers(String session, int userId) {
		
		int userId0 = findUserIdBySession(session);
		if(userId0 != -1) {
			try {
				
				JSONObject user = findUserById(userId == -1 ? userId0 : userId);
				
				JSONArray followers = user.getJSONArray("followers");
				
				JSONArray profiles = new JSONArray();
				for(int i = 0; i < followers.length(); i++) {
					
					int followerUserId = followers.getInt(i);
					JSONObject follower = findUserById(followerUserId);
					
					
					JSONObject profile = makeProfile(follower);
					profiles.put(profile);
				}
				
				return Response.Success().SetExtra(profiles.toString());
				
			} catch(JSONException e) {
				e.printStackTrace();
				return Response.Error().SetExtra("EXCEPTION");
			}
		}
		
		return Response.Error().SetExtra("UNKNOWN_SESSION");
		
	}
	
	public Response WriteTweet(String session, String content) {
		
		int userId = findUserIdBySession(session);
		if(userId != -1) {
			try {
				
				JSONObject user = findUserById(userId);
				
				JSONObject tweet = createTweet();
				
				tweet.put("id", mTweets.length())
					.put("content", makeAtUserSpan(replaceFuck(content)))
					.put("userId", userId);
				
				mTweets.put(tweet);
				user.getJSONArray("tweets").put(tweet.get("id"));
				
				Save();
				return Response.Success();
				
			} catch(JSONException e) {
				e.printStackTrace();
				return Response.Error().SetExtra("EXCEPTION");
			}
		}
		
		return Response.Error().SetExtra("UNKNOWN_SESSION");
		
	}

}