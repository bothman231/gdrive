package com.botham.gdrive;

import com.botham.gdrive.exception.GdriveException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

public class Utilities {
	
	static Logger log = Logger.getLogger(Utilities.class.getName());
	
    
    private static final String APPLICATION_NAME = "Drive API Java Quickstart";
    
    //private static final String GDRIVE_USER = "fncserver";
    //private static final String GDRIVE_USER = "231saleln";
    private static final String GDRIVE_USER = "kryten4813";
    //private static final String GDRIVE_USER = "petersen4813";
    //private static final String GDRIVE_USER = "bluemidget4813";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/"+GDRIVE_USER);
    
    //private static final java.io.File DATA_STORE_DIR1 = new java.io.File(System.getProperty("user.home"), ".credentials/"+GDRIVE_USER);

    /** Global instance of the {@link FileDataStoreFactory}. */
    public static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);
    
    private static final boolean DELETE_STORE = false;
   
    
	public static void dataStore(String user) {
		
		String mName="dataStore";
		
		if (log.isDebugEnabled()) {
		   log.debug(mName+" Starts, user="+user);
		}
		
		java.io.File DATA_STORE_DIR1 = new java.io.File(System.getProperty("user.home"), ".credentials/" + user);
		
		
		try {
		//	String mName = "dataStore";

			if (log.isDebugEnabled()) {
				log.debug(mName + " Starts");
			}

			if (DELETE_STORE) {
				java.io.File credentialFile = new java.io.File(DATA_STORE_DIR1 + "/" + "StoredCredential");

				if (credentialFile.exists()) {
					if (log.isDebugEnabled()) {
						log.debug(mName + " remove " + credentialFile.getPath());
					}
					credentialFile.delete();
				} else {
				}
			} else {

			}

			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR1);

			if (log.isDebugEnabled()) {
				log.debug(mName + " Ends, DATA_STORE_FACTORY="+DATA_STORE_FACTORY);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

    
    

    public static Credential authorize(String user) throws IOException, GdriveException {
    	String mName="authorize";
    	
    	if (log.isDebugEnabled()) {
     	   log.debug(mName+" Starts");
     	}
    	
        // Load client secrets.
    	
    	String jsonFile="";
    	jsonFile="/clientFiles/news_gdrive_"+user+".json"; // 929578514001
    	//jsonFile="/clientFiles/news_gdrive_fncserver.json";

    	if (log.isDebugEnabled()) {
    		log.debug(mName+" Loading client secs from "+jsonFile);
    	}
    	
    	
    	/*
    	java.io.File file = new java.io.File(jsonFile);
    	
    	
    	if (file.exists()) {
    		
    	} else {
    		throw new GdriveException("No credentials exist for user: "+user);
    	}
    	*/
    	
    	
        InputStream in = Utilities.class.getResourceAsStream(jsonFile);
        
    	if (log.isDebugEnabled()) {
    		log.debug(mName+" in="+in);
    	}
        
    	if (in==null) {
    		throw new GdriveException("No credentials exist for user: "+user);
    	}
    	
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        
        
    	if (log.isDebugEnabled()) {
    		log.debug(mName+" clientSecrets="+clientSecrets);
    		log.debug(mName+" "+clientSecrets.containsKey("project_id"));
    	}
        
      //  System.out.println("clientSecrets="+clientSecrets.toPrettyString());
        //clientSecrets.

        // Build flow and trigger user authorization request.
    	if (log.isErrorEnabled()) {
    		log.error(mName+" DATA_STORE_FACTORY="+DATA_STORE_FACTORY);
    	}
    	
    	GoogleAuthorizationCodeFlow flow = null;
    			
    	try {
           flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
           
    	} catch (Exception e) {
    		log.error(mName+" "+e.getMessage());
    	}
        
    	if (log.isDebugEnabled()) {
    		log.debug(mName+" after flow");
    	}
    	
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        
        //System.out.println("Credentials saved to " + DATA_STORE_DIR1.getAbsolutePath());
        
    	if (log.isDebugEnabled()) {
      	   log.debug(mName+" Ends");
      	}
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService(String user) throws IOException, GdriveException {
    	String mName="getDriveService";
    	
    	if (log.isDebugEnabled()) {
     	   log.debug(mName+" Starts");
     	}
    	
        Credential credential = authorize(user);
        
    	if (log.isDebugEnabled()) {
      	   log.debug(mName+" Ends, credential="+credential.toString());
      	}
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    public static void main(String[] args) throws IOException, GdriveException {
    	String mName="main";
    	if (log.isDebugEnabled()) {
       	   log.debug(mName+" Starts, user="+GDRIVE_USER);
       	}

    	about(GDRIVE_USER);
    	
    	listFiles(GDRIVE_USER);
    	
    	//emptyTrash("fncserver");
    	

    	
    	if (log.isDebugEnabled()) {
           log.debug(mName+" Ends");
        }
    }
    	
    public static GdriveResult listFiles(String user) throws IOException, GdriveException {
    	
    	String mName="listFiles";
    	if (log.isDebugEnabled()) {
            log.debug(mName+" Starts, user="+user);
         }
    	
    	GdriveResult gdriveResult = new GdriveResult();
    	
    	dataStore(user);
    	
    	//newsclips2017-08-16_2330.zip
    	
        // Build a new authorized API client service.
        Drive service = getDriveService(user);

        // Print the names and IDs for up to 10 files.
        
// Example, search for 1 file....        
        String searchQuery="name='newsclips2017-08-16_2330.zip'";
        searchQuery="";
        
        Integer pageSize=99;
       
    	if (log.isDebugEnabled()) {
           log.debug(mName+" Before ex");
        }
    	
        //FileList result = service.files().list().setPageSize(pageSize).setQ(searchQuery).setFields("nextPageToken, files(id, name)").execute();
    	
        //FileList result = service.files().list().setPageSize(pageSize).setFields("nextPageToken, files()").execute();
        
      
        StringBuilder fields = new StringBuilder();
        fields.append("id");
        fields.append(", name");
        fields.append(", trashed");
        fields.append(", kind");
        fields.append(", mimeType");
        
        
        //id, name, trashed, description, kind
        
        FileList result = service.files().list().setPageSize(pageSize).setFields("nextPageToken, files("+fields.toString()+")").execute();
        
    	if (log.isDebugEnabled()) {
           log.debug(mName+" After ex");
        }  
        
        List<File> files = result.getFiles();
        
    	if (log.isDebugEnabled()) {
           log.debug(mName+" Before files check");
        } 
    	
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                //System.out.printf("%s (%s) %s\n", file.getName(), file.getId(), file.getTrashed());
                //System.out.println("file="+file.toPrettyString());
                System.out.println("file="+file.toString());
                		// file.getDescription()); Null
               //file.getMimeType();
                		
            }
        }
        
    	if (log.isDebugEnabled()) {
            log.debug(mName+" Ends, ");
            //log.debug(mName+" Ends, file count="+result.size());
         }
    	
    	gdriveResult.setResultMessage("List files was successful");
    	gdriveResult.setFiles(files);
    	gdriveResult.setOutput("");
    	
    	return gdriveResult;
    }
    
    public static GdriveResult emptyTrash(String user) throws IOException, GdriveException {
    	String mName="emptyTrash";
        if (log.isDebugEnabled()) {
        	log.debug(mName+" Starts");
        }
    	
    	GdriveResult gdriveResult = new GdriveResult();
    	
    	dataStore(user);
    	
        Drive service = getDriveService(user);
        
        service.files().emptyTrash().execute();
        
        if (log.isDebugEnabled()) {
        	log.debug(mName+" Trash emptied for "+user);
        }
        
        //System.out.println(o.toString());
        if (log.isDebugEnabled()) {
        	log.debug(mName+" Ends");
        }
        
    	gdriveResult.setResultMessage("Trash emptied for "+user);
    	
        return gdriveResult;
    }
    
    
    
    
    
    public static GdriveResult about(String user) throws IOException, GdriveException {
    	String mName="about";
    	
    	if (log.isDebugEnabled()) {
      	   log.debug(mName+" Starts, user="+user);
      	}
    	
    	GdriveResult gdriveResult = new GdriveResult();
    	
    	dataStore(user);
    	
        Drive service = getDriveService(user);
        
        About about = service.about().get().setFields("user, storageQuota").execute();
        
        //System.out.println(mName+" toPrettyString="+about.toPrettyString());
        //System.out.println(o.toString());
        
        //gdriveResult.setOuput(about.toString());
        gdriveResult.setOutput("");
        
        
    	if (log.isDebugEnabled()) {
       	   log.debug(mName+" Ends");
       	}
    	
    	gdriveResult.setResultMessage("About was successful");
    	gdriveResult.setAbout(about);
    	
    	return gdriveResult;
        
    }

}
