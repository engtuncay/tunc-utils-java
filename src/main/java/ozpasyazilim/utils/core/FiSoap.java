package ozpasyazilim.utils.core;

import ozpasyazilim.utils.datatypes.FiKeyString;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FiSoap {

	FiKeyString mapHeaders;

	//String soapAction, String rootTagName
	public static Fdr<String> requestRaw(String endPoint, String soapRequest, FiKeyString mapHeaders) throws IOException {
//MalformedURLException, IOException daha geniş olduğu için çıkarıldı

		Fdr<String> fdr = new Fdr<>();

		//Code to make a webservice HTTP request
		String responseString = "";
		String outputString = "";
		String wsURL = endPoint; //"<Endpoint of the webservice to be consumed>";

		URL url = new URL(wsURL);
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		String xmlInput = soapRequest; //"entire SOAP Request";

		byte[] buffer; //new byte[xmlInput.length()];
		buffer = xmlInput.getBytes();
		bout.write(buffer);
		byte[] boutAsArr = bout.toByteArray();

		//String SOAPAction = soapAction; //"<SOAP action of the webservice to be consumed>";

		//Set the appropriate HTTP parameters. (default parameters)
		httpConn.setRequestProperty("Content-Length", String.valueOf(boutAsArr.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8"); //
//		httpConn.setRequestProperty("Postman-Token","8052f355-19cf-492b-8608-9f796aaeb59b");
//		httpConn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36"); 		//PostmanRuntime/7.28.4
//		httpConn.setRequestProperty("Accept-Encoding","gzip, deflate, br");
//		httpConn.setRequestProperty("Accept","*/*");
		// httpConn.setRequestProperty("SOAPAction", FiString.orEmpty(soapAction));

		// custom user headers
		if (!FiCollection.isEmptyMap(mapHeaders)) {
			mapHeaders.forEach((key, value) -> {
				httpConn.setRequestProperty(key, FiString.orEmpty(value));
			});
		}

		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);

		OutputStream out = httpConn.getOutputStream();
		//Write the content of the request to the outputstream of the HTTP Connection.
		out.write(boutAsArr);
		out.close();
		//Ready with sending the request.

		//Read the response.
		InputStreamReader isr = null;
		if (httpConn.getResponseCode() == 200) {
			isr = new InputStreamReader(httpConn.getInputStream());
			fdr.setBoResult(true);
		} else {
			if (httpConn.getErrorStream() != null) isr = new InputStreamReader(httpConn.getErrorStream());
			fdr.setBoResult(false);
		}

		if (isr != null) {
			BufferedReader in = new BufferedReader(isr);

			//Write the SOAP message response to a String.
			while ((responseString = in.readLine()) != null) {
				outputString = outputString + responseString;
			}
			fdr.setValue(outputString);
		} else {
			fdr.setMessage("!!! Error Code:" + httpConn.getResponseCode());
		}

//		Loghelper.debugLog(FiSoap.class, "Soap Response:"+outputString);

		//Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
		// Document document = parseXmlFile(outputString); // Write a separate method to parse the xml input.
		// NodeList nodeLst = document.getElementsByTagName(rootTagName); //"<TagName of the element to be retrieved>"
		// String elementValue = nodeLst.item(0).getTextContent();
		// System.out.println(elementValue);

		// Write the SOAP message formatted to the console.
		// String formattedSOAPResponse = formatXML(outputString); // Write a separate method to format the XML input.
		// System.out.println(formattedSOAPResponse);
		// return elementValue;

		return fdr;
	}

	public static Fdr<String> requestRawHttps(String endPoint, String soapRequest, FiKeyString mapHeaders) throws MalformedURLException, IOException {

		Fdr<String> fdr = new Fdr<>();

		//Code to make a webservice HTTP request
		String responseString = "";
		StringBuilder outputString = new StringBuilder();
		String wsURL = endPoint; //"<Endpoint of the webservice to be consumed>";

		URL url = new URL(wsURL);
		//URLConnection connection = url.openConnection();
		HttpsURLConnection httpConn = (HttpsURLConnection) url.openConnection();

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		String xmlInput = soapRequest; //"entire SOAP Request";

		byte[] buffer; //new byte[xmlInput.length()];
		buffer = xmlInput.getBytes();
		bout.write(buffer);
		byte[] boutAsArr = bout.toByteArray();

		//String SOAPAction = soapAction; //"<SOAP action of the webservice to be consumed>";

		//Set the appropriate HTTP parameters. (default parameters)
		httpConn.setRequestProperty("Content-Length", String.valueOf(boutAsArr.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8"); //
//		httpConn.setRequestProperty("Postman-Token","8052f355-19cf-492b-8608-9f796aaeb59b");
//		httpConn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36"); 		//PostmanRuntime/7.28.4
//		httpConn.setRequestProperty("Accept-Encoding","gzip, deflate, br");
//		httpConn.setRequestProperty("Accept","*/*");
		// httpConn.setRequestProperty("SOAPAction", FiString.orEmpty(soapAction));

		// custom user headers
		if (!FiCollection.isEmptyMap(mapHeaders)) {
			mapHeaders.forEach((key, value) -> {
				httpConn.setRequestProperty(key, FiString.orEmpty(value));
			});
		}

		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);

		OutputStream out = httpConn.getOutputStream();
		//Write the content of the request to the outputstream of the HTTP Connection.
		out.write(boutAsArr);
		out.close();
		//Ready with sending the request.

		//Read the response.
		InputStreamReader isr = null;
		if (httpConn.getResponseCode() == 200) {
			isr = new InputStreamReader(httpConn.getInputStream());
			fdr.setBoResult(true);
		} else {
			if (httpConn.getErrorStream() != null) isr = new InputStreamReader(httpConn.getErrorStream());
			fdr.setLnErrorCode(httpConn.getResponseCode());
			fdr.setBoResult(false);
		}

		if (isr != null) {
			BufferedReader in = new BufferedReader(isr);

			//Write the SOAP message response to a String.
			while ((responseString = in.readLine()) != null) {
				outputString.append(responseString);
			}
			fdr.setValue(outputString.toString());
		} else {
			fdr.setMessage("!!! Error Code:" + httpConn.getResponseCode());
		}

//		Loghelper.debugLog(FiSoap.class, "Soap Response:"+outputString);

		//Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
		// Document document = parseXmlFile(outputString); // Write a separate method to parse the xml input.
		// NodeList nodeLst = document.getElementsByTagName(rootTagName); //"<TagName of the element to be retrieved>"
		// String elementValue = nodeLst.item(0).getTextContent();
		// System.out.println(elementValue);

		// Write the SOAP message formatted to the console.
		// String formattedSOAPResponse = formatXML(outputString); // Write a separate method to format the XML input.
		// System.out.println(formattedSOAPResponse);
		// return elementValue;

		return fdr;
	}

	public static Fdr<FiXml> requestFiXml(String endPoint, String soapRequestXml, FiKeyString mapHeaders, Boolean boHttps) {

		Fdr<FiXml> fdrXmlDoc = new Fdr<>();

		try {
			Fdr<String> fdrRequest = null;
			if (FiBoolean.isTrue(boHttps)) {
				fdrRequest = requestRawHttps(endPoint, soapRequestXml, mapHeaders);
			} else {
				fdrRequest = requestRaw(endPoint, soapRequestXml, mapHeaders);
			}


			if (fdrRequest.getValue() != null) {
				FiXml fiXml = FiXml.bui().makeDocument(fdrRequest.getValue());
				fiXml.setTxXmlRaw(fdrRequest.getValue());
				fdrXmlDoc.setValue(fiXml);
			}
			fdrXmlDoc.setLnErrorCode(fdrRequest.getLnErrorCode());
			fdrXmlDoc.setMessage(fdrRequest.getMessage());
			fdrXmlDoc.combineAnd(fdrRequest);

		} catch (IOException e) {
			Loghelper.get(FiSoap.class).debug(FiException.exceptionIfToStr(e));
			fdrXmlDoc.setException(e);
			fdrXmlDoc.setBoResult(false);
			fdrXmlDoc.setMessage("Soap isteği gerçekleşirken hata oluştu. Detay için Exception inceleyiniz.");
		}

		return fdrXmlDoc;
	}

	public FiKeyString getMapHeaders() {
		return mapHeaders;
	}

	public void setMapHeaders(FiKeyString mapHeaders) {
		this.mapHeaders = mapHeaders;
	}
}
