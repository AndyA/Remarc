package com.bbc.remarc.util;

public class Configuration {

	public static final String ENV_MONGO_USER_LOCAL = "LOCAL_MONGODB_DB_USERNAME";
	public static final String ENV_MONGO_PWD_LOCAL = "LOCAL_MONGODB_DB_PWD";
	public static final String ENV_MONGO_DB_LOCAL = "LOCAL_MONGODB_DB_DB";
	
	public static final String ENV_MONGO_USER_OPENSHIFT = "OPENSHIFT_MONGODB_DB_USERNAME";
	public static final String ENV_MONGO_PWD_OPENSHIFT = "OPENSHIFT_MONGODB_DB_PASSWORD";
	public static final String ENV_MONGO_DB_OPENSHIFT = "OPENSHIFT_APP_NAME";
	public static final String ENV_MONGO_HOST_OPENSHIFT = "OPENSHIFT_MONGODB_DB_HOST";
	
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
