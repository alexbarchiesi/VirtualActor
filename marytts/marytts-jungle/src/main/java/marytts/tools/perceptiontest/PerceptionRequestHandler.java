/**
 * Copyright 2007 DFKI GmbH.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * This file is part of MARY TTS.
 *
 * MARY TTS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package marytts.tools.perceptiontest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import marytts.server.http.BaseHttpRequestHandler;
import marytts.util.http.Address;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.nio.entity.NStringEntity;
import org.xml.sax.SAXException;

/**
 * 
 * @author Sathish Pammi
 */
public class PerceptionRequestHandler extends BaseHttpRequestHandler
{
    private DataRequestHandler infoRH;
    private UserRatingStorer userRatingRH;
    
    public PerceptionRequestHandler()
    {
        super();
        
        //Add extra initialisations here
    }
    
    public PerceptionRequestHandler(DataRequestHandler infoRH, UserRatingStorer userRatingRH) {
        super();
        this.infoRH = infoRH;
        this.userRatingRH = userRatingRH;
    }

    @Override
    protected void handleClientRequest(String absPath, Map<String,String> queryItems, HttpResponse response, Address serverAddressAtClient)
    throws IOException
    {
        
        Set<String> keySet = queryItems.keySet();
        
        if(absPath.equals("/perceptionTest") && keySet.size() > 0){
            
            String infoResponse = null;
            
            if(keySet.size() == 1 && keySet.contains("EMAIL")){
                infoResponse = getCaseOneInfoResponse(queryItems);
            } 
            else if ( keySet.size() > 1 
                    && keySet.contains("EMAIL")
                    && keySet.contains("RESULTS")
                    && keySet.contains("PRESENT_SAMPLE_BASENAME")
                    && keySet.contains("PRESENT_SAMPLE_NUMBER") )
            {
                infoResponse = storeUserRatings(queryItems);
            }
            else if ( keySet.size() > 1 
                    && keySet.contains("EMAIL") 
                    && keySet.contains("PRESENT_SAMPLE_NUMBER") )
            {
                infoResponse = getCaseTwoInfoResponse(queryItems);
            }
            
            if (infoResponse == null) { // error condition, handleInfoRequest has set an error message
                return;
            }

            response.setStatusCode(HttpStatus.SC_OK);
            try {
                NStringEntity entity = new NStringEntity(infoResponse, "UTF-8");
                entity.setContentType("text/plain; charset=UTF-8");
                response.setEntity(entity);
            } catch (UnsupportedEncodingException e){}
        }
        
    }
    
    private String storeUserRatings(Map<String, String> queryItems) {
        //String infoResponce = getCaseTwoInfoResponse(queryItems);
        //System.out.println(queryItems.get("RESULTS"));
        String eMailID = queryItems.get("EMAIL");
        int presentSampleNumber = (new Integer(queryItems.get("PRESENT_SAMPLE_NUMBER"))).intValue();
        String baseName = queryItems.get("PRESENT_SAMPLE_BASENAME");
        String result = queryItems.get("RESULTS");
        
        try {
            userRatingRH.writeSampleResult(eMailID, presentSampleNumber, baseName, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    private String getCaseOneInfoResponse(Map<String, String> queryItems) {
        
        String eMailID = queryItems.get("EMAIL").trim();
        
        
        int presentSampleNumber = 0;
        try {
            presentSampleNumber = this.userRatingRH.ratingsDoneSoFar(eMailID);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String infoResponse = queryItems.get("EMAIL")+"\n";
        infoResponse += this.infoRH.getNumberOfSamples()+"\n"; 
        infoResponse += presentSampleNumber +"\n";
        if ( presentSampleNumber < this.infoRH.getNumberOfSamples() ) {
            infoResponse += this.infoRH.getSampleBaseName(presentSampleNumber)+"\n";
        }
        else infoResponse += "DummyBaseName\n";
        infoResponse += this.infoRH.getOptions()+"\n";
        infoResponse += this.infoRH.getQueryStatement()+"\n";
        
        return infoResponse;
    }
    
    private String getCaseTwoInfoResponse(Map<String, String> queryItems) {
        
        String sampleNumber = queryItems.get("PRESENT_SAMPLE_NUMBER");
        int presentSampleNumber = (new Integer(sampleNumber)).intValue();
        
        if(presentSampleNumber >= this.infoRH.getNumberOfSamples() ){
            return null;
        }
        
        String infoResponse = queryItems.get("EMAIL")+"\n";
        infoResponse += this.infoRH.getNumberOfSamples()+"\n";
        infoResponse += presentSampleNumber+"\n";
        infoResponse += this.infoRH.getSampleBaseName(presentSampleNumber)+"\n";
        infoResponse += this.infoRH.getOptions()+"\n";
        infoResponse += this.infoRH.getQueryStatement()+"\n";
        
        return infoResponse;
    }
}

