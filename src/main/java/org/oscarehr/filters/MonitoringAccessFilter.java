/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.oscarehr.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.oscarehr.util.MiscUtils;

/**
 * Restricts the JavaMelody monitoring page to users with an authenticated Oscar session.
 * <p>
 * The JavaMelody MonitoringFilter is mapped first in the filter chain and serves the
 * monitoring page itself, so the regular LoginFilter never sees those requests. This
 * filter must therefore be mapped to the monitoring URL ahead of the MonitoringFilter.
 */
public class MonitoringAccessFilter implements Filter {

	private static final Logger logger = MiscUtils.getLogger();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("Starting Filter : " + getClass().getSimpleName());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpSession session = httpRequest.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			logger.warn("Unauthenticated request to monitoring page denied from " + httpRequest.getRemoteAddr());
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/logout.jsp");
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
