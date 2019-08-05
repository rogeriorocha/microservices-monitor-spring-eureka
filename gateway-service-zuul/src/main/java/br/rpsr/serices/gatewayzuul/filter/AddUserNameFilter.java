package br.rpsr.serices.gatewayzuul.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class AddUserNameFilter extends ZuulFilter {

	public static final String USER_HEADER = "X-User-Header";

	@Override
	public String filterType() { 
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.SIMPLE_HOST_ROUTING_FILTER_ORDER - 10;
	}

	@Override
	public boolean shouldFilter() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return authentication != null && authentication.getPrincipal() != null;
	}

	@Override
	public Object run() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		String requestURI = (String) requestContext.get("requestURI"); 
 		
		if (requestURI.startsWith("/auth"))
			return null;
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			Object principal = auth.getPrincipal();
			if (principal != null) {
				//UserDetails userDetails = (UserDetails) principal;

				requestContext.addZuulRequestHeader(USER_HEADER, (String) principal); //userDetails.getUsername());
				
				
			}
			
		}
		

		return null;
	}
}
