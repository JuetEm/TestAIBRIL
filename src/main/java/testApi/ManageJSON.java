package testApi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class ManageJSON {

	public ManageJSON() {
		// TODO Auto-generated constructor stub
	}

	static int a = 0;;
	int k = 0;

	void l(String message) {
		System.out.println("In " + "num." + k + " Progress : " + message);
		k++;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ManageJSON t = new ManageJSON();

		JsonParser parser = new JsonParser();

		List<String> jsonList = new ArrayList<String>();
		List<String> fieldList = new ArrayList<String>();
		List<String> outputFileName = new ArrayList<String>();
		String tempFileName = null;
		String[] escapeExtention = null;
		String output = null;

		int qNum = 1;

		String outputFile = null;
		String path = "C:\\json";
		File dirFile = new File(path);
		File[] fileList = dirFile.listFiles();
		for (File tempFile : fileList) {
			if (tempFile.isFile()) {
				String tempPath = tempFile.getParent();
				tempFileName = tempFile.getName();
				// t.l(tempFileName);
				escapeExtention = tempFileName.split("\\.");
				outputFile = escapeExtention[0].substring(0, 5);
				t.l("outputFile : " + outputFile);
				// System.out.println(escapeExtention.length);
				// System.out.println("1. "+escapeExtention[0]+", 2. "+escapeExtention[1]);
				String fieldName = tempFileName.substring(6, escapeExtention[0].length());
				String fileLocation = tempPath + "\\" + tempFileName;
				jsonList.add(fileLocation);
				fieldList.add(fieldName);
				outputFileName.add(outputFile);
				t.l(tempPath + "\\" + tempFileName + ", field : " + fieldName);
			}
		}

		try {
			String fn = null;
//			output = "C:\\Users\\Juet\\Desktop\\업무(PC)\\study\\사장님 연구 과제\\SKStudy\\json" + "\\" + fn + ".txt";
			output = null;

//			FileWriter fw = new FileWriter(output);
//			BufferedWriter bw = new BufferedWriter(fw);
//			StringBuilder sb = new StringBuilder();
			FileWriter fw = null;
			BufferedWriter bw = null;
			StringBuilder sb = new StringBuilder();

			for (int j = 0; j < jsonList.size(); j++) {
				a++;
				if (qNum == 6) {
					qNum = 1;
				}
				fn = outputFileName.get(j);
				output = "C:\\Users\\Juet\\Desktop\\업무(PC)\\study\\사장님 연구 과제\\SKStudy\\json" + "\\" + fn+"-"+qNum+".Question " + ".txt";
				if (a % 3 == 0 || a== 1) {
					t.l("a value check : "+a);
					fw = new FileWriter(output);
					bw = new BufferedWriter(fw);
//					sb = new StringBuilder();
				}
				Object obj = parser.parse(new FileReader(jsonList.get(j)));
				t.l("fn : "+fn+", output : "+output);
				// t.l("jsonList ."+j+" : "+jsonList.get(j));
				JsonObject jsonObj = (JsonObject) obj;
				// t.l(j+". "+jsonObj.toString());
				JsonArray jsonArray = (JsonArray) jsonObj.getAsJsonArray(fieldList.get(j));
				 t.l("fieldList : "+j+". "+fieldList.get(j));
				// t.l("jsonArray Size : "+jsonArray.size());
				int o = 0;
				int s = 1;
				for (int l = 0; l < jsonArray.size(); l++) {
					JsonObject jObj = jsonArray.get(l).getAsJsonObject();

					switch (fieldList.get(j)) {
					case "categories":
						if(o==0) {
							sb.append("\n");
							o++;
						}
						String score = jObj.get("score").toString();
						String label = jObj.get("label").toString();
						t.l("categoriesScore : " + score + ", label : " + label);
						sb.append(s+". "+"Categories - score : " + score + ", label : " + label + "\n");
						s++;
						break;
					case "concepts":
						if(o==0) {
							sb.append("\n");
							o++;
						}
						String conceptsText = jObj.get("text").toString();
						String conceptsRelevance = jObj.get("relevance").toString();
						String conceptsDbpedia_resource = jObj.get("dbpedia_resource").toString();
						t.l("conceptsText : " + conceptsText + ", conceptsRelevance : " + conceptsRelevance
								+ ", conceptsDbpedia_resource : " + conceptsDbpedia_resource);
						sb.append(s+". "+"Concepts - text : " + conceptsText + ", relevance : " + conceptsRelevance
								+ ", dbpedia_resource : " + conceptsDbpedia_resource + "\n");
						s++;
						break;
					case "keywords":
						if(o==0) {
							sb.append("\n");
							o++;
						}
						String keywordsText = jObj.get("text").toString();
						String keywordsRelevance = jObj.get("relevance").toString();
						t.l("keywordsText : " + keywordsText + ", keywordsRelevance : " + keywordsRelevance);
						sb.append(
								s+". "+"Keywords - text : " + keywordsText + ", relevance : " + keywordsRelevance + "\n");
						s++;
						break;
					}
					// t.l("elements : "+elements);
				}
				t.l(a+". sb String : "+sb.toString());
				if (a % 3 == 0) {
					t.l("a value : " + a);
					t.l("qNum : "+qNum);
					qNum++;
					bw.write(sb.toString());
					bw.newLine();
					bw.close();
					
					sb = new StringBuilder();
				}
			}
			// bw.write(sb.toString());
			// bw.newLine();
			// bw.close();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
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

}
