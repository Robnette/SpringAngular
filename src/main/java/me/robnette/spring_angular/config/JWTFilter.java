package me.robnette.spring_angular.config;

import me.robnette.spring_angular.exception.ForbiddenException;
import me.robnette.spring_angular.util.Util;
import me.robnette.spring_angular.util.Constant;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTFilter extends GenericFilterBean {


	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String authHeader = request.getHeader(Constant.AUTHORIZATION_HEADER);
		if (authHeader == null || !authHeader.startsWith(Constant.BEARER_AUTHENTICATION)) {
			((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header.");
		} else {
			try {
				String token = authHeader.substring(Constant.BEARER_AUTHENTICATION.length());
				SecurityContextHolder.getContext().setAuthentication(Util.getAuthentication(token));
				filterChain.doFilter(req, res);
			} catch (SignatureException e) {
				((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
			} catch (ForbiddenException e) {
				((HttpServletResponse) res).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			}

		}
	}
}
