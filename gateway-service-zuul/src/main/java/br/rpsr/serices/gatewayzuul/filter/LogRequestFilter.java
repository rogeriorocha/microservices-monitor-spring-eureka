package br.rpsr.serices.gatewayzuul.filter;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.HttpServletRequestWrapper;

@Component
public class LogRequestFilter extends ZuulFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogRequestFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = new HttpServletRequestWrapper(ctx.getRequest());

		LOGGER.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

		String requestData = null;
		JSONParser jsonParser = new JSONParser();
		JSONObject requestJson = null;
		
		for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();) {
		    String nextHeaderName = e.nextElement();
		    String headerValue = request.getHeader(nextHeaderName);
		    LOGGER.info(nextHeaderName+ "="+headerValue);
		}		

		try {
			if (request.getContentLength() > 0) {
				requestData = CharStreams.toString(request.getReader());
			}

			if (requestData == null) {
				return null;
			}
			requestJson = (JSONObject) jsonParser.parse(requestData);
		} catch (Exception e) {
			LOGGER.error("Error parsing JSON request", e);
			return null;
		}

		LOGGER.info(String.format("%s request payload %s", request.getMethod(), requestJson.toJSONString()));

		return null;
	}

	@Override
	public String filterType() {

		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {

		return FilterConstants.SIMPLE_HOST_ROUTING_FILTER_ORDER - 1;
	}

}
