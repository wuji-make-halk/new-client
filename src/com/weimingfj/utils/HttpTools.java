package com.weimingfj.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.weimingfj.common.form.RequestDataForm;
import com.weimingfj.common.form.ResponseData;
import com.weimingfj.common.utils.JsonUtil;
import com.weimingfj.common.utils.MapUtils;

/***
 * Http 工具
 * 
 * @author lihw
 * @created 2015年10月12日 上午11:15:34
 *
 */
public class HttpTools {

	static final Charset UTF8 = Charset.forName("UTF-8");

	/**
	 * 普通表单的请求
	 * 
	 * @author lihw
	 * @created 2015年10月12日 下午12:36:48
	 *
	 * @param url
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public static String postCommonForm(String url, Map<String, String> form) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		buildCommonPost(post, form);
		CloseableHttpResponse resp = client.execute(post);
		try {
			return EntityUtils.toString(resp.getEntity());
		} finally {
			client.close();
		}
	}

	/**
	 * 文件表单上传
	 * 
	 * @author lihw
	 * @created 2015年10月12日 下午12:39:03
	 *
	 * @param url
	 * @param params
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public static String postMultipartForm(String url, Map<String, String> params, Map<String, File> files)
			throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		buildMutilpartPost(post, params, files);
		CloseableHttpResponse resp = client.execute(post);
		try {
			return EntityUtils.toString(resp.getEntity());
		} finally {
			client.close();
		}
	}

	/**
	 * 设置文件上传的请求表单
	 * 
	 * @author lihw
	 * @created 2015年10月12日 上午11:51:58
	 *
	 * @param post
	 * @param form
	 * @param files
	 * @return
	 */
	private static HttpPost buildMutilpartPost(HttpPost post, Map<String, String> form, Map<String, File> files) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setCharset(UTF8);
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		if (form != null && !form.isEmpty()) {
			Iterator<String> it = form.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = form.get(key);
				builder.addPart(key,
						new StringBody(value, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), UTF8)));
			}
		}
		if (files != null && !files.isEmpty()) {
			Iterator<String> it = files.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				File file = files.get(key);
				if (!file.exists()) {
					System.out.println("文件[paramName = " + key + "]不存在");
				} else if (file.isDirectory()) {
					System.out.println("文件[paramName = " + key + ", file = " + file.getAbsolutePath() + "]是一个文件夹");
				} else {
					builder.addPart(key, new FileBody(file));
				}
			}
		}
		post.setEntity(builder.build());
		return post;
	}

	/**
	 * 设置普遍的请求表达
	 * 
	 * @author lihw
	 * @created 2015年10月12日 上午11:51:42
	 *
	 * @param post
	 * @param commonForm
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static HttpPost buildCommonPost(HttpPost post, Map<String, String> commonForm)
			throws UnsupportedEncodingException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (commonForm != null && !commonForm.isEmpty()) {
			Iterator<String> it = commonForm.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = commonForm.get(key);
				params.add(new BasicNameValuePair(key, value));
			}
		}
		post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
		return post;
	}

	/**
	 * 读取请求内容
	 * 
	 * <pre>
	 * !!! 如果在调用request的getParameter(), getParameterValues(), getParameterMap(), getReader()等方法后,
	 * 再调用此方法时, 必然会报错. 因为此时的request.getInputStream()返回的必然为null值.
	 * 这是由于request.getInputStream()只能被读一次.
	 * </pre>
	 * 
	 * @author lihw
	 * @created 2015年10月12日 上午11:23:16
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getRequestBody(HttpServletRequest request) throws Exception {
		ServletInputStream sis = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(sis, UTF8));
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	/**
	 * 判断上传的是否是Mutilpart表单
	 * 
	 * @author lihw
	 * @created 2015年10月12日 上午10:53:41
	 *
	 * @param request
	 * @return
	 */
	public static boolean isMultipartRequest(HttpServletRequest request) {
		return ServletFileUpload.isMultipartContent(request);
	}

	/**
	 * 获取请求参数
	 * 
	 * @author lihw
	 * @created 2015年10月12日 上午11:08:00
	 *
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getReqeustFormParams(HttpServletRequest request) throws Exception {
		if (isMultipartRequest(request)) {
			return getMultipartFormRequestParams(request);
		} else {
			return getCommonFormRequestParams(request);
		}
	}

	/**
	 * 获取普通表单的参数
	 * 
	 * @author lihw
	 * @created 2015年10月12日 上午11:13:32
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static Map<String, Object> getCommonFormRequestParams(HttpServletRequest request) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<?, ?> reqMap = request.getParameterMap();
		Iterator<?> entries = reqMap.entrySet().iterator();
		Map.Entry<?, ?> entry;
		String name = null;
		String value = null;
		while (entries.hasNext()) {
			entry = (Entry<?, ?>) entries.next();
			name = (String) entry.getKey();
			Object valObj = reqMap.get(name);
			if (null == valObj) {
				//
			} else if (valObj instanceof String[]) {
				String strValues = Arrays.toString((String[]) valObj);
				value = strValues.substring(1, strValues.length() - 1);
			} else {
				value = valObj.toString();
			}
			params.put(name, value);
		}
		return params;
	}

	/**
	 * 获取Multipart表单的参数
	 * 
	 * @author lihw
	 * @created 2015年10月12日 上午11:13:47
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> getMultipartFormRequestParams(HttpServletRequest request) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		DiskFileItemFactory dfif = new DiskFileItemFactory();
		dfif.setSizeThreshold(4 * 1024 * 1024);
		File temp = new File("/temp/");
		if (!temp.exists()) {
			temp.mkdirs();
		}
		dfif.setRepository(temp);
		ServletFileUpload fileUpload = new ServletFileUpload(dfif);
		List<FileItem> items = fileUpload.parseRequest(request);
		for (FileItem item : items) {
			if (item.isFormField()) {
				params.put(item.getFieldName(), item.getString());
			} else {
				String fileName = item.getFieldName();
				File file = new File(temp.getAbsolutePath() + "/" + System.currentTimeMillis() + "_" + fileName);
				item.write(file);
				params.put(fileName, file);
			}
		}
		return params;
	}

	/**
	 * 获取结果集
	 * 
	 * @param requestDataForm
	 * @param token
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String getResultData(RequestDataForm requestDataForm, String token, String url) throws Exception {
    	String param=requestDataForm.getString("param_data");
    	Map<String, Object> paramMap = JsonUtil.jsonToMapObject(param);
    	Map<String,Object> body=(Map<String, Object>) paramMap.get("body");
		String result = "";
		Map<String, String> form = new HashMap<String, String>();
		Map<String, File> files = new HashMap<String, File>();

		if (body != null && !body.isEmpty()) {
			Iterator<String> it = body.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				Object value = body.get(key);
				if (value instanceof File) {
					files.put(key, (File) value);
				} else {
					form.put(key, String.valueOf(value));
				}
			}
		}
		
		// 将head信息传到接口
		form.put("__command", requestDataForm.getString("__command"));
		form.put("__userId", requestDataForm.getString("__userId"));
		form.put("__markId", requestDataForm.getString("__markId"));
		form.put("__time", requestDataForm.getString("__time"));
		form.put("__version", requestDataForm.getString("__version"));
		
		if (!files.isEmpty()) {
			result = HttpTools.postMultipartForm(url, form, files);
		} else {
			result = HttpTools.postCommonForm(url, form);
		}

		return result;
	}

	public static ResponseData getResponseData(String result) {
		ResponseData responseData = new ResponseData();
		System.out.println(result);
		Map<String, Object> resultMap =  JsonUtil.jsonToMapObject(result);
		responseData.setResult(MapUtils.getString(resultMap, "result"));
		responseData.setInfo(MapUtils.getString(resultMap, "info"));
		System.out.println(JsonUtil.beanToJson(resultMap.get("rsObj")));
		responseData.setRsObj(resultMap.get("rsObj"));
		return responseData;
	}
	public static void main(String[] args) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userName", "严格");
//		Map<String, File> files = new HashMap<String, File>();
//		files.put("abc", new File("C:\\Users\\ge\\Desktop\\QQ图片20151014184059.png"));
//		String str = postMultipartForm("http://192.168.193.202:8080/service-api/img/upload", params, files);
		String str=postCommonForm("http://localhost:8080/service-api/user/login",params);
		System.out.println(str);
//		String a="{\"result\":\"1\",\"info\":\"登录成功\",\"rsObj\":{\"UserCode\":\"SJB_1440316494560\"}}";
//		Map<String, Object> resultMap1 = JsonUtil.jsonToMap(a);
//		System.out.println(JsonUtil.beanToJson(resultMap1.get("rsObj")));
//		
//		System.out.println(JsonUtil.beanToJson(resultMap1));
	}
}
