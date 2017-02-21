/*
 * Copyright (C) 2016 BBC
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
 */

package com.bbc.remarc.ws;

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Service that allows updating and retrieval of Themes
 *
 * @author Peter Brock
 */
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
