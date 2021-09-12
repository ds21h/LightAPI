/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.light.api;

import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jb.light.support.Data;
import jb.light.support.Switch;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author ds21h
 */
public class SwitchR {

    private String mId;

    /**
     * Creates a new instance of SwitchR
     */
    private SwitchR(String pId) {
        mId = pId;
    }

    /**
     * Do not use!
     * Just to avoid the "no valid constructor" error on loading
     */
    public SwitchR(){
        mId = "";
    }
    
    /**
     * Get instance of the SwitchR
     */
    public static SwitchR getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of SwitchR class.
        return new SwitchR(id);
    }

    /**
     * Retrieves representation of an instance of jb.light.api.SwitchR
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        Data lData;
        Switch lSwitch;
        JSONObject lSwitchJ;

        lData = new Data();
        lSwitch = lData.xSwitch(mId);
        lData.xClose();
        lSwitchJ = lSwitch.xSwitch();
        return lSwitchJ.toString();
    }

    /**
     * PUT method for updating or creating an instance of SwitchR
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putJsonIn(String pRequest) {
        JSONObject lRequest;
        JSONObject lReply;
        JSONObject lSwitchJ;
        String lAction;
        Data lData;
        Switch lSwitchIn;
        Switch lSwitch;
        String lResult;
        String lText;
        String lLanguage;

        try {
            lRequest = new JSONObject(pRequest);
        } catch (JSONException pExc) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        lAction = lRequest.optString("action", "");
        lLanguage = lRequest.optString("lang", "").toLowerCase();
        lSwitchJ = lRequest.optJSONObject("switch");
        if (lSwitchJ == null) {
            lSwitchIn = new Switch();
        } else {
            lSwitchIn = new Switch(lSwitchJ);
        }
        lReply = new JSONObject();
        if (mId.equals("")) {
            lReply.put("result", "NOK");
            lReply.put("descr", Message.xMessage("NoID", lLanguage));
            return Response.ok(lReply.toString()).build();
        }
        if (!mId.equals(lSwitchIn.xName())) {
            lReply.put("result", "NOK");
            lReply.put("descr", Message.xMessage("InconsistentID", lLanguage));
            return Response.ok(lReply.toString()).build();
        }
        lData = new Data();
        switch (lAction) {
            case "modify":
                lSwitch = lData.xSwitch(mId);
                if (lSwitch.xName().equals(mId)) {
                    if (lSwitch.xIsEqual(lSwitchIn)) {
                        lResult = "OK";
                        lText = Message.xMessage("NoChange", lLanguage);
                    } else {
                        if (lSwitchIn.xTestSwitch() == Switch.ResultOK) {
                            lData.xChangeSwitch(lSwitchIn);
                            lResult = "OK";
                            lText = Message.xMessage("SwChanged", lLanguage);
                        } else {
                            lResult = "NOK";
                            lText = Message.xMessage("SwIncorr", lLanguage);
                        }
                    }
                } else {
                    lResult = "NOK";
                    lText = Message.xMessage("NoSw", lLanguage);
                }
                break;
            case "new":
                lSwitch = lData.xSwitch(mId);
                if (lSwitch.xName().equals(mId)) {
                    lResult = "NOK";
                    lText = Message.xMessage("SwExist", lLanguage);
                } else {
                    if (lSwitchIn.xTestSwitch() == Switch.ResultOK) {
                        lData.xNewSwitch(lSwitchIn);
                        lResult = "OK";
                        lText = Message.xMessage("SwCreated", lLanguage);
                    } else {
                        lResult = "NOK";
                        lText = Message.xMessage("SwIncorr", lLanguage);
                    }
                }
                break;
            case "delete":
                lSwitch = lData.xSwitch(mId);
                if (lSwitch.xName().equals(mId)) {
                    lData.xDeleteSwitch(mId);
                    lResult = "OK";
                    lText = Message.xMessage("SwDel", lLanguage);
                } else {
                    lResult = "NOK";
                    lText = Message.xMessage("NoSw", lLanguage);
                }
                break;
            default:
                lResult = "NOK";
                lText = Message.xMessage("ActUnsupp", lLanguage);
                break;
        }
        lReply.put("result", lResult);
        lReply.put("descr", lText);
        if (lResult.equals("OK")) {
            lReply.put("switch", lSwitchJ);
        }
        return Response.ok(lReply.toString()).build();
    }
}
