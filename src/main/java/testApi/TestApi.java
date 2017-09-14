package testApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.ConceptsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SentimentOptions;

public class TestApi {

	public TestApi() {
		// TODO Auto-generated constructor stub
	}

	Logger t = new Logger();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Logger t = new Logger();
		TestApi d = new TestApi();
//		List<String> contents = d.getFiles("C:\\Users\\Juet\\Desktop\\업무(PC)\\study\\사장님 연구 과제\\SKStudy\\text");
		List<String> contents = d.getFiles("C:\\Users\\Juet\\Desktop\\업무(PC)\\study\\사장님 연구 과제\\SKStudy\\사장님Sample\\106_테스트 데이터_170914");

		NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
				NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, "4dd8d745-fd2c-4436-988c-9adf62df123f",
				"5MV2ODutICGc");

		// ListModelsResults models = service.getModels().execute();
		// t.l("models : "+models);

		// String text = "IBM is an American multinational technology company
		// headquartered in Armonk, New York, United States, with operations in over 170
		// countries.";

		EntitiesOptions entitiesOptions = new EntitiesOptions.Builder().emotion(true).sentiment(true).limit(2).build();

		KeywordsOptions keywordsOptions = new KeywordsOptions.Builder().emotion(true).sentiment(true).limit(2).build();

		SentimentOptions sentimentOptions = new SentimentOptions.Builder().build();

		CategoriesOptions categoriesOptions = new CategoriesOptions();

		ConceptsOptions conceptsOptions = new ConceptsOptions.Builder().limit(50).build();

		Features features = new Features.Builder().entities(entitiesOptions).keywords(keywordsOptions)
				.categories(categoriesOptions).concepts(conceptsOptions).sentiment(sentimentOptions).build();

		AnalyzeOptions parameters = null;
		AnalysisResults response = null;

		JSONArray jArray = new JSONArray();
		for (int q = 0; q < contents.size(); q++) {
			parameters = new AnalyzeOptions.Builder().text(contents.get(q)).features(features).returnAnalyzedText(true)
					.build();
			response = service.analyze(parameters).execute();
			// t.l("contents : "+contents.get(q)+"\nresponse : "+response);
			JSONObject jObj = new JSONObject(response);
			jArray.add(jObj);
		}
		t.l("jArray raw contents : " + jArray);
		d.jsonToText(jArray);
	}

	public List<String> writeFiles(String dir, List<String> contents) {
		File file = new File(dir);
		try {
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			for (String str : contents) {
				wr.write(str);
			}
			wr.flush();
			wr.close();
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

		return null;
	}

	/**
	 * @param jArray
	 *            사실 단순히 문자열을 받는 향상된 스위치 문이나, 이프문을 사용헤서 걸러도 충분히 제이슨 파일을 간단히 다룰 수 있다.
	 *            다만 하드코딩을 벗어나서 코딩을 해보자는 생각에 이렇게 복잡한 중첩 반복문을 사용하게 되었다. 그다지 효율적인 코드라고
	 *            할 수 없다.
	 */
	public void jsonToText(JSONArray jArray) {
		// t.l("jsonArray in method : \n"+jArray);

		int listCount = 1;
		List<String> contents = new ArrayList<String>();
		try {
			for (int f = 0; f < jArray.size(); f++) {
				JSONObject jsonObject = (JSONObject) jArray.get(f);
				t.l(f + ". jsonObject in method : " + jsonObject + "\n\r");
				List<String> jsonNames = new ArrayList<String>();
				for (int s = 0; s < JSONObject.getNames(jsonObject).length; s++) {
					jsonNames.add(JSONObject.getNames(jsonObject)[s]);
				}
				// List<String> jsonChild = new ArrayList<String>();
				for (String jNames : jsonNames) {
					t.l("\n\r" + f + ". " + "jsonNames in method: " + jNames + "\n\r");
					if (jNames != null && (jNames.equals("language") || jNames.equalsIgnoreCase("analyzedText"))) {
						contents.add(jNames+" - "+jsonObject.getString(jNames));
						continue;
					}
					try {
						JSONObject obj = jsonObject.getJSONObject(jNames);
						t.l(f + "-1. obj : " + obj);
						t.l("getNames Length : " + JSONObject.getNames(obj).length + ", getNames name[0] : "
								+ JSONObject.getNames(obj)[0]);
						String[] objNames = JSONObject.getNames(obj);
						String row = "";
						for (int g = 0; g < JSONObject.getNames(obj).length; g++) {
							t.l(g + ". objNAmes : " + objNames[g]);
							String key = objNames[g];
							t.l(f + "-1. obj : " + obj.get(JSONObject.getNames(obj)[g]));
							String value = "" + obj.get(JSONObject.getNames(obj)[g]);
							t.l(f + "-1-" + listCount + ". key : " + key + ", value : " + value);
							// contents.add(key+" : "+value);
							row += key + " : " + value + ", ";
							listCount++;
						}
						String editRow = row.substring(0, row.length() - 2);
						contents.add(jNames + " - " + editRow);

					} catch (JSONException e) {
						e.printStackTrace();
						org.json.JSONArray obj = jsonObject.getJSONArray(jNames);
						t.l(f + "-2. obj : " + obj);
						for (int g = 0; g < obj.length(); g++) {
							JSONObject aObj = obj.getJSONObject(g);
							// t.l(f + "-2. aObj length : " + aObj.length());
							String row = "";
							for (int z = 0; z < aObj.length(); z++) {
								String[] aObjNames = JSONObject.getNames(aObj);
								String key = aObjNames[z];
								t.l(z + ". aObjNames : " + aObjNames[z]);
								String value = "" + aObj.get(JSONObject.getNames(aObj)[z]);
								t.l(f + "-2. aObj : " + aObj.get(JSONObject.getNames(aObj)[z]));
								t.l(f + "-2" + listCount + ". key : " + key + ", value : " + value);
								// contents.add(key+" : "+value);
								row += key + " : " + value + ", ";
							}
							String editRow = row.substring(0, row.length() - 2);
							contents.add(jNames + " - " + editRow);
						}
					}
				}
			} 
			for (String string : contents) {
				t.l("contents Check : " + string);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getFiles(String dirPath) {
		List<String> fileContentList = new ArrayList<String>();
		File dirFiles = new File(dirPath);
		File[] fileList = dirFiles.listFiles();

		// FileReader fr = null;
		BufferedReader br = null;

		String tempPath = null;
		String tempFileName = null;
		try {
			for (File file : fileList) {
				if (file.isFile()) {
					tempPath = file.getParent();
					tempFileName = file.getName();
					// fr = new FileReader(tempPath + "\\" + tempFileName);
					// br = new BufferedReader(fr);
					br = new BufferedReader(
							new InputStreamReader(new FileInputStream(tempPath + "\\" + tempFileName), "utf-8"));
					// fileContentList.add(fr.toString());
					// t.l("path : " + tempPath + "\\" + tempFileName);

					int c;
					String content = "";
					String replaceContent = "";
					while ((c = br.read()) != -1) {
						content += (char) c;
					}
					br.close();
					// t.l(tempFileName + " - content : " + content);
					if (content.contains("\"")) {
						replaceContent = content.replace("\"", "'");
					} else {
						replaceContent = content;
					}
					t.l(tempFileName + " - replaceContent : " + "\n\r" + replaceContent);
					fileContentList.add(replaceContent);
					// t.l("Location : " + tempPath + "\\" + tempFileName + ", contents : ");
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileContentList;
	}

}
