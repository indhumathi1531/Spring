package com.test;

import java.util.HashMap;
import java.util.Map;
//import org.json.simple.JSONValue;

public class Map2Json {

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("1", "India");
		map.put("2", "Japan");
		map.put("3", "China");
		// String jsonStr = JSONVALUE.toJSONString(map);
		// JSONObject js = new JSONObject(map);
		// System.out.println(js);
	}

}
