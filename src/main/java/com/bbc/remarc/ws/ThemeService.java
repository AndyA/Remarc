package com.bbc.remarc.ws;

import java.util.List;

import javax.ws.rs.core.Response;

public interface ThemeService {

	/**
	 * Update all themes
	 * @param json JSON array containing new values for all 10 themes
	 * @return HTTP Response
	 */
	public Response updateThemes(List<String> json);
	
	/**
	 * Get all themes
	 * @return collection of themes as JSON 
	 */
	public Response find();
}
