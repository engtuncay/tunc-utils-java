package ozpasyazilim.utils.core;

import ozpasyazilim.utils.datatypes.FiKeyString;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FiSoap {


	FiKeyString mapHeaders;

	//String soapAction, String rootTagName

	/**
	 *
	 *
	 * @param endPoint
	 * @param soapRequest entire SOAP Request
	 * @param mapHeaders
	 * @return
	 */
	public static Fdr<String> requestRawHttp(String endPoint, String soapRequest, FiKeyString mapHeaders) {

		//MalformedURLException, IOException daha geniş olduğu için çıkarıldı //throws IOException
		Fdr<String> fdrMain = new Fdr<>();

		try {
			//Code to make a webservice HTTP request
			String responseString = "";
			StringBuilder outputString = new StringBuilder();
			//String wsURL = endPoint; //"<Endpoint of the webservice to be consumed>";

			URL url = new URL(endPoint);
			//URLConnection connection = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

			//soapRequest.getBytes();
			byte[] buffer = soapRequest.getBytes(StandardCharsets.UTF_8);//new byte[soapRequest.length()];

			// ByteArrayOutputStream baouts = new ByteArrayOutputStream();
			// baouts.write(buffer);
			// byte[] boutAsArr = baouts.toByteArray();
			//byte[] boutAsArr = buffer;

			//String SOAPAction = soapAction; //"<SOAP action of the webservice to be consumed>";

			//Set the appropriate HTTP parameters. (default parameters)
			httpConn.setRequestProperty("Content-Type", "text/xml;charset=utf-8;"); //
			httpConn.setRequestProperty("Content-Length", String.valueOf(buffer.length)); //boutAsArr
			//httpConn.setRequestProperty("Postman-Token","8052f355-19cf-492b-8608-9f796aaeb59b");
			//httpConn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36"); 		//PostmanRuntime/7.28.4
			//httpConn.setRequestProperty("Accept-Encoding","gzip, deflate, br");
			//httpConn.setRequestProperty("Accept","*/*");
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
			out.write(buffer); //boutAsArr
			out.close();
			//Ready with sending the request.

			//Read the response.
			InputStreamReader isr = null;
			fdrMain.setLnResponseValue(httpConn.getResponseCode());

			if (httpConn.getResponseCode() == 200) {
				isr = new InputStreamReader(httpConn.getInputStream(),StandardCharsets.UTF_8);
				fdrMain.setBoResult(true);
			} else {
				if (httpConn.getErrorStream() != null) isr = new InputStreamReader(httpConn.getErrorStream(),StandardCharsets.UTF_8);
			}

			if (isr != null) {
				BufferedReader in = new BufferedReader(isr);
				//Write the SOAP message response to a String.
				while ((responseString = in.readLine()) != null) {
					outputString.append(responseString);
				}
				fdrMain.setValue(outputString.toString());
			} else {
				fdrMain.setMessage("!!! Error Code:" + httpConn.getResponseCode());
			}
			//Loghelper.debugLog(FiSoap.class, "Soap Response:"+outputString);
		} catch (IOException exception) {
			Loghelper.get(getClassi()).debug(FiException.exceptionIfToString(exception));
			fdrMain.setBoResult(false);
			fdrMain.setException(exception);
			fdrMain.setMessage("Soap isteği gerçekleşirken hata oluştu. Detay için Exception inceleyiniz.");
		}
		return fdrMain;
	}

	private static Class<FiSoap> getClassi() {
		return FiSoap.class;
	}

	public static Fdr<String> requestRawHttps(String endPoint, String soapRequest, FiKeyString fksHeaders) {

		Fdr<String> fdr = new Fdr<>();

		try {
			//Code to make a webservice HTTP request
			String responseString = "";
			StringBuilder outputString = new StringBuilder();
			String wsURL = endPoint; //"<Endpoint of the webservice to be consumed>";

			URL url = new URL(wsURL);
			//URLConnection connection = url.openConnection();
			HttpsURLConnection httpConn = (HttpsURLConnection) url.openConnection();

			String xmlInput = soapRequest; //"entire SOAP Request";
			byte[] bytRequestBuffer = xmlInput.getBytes(StandardCharsets.UTF_8); //new byte[xmlInput.length()];

			ByteArrayOutputStream bytOutStream = new ByteArrayOutputStream();
			bytOutStream.write(bytRequestBuffer);
			byte[] bytOutHttpParams = bytOutStream.toByteArray();
			//post.getBytes(Charsets.UTF_8);

			//String SOAPAction = soapAction; //"<SOAP action of the webservice to be consumed>";

			//Set the appropriate HTTP parameters. (default parameters)
			httpConn.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
			httpConn.setRequestProperty("Content-Length", String.valueOf(bytOutHttpParams.length));
//		httpConn.setRequestProperty("Postman-Token","8052f355-19cf-492b-8608-9f796aaeb59b");
//		httpConn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36"); 		//PostmanRuntime/7.28.4
//		httpConn.setRequestProperty("Accept-Encoding","gzip, deflate, br");
//		httpConn.setRequestProperty("Accept","*/*");
//    httpConn.setRequestProperty("SOAPAction", FiString.orEmpty(soapAction));

			// custom user headers
			if (!FiCollection.isEmptyMap(fksHeaders)) {
				fksHeaders.forEach((key, value) -> {
					httpConn.setRequestProperty(key, FiString.orEmpty(value));
				});
			}

			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);

			OutputStream out = httpConn.getOutputStream();
			//Write the content of the request to the outputstream of the HTTP Connection.
			out.write(bytOutHttpParams);
			out.close();
			//Ready with sending the request.

			//Read the response.
			InputStreamReader isr = null;
			fdr.setLnResponseValue(httpConn.getResponseCode());

			if (httpConn.getResponseCode() == 200) {
				isr = new InputStreamReader(httpConn.getInputStream(),StandardCharsets.UTF_8);
			} else {
				if (httpConn.getErrorStream() != null) isr = new InputStreamReader(httpConn.getErrorStream(),StandardCharsets.UTF_8);
				//fdr.setLnErrorCode(httpConn.getResponseCode());
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
			// Exception fırlatmadığı için boResult True verildi.
			fdr.setBoResult(true);

		} catch (Exception exception) { //	//throws MalformedURLException, IOException
			Loghelper.get(FiSoap.class).debug(FiException.exceptionIfToString(exception));
			Loghelper.get(FiSoap.class).debug(FiException.exceptiontostring1(exception));
			fdr.setBoResult(false);
			fdr.setMessage("Soap isteği gerçekleşirken hata oluştu. Detay için Exception inceleyiniz.");
			fdr.setException(exception);
		}

		return fdr;
	}

	public static Fdr<FiXml> requestFiXmlHttps(String endPoint, String soapRequestXml, FiKeyString mapHeaders) {
		return requestFiXml(endPoint, soapRequestXml, mapHeaders, true);
	}

	public static Fdr<FiXml> requestFiXmlHttp(String endPoint, String soapRequestXml, FiKeyString mapHeaders) {
		return requestFiXml(endPoint, soapRequestXml, mapHeaders, false);
	}

	public static Fdr<FiXml> requestFiXml(String endPoint, String soapRequestXml, FiKeyString mapHeaders, Boolean boUseHttps) {

		Fdr<FiXml> fdrXmlDoc = new Fdr<>();

		Fdr<String> fdrRequest = null;
		if (FiBool.isTrue(boUseHttps)) {
			fdrRequest = requestRawHttps(endPoint, soapRequestXml, mapHeaders);
		} else {
			fdrRequest = requestRawHttp(endPoint, soapRequestXml, mapHeaders);
		}

		//Loghelper.get(FiSoap.class).debug("Response:" + fdrRequest.getValue());

		if (fdrRequest.isTrueBoResult()) {

			if (fdrRequest.getValue() != null) {
				FiXml fiXml = FiXml.bui().makeDocument(fdrRequest.getValue());
				fiXml.setTxXmlRaw(fdrRequest.getValue());
				fdrXmlDoc.setValue(fiXml);
			}

			fdrXmlDoc.setLnResponseValue(fdrRequest.getLnResponseValue());
			//fdrXmlDoc.setLnErrorCode(fdrRequest.getLnErrorCode());
			fdrXmlDoc.setMessage(fdrRequest.getMessage());
			fdrXmlDoc.combineAnd(fdrRequest);
		} else {
			fdrXmlDoc.setBoResult(false);
			fdrXmlDoc.setMessage("Soap isteği gerçekleşirken hata oluştu. Detay için Exception inceleyiniz.");
			fdrXmlDoc.setException(fdrRequest.getException());
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

// Loghelper.debugLog(FiSoap.class, "Soap Response:"+outputString);

//Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
// Document document = parseXmlFile(outputString); // Write a separate method to parse the xml input.
// NodeList nodeLst = document.getElementsByTagName(rootTagName); //"<TagName of the element to be retrieved>"
// String elementValue = nodeLst.item(0).getTextContent();
// System.out.println(elementValue);

// Write the SOAP message formatted to the console.
// String formattedSOAPResponse = formatXML(outputString); // Write a separate method to format the XML input.
// System.out.println(formattedSOAPResponse);
// return elementValue;