package Handlers.ReadWrite;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class WriteString {
  public static void ws (String str, OutputStream os) throws IOException {
    OutputStreamWriter sw = new OutputStreamWriter(os);
    sw.write(str);
    sw.flush();
  }
}
