package lll.weibo;

public class Person {
	public int _id;
	public String background;
	public String icon;
	public String name;
	public String gender;
	public String nation;
	public int age;
	public int followings;
	public int followers;
	public String info;
	public String username;
	public String password;

	public Person() {
	}

	public Person(String background, String icon, String name, String gender,
			String nation, int age, int followings, int followers, String info,String username,String password) {
		this.background = background;
		this.icon = icon;
		this.name = name;
		this.gender = gender;
		this.nation = nation;
		this.age = age;
		this.followings = followings;
		this.followers = followers;
		this.info = info;
		this.username=username;
		this.password=password;
	}
}