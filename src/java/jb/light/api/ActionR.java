/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.light.api;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import jb.light.support.Action;
import jb.light.support.Current;
import jb.light.support.Data;
import jb.light.support.Switch;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Jan
 */
@Path("Action")
public class ActionR {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ActionR
     */
    public ActionR() {
    }

    /**
     * PUT method for updating or creating an instance of AktieR
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String putXml(String pParams) {
        Parameters lParameters;
        JSONObject lReply;
        int lCount;
        Data lData;
        String lResult;
        int lNumberOK;
        String lText;

        lParameters = new Parameters(pParams);
        lReply = new JSONObject();
        if (lParameters.xNumberPar() == 0) {
            lReply.put("result", "NOK");
            lReply.put("descr", "No parameters found");
            return lReply.toString();
        }
        if (!lParameters.xOK()) {
            lReply.put("result", "NOK");
            lReply.put("descr", "Format error in parameters");
            return lReply.toString();
        }
        
        lData = new Data();
        lNumberOK = 0;
        for (lCount = 0; lCount < lParameters.xNumberPar(); lCount++) {
            lResult = sExecAction(lParameters.xKey(lCount), lParameters.xValue(lCount), lData);
            if (lResult.equals("OK")) {
                lNumberOK++;
            }
        }
        if (lNumberOK==0){
            lResult = "NOK";
            lText = "Action(s) not processed";
        } else {
            if (lNumberOK==lParameters.xNumberPar()){
                lResult = "OK";
                lText = "Action(s) processed";
            } else {
                lResult = "xOK";
                lText = "Action(s) partly processed";
            }
        }
        lReply.put("result", lResult);
        lReply.put("descr", lText);
        return lReply.toString();
    }

    private String sExecAction(String pPar, String pValue, Data pData) {
        Action lAction;
        String lResult;

        switch (pPar) {
            case "switchon": {
                lResult = sTestSwitch(pData, pValue);
                if (lResult.equals("OK")) {
                    lAction = new Action(Action.cActionSwitchOn, pValue);
                    pData.xNewAction(lAction);
                }
                break;
            }
            case "switchoff": {
                lResult = sTestSwitch(pData, pValue);
                if (lResult.equals("OK")) {
                    lAction = new Action(Action.cActionSwitchOff, pValue);
                    pData.xNewAction(lAction);
                }
                break;
            }
            case "lightoff": {
                lResult = sTestLightsOff(pData, pValue);
                if (lResult.equals("OK")) {
                    lAction = new Action(Action.cActionSwitchLightOff, pValue);
                    pData.xNewAction(lAction);
                }
                break;
            }
            case "switchallon": {
                lAction = new Action(Action.cActionSwitchAllOn);
                pData.xNewAction(lAction);
                lResult = "OK";
                break;
            }
            case "switchalloff": {
                lAction = new Action(Action.cActionSwitchAllOff);
                pData.xNewAction(lAction);
                lResult = "OK";
                break;
            }
            default: {
                lResult = "NOK";
                break;
            }
        }
        return lResult;
    }

    private String sTestSwitch(Data pData, String pId) {
        Switch lSwitch;
        String lResult;

        lSwitch = pData.xSwitch(pId);
        if (lSwitch.xName().equals(pId)) {
            if (lSwitch.xActive()) {
                lResult = "OK";
            } else {
                lResult = "NOK";
            }
        } else {
            lResult = "NOK";
        }
        return lResult;
    }

    private String sTestLightsOff(Data pData, String pLightsOff) {
        String lResult;
        ZonedDateTime lLightsOff;
        Current lCurrent;

        try {
            lLightsOff = ZonedDateTime.parse(pLightsOff, DateTimeFormatter.ISO_ZONED_DATE_TIME);
            lCurrent = pData.xCurrent();
            if (lLightsOff.isAfter(lCurrent.xUpdate())) {
                lResult = "NOK";
            } else {
                if (lLightsOff.isBefore(lCurrent.xSunset())) {
                    lResult = "NOK";
                } else {
                    lResult = "OK";
                }
            }
        } catch (DateTimeParseException pExc) {
            lResult = "NOK";
        }

        return lResult;
    }
}
