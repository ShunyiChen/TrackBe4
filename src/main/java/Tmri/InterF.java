package Tmri;

import java.net.*;
import java.util.*;

import org.codehaus.xfire.client.*;
import java.util.Map.Entry;
import java.io.*;
import org.xml.sax.InputSource;
import org.jdom.input.SAXBuilder;

/**
 * <p>Title: 大连市公安局车管所数字档案影像管理系统</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: HzSoft Copyright (c) 2014</p>
 *
 * <p>Company: 大连恒智科技发展有限公司</p>
 *
 * @author not attributable
 * @version 2.0
 */
public class InterF {
  Client client = null;

  /**
   * 输入号牌种类，号牌号码
   * 输出车辆基本信息
   * @param hpzl String
   * @param hphm String
   * @return Map
   */
  public ArrayList getCarView(String hpzl, String hphm) {
    ArrayList resultList = new ArrayList();
    initClient();
    try {
      String xml =
        "<?xml version=\"1.0\" encoding=\"GBK\"?><root><QueryCondition><hpzl>" +
        hpzl + "</hpzl><hphm>" + hphm + "</hphm></QueryCondition></root>";
      Object url[];
      String result = "";
      try {
        url = client.invoke("queryObjectOut", new Object[] {"01C21", xml});
        result = URLDecoder.decode(url[0].toString(), "UTF-8");
        System.out.println(result);
      }
      catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      StringReader read = new StringReader(new String(result));
      //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
      InputSource source = new InputSource(read);
      //创建一个新的SAXBuilder
      SAXBuilder sb = new SAXBuilder();
      org.jdom.Document doc = sb.build(source);
      //取的根元素
      org.jdom.Element root = doc.getRootElement();
      org.jdom.Element ele = root.getChild("body");
      List list = ele.getChildren();
      for (int i = 0; i < list.size(); i++) {
        org.jdom.Element element = (org.jdom.Element) list.get(i);
        List value = element.getChildren();
        HashMap<String, String> map = new HashMap<String, String> ();
        for (int j = 0; j < value.size(); j++) {
          org.jdom.Element element1 = (org.jdom.Element) value.get(j);
          map.put(element1.getName(), element1.getValue());
        }
        resultList.add(map);
      }
    }
    catch (Exception ex) {
      System.out.println("ex:" + ex);
    }
    return resultList;
  }

  /**
   * 输入 身份证
   * 输出 驾驶证基本信息
   * @param bslsh String
   * @return Map
   */
  public ArrayList getDriverView(String bslsh) {
    ArrayList<Map> resultList = new ArrayList<Map> ();
    Map<String, String> Imp = new HashMap<String, String> ();
    Imp.put("LSH", bslsh);
//    String Xml = getXml(Imp);
    initClient();
    Object url[];
    String xml =
      "<?xml version=\"1.0\" encoding=\"GBK\"?><root><QueryCondition><sfzmhm>" +
      bslsh + "</sfzmhm></QueryCondition></root>"; //1140612042669
    System.out.println("xml:" + xml);
    String result = "";
    try {
      url = client.invoke("queryObjectOut", new Object[] {"02C06", xml});
      result = URLDecoder.decode(url[0].toString(), "UTF-8");
      System.out.println(result);
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try {
      StringReader read = new StringReader(new String(result));
      //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
      InputSource source = new InputSource(read);
      //创建一个新的SAXBuilder
      SAXBuilder sb = new SAXBuilder();
      org.jdom.Document doc = sb.build(source);
      //取的根元素
      org.jdom.Element root = doc.getRootElement();
      org.jdom.Element ele = root.getChild("body");
      List list = ele.getChildren();
      for (int i = 0; i < list.size(); i++) {
        org.jdom.Element element = (org.jdom.Element) list.get(i);
        List value = element.getChildren();
        HashMap<String, String> map = new HashMap<String, String> ();
        for (int j = 0; j < value.size(); j++) {
          org.jdom.Element element1 = (org.jdom.Element) value.get(j);
          map.put(element1.getName(), element1.getValue());
        }
        resultList.add(map);
      }
    }
    catch (Exception ex) {
      System.out.println("ex:" + ex);
    }
    return resultList;
  }

  /**
   * 输入 业务流水号
   * 输出 驾驶证业务信息
   * @param bslsh String
   * @return Map
   */
  public ArrayList getDriverBusView(String bslsh) {
    ArrayList<Map> resultList = new ArrayList<Map> ();
    Map<String, String> Imp = new HashMap<String, String> ();
    Imp.put("LSH", bslsh);
//      String Xml = getXml(Imp);
    initClient();
    Object url[];
    String xml =
      "<?xml version=\"1.0\" encoding=\"GBK\"?><root><QueryCondition><lsh>" +
      bslsh + "</lsh></QueryCondition></root>"; //1140612042669
    String result = "";
    try {
      url = client.invoke("queryObjectOut", new Object[] {"02C15", xml});
      result = URLDecoder.decode(url[0].toString(), "UTF-8");
      System.out.println(result);
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try {
      StringReader read = new StringReader(new String(result));
      //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
      InputSource source = new InputSource(read);
      //创建一个新的SAXBuilder
      SAXBuilder sb = new SAXBuilder();
      org.jdom.Document doc = sb.build(source);
      //取的根元素
      org.jdom.Element root = doc.getRootElement();
      org.jdom.Element ele = root.getChild("body");
      List list = ele.getChildren();
      for (int i = 0; i < list.size(); i++) {
        org.jdom.Element element = (org.jdom.Element) list.get(i);
        List value = element.getChildren();
        HashMap<String, String> map = new HashMap<String, String> ();
        for (int j = 0; j < value.size(); j++) {
          org.jdom.Element element1 = (org.jdom.Element) value.get(j);
          map.put(element1.getName(), element1.getValue());
        }
        resultList.add(map);
      }
    }
    catch (Exception ex) {
      System.out.println("ex:" + ex);
    }
    return resultList;
  }

  /**
   * 输入 业务流水号
   * 输出 业务记录
   * @param bslsh String
   * @return Map
   */
  public ArrayList getbusView(String bslsh,String hpzl,String hphm) {
//	  if(bslsh == null || bslsh.equals("")) {
//		  HashMap m = new HashMap();
//		  m.put("hpzl", "2-小型汽车");
//		  m.put("hphm", "辽B77777");
//		  m.put("clsbdh", "7777");
//		  ArrayList<HashMap> rs = new ArrayList<HashMap>(); 
//		  rs.add(m);
//		  return rs;
//	  }
//	  if(bslsh.equals("1")) {
//		  HashMap m = new HashMap();
//		  m.put("hpzl", "2-小型汽车");
//		  m.put("hphm", "辽BB8K57");
//		  m.put("clsbdh", "5692");
//		  ArrayList<HashMap> rs = new ArrayList<HashMap>(); 
//		  rs.add(m);
//		  return rs;
//	  }
//	  if(bslsh.equals("2")) {
//		  HashMap m = new HashMap();
//		  m.put("hpzl", "2-小型汽车");
//		  m.put("hphm", "辽BBBBBB");
//		  m.put("clsbdh", "BBBB");
//		  ArrayList<HashMap> rs = new ArrayList<HashMap>(); 
//		  rs.add(m);
//		  return rs;
//	  }
//	return null;
	  
    ArrayList<Map> resultList = new ArrayList<Map> ();
    Map<String, String> Imp = new HashMap<String, String> ();
    Imp.put("LSH", bslsh);
    String Xml = getXml(Imp);
    initClient();
    Object url[];
    String xml =
      "<?xml version=\"1.0\" encoding=\"GBK\"?><root><QueryCondition><lsh>" +
      bslsh + "</lsh><hpzl>" +
        hpzl + "</hpzl><hphm>" + hphm + "</hphm></QueryCondition></root>"; //1140612042669
    String result = "";
    try {
//      url = client.invoke("queryObjectOut", new Object[] {"01C26", xml});
      url = client.invoke("queryObjectOut", new Object[] {"01C33", xml});
      result = URLDecoder.decode(url[0].toString(), "UTF-8");
      System.out.println(result);
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try {
      StringReader read = new StringReader(new String(result));
      //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
      InputSource source = new InputSource(read);
      //创建一个新的SAXBuilder
      SAXBuilder sb = new SAXBuilder();
      org.jdom.Document doc = sb.build(source);
      //取的根元素
      org.jdom.Element root = doc.getRootElement();
      org.jdom.Element ele = root.getChild("body");
      List list = ele.getChildren();
      for (int i = 0; i < list.size(); i++) {
        org.jdom.Element element = (org.jdom.Element) list.get(i);
        List value = element.getChildren();
        HashMap<String, String> map = new HashMap<String, String> ();
        for (int j = 0; j < value.size(); j++) {
          org.jdom.Element element1 = (org.jdom.Element) value.get(j);
          map.put(element1.getName(), element1.getValue());
        }
        resultList.add(map);
      }
    }
    catch (Exception ex) {
      System.out.println("ex:" + ex);
    }
    return resultList;
  }

  /**
   * getXml
   *
   * @param string String
   * @param hpzl String
   * @param string1 String
   * @param hphm String
   * @return String
   */
  private String getXml(Map<String, String> ImpMap) {
    String rsult = "";
    StringBuffer xml = new StringBuffer();
    xml.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
    xml.append("<root>");
    xml.append("<QueryCondition>");
    Set set = ImpMap.entrySet();
    Iterator it = set.iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Entry) it.next();
      xml.append("<" + entry.getKey() + ">");
      xml.append(entry.getValue());
      xml.append("</" + entry.getKey() + ">");
    }
    xml.append("< / QueryCondition >");
    xml.append("</ root > ");

    try {
      rsult = URLEncoder.encode(xml.toString(), "UTF-8");
    }
    catch (UnsupportedEncodingException ex) {
    }

    return rsult;
  }

  /**
   * initClient
   */
  private void initClient() {
    try {
//      client = new Client(new URL("http://" + Staticbl.InterIP + ":6000/trffweb.dll/wsdl/Itrffweb/"));
//      client = new Client(new URL("http://10.80.29.32:6000/trffweb.dll/wsdl/Itrffweb/"));
      client = new Client(new URL("http://10.80.29.16:6001/wsdl/Itrffweb/"));
    }
    catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    InterF in = new InterF();
    in.getbusView("1161124029872","","");
//  in.getDriverView("1161124029872");
//  in.getCarView("02", "BM5H09");
  }
}
