/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.light.api;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import jb.light.support.Current;
import jb.light.support.Data;
import jb.light.support.Switch;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Jan
 */
@Path("Survey")
public class SurveyR {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SurveyR
     */
    public SurveyR() {
    }

    /**
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        Data lData;
        List<Switch> lSwitches;
        Current lCurrent;
        JSONArray lSwitchesX;
        JSONObject lResult;
        int lCount;

        lData = new Data();
        lSwitches = lData.xSwitches();
        lSwitchesX = new JSONArray();
        for (lCount=0; lCount< lSwitches.size(); lCount++) {
            lSwitchesX.put(lSwitches.get(lCount).xSwitch());            
        }
        lResult = new JSONObject();
        lCurrent = lData.xCurrent();
        lResult.put("currenttime", ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        lResult.put("sunset", lCurrent.xSunset().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        lResult.put("lightoff", lCurrent.xLightOff().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        lResult.put("fase", lCurrent.xFase());
        lResult.put("lightreading", lCurrent.xLightReading());
        lResult.put("switches", lSwitchesX);
        lData.xClose();
        return lResult.toString();
    }
}
