package com.shopme.settings;

import jakarta.servlet.http.HttpServletRequest;

public class Utilities {
		public static String getSiteURL(HttpServletRequest request) {
			String siteURL = request.getRequestURL().toString();

			return siteURL.replace(request.getServletPath(), "");
		}

	}

