The OpenShift `jbossews` cartridge documentation can be found at:

http://openshift.github.io/documentation/oo_cartridge_guide.html#tomcat

=== OpenShift MongoDB ===
MongoDB:
Root User:     admin
Root Password: tzJ7z2arDeKG
Database Name: remarc

Connection URL: mongodb://$OPENSHIFT_MONGODB_DB_HOST:$OPENSHIFT_MONGODB_DB_PORT/

=== Resources ===
If running locally:
 - On deployment, will create a folder in the web deployment folder called remarc_resources
 - Will copy all samples in src/main/resources into this folder - these are our test files
 - These can be accessed via the url: http://localhost:8080/remarc/remarc_resources/sample_1.jpg
 
If running on OpenShift:
 - On deployment, will find the OPENSHIFT_DATA_DIR folder
 - Will copy all samples into the root of this folder - these are our test files
 - These can be accessed via the same url: http://localhost:8080/remarc/remarc_resources/sample_1.jpg
 - This is made possible by the configuration in .openshift/config/server.xml
 - Need to add this line under <Host>:
   <Context docBase="/var/lib/openshift/556c2fab5973ca1e4e000002/app-root/data" path="/remarc_resources" />
 - NOTE: This docBase URL will be DIFFERENT for other installations. Remember to update it else it'll crash on deployment
 
 === Offline ===
If running locally:
 - Need to add into the server's web.xml:
   <mime-mapping>
		<extension>appcache</extension>
		<mime-type>text/cache-manifest</mime-type>
</mime-mapping>