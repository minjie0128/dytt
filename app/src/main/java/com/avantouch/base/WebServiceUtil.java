package com.avantouch.base;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebServiceUtil {
	// 定义Web Service的命名空间
	static final String SERVICE_NAMESPACE = "http://tempuri.org/";
	// 定义Web Service提供服务的URL

//    http://180.153.108.78/SmartClubService/WebService.asmx


	//static final String SERVICE_URL = "http://app.alading.com/AladingAppServiceDemo20/WebService.asmx";asmx

    static final String SERVICE_URL = "http://app.alading.com/SmartClubService/WebService.asmx";


	// testing :http://app.alading.com/AladingAppServiceDemo20/WebService.asmx
	// release :http://app.alading.com/AladingAppService/WebService.asmx
	// local :http://192.168.1.111/SmartClubService/WebService.asmx
	// http://192.168.10.28/AladingAppService/WebService.asmx

	static String tag = "WebServiceUtil";

	/**
	 * 发送webservice请求，并返回特定对象链表
	 * 
	 * @param method
	 *            方法名
	 * @param paramNames
	 *            参数列表
	 * @param paramValues
	 *            参数值列表
	 * @return
	 */
	public static List<Object> callService(String method[],
			String[] paramNames, String[] paramValues) {
		// 创建HttpTransportSE传输对象
		List<Object> result = new ArrayList<Object>();
		String methodName = method[0];

		// 使用SOAP1.1协议创建Envelop对象
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		// 实例化SoapObject对象
		SoapObject soapObject = new SoapObject(SERVICE_NAMESPACE, methodName);

		HttpTransportSE httpTranstation = new HttpTransportSE(SERVICE_URL,
				20000);
		httpTranstation.debug = true;

		// 设置webservice方法参数值
		for (int i = 0; i < paramNames.length; i++) {
			soapObject.addProperty(paramNames[i], paramValues[i]);
		}

		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.implicitTypes = true;
		try {
			// 调用Web Service
			httpTranstation.call(SERVICE_NAMESPACE + methodName, envelope);
			if (envelope.getResponse() != null) {
				// 获取服务器响应返回的SOAP消息
				SoapObject bodyIn = (SoapObject) envelope.bodyIn;
				return parseResponse(bodyIn);
			}
		}
//        catch (SoapFault fault) {
//			result.add("0001|调用后台方法错误，请联系技术人员。");
//			if (fault != null)
//				Log.i(tag, fault.getMessage());
//		} catch (IOException e) {
//			result.add("0001|网络连接超时，请重试。");
//			if (e != null)
//				Log.i(tag, e.getMessage());
//		} catch (XmlPullParserException e) {
//			result.add("0001|返回结果处理失败，请联系技术人员。");
//			if (e != null)
//				Log.i(tag, e.getMessage());
//		}

        catch (Exception e) {
			result.add("0001|网络连接超时，请重试!");
//			if (e != null && e.getMessage()!=null)
//				Log.i(tag, e.getMessage());
		}
		return result;
	}

	/**
	 * 处理webservice回复，并返回对象列表
	 * 
	 * @param result
	 * @return List<Object>
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static List<Object> parseResponse(SoapObject result) {
		List<Object> results = new ArrayList<Object>();
		int length = result.getPropertyCount();
		for (int i = 0; i < length; i++) {
			results.add(result.getProperty(i).toString());
		}

		return results;

	}

	/**
	 * 反射设置对象的特定属性的值
	 * 
	 * @param targetObj
	 * @param propName
	 * @param propValue
	 */
	public static void setPropValue(Object targetObj, String propName,
			Object propValue) {
		Class targetClass = targetObj.getClass();
		try {
			Class targetC = Class.forName(targetClass.getName());
			Field field = targetC.getDeclaredField(propName);
			field.setAccessible(true);
			if (field.getType().equals(Integer.class)) {
				field.set(targetObj,
						new Integer(Integer.valueOf(propValue.toString())));
			}
			if (field.getType().equals(int.class)) {
				field.setInt(targetObj, Integer.valueOf(propValue.toString()));
			}
			if (field.getType().equals(String.class)) {
				field.set(targetObj, propValue.toString());
			}
			if (field.getType().equals(double.class)) {
				field.setDouble(targetObj, Double.valueOf(propValue.toString()));
			}
			if (field.getType().equals(Double.class)) {
				field.set(targetObj,
						new Double(Double.valueOf(propValue.toString())));
			}
			if (field.getType().equals(float.class)) {
				field.setFloat(targetObj, Float.valueOf(propValue.toString()));
			}
			if (field.getType().equals(Float.class)) {
				field.set(targetObj,
						new Float(Float.valueOf(propValue.toString())));
			}
			if (field.getType().equals(Long.class)) {
				field.set(targetObj,
						new Long(Long.valueOf(propValue.toString())));
			}
			if (field.getType().equals(long.class)) {
				field.setFloat(targetObj, Long.valueOf(propValue.toString()));
			}
			if (field.getType().equals(short.class)) {
				field.setShort(targetObj, Short.valueOf(propValue.toString()));
			}
			if (field.getType().equals(Short.class)) {
				field.set(targetObj,
						new Short(Short.valueOf(propValue.toString())));
			}
			if (field.getType().equals(Date.class)) {
				field.set(targetObj, new Date(Date.parse(propValue.toString())));
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
// 转载请注明出处：http://forhope.iteye.com/blog/1461407
