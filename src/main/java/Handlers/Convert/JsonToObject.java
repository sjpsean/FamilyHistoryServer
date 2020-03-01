package Handlers.Convert;

import com.google.gson.Gson;

public class JsonToObject {
  public static <T> T jsonToObject(String s, Class<T> returnType) {
    return (new Gson()).fromJson(s, returnType);
  }
}
