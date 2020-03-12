
package com.albiontools.jsonhandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class JSONFromURL {

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

	private String getJSONString(String url) {
		InputStream is = null;
		try {
			is = new URL(url).openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String jsonString = null;
		try {
			jsonString = readAll(rd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
    
	public JSONArray getJSONArrayOutOfURL(String url) {
		JSONArray json = null;
		try {
			json = new JSONArray(getJSONString(url));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;

	}
    
	public JSONObject getJSONObjectOutOfURL(String url) {
		JSONObject json = null;
		try {
			json = new JSONObject(getJSONString(url));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

}
