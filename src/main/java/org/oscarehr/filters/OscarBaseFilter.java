/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Base filter class for Oscar applications providing a common path exclusion logic.
 */
public abstract class OscarBaseFilter implements Filter {

    private static final Set<String> EXCLUDED_PATHS = new HashSet<>();

    static {
        EXCLUDED_PATHS.add("/ops/");
    }

    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String contextPath = httpRequest.getContextPath();
            if (isURLExcludedForCustomFilters(httpRequest.getRequestURI(), contextPath)) {
                chain.doFilter(request, response);
                return;
            }
        }

        this.doFilterInternal(request, response, chain);
    }

    /**
     * Wrapper filter method for generic servlet requests.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the filter chain
     * @throws IOException if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    protected abstract void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException;

    /**
     * Checks if the given request URI is in the list of excluded paths.
     *
     * @param requestURI  the URI of the current request
     * @param contextPath the context path of the web application
     * @return true if the URI is excluded, false otherwise
     */
    private static boolean isURLExcludedForCustomFilters(String requestURI, String contextPath) {
        return EXCLUDED_PATHS.stream()
                .map(contextPath::concat)
                .anyMatch(requestURI::startsWith);
    }

}
