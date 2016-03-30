package com.bbc.remarc.ws;

import java.util.HashMap;

import javax.ws.rs.core.Response;

public interface AudioService {

	/**
	 * Retrieve audio metadata
	 * @param id identifier
	 * @return audio metadata (JSON)
	 */
	public Response getAudioMetadata(String id);
	
	/**
	 * Updates audio metadata
	 * @param id identifier
	 * @param json JSON request data parsed into HashMap
	 * @return HTTP response
	 */
	public Response updateAudioMetadata(String id, HashMap<String, String> json);
	
	/**
	 * Deletes audio metadata (and associated resources)
	 * @param id identifier
	 * @return HTTP response
	 */
	public Response deleteAudioMetadata(String id);
	
	/**
	 * Search for random audio metadata  
	 * @param limit maximum to return
	 * @param decade decade of audio
	 * @param theme theme category of audio
	 * @return collection of metadata as JSON 
	 */
	public Response find(Long limit,
			String decade,
			String theme);
}
