package org.gsstrozi.jsontohtml.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class FileTemplateUtil
{
  public static String replaceBody(String html, String body)
  {
    if (html != null) {
      return html.replace("@body@", body);
    }
    return html;
  }
  
  public static StringBuilder getTemplateFile()
    throws IOException
  {
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("template.html");
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
    return readAll(reader);
  }
  
  public static StringBuilder readAll(Reader rd)
    throws IOException
  {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char)cp);
    }
    return sb;
  }
}
