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
import java.util.HashMap;
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
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.ListModelsResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.RelationsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SentimentOptions;

public class TestApi {

	public TestApi() {
		// TODO Auto-generated constructor stub
	}

	String resultDir = "C:\\Users\\Juet\\Desktop\\업무(PC)\\study\\사장님 연구 과제\\SKStudy\\textResult";
	String logDir = "C:\\Users\\Juet\\Desktop\\업무(PC)\\study\\사장님 연구 과제\\SKStudy\\log";

	// Logger t = new Logger(false, "classMethod-T");
	// Logger g = new Logger(false, "classMethod-G");

	Logger t = new Logger(false);
	Logger g = new Logger(false);
	Logger n = new Logger(false);
	Logger r = new Logger(false);

	static String TestApilog = "";

	List<FeaturesClass> featuresClassValueList = new ArrayList<FeaturesClass>();
	FeaturesClass fc = null;

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Logger t = new Logger(false, "insideMain-T");
		TestApi d = new TestApi();

		/*
		 * 동적으로 코드를 로그 파일을 생성해 보려고 했으나 메인 메서드 내부의 로그를 파일로 옮긴는 것은 성공했지만 메인메서드 밖에 메서드에서
		 * 호출한 로그클래스로는 파일은 생성되지만 내용은 찍히지 않는 현상을 경험하고 있다. 안드로이드라면
		 */
		// d.logStart();
		// List<String> contents =
		// d.getFiles("C:\\Users\\Juet\\Desktop\\업무(PC)\\study\\사장님 연구
		// 과제\\SKStudy\\text");
		List<String> contents = d
				.getFiles("C:\\Users\\Juet\\Desktop\\업무(PC)\\study\\사장님 연구 과제\\SKStudy\\사장님Sample\\106_테스트 데이터_170914");

		// bluemixID : sbkyo@naver.com
		// NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
		// NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
		// "4dd8d745-fd2c-4436-988c-9adf62df123f",
		// "5MV2ODutICGc");

		// bluemixID : sbkyo88@gmail.com
		NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
				NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, "cf0a651e-5371-458c-acbc-837bdb00f58f",
				"0MsDxmo4sgV5");

		ListModelsResults models = service.getModels().execute();
		t.l("models : " + models);
		// System.out.println(models);
		String modelId = "" + d.getWKSModels(models).get("model_0").get("modelId");
		t.l("modelId : " + modelId);
		// String text = "IBM is an American multinational technology company
		// headquartered in Armonk, New York, United States, with operations in over 170
		// countries.";

		// EntitiesOptions entitiesOptions = new
		// EntitiesOptions.Builder().model(modelId).emotion(true).sentiment(true).limit(2).build();
		EntitiesOptions entitiesOptions = new EntitiesOptions.Builder().model(modelId).build();

		RelationsOptions relationsOptions = new RelationsOptions.Builder().model(modelId).build();

		KeywordsOptions keywordsOptions = new KeywordsOptions.Builder().emotion(true).sentiment(true).limit(2).build();

		SentimentOptions sentimentOptions = new SentimentOptions.Builder().build();

		CategoriesOptions categoriesOptions = new CategoriesOptions();

		ConceptsOptions conceptsOptions = new ConceptsOptions.Builder().limit(8).build();

		Features features = new Features.Builder().entities(entitiesOptions).relations(relationsOptions)
				.keywords(keywordsOptions).categories(categoriesOptions).concepts(conceptsOptions)
				.sentiment(sentimentOptions).build();
		Features entityFeatures = new Features.Builder().entities(entitiesOptions).build();

		AnalyzeOptions parameters = null;
		AnalyzeOptions entityParameters = null;

		AnalysisResults response = null;
		AnalysisResults entityResponse = null;

		JSONArray jArray = new JSONArray();
		try {
			for (int q = 0; q < contents.size(); q++) {
				parameters = new AnalyzeOptions.Builder().text(contents.get(q)).features(features)
						.returnAnalyzedText(true).build();
				entityParameters = new AnalyzeOptions.Builder().language("en").text(contents.get(q))
						.features(entityFeatures).returnAnalyzedText(true).build();

				response = service.analyze(parameters).execute();
				entityResponse = service.analyze(entityParameters).execute();
				// t.l("entityResponse : " + entityResponse);
				// t.l("contents : "+contents.get(q)+"\nresponse : "+response);
				JSONObject entityObject = new JSONObject(entityResponse);
				org.json.JSONArray entityJArray = entityObject.getJSONArray("entities");
				JSONObject jObj = new JSONObject(response);
				jObj.put("entities", entityJArray);
				jArray.add(jObj);

				/* NLU INPUT 각각 FeaturesClass featuresValue List 생성 */
				// d.fc = new FeaturesClass(jArray);
				// d.featuresValue.add(d.fc);
			}
			for (int w = 0; w < jArray.size(); w++) {
				d.fc = new FeaturesClass(jArray.get(w));
				d.featuresClassValueList.add(d.fc);
			}
			// d.fc = new FeaturesClass(jArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		t.l("{\"jArray raw contents\":" + jArray + "}");
		List<String> contentsList = d.jsonToText(jArray);
		t.l("listSize: " + contentsList.size());
		for (String str : contentsList) {
			t.l(str);
		}
		t.l("FeaturesClass Value Check\n" + d.classValueCheck(d.featuresClassValueList));
		t.logFile(Logger.log, d.logDir);
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
	public List<String> jsonToText(JSONArray jArray) {
		// t.l("jsonArray in method : \n"+jArray);

		int listCount = 1;
		List<String> contents = new ArrayList<String>();
		try {

			List<String> jsonNames; /* 1차 return값 JSONArray 에서 받아온 JSONObject들 이름 - feature names 들이어야 정상 */

			String[] objNames; /* 1차 리턴값에서 얻어낸 JSONObject의 field 값 - text, score, relevance 등등 리턴되어야 정상 */
			String[] jdoNames; /*
								 * 1차 리턴값에서 얻어낸 JSONObject의 field 값이 품고 있는 JSONObject 혹은 JSONArray 값의 이름들 - 현
								 * 자료에는 없는 것으로 보입...확인 필요
								 */

			String[] aObjNames; /*
								 * 1차 리턴값에서 얻어낸 JSONArray의 JSONObject의 이름들 - entity:count 등등, concept:text 등등,
								 * relations:type 등등...
								 */
			String[] jdobNames; /*
								 * 1차 리턴값에서 얻어낸 JSONArray의 JSONObject들이 품고 있는 JSONObject 혹은 JSONArray 값의 이름들 -
								 * relations:argument:entities, entities:diambiguation:subtype...
								 */
			String[] jdobjNames; /*
									 * 1차 리턴값에서 얻어낸 JSONArray의 JSONObject들이 품고 있는 JSONObject 혹은 JSONArray 값의 이름들 중
									 * JSONArray가 품고 있는 field 이름 - relations:argument:entities:text 등
									 */
			String[] jDNames; /*
								 * 1차 리턴값에서 얻어낸 JSONArray의 JSONObject 들이 품고있는 JSONObject 혹은 JSONArray 값의 이름들 중
								 * JSONObject가 품고 있는 field 이름 - entities:disambiguation:subtype 등
								 */

			for (int f = 0; f < jArray.size(); f++) {
				JSONObject jsonObject = (JSONObject) jArray.get(f);
				t.l(f + ". jsonObject in method : " + jsonObject + "\n\r");
				jsonNames = new ArrayList<String>();
				for (int s = 0; s < JSONObject.getNames(jsonObject).length; s++) {
					jsonNames.add(JSONObject.getNames(jsonObject)[s]);

					r.l("features name - " + s + " : " + jsonNames.get(s));
				}
				// List<String> jsonChild = new ArrayList<String>();
				for (String jNames : jsonNames) {
					t.l("\n\r" + f + ". " + "jsonNames in method: " + jNames + "\n\r");
					if (jNames != null && (jNames.equals("language") || jNames.equalsIgnoreCase("analyzedText"))) {
						r.l(jNames + "'s field names : " + jsonObject.getString(jNames));
						if (!jNames.equals("analyzedText") && jsonNames.contains("entities")) {
							contents.add(jNames + " - " + jsonObject.getString(jNames) + " [and en(Entity)]");
						} else {
							contents.add(jNames + " - " + jsonObject.getString(jNames));
						}
						continue;
					}
					try {
						JSONObject obj = jsonObject.getJSONObject(jNames);
						t.l(f + "-1. obj : " + obj);
						t.l("getNames Length : " + JSONObject.getNames(obj).length + ", getNames name[0] : "
								+ JSONObject.getNames(obj)[0]);
						objNames = JSONObject.getNames(obj);
						String row = "";
						for (int g = 0; g < JSONObject.getNames(obj).length; g++) {
							t.l(g + ". objNAmes : " + objNames[g]);
							String key = objNames[g];
							t.l(f + "-1. obj : " + obj.get(JSONObject.getNames(obj)[g]));
							String value = "" + obj.get(JSONObject.getNames(obj)[g]);
							t.l(f + "-1-" + listCount + ". key : " + key + ", value : " + value);

							r.l(jNames + "'s field name : " + objNames[g]);

							if (value.startsWith("[")) {
								org.json.JSONArray jDArray = obj.getJSONArray(key);
								n.l("1-jDArray : " + jDArray);
								r.l("1번 영역 JSONArray Check");
							} else if (value.startsWith("{")) {
								JSONObject jDObject = obj.getJSONObject(key);
								n.l("1-jDObject : " + jDObject);
								jdoNames = JSONObject.getNames(jDObject);
								String jdoKey = "";
								String jdoValue = "";
								for (int m = 0; m < jdoNames.length; m++) {

									r.l(jNames + "'s field name : " + objNames[g] + "'s content names : "
											+ jdoNames[m]);

									jdoKey = jdoNames[m];
									jdoValue = "" + jDObject.get(jdoNames[m]);
								}
							}
							// contents.add(key+" : "+value);
							row += key + " : " + value + ", ";
							listCount++;
						}
						String editRow = row.substring(0, row.length() - 2);
						if (jNames.equals("usage") && jsonNames.contains("entities")) {
							contents.add(jNames + " - " + editRow + " [and Entity(en)]");
						} else {
							contents.add(jNames + " - " + editRow);
						}

					} catch (JSONException e) {
						// e.printStackTrace();
						org.json.JSONArray obj = jsonObject.getJSONArray(jNames);
						t.l(f + "-2. obj : " + obj);
						for (int g = 0; g < obj.length(); g++) {
							JSONObject aObj = obj.getJSONObject(g);
							// t.l(f + "-2. aObj length : " + aObj.length());
							String row = "";
							aObjNames = JSONObject.getNames(aObj);
							for (int z = 0; z < aObj.length(); z++) {
								// aObjNames = JSONObject.getNames(aObj);

								r.l(jNames + "'s field names : " + aObjNames[z]);

								String key = aObjNames[z];
								t.l(z + ". aObjNames : " + aObjNames[z]);
								String value = "" + aObj.get(JSONObject.getNames(aObj)[z]);
								t.l(f + "-2. aObj : " + aObj.get(JSONObject.getNames(aObj)[z]));
								t.l(f + "-2" + "-" + listCount + ". key : " + key + ", value : " + value);
								String jDValue = "";
								String jDKey = "";
								if (value.startsWith("[")) {
									org.json.JSONArray jDArray = aObj.getJSONArray(key);
									n.l("2-key : " + key + ", jDArray : " + jDArray);
									for (int x = 0; x < jDArray.length(); x++) {
										JSONObject jdobject = jDArray.getJSONObject(x);
										jdobNames = JSONObject.getNames(jdobject);
										String jdkey = "";
										String jdvalue = "";
										for (int c = 0; c < jdobNames.length; c++) {

											r.l(jNames + "'s field name : " + aObjNames[z] + "'s content names : "
													+ jdobNames[c]);

											jdkey = jdobNames[c];
											jdvalue = "" + jdobject.get(jdobNames[c]);
											if (jdvalue.startsWith("[")) {
												org.json.JSONArray jdarray = jdobject.getJSONArray(jdobNames[c]);
												for (int v = 0; v < jdarray.length(); v++) {
													JSONObject jdobj = jdarray.getJSONObject(v);
													jdobjNames = JSONObject.getNames(jdobj);
													String jdobjKey = "";
													String jdobjValue = "";
													for (int b = 0; b < jdobjNames.length; b++) {

														r.l(jNames + "'s field name : " + aObjNames[z]
																+ "'s content names : " + jdobNames[c]
																+ "'s field name : " + jdobjNames[b]);

														jdobjKey = jdobjNames[b];
														jdobjValue = "" + jdobj.get(jdobjNames[b]);
													}
												}
											}
										}
									}
								} else if (value.startsWith("{")) {
									JSONObject jDObject = aObj.getJSONObject(key);
									n.l("2-jDObject : " + jDObject);
									jDNames = JSONObject.getNames(jDObject);
									for (int w = 0; w < jDNames.length; w++) {

										r.l(jNames + "'s field name : " + aObjNames[z] + "'s content names : "
												+ jDNames[w]);

										jDKey = jDNames[w];
										jDValue = "" + jDObject.get(jDNames[w]);
										n.l("2-jDKey : " + jDKey + ", jDValue : " + jDValue);
										if (jDValue.startsWith("[")) {
											org.json.JSONArray jdarray = jDObject.getJSONArray(jDKey);
											for (int n = 0; n < jdarray.length(); n++) {
												/*
												 * 값이 NONE 인 상태여서 자료형이 어떤 식으로 나오는지 알 수가 없다. 자료형 확인 후 해결해야 할 듯 아마도
												 * JSONObject 이거나 JSONArray 겠지...
												 */
											}
										}
									}
								}
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
			// e.printStackTrace();
		}
		return contents;
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

	@SuppressWarnings("rawtypes")
	public HashMap<String, HashMap> getWKSModels(ListModelsResults models) {
		HashMap<String, HashMap> jNameValueMap = new HashMap<String, HashMap>();
		HashMap<String, String> numJNameValueMap = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(models);
			org.json.JSONArray jsonArray = jsonObject.getJSONArray("models");
			for (int j = 0; j < jsonArray.length(); j++) {
				JSONObject jObject = jsonArray.getJSONObject(j);
				g.l("jObject : " + jObject);
				g.l("jObject model id : " + jObject.get("modelId"));
				String[] jNames = JSONObject.getNames(jObject);
				String[] jNameValues = new String[jNames.length];
				for (int h = 0; h < jNames.length; h++) {
					jNameValues[h] = "" + jObject.get(jNames[h]);
					numJNameValueMap.put(jNames[h], jNameValues[h]);
					g.l(h + ". jNames : " + jNames[h] + ", jValues : " + jNameValues[h]);
				}
				jNameValueMap.put("model_" + j, numJNameValueMap);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jNameValueMap;
	}

	public String classValueCheck(List<FeaturesClass> featuresClassValueList) {
		StringBuilder sb = new StringBuilder();

		for (int a = 0; a < featuresClassValueList.size(); a++) {
			sb.append("\r\n");
			String anaylzedText = featuresClassValueList.get(a).getAnalyzed_text();
			sb.append("ANALYZED_TEXT\n");
			sb.append("anaylzedText\n" + anaylzedText);
			String language = featuresClassValueList.get(a).getLanguage().getLanguage().get(0);
			sb.append("LANGUAGE\n");
			sb.append("language : " + language + "\n");
			int usage = featuresClassValueList.get(a).getUsage().getFeatures().get(0);
			sb.append("USAGE\n");
			sb.append("usage : " + usage + "\n");
			float sentiment_document_score = featuresClassValueList.get(a).getSentiment().getDocument().getScore();
			sb.append("SENTIMENT\n");
			sb.append("sentiment_document_score : " + sentiment_document_score + "\n");

			List<String> concepts_dbpediaResource = featuresClassValueList.get(a).getConcepts().getDbpediaResource();
			List<String> concepts_text = featuresClassValueList.get(a).getConcepts().getText();
			List<Float> concepts_relevance = featuresClassValueList.get(a).getConcepts().getRelevance();
			sb.append("CONCEPTS\n");
			for (int s = 0; s < concepts_dbpediaResource.size(); s++) {
				sb.append("concepts_dbpediaResource : " + concepts_dbpediaResource.get(s) + ", concepts_text : "
						+ concepts_text.get(s) + ", concepts_relevance : " + concepts_relevance.get(s) + "\n");
			}

			List<Integer> entities_count = featuresClassValueList.get(a).getEntities().getCount();
			List<String> entities_text = featuresClassValueList.get(a).getEntities().getText();
			List<String> entities_type = featuresClassValueList.get(a).getEntities().getType();
			List<Disambiguation> entities_disambiguation = featuresClassValueList.get(a).getEntities()
					.getDisambiguationList();
			sb.append("ENTITIES\n");
			for (int d = 0; d < entities_count.size(); d++) {
				sb.append("entities_count : " + entities_count.get(d) + ", entities_text : " + entities_text.get(d)
						+ ", entities_type : " + entities_type.get(d)
						/* indexOutOfBounds Exception */
						+ ", entities_disambiguation : " + entities_disambiguation.get(d).getDisambiguationObj() + "\n");
			}

			List<Float> categories_score = featuresClassValueList.get(a).getCategories().getScore();
			List<String> categories_label = featuresClassValueList.get(a).getCategories().getLabel();
			sb.append("CATEGORIES\n");
			for (int f = 0; f < categories_score.size(); f++) {
				sb.append("categories_score : " + categories_score.get(f) + ", categories_label : "
						+ categories_label.get(f) + "\n");
			}

			List<String> relations_sentencss = featuresClassValueList.get(a).getRelations().getSentence();
			List<Float> relations_score = featuresClassValueList.get(a).getRelations().getScore();
			List<Arguments> relations_argumnets = featuresClassValueList.get(a).getRelations().getArgumentsList();
			List<String> relations_type = featuresClassValueList.get(a).getRelations().getType();
			List<rEntities> relations_arguments_entities = featuresClassValueList.get(a).getRelations().getArgument()
					.getEntitiesList();
			sb.append("RELATIONS\n");
			for (int g = 0; g < relations_sentencss.size(); g++) {
				sb.append("relations_sentencss : " + relations_sentencss.get(g) + ", relations_score : "
						+ relations_score.get(g) + ", relations_type : " + relations_type.get(g)
						+ ", relations_argumnets : " + relations_argumnets.get(g).getArgumentAry() + "\n");
				sb.append("relations_ARGUMENTS\n");
				for (int h = 0; h < relations_arguments_entities.size(); h++) {
					sb.append("relations_arguments_ENTITIES\n");
					sb.append(
							/* indexOutOfBounds Exception */
							// "relations_arguments_entities :
							// "+relations_arguments_entities.get(h).getrEntitiesAry()
							"relations_arguments_entities : " + featuresClassValueList.get(a).getRelations()
									.getArgumentsList().get(g).getEntitiesList().get(h).getrEntitiesAry() + "\n");
					/*
					 * relations_arguments_entities_text 와 relations_arguments_entities_type 이 문서
					 * 전반에 걸쳐 똑같은 현상
					 */
					sb.append("relations_arguments_entities_text : "
							+ featuresClassValueList.get(a).getRelations().getArgumentsList().get(g).getEntitiesList()
									.get(h).getText()
							+ ", relations_arguments_entities_type : " + featuresClassValueList.get(a).getRelations()
									.getArgumentsList().get(g).getEntitiesList().get(h).getType()
							+ "\n");

				}
			}
			sb.append("\r\n");
		}

		return sb.toString();
	}

	public void logStart() {
		t.logFile(Logger.log, logDir);
		g.logFile(Logger.log, logDir);
	}

}
