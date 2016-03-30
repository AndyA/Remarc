package com.bbc.remarc.ws;

import java.util.HashMap;

import javax.ws.rs.core.Response;

public interface VideoService {
	
	/**
	 * Retrieve video metadata
	 * @param id identifier
	 * @return audio metadata (JSON)
	 */
	public Response getVideoMetadata(String id);
	
	/**
	 * Updates video metadata
	 * @param id identifier
	 * @param json JSON request data parsed into HashMap
	 * @return HTTP response
	 */
	public Response updateVideoMetadata(String id, HashMap<String, String> json);
	
	/**
	 * Deletes video metadata (and associated resources)
	 * @param id identifier
	 * @return HTTP response
	 */
	public Response deleteVideoMetadata(String id);
	
	/**
	 * Search for random video metadata  
	 * @param limit maximum to return
	 * @param decade decade of video
	 * @param theme theme category of video
	 * @return collection of metadata as JSON 
	 */
	public Response find(Long limit,
			String decade,
			String theme);

}
