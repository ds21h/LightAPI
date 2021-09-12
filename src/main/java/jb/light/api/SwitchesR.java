/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.light.api;

import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import jb.light.support.Data;
import jb.light.support.Switch;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author ds21h
 */
@Path("/Switches")
public class SwitchesR {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SwitchesR
     */
    public SwitchesR() {
    }

    /**
     * Retrieves representation of an instance of jb.light.api.SwitchesR
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        Data lData;
        List<Switch> lSwitches;
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
        lResult.put("switches", lSwitchesX);
        lData.xClose();
        return lResult.toString();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public SwitchR getSwitchR(@PathParam("id") String id) {
        return SwitchR.getInstance(id);
    }
}
