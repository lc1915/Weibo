package lll.weibo;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

	static String ReadTextFromStream(InputStream is) {
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			StringBuilder sb = new StringBuilder("");
			String line = null;
			while((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	static void SaveToFile(String path, String content) {
		
		try {
			FileWriter fw = new FileWriter(path, false);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}