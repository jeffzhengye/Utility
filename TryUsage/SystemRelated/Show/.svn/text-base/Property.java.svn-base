package SystemRelated.Show;

import java.util.Properties;
import java.io.UnsupportedEncodingException;

public class Property {
  public Property() {
  }
  public static String getDefaultEncoding()
  {
    return System.getProperty("file.encoding");
  }
  public static void list() throws UnsupportedEncodingException {
    Properties pro = System.getProperties();
    pro.list(System.out);
    String separator = pro.getProperty("line.separator");
    byte b[] = separator.getBytes();
    Integer integer = new Integer((int)b[1]);
    String hex = integer.toHexString(b[0]);
    System.out.println(hex);
    hex = integer.toHexString(b[1]);
    System.out.println(hex);
  }

  /**
   *
   */
  public static void main(String args[]) throws UnsupportedEncodingException {
    list();
  }
}