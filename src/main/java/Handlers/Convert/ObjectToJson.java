package Handlers.Convert;

import com.google.gson.Gson;

public class ObjectToJson {
  public static String objectToJson(Object o) {
    return new Gson().toJson(o);
  }
}
