package com.botham.gdrive;

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


public class QuickstartV3 {
	
	static Logger log = Logger.getLogger(QuickstartV3.class.getName());
	
    
    private static final String APPLICATION_NAME = "Drive API Java Quickstart";
    
    private static final String GDRIVE_USER = "fncserver";
    //private static final String GDRIVE_USER = "231saleln";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/"+GDRIVE_USER);

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

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
    


    static {
        try {
        	String mName="static";
        	
        	if (log.isDebugEnabled()) {
        	   log.debug(mName+" Starts");
        	}
            
        	if (DELETE_STORE) {
        	   java.io.File credentialFile = new java.io.File(DATA_STORE_DIR+"/"+"StoredCredential");
        	
               if (credentialFile.exists()) {
                  if (log.isDebugEnabled()) {
             	     log.debug(mName+" remove "+credentialFile.getPath());
             	  }
            	  credentialFile.delete();
               } else {
               }
            } else {
               
            }
        	
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);

            
            
            
            
        	if (log.isDebugEnabled()) {
         	   log.debug(mName+" Ends");
         	}
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
    	String mName="authorize";
    	
    	if (log.isDebugEnabled()) {
     	   log.debug(mName+" Starts");
     	}
    	
        // Load client secrets.
    	
    	String jsonFile="";
    	jsonFile="/clientFiles/news_gdrive_"+GDRIVE_USER+".json"; // 929578514001
    	//jsonFile="/clientFiles/news_gdrive_fncserver.json";

    	if (log.isDebugEnabled()) {
    		log.debug(mName+" Loading client secs from "+jsonFile);
    	}
    	
        InputStream in = QuickstartV3.class.getResourceAsStream(jsonFile);
        
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        
        System.out.println("clientSecrets="+clientSecrets.toPrettyString());
        //clientSecrets.

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        
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
    public static Drive getDriveService() throws IOException {
    	String mName="getDriveService";
    	
    	if (log.isDebugEnabled()) {
     	   log.debug(mName+" Starts");
     	}
    	
        Credential credential = authorize();
        
    	if (log.isDebugEnabled()) {
      	   log.debug(mName+" Ends");
      	}
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    public static void main(String[] args) throws IOException {
    	String mName="main";
    	if (log.isDebugEnabled()) {
       	   log.debug(mName+" Starts");
       	}

    	about();
    	listFiles();
    	emptyTrash();
    	

    	
    	if (log.isDebugEnabled()) {
           log.debug(mName+" Ends");
        }
    }
    	
    public static void listFiles() throws IOException {
    	String mName="listFiles";
    	if (log.isDebugEnabled()) {
            log.debug(mName+" Starts");
         }
    	
    	//newsclips2017-08-16_2330.zip
    	
        // Build a new authorized API client service.
        Drive service = getDriveService();

        // Print the names and IDs for up to 10 files.
        String searchQuery="name='newsclips2017-08-16_2330.zip'";
        FileList result = service.files().list().setPageSize(10).setQ(searchQuery).setFields("nextPageToken, files(id, name)").execute();
        
        
        List<File> files = result.getFiles();
        
        
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
        
    	if (log.isDebugEnabled()) {
            log.debug(mName+" Ends");
         }
    }
    
    public static void emptyTrash() throws IOException {
    	String mName="emptyTrash";
        if (log.isDebugEnabled()) {
        	log.debug(mName+" Starts");
        }
    	
        Drive service = getDriveService();
        
        service.files().emptyTrash().execute();
        
        if (log.isDebugEnabled()) {
        	log.debug(mName+" Trash emptied for "+GDRIVE_USER);
        }
        
        //System.out.println(o.toString());
        if (log.isDebugEnabled()) {
        	log.debug(mName+" Ends");
        }
        
    }
    
    public static void about() throws IOException {
    	String mName="about";
    	
    	if (log.isDebugEnabled()) {
      	   log.debug(mName+" Starts");
      	}
    	
        Drive service = getDriveService();
        
        About about = service.about().get().setFields("user, storageQuota").execute();
        
        System.out.println(about.toPrettyString());
        //System.out.println(o.toString());
        
    	if (log.isDebugEnabled()) {
       	   log.debug(mName+" Ends");
       	}
    }

}
