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
		// ��ΪgetWritableDatabase�ڲ�������mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// ����Ҫȷ��context�ѳ�ʼ��,���ǿ��԰�ʵ����DBManager�Ĳ������Activity��onCreate��
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
		db.beginTransaction(); // ��ʼ����
		try {
			for (Person person : persons) {
				db.execSQL(
						"INSERT INTO person VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						new Object[] { person.background, person.icon,
								person.name, person.gender, person.nation,
								person.age, person.followings,
								person.followers, person.info });
			}
			db.setTransactionSuccessful(); // ��������ɹ����
		} finally {
			db.endTransaction(); // ��������
		}
	}

	public void add0(List<Tweet> tweets) {
		db.execSQL("CREATE TABLE IF NOT EXISTS tweet"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,name NVARCHAR,weibo TEXT)");
		db.beginTransaction(); // ��ʼ����
		try {
			for (Tweet tweet : tweets) {
				db.execSQL("INSERT INTO tweet VALUES(null, ?, ?)",
						new Object[] { tweet.name, tweet.tweet });
			}
			db.setTransactionSuccessful(); // ��������ɹ����
		} finally {
			db.endTransaction(); // ��������
		}
	}

	public void add1(List<Sign> signs) {
		db.execSQL("CREATE TABLE IF NOT EXISTS sign"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, username NVARCHAR,password TEXT)");
		db.beginTransaction(); // ��ʼ����
		try {
			for (Sign sign : signs) {
				db.execSQL("INSERT INTO sign VALUES(null, ?, ?)", new Object[] {
						sign.username, sign.password });
			}
			db.setTransactionSuccessful(); // ��������ɹ����
		} finally {
			db.endTransaction(); // ��������
		}
	}

	public void add2(List<Fan> fans) {
		db.execSQL("CREATE TABLE IF NOT EXISTS fan"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name NVARCHAR,ilike TEXT)");
		db.beginTransaction(); // ��ʼ����
		try {
			for (Fan fan : fans) {
				db.execSQL("INSERT INTO fan VALUES(null, ?, ?)", new Object[] {
						fan.name, fan.ilike });
			}
			db.setTransactionSuccessful(); // ��������ɹ����
		} finally {
			db.endTransaction(); // ��������
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

	// ��ѯ��½�û���΢��
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
			// ������ʽ�滻�ַ�
			Pattern pattern = Pattern.compile("�Ҳ�|����|�Բ�|����");
			Matcher matcher = pattern.matcher(tweet0.toString());
			StringBuffer sbr = new StringBuffer();
			while (matcher.find()) {
				matcher.appendReplacement(sbr, "ôô��");
			}
			matcher.appendTail(sbr);
			tweet.tweet = sbr.toString();
			tweets.add(tweet);
		}
		c.close();
		return tweets;
	}
	
	// ��ѯ���������˵�΢��
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
			// ������ʽ�滻�ַ�
			Pattern pattern = Pattern.compile("�Ҳ�|����|�Բ�|����");
			Matcher matcher = pattern.matcher(tweet0.toString());
			StringBuffer sbr = new StringBuffer();
			while (matcher.find()) {
				matcher.appendReplacement(sbr, "ôô��");
			}
			matcher.appendTail(sbr);
			tweet.tweet = sbr.toString();
			tweets.add(tweet);
		}
		c.close();
		return tweets;
	}

	// ��ѯ�����������Ǹ��û��ĸ�����Ϣ
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

	// ��ѯ�����������Ǹ��û���΢��
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
			// ������ʽ�滻�ַ�
			Pattern pattern = Pattern.compile("�Ҳ�|����|�Բ�|����");
			Matcher matcher = pattern.matcher(tweet0.toString());
			StringBuffer sbr = new StringBuffer();
			while (matcher.find()) {
				matcher.appendReplacement(sbr, "ôô��");
			}
			matcher.appendTail(sbr);
			tweet.tweet = sbr.toString();
			tweets.add(tweet);
		}
		c.close();
		return tweets;
	}

	// ��ѯ������ע����
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
	
	// ��ѯ���������Ǹ�������ע����
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

	// ��ѯ��ע����ˣ���ķ�˿��
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
	
	// ��ѯ��ע���������Ǹ��˵��ˣ��Ǹ��˵ķ�˿��
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