package br.rpsr.serices.gatewayzuul.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;


@Component
public class LogResponseFilter extends ZuulFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogRequestFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		
		HttpServletRequest request = new HttpServletRequestWrapper(ctx.getRequest());
		
		LOGGER.info(String.format("%s response ", request.getMethod())); 
				
		String responseData;
		try {
			responseData = getResponseData(ctx);
			LOGGER.info("******" + responseData);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		

		return null;

	}

	private String getResponseData(RequestContext ctx) throws IOException {
		String responseData = null;

		final InputStream responseDataStream = ctx.getResponseDataStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream copy = new ByteArrayOutputStream();
		int read = 0;
		byte[] buff = new byte[1024];
		while ((read = responseDataStream.read(buff)) != -1) {
			bos.write(buff, 0, read);
			copy.write(buff, 0, read);
		}
		InputStream isFromFirstData = new ByteArrayInputStream(bos.toByteArray());

		boolean responseGZipped = ctx.getResponseGZipped();
		try {
			InputStream zin = null;
			if (responseGZipped) {
				zin = new GZIPInputStream(isFromFirstData);
				responseData = CharStreams.toString(new InputStreamReader(zin, "UTF-8"));
			} else {
				zin = responseDataStream;
				responseData = copy.toString(); 
			}
			
			ctx.setResponseDataStream(new ByteArrayInputStream(copy.toByteArray()));

		} catch (IOException e) {
			LOGGER.warn("Error reading body {}", e.getMessage());
		}

		return responseData;
	}

	/*
	 * HttpServletRequest request = new HttpServletRequestWrapper(ctx.getRequest());
	 * 
	 * LOGGER.info(String.format("%s response to %s", request.getMethod(),
	 * request.getRequestURL().toString()));
	 * 
	 * 
	 * 
	 * final InputStream responseDataStream = ctx.getResponseDataStream(); try {
	 * final String responseData = CharStreams.toString(new
	 * InputStreamReader(responseDataStream, "UTF-8"));
	 * ctx.setResponseBody(responseData);
	 * 
	 * LOGGER.info(String.format("%s response data %s", request.getMethod(),
	 * responseData)); responseDataStream.close(); } catch (IOException e) {
	 * LOGGER.warn("Error reading body", e); } finally { try {
	 * responseDataStream.close(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 */

	/*
	 * String requestData = null; JSONParser jsonParser = new JSONParser();
	 * JSONObject requestJson = null;
	 */
//		try {
//			if (request.getContentLength() > 0) {
//				requestData = CharStreams.toString(request.getReader());
//			}
//
//			if (requestData == null) {
//				return null;
//			}
//			requestJson = (JSONObject) jsonParser.parse(requestData);
//		} catch (Exception e) {
//			LOGGER.error("Error parsing JSON request", e);
//			return null;
//		}

	// LOGGER.info(String.format("%s response payload %s", request.getMethod(),
	// requestJson.toJSONString()));

	@Override
	public String filterType() {
		return FilterConstants.POST_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.SIMPLE_HOST_ROUTING_FILTER_ORDER - 1;
	}

}
