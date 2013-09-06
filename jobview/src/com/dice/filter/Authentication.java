package com.dice.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: duket
 * Date: 4/11/12
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Authentication implements Filter {
	public void init(FilterConfig filterConfig) throws ServletException { /****/ }

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpSession session = null;
		try {
			session = ((HttpServletRequest)servletRequest).getSession();
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXX: :" + ((HttpServletRequest)servletRequest).getRequestURI());
			if (((HttpServletRequest)servletRequest).getRequestURI().contains("/login.html")) {
				filterChain.doFilter(servletRequest, servletResponse);
			} else  if (session.getAttribute("solrprof.authenticated") == null) {
				((HttpServletResponse)servletResponse).sendRedirect("/solrprof/login.html");
			} else {
				filterChain.doFilter(servletRequest, servletResponse);
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public void destroy() { /****/ }
}
