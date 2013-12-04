package lll.weibo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	private DBHelper helper;
	static SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	/**
	 * add persons
	 * 
	 * @param persons
	 */
	public void add(List<Person> persons) {
		db.execSQL("CREATE TABLE IF NOT EXISTS person"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,background NVARCHAR,icon TEXT, name NVARCHAR,gender CHAR,nation CHAR, age CHAR,followings CHAR,followers CHAR, info TEXT)");
		db.beginTransaction(); // 开始事务
		try {
			for (Person person : persons) {
				db.execSQL(
						"INSERT INTO person VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						new Object[] { person.background, person.icon,
								person.name, person.gender, person.nation,
								person.age, person.followings,
								person.followers, person.info });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void add0(List<Tweet> tweets) {
		db.execSQL("CREATE TABLE IF NOT EXISTS tweet"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name NVARCHAR,weibo TEXT)");
		db.beginTransaction(); // 开始事务
		try {
			for (Tweet tweet : tweets) {
				db.execSQL("INSERT INTO tweet VALUES(null, ?, ?)",
						new Object[] { tweet.name, tweet.tweet });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void add1(List<Sign> signs) {
		db.execSQL("CREATE TABLE IF NOT EXISTS sign"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, username NVARCHAR,password TEXT)");
		db.beginTransaction(); // 开始事务
		try {
			for (Sign sign : signs) {
				db.execSQL("INSERT INTO sign VALUES(null, ?, ?)", new Object[] {
						sign.username, sign.password });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void add2(List<Fan> fans) {
		db.execSQL("CREATE TABLE IF NOT EXISTS fan"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name NVARCHAR,ilike TEXT)");
		db.beginTransaction(); // 开始事务
		try {
			for (Fan fan : fans) {
				db.execSQL("INSERT INTO fan VALUES(null, ?, ?)", new Object[] {
						fan.name, fan.ilike });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * query all persons, return list
	 * 
	 * @return List<Person>
	 */
	public List<Person> query() {
		ArrayList<Person> persons = new ArrayList<Person>();
		String username = SignIn.username;
		Cursor c = db.rawQuery("select * from person where name like ?",
				new String[] { username });
		while (c.moveToNext()) {
			Person person = new Person();
			person._id = c.getInt(c.getColumnIndex("_id"));
			person.background = c.getString(c.getColumnIndex("background"));
			person.icon = c.getString(c.getColumnIndex("icon"));
			person.name = c.getString(c.getColumnIndex("name"));
			person.gender = c.getString(c.getColumnIndex("gender"));
			person.nation = c.getString(c.getColumnIndex("nation"));
			person.age = c.getInt(c.getColumnIndex("age"));
			person.followings = c.getInt(c.getColumnIndex("followings"));
			person.followers = c.getInt(c.getColumnIndex("followers"));
			person.info = c.getString(c.getColumnIndex("info"));
			persons.add(person);
		}
		c.close();
		return persons;
	}

	public Person query0() {
		Person person = new Person();
		String username = SignIn.username;
		Cursor c = db.rawQuery("select * from person where name like ?",
				new String[] { username });
		while (c.moveToLast()) {

			person._id = c.getInt(c.getColumnIndex("_id"));
			person.background = c.getString(c.getColumnIndex("background"));
			person.icon = c.getString(c.getColumnIndex("icon"));
			person.name = c.getString(c.getColumnIndex("name"));
			person.gender = c.getString(c.getColumnIndex("gender"));
			person.nation = c.getString(c.getColumnIndex("nation"));
			person.age = c.getInt(c.getColumnIndex("age"));
			person.followings = c.getInt(c.getColumnIndex("followings"));
			person.followers = c.getInt(c.getColumnIndex("followers"));
			person.info = c.getString(c.getColumnIndex("info"));

			if (c.isLast())
				break;
		}
		c.close();
		return person;
	}

	// 查询登陆用户的微博
	public List<Tweet> query1() {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		String username = SignIn.username;
		Cursor c = db.rawQuery("SELECT * FROM tweet where name like ?",
				new String[] { username });
		while (c.moveToNext()) {
			Tweet tweet = new Tweet();
			tweet._id = c.getInt(c.getColumnIndex("_id"));
			tweet.name = c.getString(c.getColumnIndex("name"));
			String tweet0 = c.getString(c.getColumnIndex("weibo"));
			// 正则表达式替换字符
			Pattern pattern = Pattern.compile("我操|你妈|卧槽|你妹");
			Matcher matcher = pattern.matcher(tweet0.toString());
			StringBuffer sbr = new StringBuffer();
			while (matcher.find()) {
				matcher.appendReplacement(sbr, "么么哒");
			}
			matcher.appendTail(sbr);
			tweet.tweet = sbr.toString();
			tweets.add(tweet);
		}
		c.close();
		return tweets;
	}
	
	// 查询你搜索的人的微博
	public List<Tweet> query10() {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		String theusername = Search.theusername;
		Cursor c = db.rawQuery("SELECT * FROM tweet where name like ?",
				new String[] { theusername });
		while (c.moveToNext()) {
			Tweet tweet = new Tweet();
			tweet._id = c.getInt(c.getColumnIndex("_id"));
			tweet.name = c.getString(c.getColumnIndex("name"));
			String tweet0 = c.getString(c.getColumnIndex("weibo"));
			// 正则表达式替换字符
			Pattern pattern = Pattern.compile("我操|你妈|卧槽|你妹");
			Matcher matcher = pattern.matcher(tweet0.toString());
			StringBuffer sbr = new StringBuffer();
			while (matcher.find()) {
				matcher.appendReplacement(sbr, "么么哒");
			}
			matcher.appendTail(sbr);
			tweet.tweet = sbr.toString();
			tweets.add(tweet);
		}
		c.close();
		return tweets;
	}

	// 查询你所搜索的那个用户的个人信息
	public Person query2() {
		Person person = new Person();
		String theusername = Search.theusername;
		Cursor c = db.rawQuery("select * from person where name like ?",
				new String[] { theusername });
		while (c.moveToLast()) {

			person._id = c.getInt(c.getColumnIndex("_id"));
			person.background = c.getString(c.getColumnIndex("background"));
			person.icon = c.getString(c.getColumnIndex("icon"));
			person.name = c.getString(c.getColumnIndex("name"));
			person.gender = c.getString(c.getColumnIndex("gender"));
			person.nation = c.getString(c.getColumnIndex("nation"));
			person.age = c.getInt(c.getColumnIndex("age"));
			person.followings = c.getInt(c.getColumnIndex("followings"));
			person.followers = c.getInt(c.getColumnIndex("followers"));
			person.info = c.getString(c.getColumnIndex("info"));

			if (c.isLast())
				break;
		}
		c.close();
		return person;
	}

	// 查询你所搜索的那个用户的微博
	public List<Tweet> query3() {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		String theusername = Search.theusername;
		Cursor c = db.rawQuery("SELECT * FROM tweet where name like ?",
				new String[] { theusername });
		while (c.moveToNext()) {
			Tweet tweet = new Tweet();
			tweet._id = c.getInt(c.getColumnIndex("_id"));
			tweet.name = c.getString(c.getColumnIndex("name"));
			String tweet0 = c.getString(c.getColumnIndex("weibo"));
			// 正则表达式替换字符
			Pattern pattern = Pattern.compile("我操|你妈|卧槽|你妹");
			Matcher matcher = pattern.matcher(tweet0.toString());
			StringBuffer sbr = new StringBuffer();
			while (matcher.find()) {
				matcher.appendReplacement(sbr, "么么哒");
			}
			matcher.appendTail(sbr);
			tweet.tweet = sbr.toString();
			tweets.add(tweet);
		}
		c.close();
		return tweets;
	}

	// 查询你所关注的人
	public List<Fan> query4() {
		ArrayList<Fan> fans = new ArrayList<Fan>();
		String username = SignIn.username;
		Cursor c = db.rawQuery("SELECT * FROM fan where name like ?",
				new String[] { username });
		while (c.moveToNext()) {
			Fan fan = new Fan();
			fan._id = c.getInt(c.getColumnIndex("_id"));
			fan.name = c.getString(c.getColumnIndex("name"));
			fan.ilike = c.getString(c.getColumnIndex("ilike"));
			fans.add(fan);
		}
		c.close();
		return fans;
	}
	
	// 查询你搜索的那个人所关注的人
	public List<Fan> query40() {
		ArrayList<Fan> fans = new ArrayList<Fan>();
		String theusername = Search.theusername;
		Cursor c = db.rawQuery("SELECT * FROM fan where name like ?",
				new String[] { theusername });
		while (c.moveToNext()) {
			Fan fan = new Fan();
			fan._id = c.getInt(c.getColumnIndex("_id"));
			fan.name = c.getString(c.getColumnIndex("name"));
			fan.ilike = c.getString(c.getColumnIndex("ilike"));
			fans.add(fan);
		}
		c.close();
		return fans;
	}

	// 查询关注你的人（你的粉丝）
	public List<Fan> query5() {
		ArrayList<Fan> fans = new ArrayList<Fan>();
		String username = SignIn.username;
		Cursor c = db.rawQuery("SELECT * FROM fan where ilike like ?",
				new String[] { username });
		while (c.moveToNext()) {
			Fan fan = new Fan();
			fan._id = c.getInt(c.getColumnIndex("_id"));
			fan.name = c.getString(c.getColumnIndex("name"));
			fan.ilike = c.getString(c.getColumnIndex("ilike"));
			fans.add(fan);
		}
		c.close();
		return fans;
	}
	
	// 查询关注你搜索的那个人的人（那个人的粉丝）
		public List<Fan> query50() {
			ArrayList<Fan> fans = new ArrayList<Fan>();
			String theusername = Search.theusername;
			Cursor c = db.rawQuery("SELECT * FROM fan where ilike like ?",
					new String[] { theusername });
			while (c.moveToNext()) {
				Fan fan = new Fan();
				fan._id = c.getInt(c.getColumnIndex("_id"));
				fan.name = c.getString(c.getColumnIndex("name"));
				fan.ilike = c.getString(c.getColumnIndex("ilike"));
				fans.add(fan);
			}
			c.close();
			return fans;
		}

	/**
	 * query all persons, return cursor
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheCursor() {
		Cursor c = db.rawQuery("SELECT * FROM person", null);
		return c;
	}

	public Cursor queryTheCursor0() {
		Cursor c = db.rawQuery("SELECT * FROM tweet", null);
		return c;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}