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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.log4j.Logger;

import com.bbc.remarc.util.Configuration;
import com.bbc.remarc.util.ResourceManager;

@Path("/upload")
public class UploadServiceImpl implements UploadService {

	private static Logger log = Logger.getLogger(UploadServiceImpl.class
			.getSimpleName());

	@Context
	private ServletContext servletContext;

	@POST
	@Produces("application/json")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@Context HttpServletRequest request) {

		log.debug("performing upload");

		final String resourcesPath = (String) servletContext
				.getAttribute("resourcePathUri");

		if (resourcesPath == null) {
			throw new IllegalStateException(
					"Application hasnt got a resources path configured");
		}

		final String uploadFolder = resourcesPath + Configuration.UPLOAD_DIR;
		final String contentFolder = resourcesPath + Configuration.CONTENT_DIR;

		if (ServletFileUpload.isMultipartContent(request)) {

			// Configure a repository (to ensure a secure temp location is used)
			File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
			
			// Create a factory for storing disk-based file items (our archive could be too large for memory)
			DiskFileItemFactory factory = newDiskFileItemFactory(servletContext, repository);

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			try {
				
				// Parse the request
				List<FileItem> items = upload.parseRequest(request);

				if (items.size() == 1) {

					FileItem archive = items.get(0);

					// write the archive to the upload folder
					File uploadedFile = new File(uploadFolder + archive.getName());
					archive.write(uploadedFile);

					log.debug("upload complete");
					
					unzipArchive(uploadedFile, uploadFolder);
					
					log.debug("unzip complete");
					
					uploadedFile.delete();
					
					log.debug("removed uploaded archive");
					
				} else {
					throw new IllegalStateException(
							"Received more file items than expected. Expected 1, got "
									+ items.size());
				}
				
			} catch (Exception e) {
				throw new RuntimeException("Error writing the archive to disk", e);
			}

			log.debug("processing contents of upload");
			
			ResourceManager.processUploadDir(uploadFolder, contentFolder);
		}

		
		URI uri = null;
		try {
			uri = new URI("../index.html");
		} catch (URISyntaxException e) {
			log.error("Error creating admin.html URI: " + e);
		}
		
		Response resp;
		if (uri != null) {
			resp = Response.seeOther(uri).build();
		} else {
			resp = Response.ok().build();
		}
		
		return resp;
	}

	private void unzipArchive(File archive, String destination) {
		
		try {
			
			ZipFile zippedFolder = new ZipFile(archive);
			zippedFolder.extractAll(destination);
			
		} catch (ZipException e) {
			throw new RuntimeException("Error unzipping archive", e);
		}
	}
	
	/**
	 * Helper method to create a DiskFileItemFactory, which includes a cleaning tracker to remove old tmp files
	 * @param context Servlet Context
	 * @param repository Temp Directory
	 * @return created DiskFileItemFactory
	 */
	private DiskFileItemFactory newDiskFileItemFactory(ServletContext context, File repository) {
		FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(context);
		DiskFileItemFactory factory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
		factory.setFileCleaningTracker(fileCleaningTracker);
		return factory;
	}
}
