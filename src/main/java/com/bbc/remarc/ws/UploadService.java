package com.bbc.remarc.ws;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

public interface UploadService {

	public Response upload(HttpServletRequest request);

}
