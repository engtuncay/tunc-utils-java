package ozpasyazilim.utils.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import ozpasyazilim.utils.datatypes.FiKeybean;
import ozpasyazilim.utils.datatypes.FiKeytext;
import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FiRest {

  FiKeytext mapHeaders;

  private static Class<FiRest> getClassi() {
    return FiRest.class;
  }

  // Convenience overloads for HTTPS and defaults
  public static Fdr requestRest(String endPoint, String body, FiKeytext headers, String httpMethod, String contentType) {
    Fdr fdrMain = new Fdr();

    try {
      String responseString = "";
      StringBuilder stbOutput = new StringBuilder();

      URL url = new URL(endPoint);
      HttpURLConnection httpConn = null;

      if (endPoint.contains("https://")) {
        httpConn = (HttpsURLConnection) url.openConnection();
      } else {
        httpConn = (HttpURLConnection) url.openConnection();
      }

      HttpURLConnection finalHttpConn = httpConn;
      // apply custom user headers if provided
      if (!FiCollection.isEmptyMap(headers)) {
        headers.forEach((key, value) -> finalHttpConn.setRequestProperty(key, FiString.orEmpty(value)));
      }

      // If caller didn't provide a User-Agent, set a sensible default. Some servers reject requests
      // that don't include a User-Agent header (or require a particular one).
      boolean hasUserAgent = false;
      if (!FiCollection.isEmptyMap(headers)) {
        for (String hk : headers.keySet()) {
          if ("User-Agent".equalsIgnoreCase(hk)) {
            hasUserAgent = true;
            break;
          }
        }
      }
      if (!hasUserAgent) {
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
      }

      // Provide a permissive Accept header by default to increase compatibility with REST endpoints
      if (!FiCollection.isEmptyMap(headers) || !hasUserAgent) {
        // only set Accept if caller didn't override it
        boolean hasAccept = false;
        if (!FiCollection.isEmptyMap(headers)) {
          for (String hk : headers.keySet()) {
            if ("Accept".equalsIgnoreCase(hk)) {
              hasAccept = true;
              break;
            }
          }
        }
        if (!hasAccept) {
          httpConn.setRequestProperty("Accept", "application/json, text/*, */*");
        }
      }

      if (httpMethod == null || httpMethod.isEmpty()) httpMethod = (body == null ? "GET" : "POST");
      if (contentType == null || contentType.isEmpty()) contentType = "application/json;charset=utf-8";

      httpConn.setRequestMethod(httpMethod);
      boolean sendBody = body != null && !body.isEmpty() && !"GET".equalsIgnoreCase(httpMethod) && !"DELETE".equalsIgnoreCase(httpMethod);
      httpConn.setDoInput(true);
      httpConn.setDoOutput(sendBody);

      if (sendBody) {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        httpConn.setRequestProperty("Content-Type", contentType);
        httpConn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
        try (OutputStream out = httpConn.getOutputStream()) {
          out.write(bytes);
        }
      }

      int code = httpConn.getResponseCode();
      fdrMain.setLnResponseCode(code);

      InputStreamReader isr = null;
      if (code >= 200 && code < 300) {
        if (httpConn.getInputStream() != null)
          isr = new InputStreamReader(httpConn.getInputStream(), StandardCharsets.UTF_8);
        fdrMain.setFdBoResult(true);
      } else {
        if (httpConn.getErrorStream() != null)
          isr = new InputStreamReader(httpConn.getErrorStream(), StandardCharsets.UTF_8);
        fdrMain.setFdBoResult(false);
      }

      if (isr != null) {
        try (BufferedReader br = new BufferedReader(isr)) {
          while ((responseString = br.readLine()) != null) {
            stbOutput.append(responseString);
          }
        }
        fdrMain.setFdTxValue(stbOutput.toString());
        Fdr fdrParse = parseJsonToFiKeybean(stbOutput.toString());
        fdrMain.setFdFkbVal(fdrParse.getFdFkbVal());
      } else {
        // include response headers in the debug message to help diagnose 403/other errors
        StringBuilder hdrs = new StringBuilder();
        try {
          httpConn.getHeaderFields().forEach((k, v) -> {
            hdrs.append(k).append(": ").append(v).append("; ");
          });
        } catch (Exception ignore) {
        }
        fdrMain.setFdrTxMessageWitAddLog("!!! Error Code: " + code + "; Response headers: " + hdrs.toString());
      }

    } catch (Exception exception) {
      Loghelper.get(FiRest.class).debug(FiException.exToErrorLog(exception));
      fdrMain.setFdBoResult(false);
      fdrMain.setFdrTxMessageWitAddLog("REST isteği gerçekleşirken hata oluştu. Detay için Exception inceleyiniz.");
      fdrMain.setException(exception);
    }

    return fdrMain;
  }


  public FiKeytext getMapHeaders() {
    return mapHeaders;
  }

  public void setMapHeaders(FiKeytext mapHeaders) {
    this.mapHeaders = mapHeaders;
  }

  /**
   * JSON stringini FiKeybean nesnesine çevirir.
   *
   * @param json JSON string
   * @return FiKeybean veya null (parse hatası varsa)
   */
  public static Fdr parseJsonToFiKeybean(String json) {
    Fdr fdrMain = new Fdr();
    if (json == null || json.trim().isEmpty()) {
      fdrMain.setFdBoResult(false);
      fdrMain.setFdFkbVal(new FiKeybean());
      return fdrMain;
    }
    try {
      //com.fasterxml.jackson.databind.
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> map = mapper.readValue(json, Map.class);
      fdrMain.setFdBoResult(true);
      fdrMain.setFdFkbVal(new FiKeybean(map));
      return fdrMain;
    } catch (Exception ex) {
      // Loghelper ile loglanabilir
      Loghelper.get(getClassi()).error("FiKeybean parse error: " + ex.getMessage());
      Loghelper.get(getClassi()).error(FiException.exToErrorLog(ex));
      fdrMain.setFdBoResult(false);
      fdrMain.setFdFkbVal(new FiKeybean());
      return fdrMain;
    }
  }

}// end class


// New REST-friendly HTTP method (supports GET/POST/PUT/DELETE and custom content-type)
//  public static Fdr<String> requestRawHttpRest(String endPoint, String body, FiKeytext headers, String httpMethod, String contentType) {
//
//    Fdr<String> fdr = new Fdr<>();
//
//    if (httpMethod == null || httpMethod.isEmpty()) httpMethod = (body == null ? "GET" : "POST");
//    if (contentType == null || contentType.isEmpty()) contentType = "application/json;charset=utf-8";
//
//    try {
//      String responseString = "";
//      StringBuilder outputString = new StringBuilder();
//
//      URL url = new URL(endPoint);
//      HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//
//      // custom user headers
//      if (!FiCollection.isEmptyMap(headers)) {
//        headers.forEach((key, value) -> {
//          httpConn.setRequestProperty(key, FiString.orEmpty(value));
//        });
//      }
//
//      httpConn.setRequestMethod(httpMethod);
//      // For methods that send a body, enable output
//      boolean sendBody = body != null && !body.isEmpty() && !"GET".equalsIgnoreCase(httpMethod) && !"DELETE".equalsIgnoreCase(httpMethod);
//      httpConn.setDoInput(true);
//      httpConn.setDoOutput(sendBody);
//
//      if (sendBody) {
//        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
//        httpConn.setRequestProperty("Content-Type", contentType);
//        httpConn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
//        try (OutputStream out = httpConn.getOutputStream()) {
//          out.write(bytes);
//        }
//      }
//
//      int code = httpConn.getResponseCode();
//      fdr.setLnResponseCode(code);
//
//      InputStreamReader isr = null;
//      if (code >= 200 && code < 300) {
//        if (httpConn.getInputStream() != null)
//          isr = new InputStreamReader(httpConn.getInputStream(), StandardCharsets.UTF_8);
//        fdr.setFdBoResult(true);
//      } else {
//        if (httpConn.getErrorStream() != null)
//          isr = new InputStreamReader(httpConn.getErrorStream(), StandardCharsets.UTF_8);
//        fdr.setFdBoResult(false);
//      }
//
//      if (isr != null) {
//        try (BufferedReader in = new BufferedReader(isr)) {
//          while ((responseString = in.readLine()) != null) {
//            outputString.append(responseString);
//          }
//        }
//        fdr.setValue(outputString.toString());
//      } else {
//        fdr.setFdrTxMessageWitAddLog("!!! Error Code:" + code);
//      }
//
//    } catch (IOException exception) {
//      Loghelper.get(FiRest.class).error(FiException.exceptionIfToString(exception));
//      fdr.setFdBoResult(false);
//      fdr.setException(exception);
//      fdr.setFdrTxMessageWitAddLog("REST isteği gerçekleşirken hata oluştu. Detay için Exception inceleyiniz.");
//    }
//
//    return fdr;
//  }
