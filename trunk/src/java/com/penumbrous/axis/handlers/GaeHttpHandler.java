/**
 * Copyright (C) 2010 Fred Faber.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.penumbrous.axis.handlers;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.axis.AxisFault;
import org.apache.axis.Constants;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.encoding.Base64;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.axis.utils.Messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_ACCEPT;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_AUTHORIZATION;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_CONNECTION;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_CONNECTION_CLOSE;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_CONTENT_LENGTH;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_CONTENT_TYPE;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_DEFAULT_CHAR_ENCODING;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_EXPECT;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_EXPECT_100_Continue;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_HOST;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_SOAP_ACTION;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_TRANSFER_ENCODING;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_TRANSFER_ENCODING_CHUNKED;
import static org.apache.axis.transport.http.HTTPConstants.HEADER_USER_AGENT;
import static org.apache.axis.transport.http.HTTPConstants.MC_HTTP_STATUS_CODE;
import static org.apache.axis.transport.http.HTTPConstants.MC_HTTP_STATUS_MESSAGE;
import static org.apache.axis.transport.http.HTTPConstants.REQUEST_HEADERS;

/**
 * This class is an implementation of {@code org.apache.axis.Handler} that
 * is compatible with the security manager within the Google App Engine runtime
 * environment.  This compatibility is based of the use of the
 * {@link URLFetchService} for the underlying http transport mechanism.
 * <p>
 * This class is largely based off of both the
 * {@code org.apache.axis.transport.http.HTTPSender} class, and a fork of that
 * class which was authored by Joe Swainston.
 *
 * @author penumbrousdotcom@gmail.com (Fred Faber)
 */
public class GaeHttpHandler extends BasicHandler {

  /** The logger used for this class */
  private static final Logger logger =
      Logger.getLogger(GaeHttpHandler.class.getCanonicalName());

  private static final String NEWLINE = "\r\n";

  private static final String ACCEPT_HEADERS =
          HTTPConstants.HEADER_ACCEPT_APPL_SOAP + ", " +
          HTTPConstants.HEADER_ACCEPT_APPLICATION_DIME + ", " +
          HTTPConstants.HEADER_ACCEPT_MULTIPART_RELATED + ", " +
          HTTPConstants.HEADER_ACCEPT_TEXT_ALL;

  private static final String USER_AGENT_HEADERS =
      Messages.getMessage("axisUserAgent");

  /** We effectively disable caching for soap requests. */
  private static final String CACHE_HEADERS =
      HTTPConstants.HEADER_CACHE_CONTROL + ": " +
          HTTPConstants.HEADER_CACHE_CONTROL_NOCACHE + NEWLINE +
          HTTPConstants.HEADER_PRAGMA + ": " +
          HTTPConstants.HEADER_CACHE_CONTROL_NOCACHE + NEWLINE;

  private static final String HEADER_CONTENT_TYPE_LC =
      HEADER_CONTENT_TYPE.toLowerCase();

  private static final String HEADER_LOCATION_LC =
      HTTPConstants.HEADER_LOCATION.toLowerCase();

  private static final String HEADER_CONTENT_LOCATION_LC =
      HTTPConstants.HEADER_CONTENT_LOCATION.toLowerCase();

  private static final String HEADER_CONTENT_LENGTH_LC =
      HEADER_CONTENT_LENGTH.toLowerCase();

  private static final String HEADER_TRANSFER_ENCODING_LC =
      HEADER_TRANSFER_ENCODING.toLowerCase();

  private final URLFetchService urlFetchService;

  /** Invoked by axis via reflection. */
  public GaeHttpHandler() {
    this.urlFetchService = URLFetchServiceFactory.getURLFetchService();
  }

  @Override
  public void invoke(MessageContext msgContext) throws AxisFault {

    logger.info("Received message context: " + msgContext);
    try {
      URL transportUrl = new URL(msgContext.getStrProp(MessageContext.TRANS_URL));

      String host = transportUrl.getHost();
      int port = transportUrl.getPort();

      HTTPRequest httpRequest =
          createHttpRequest(msgContext, transportUrl, host, port);

      HTTPResponse httpResponse = urlFetchService.fetch(httpRequest);
      if (httpResponse.getResponseCode() != HttpURLConnection.HTTP_OK) {
        logger.warning("Failed request: " + httpRequest);
      }

      processHttpResponse(httpResponse, msgContext);

    } catch (Exception e) {
      logger.warning(e.getMessage());
      throw AxisFault.makeFault(e);
    }
  }

  HTTPRequest createHttpRequest(MessageContext msgContext, URL transportUrl,
      String host, int port) throws IOException, SOAPException {

    Message reqMessage = msgContext.getRequestMessage();
    MimeHeaders mimeHeaders = reqMessage.getMimeHeaders();

    String portSuffix = port == -1 ? "" : ":" + port;
    String contentLength = Long.toString(reqMessage.getContentLength());

    String[] contentTypeHeader = mimeHeaders.getHeader(HEADER_CONTENT_TYPE);
    final String contentType;
    if (contentTypeHeader != null && contentTypeHeader.length > 0) {
      contentType = mimeHeaders.getHeader(HEADER_CONTENT_TYPE)[0];
    } else {
      contentType = reqMessage.getContentType(msgContext.getSOAPConstants());
    }

    // We require a content type.
    if (isNullOrEmpty(contentType)) {
      throw new AxisFault(Messages.getMessage("missingContentType"));
    }

    final String action;
    if (msgContext.useSOAPAction()) {
      action = firstNonNull(msgContext.getSOAPActionURI(), "");
    } else {
      action = "";
    }

    StringBuilder headers = new StringBuilder()
        .append(createHeader(HEADER_CONTENT_TYPE, contentType))
        .append(createHeader(HEADER_ACCEPT, ACCEPT_HEADERS))
        .append(createHeader(HEADER_USER_AGENT, USER_AGENT_HEADERS))
        .append(CACHE_HEADERS)
        .append(createHeader(HEADER_HOST, host + portSuffix))
        .append(createHeader(HEADER_SOAP_ACTION, "\"" + action + "\""))
        .append(createHeader(HEADER_CONTENT_LENGTH, contentLength))
        .append(createHeader(HEADER_CONNECTION, HEADER_CONNECTION_CLOSE));

    // Transfer MIME headers of SOAPMessage to HTTP headers.
    @SuppressWarnings("unchecked")
    Iterator<MimeHeader> mimeHeaderIterator = mimeHeaders.getAllHeaders();
    while (mimeHeaderIterator.hasNext()) {
      MimeHeader mimeHeader = mimeHeaderIterator.next();
      String headerName = mimeHeader.getName();
      if (headerName.equals(HEADER_CONTENT_TYPE)
          || headerName.equals(HEADER_SOAP_ACTION)) {
        continue;
      }
      headers.append(createHeader(mimeHeader.getName(), mimeHeader.getValue()));
    }

    String userId = msgContext.getUsername();
    String password = msgContext.getPassword();

    // We try to pick off the userId from the URL if it's not in the context.
    if (userId == null && transportUrl.getUserInfo() != null) {
      String info = transportUrl.getUserInfo();
      int sep = info.indexOf(':');

      if ((sep >= 0) && (sep + 1 < info.length())) {
        userId = info.substring(0, sep);
        password = info.substring(sep + 1);
      } else {
        userId = info;
      }
    }

    // If we have a userId, then we add a basic auth header.
    if (userId != null) {
      String basicAuthHeader = new StringBuilder()
          .append(userId)
          .append(":")
          .append((password == null) ? "" : password)
          .toString();

      String authentication = " Basic " + Base64.encode(
          basicAuthHeader.getBytes(HEADER_DEFAULT_CHAR_ENCODING));

      headers.append(createHeader(HEADER_AUTHORIZATION, authentication));
    }

    if (msgContext.getMaintainSession()) {
      headers.append(createCookieHeader(msgContext, CookieHeader.COOKIE));
      headers.append(createCookieHeader(msgContext, CookieHeader.COOKIE2));
    }

    @SuppressWarnings("unchecked")
    Map<String, String> userHeaderTable =
        (Map<String, String>) msgContext.getProperty(REQUEST_HEADERS);

    if (userHeaderTable != null) {
      for (Entry<String, String> entry : userHeaderTable.entrySet()) {

        String key = entry.getKey();
        // I don't know if null should be expected or if we should throw an
        // exception if we see it...I'll leave it defensive to mirror axis code.
        if (key == null) {
          continue;
        } else {
          key = key.trim();
        }
        String value = entry.getValue();
        if (value == null) {
          logger.warning("Not adding null-valued key to headers: " + key);
          continue;
        } else {
          value = value.trim();
        }

        
        if (key.equalsIgnoreCase(HEADER_TRANSFER_ENCODING)) {
          if (value.equalsIgnoreCase(HEADER_TRANSFER_ENCODING_CHUNKED)) {
            // From app engine docs:
            // http://code.google.com/appengine/docs/java/urlfetch/
            // "Chunked and hanging requests, however, are not supported"
            throw new UnsupportedOperationException(
                "Chunked encoding is not supported, yet it was detected within "
                + userHeaderTable);
          }
        }

        if (key.equalsIgnoreCase(HEADER_EXPECT)) {
          if (value.equalsIgnoreCase(HEADER_EXPECT_100_Continue)) {
            // From app engine docs:
            // http://code.google.com/appengine/docs/java/urlfetch/
            // "Chunked and hanging requests, however, are not supported"
            throw new UnsupportedOperationException(
                "100-Continues not supported, yet it was detected within "
                + userHeaderTable);
          }
        }

        headers.append(createHeader(key, value));
      }
    }

    // From here we create the http request using the parsed data from above.
    HTTPRequest httpRequest = new HTTPRequest(transportUrl, HTTPMethod.POST);
    Map<String, String> requestHeaders = parseHeadersToMap(headers.toString());
    for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
      httpRequest.addHeader(new HTTPHeader(entry.getKey(), entry.getValue()));
    }

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    reqMessage.writeTo(byteArrayOutputStream);
    httpRequest.setPayload(byteArrayOutputStream.toByteArray());

    if (logger.isLoggable(Level.FINEST)) {
      logger.finest(
          "Sending request:\n"
          + headers + new String(byteArrayOutputStream.toByteArray()));
    }

    return httpRequest;
  }

  void processHttpResponse(
      HTTPResponse httpResponse, MessageContext msgContext) throws IOException {

    int returnCode = httpResponse.getResponseCode();
    ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    for (HTTPHeader httpHeader : httpResponse.getHeaders()) {
      builder.put(httpHeader.getName(), httpHeader.getValue());
    }
    ImmutableMap<String, String> headers = builder.build();

    String contentType = extractHeaderValue(headers, HEADER_CONTENT_TYPE_LC);
    String location = extractHeaderValue(headers, HEADER_LOCATION_LC);
    String contentLength =
        extractHeaderValue(headers, HEADER_CONTENT_LENGTH_LC);

    int length =
        isNullOrEmpty(contentLength) ? -1 : Integer.parseInt(contentLength);

    if ((returnCode >= HttpURLConnection.HTTP_OK) &&
        (returnCode < HttpURLConnection.HTTP_MULT_CHOICE)) {

      if (returnCode == HttpURLConnection.HTTP_ACCEPTED || length == 0) {
        logger.info("No content for valid return code of " + returnCode);
        return;
      }
    } else if (msgContext.getSOAPConstants().equals(SOAP12_CONSTANTS)) {
      // If we're SOAP 1.2, fall through, since the range of
      // valid result codes is much greater
    } else if (!isNullOrEmpty(contentType)
        && !contentType.startsWith("text/html")
        && ((returnCode >= HttpURLConnection.HTTP_INTERNAL_ERROR)
            && (returnCode < 600))) {
      // SOAP Fault should be in here - so fall through
    } else if (!isNullOrEmpty(location)
        && ((returnCode == HttpURLConnection.HTTP_MOVED_TEMP)
            || (returnCode == 307))) {
      // Temporary Redirect (HTTP: 302/307)
      // remove former result and set new target url
      msgContext.removeProperty(MC_HTTP_STATUS_CODE);
      msgContext.setProperty(MessageContext.TRANS_URL, location);
      // Try again after the redirect
      invoke(msgContext);
    } else if (returnCode == 100) {
      msgContext.removeProperty(MC_HTTP_STATUS_CODE);
      msgContext.removeProperty(MC_HTTP_STATUS_MESSAGE);
      processHttpResponse(httpResponse, msgContext);
      return;
    } else {
      // We see an unknown return code so we throw a soap fault.
      byte[] content = httpResponse.getContent();
      String detail = content == null ? "no detail" : new String(content);

      String statusMessage = msgContext.getStrProp(MC_HTTP_STATUS_MESSAGE);
      AxisFault fault = new AxisFault(
          "HTTP", "(" + returnCode + ")" + statusMessage, null, null);
      fault.setFaultDetailString(
          Messages.getMessage("return01", "" + returnCode, detail));
      fault.addFaultDetail(Constants.QNAME_FAULTDETAIL_HTTPERRORCODE,
          Integer.toString(returnCode));
      throw fault;
    }

    String contentLocation =
        extractHeaderValue(headers, HEADER_CONTENT_LOCATION_LC);

    String transferEncoding = headers.get(HEADER_TRANSFER_ENCODING_LC);
    if (transferEncoding != null) {
      transferEncoding = transferEncoding.trim().toLowerCase();
      if (transferEncoding.equals(HEADER_TRANSFER_ENCODING_CHUNKED)) {
        throw new UnsupportedOperationException(
            "Chunked encoding is unsupported");
      }
    }

    // Some soap processors will return a 200 instead of a 202 even when there
    // isn't a soap envelope (ie. length = 0), but we allow those through.
    if (!isNullOrEmpty(contentLength) && contentLength.equals("0")) {
      logger.info("No content for successful call");
      return;
    }

    // Pull the results from the response and populate the Axis message.
    Message outMsg = new Message(
        httpResponse.getContent(), false, contentType, contentLocation);

    // Transfer HTTP headers of HTTP message to MIME headers of SOAP message
    MimeHeaders mimeHeaders = outMsg.getMimeHeaders();

    for (Map.Entry<String, String> entry : headers.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      mimeHeaders.addHeader(key, value.trim());
    }

    outMsg.setMessageType(Message.RESPONSE);
    msgContext.setResponseMessage(outMsg);
  }

  /**
   * This method returns either the corresponding trimmed string value for the
   * given key within the given headers, or an empty string if either no value
   * or a null value is found.
   */
  String extractHeaderValue(Map<String, String> headers, String key) {
    return firstNonNull(headers.get(key), "").trim();
  }

  /**
   * Returns a string formatted as a http header, which is:
   * <pre>{@code <name>: <value>\r\n}</pre>
   */
  String createHeader(String name, String value) {
    checkNotNull(name, "Header name is null with value %s", value);
    checkNotNull(value, "Header value is null for name %s", name);

    return new StringBuilder()
        .append(name)
        .append(": ")
        .append(value)
        .append(NEWLINE)
        .toString();
  }

  /**
   * This method parses the given header string into {key, value} pairs that
   * are returned as a map for sake of convenient iteration when constructing
   * the {@link HTTPRequest} to send through the {@link URLFetchService}.
   * <p>
   * The returned map has an iteration order that is consistent with the
   * order in which header pairs are encountered in the given header string.
   */
  Map<String, String> parseHeadersToMap(String headerText) {

    Map<String, String> requestHeaders = Maps.newLinkedHashMap();
    // We split the headers into each of its lines...
    Iterable<String> headers =
        Splitter.on(NEWLINE).omitEmptyStrings().split(headerText);

    // ...then we split each line into key:value pairs
    Splitter splitter = Splitter.on(':').trimResults();
    for (String header : headers) {
      Iterator<String> iterator = splitter.split(header).iterator();
      if (!iterator.hasNext()) {
        logger.warning("Malformed header, no name: " + header);
        continue;
      }

      String headerName = iterator.next();
      if (!iterator.hasNext()) {
        logger.warning("Malformed header, no value: " + header);
        continue;
      }
      // This allows header values such as "host:port"
      List<String> remainingValues = Lists.newArrayList(iterator);
      String headerValue = Joiner.on(':').join(remainingValues);

      requestHeaders.put(headerName, headerValue);
    }

    return requestHeaders;
  }

  /** Contains the header keys that relate to cookies. */
  enum CookieHeader {
    COOKIE(HTTPConstants.HEADER_COOKIE),
    COOKIE2(HTTPConstants.HEADER_COOKIE2);

    final String headerKey;

    CookieHeader(String headerKey) {
      this.headerKey = headerKey;
    }
  }

  String createCookieHeader(
      MessageContext msgContext, CookieHeader cookieHeader) {
    String headerKey = cookieHeader.headerKey;
    Object cookieValue = msgContext.getProperty(headerKey);
    if (cookieValue != null) {
      // If we have multiple values we concat them into one value.
      if (cookieValue instanceof String[]) {
        String[] cookies = (String[]) cookieValue;
        String value = Joiner.on(';').join(cookies);
        return createHeader(headerKey, value);
      } else {
        return createHeader(headerKey, (String) cookieValue);
      }
    }
    return "";
  }
}

