package testApi;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class Logger {

	static int k = 0;
	boolean messageSwitch;

	String identifier;
	static String log = "";
	String logDirectory;

	public Logger() {
		// TODO Auto-generated constructor stub
		// this.k = 0;
		this.messageSwitch = true;
		// this.log = "";
	}

	public Logger(boolean message) {
		// this.k = 0;
		this.messageSwitch = message;
		// this.log = "";
	}

	public Logger(boolean message, String identifier) {
		// this.k = 0;
		this.messageSwitch = message;
		// this.log = "";
		this.identifier = identifier;
	}

	/**
	 * @param dir
	 * 
	 *            생성자를 사용해서 동적으로 로그파일을 생성해보려 했으나 NullPointerException을 뱉으며 생성되지 않는다.
	 */
	public Logger(String identifier) {
		this.identifier = identifier;
		// logFile(log, logDirectory);
	}

	public void l(String message) {
		if (messageSwitch) {
			System.out.println(k + ". log : " + message);
			// k++;
		}
		log += k + ". " + message + "\n\r";
		// TestApilog += k+". "+message+"\n\r";
		k++;
	}

	public void logFile(String log, String resultDirectory) {
		try {
			String fileName = generateFileName();
			BufferedWriter bwr = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(resultDirectory + fileName, true), "utf-8"));
			bwr.write(log + "\r\n");
			bwr.flush();
			bwr.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String generateFileName() {
		// +"\\"+System.getProperties().getProperty("user.name")+"_"++".txt"
		int min, max, randomNum;
		String filename = "";
		String time = "";
		String username = System.getProperties().getProperty("user.name");
		long timeStamp = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd HH-mm");
		time = simpleDateFormat.format(timeStamp);
		Random random = new Random();
		min = 0;
		max = 999999999;
		randomNum = random.nextInt(max - min + 1) + min;
		filename = "\\" + time + "_" + username + "_" + randomNum + "_" + identifier + ".json";
		return filename;
	}

}
