package com.maxtree.trackbe4.external.tmri;

import java.net.*;
import java.util.*;

import org.codehaus.xfire.client.*;
import java.util.Map.Entry;
import java.io.*;
import org.xml.sax.InputSource;
import org.jdom.input.SAXBuilder;

/**
 * 
 * @author chens
 *
 */
public class InterF {
	Client client = null;

	/**
	 * 获取车辆信息通过号牌种类，号码号牌
	 * 
	 * @param hpzl
	 * @param hphm
	 * @return
	 */
	public ArrayList getCarView(String hpzl, String hphm) {
		ArrayList resultList = new ArrayList();
		initClient();
		try {
			String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><QueryCondition><hpzl>" + hpzl + "</hpzl><hphm>"
					+ hphm + "</hphm></QueryCondition></root>";
			Object url[];
			String result = "";
			try {
				url = client.invoke("queryObjectOut", new Object[] { "01C21", xml });
				result = URLDecoder.decode(url[0].toString(), "UTF-8");
				System.out.println(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringReader read = new StringReader(new String(result));
			// �����µ�����ԴSAX ��������ʹ�� InputSource ������ȷ����ζ�ȡ XML ����
			InputSource source = new InputSource(read);
			// ����һ���µ�SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			org.jdom.Document doc = sb.build(source);
			// ȡ�ĸ�Ԫ��
			org.jdom.Element root = doc.getRootElement();
			org.jdom.Element ele = root.getChild("body");
			List list = ele.getChildren();
			for (int i = 0; i < list.size(); i++) {
				org.jdom.Element element = (org.jdom.Element) list.get(i);
				List value = element.getChildren();
				HashMap<String, String> map = new HashMap<String, String>();
				for (int j = 0; j < value.size(); j++) {
					org.jdom.Element element1 = (org.jdom.Element) value.get(j);
					map.put(element1.getName(), element1.getValue());
				}
				resultList.add(map);
			}
		} catch (Exception ex) {
			System.out.println("ex:" + ex);
		}
		return resultList;
	}

	/**
	 * 
	 * @param bslsh
	 * @return
	 */
	public ArrayList getDriverView(String bslsh) {
		ArrayList<Map> resultList = new ArrayList<Map>();
		Map<String, String> Imp = new HashMap<String, String>();
		Imp.put("LSH", bslsh);
		// String Xml = getXml(Imp);
		initClient();
		Object url[];
		String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><QueryCondition><sfzmhm>" + bslsh
				+ "</sfzmhm></QueryCondition></root>"; // 1140612042669
		System.out.println("xml:" + xml);
		String result = "";
		try {
			url = client.invoke("queryObjectOut", new Object[] { "02C06", xml });
			result = URLDecoder.decode(url[0].toString(), "UTF-8");
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			StringReader read = new StringReader(new String(result));
			// �����µ�����ԴSAX ��������ʹ�� InputSource ������ȷ����ζ�ȡ XML ����
			InputSource source = new InputSource(read);
			// ����һ���µ�SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			org.jdom.Document doc = sb.build(source);
			// ȡ�ĸ�Ԫ��
			org.jdom.Element root = doc.getRootElement();
			org.jdom.Element ele = root.getChild("body");
			List list = ele.getChildren();
			for (int i = 0; i < list.size(); i++) {
				org.jdom.Element element = (org.jdom.Element) list.get(i);
				List value = element.getChildren();
				HashMap<String, String> map = new HashMap<String, String>();
				for (int j = 0; j < value.size(); j++) {
					org.jdom.Element element1 = (org.jdom.Element) value.get(j);
					map.put(element1.getName(), element1.getValue());
				}
				resultList.add(map);
			}
		} catch (Exception ex) {
			System.out.println("ex:" + ex);
		}
		return resultList;
	}

	/**
	 * 
	 * @param bslsh
	 * @return
	 */
	public ArrayList getDriverBusView(String bslsh) {
		ArrayList<Map> resultList = new ArrayList<Map>();
		Map<String, String> Imp = new HashMap<String, String>();
		Imp.put("LSH", bslsh);
		// String Xml = getXml(Imp);
		initClient();
		Object url[];
		String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><QueryCondition><lsh>" + bslsh
				+ "</lsh></QueryCondition></root>"; // 1140612042669
		String result = "";
		try {
			url = client.invoke("queryObjectOut", new Object[] { "02C15", xml });
			result = URLDecoder.decode(url[0].toString(), "UTF-8");
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			StringReader read = new StringReader(new String(result));
			// �����µ�����ԴSAX ��������ʹ�� InputSource ������ȷ����ζ�ȡ XML ����
			InputSource source = new InputSource(read);
			// ����һ���µ�SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			org.jdom.Document doc = sb.build(source);
			// ȡ�ĸ�Ԫ��
			org.jdom.Element root = doc.getRootElement();
			org.jdom.Element ele = root.getChild("body");
			List list = ele.getChildren();
			for (int i = 0; i < list.size(); i++) {
				org.jdom.Element element = (org.jdom.Element) list.get(i);
				List value = element.getChildren();
				HashMap<String, String> map = new HashMap<String, String>();
				for (int j = 0; j < value.size(); j++) {
					org.jdom.Element element1 = (org.jdom.Element) value.get(j);
					map.put(element1.getName(), element1.getValue());
				}
				resultList.add(map);
			}
		} catch (Exception ex) {
			System.out.println("ex:" + ex);
		}
		return resultList;
	}

	/**
	 * ���� ҵ����ˮ�� ��� ҵ���¼
	 * 
	 * @param bslsh
	 *            String   后面两个参数可以null,
	 *            
	 *            
	 * @return Map
	 */
	public ArrayList getbusView(String bslsh, String hpzl, String hphm) {
		ArrayList<Map> resultList = new ArrayList<Map>();
		Map<String, String> Imp = new HashMap<String, String>();
		Imp.put("LSH", bslsh);
		String Xml = getXml(Imp);
		initClient();
		Object url[];
		String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><QueryCondition><lsh>" + bslsh + "</lsh><hpzl>"
				+ hpzl + "</hpzl><hphm>" + hphm + "</hphm></QueryCondition></root>"; // 1140612042669
		String result = "";
		try {
			// url = client.invoke("queryObjectOut", new Object[] {"01C26", xml});
			url = client.invoke("queryObjectOut", new Object[] { "01C33", xml });
			result = URLDecoder.decode(url[0].toString(), "UTF-8");
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			StringReader read = new StringReader(new String(result));
			// �����µ�����ԴSAX ��������ʹ�� InputSource ������ȷ����ζ�ȡ XML ����
			InputSource source = new InputSource(read);
			// ����һ���µ�SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			org.jdom.Document doc = sb.build(source);
			// ȡ�ĸ�Ԫ��
			org.jdom.Element root = doc.getRootElement();
			org.jdom.Element ele = root.getChild("body");
			List list = ele.getChildren();
			for (int i = 0; i < list.size(); i++) {
				org.jdom.Element element = (org.jdom.Element) list.get(i);
				List value = element.getChildren();
				HashMap<String, String> map = new HashMap<String, String>();
				for (int j = 0; j < value.size(); j++) {
					org.jdom.Element element1 = (org.jdom.Element) value.get(j);
					map.put(element1.getName(), element1.getValue());
				}
				resultList.add(map);
			}
		} catch (Exception ex) {
			System.out.println("ex:" + ex);
		}
		return resultList;
	}

	/**
	 * getXml
	 *
	 * @param string
	 *            String
	 * @param hpzl
	 *            String
	 * @param string1
	 *            String
	 * @param hphm
	 *            String
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
		} catch (UnsupportedEncodingException ex) {
		}

		return rsult;
	}

	/**
	 * initClient
	 */
	private void initClient() {
		try {
			// client = new Client(new URL("http://" + Staticbl.InterIP +
			// ":6000/trffweb.dll/wsdl/Itrffweb/"));
			client = new Client(new URL("http://10.80.29.32:6000/trffweb.dll/wsdl/Itrffweb/"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		InterF in = new InterF();
		in.getbusView("1161124029872", "", "");
		// in.getDriverView("1161124029872");
		// in.getCarView("02", "BM5H09");
	}
}
