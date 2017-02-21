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

package com.bbc.remarc.util;

public class Configuration {

	public static final String ENV_MONGO_USER_LOCAL = "LOCAL_MONGODB_DB_USERNAME";
	public static final String ENV_MONGO_PWD_LOCAL = "LOCAL_MONGODB_DB_PWD";
	public static final String ENV_MONGO_DB_LOCAL = "LOCAL_MONGODB_DB_DB";
	public static final String ENV_MONGO_HOST_LOCAL = "LOCAL_MONGODB_DB_HOST";
	public static final String ENV_MONGO_PORT_LOCAL = "LOCAL_MONGODB_DB_PORT";
	
	public static final String ENV_MONGO_USER_OPENSHIFT = "OPENSHIFT_EXTMONGODB_DB_USERNAME";
	public static final String ENV_MONGO_PWD_OPENSHIFT = "OPENSHIFT_EXTMONGODB_DB_PASSWORD";
	public static final String ENV_MONGO_DB_OPENSHIFT = "OPENSHIFT_EXTMONGODB_DB_NAME";
	public static final String ENV_MONGO_HOST_OPENSHIFT = "OPENSHIFT_EXTMONGODB_DB_HOST";
	public static final String ENV_MONGO_PORT_OPENSHIFT = "OPENSHIFT_EXTMONGODB_DB_PORT";
	
	public static final String ENV_DATA_DIR_OPENSHIFT = "OPENSHIFT_DATA_DIR";
	public static final String ENV_DATA_DIR_LOCAL = "LOCAL_DATA_DIR";
	public static final String ATT_DATA_DIR = "resourcePathUri";
	
	public static final String UPLOAD_DIR_NAME = "upload_tmp";
	public static final String UPLOAD_DIR = UPLOAD_DIR_NAME + "/";
	
	public static final String CONTENT_DIR_NAME = "content";
	public static final String CONTENT_DIR = CONTENT_DIR_NAME + "/";
	
	public static final String AUDIO_DIR_NAME = "audio";
	public static final String AUDIO_DIR = AUDIO_DIR_NAME + "/";
	public static final String VIDEO_DIR_NAME = "video";
	public static final String VIDEO_DIR = VIDEO_DIR_NAME + "/";
	public static final String IMAGE_DIR_NAME = "images";
	public static final String IMAGE_DIR = IMAGE_DIR_NAME + "/";
	
	public static final String RELATIVE_BASE_URL = "/remarc_resources/" + CONTENT_DIR;
		
}
