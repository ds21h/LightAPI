/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.light.api;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jb.light.support.Action;
import jb.light.support.Data;
import jb.light.support.Setting;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Jan
 */
@Path("Setting")
public class SettingR {

    private static final int ResultOK = 0;
    private static final int ResultNoChange = 1;
    private static final int ResultPartOK = 2;
    private static final int ResultError = 9;

    @Context
    private UriInfo mContext;

    private String mLang = "";

    /**
     * Creates a new instance of SettingR
     */
    public SettingR() {
    }

    /**
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        Data lData;
        Setting lSetting;
        JSONObject lSettingJ;

        lData = new Data();
        lSetting = lData.xSetting();
        lData.xClose();
        lSettingJ = lSetting.xSetting();
        return lSettingJ.toString();
    }

    /**
     *
     * @param pRequest representation for the resource
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putJSON(String pRequest) {
        JSONObject lRequest;
        JSONObject lPartRequest;
        JSONObject lReply;
        String lResultS;
        String lText;
        Setting lSetting;
        JSONObject lSettingJ;
        Data lData;
        Action lAction;
        int lResult;
        boolean lChanged;
        int lNumberOK = 0;
        int lNumberNOK = 0;

        try {
            lRequest = new JSONObject(pRequest);
            mLang = lRequest.optString("lang", "");
        } catch (JSONException pExc) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        lData = new Data();
        lSetting = lData.xSetting();
        lChanged = false;
        lPartRequest = lRequest.optJSONObject(Setting.cLocation);
        if (lPartRequest != null) {
            lResult = sProcessLocation(lPartRequest, lSetting);
            switch (lResult) {
                case ResultOK:
                    lChanged = true;
                    lNumberOK++;
                    break;
                case ResultPartOK:
                    lChanged = true;
                    lNumberOK++;
                    lNumberNOK++;
                    break;
                case ResultError:
                    lNumberNOK++;
                    break;
            }
        }
        lPartRequest = lRequest.optJSONObject(Setting.cSensor);
        if (lPartRequest != null) {
            lResult = sProcessSensor(lPartRequest, lSetting);
            switch (lResult) {
                case ResultOK:
                    lChanged = true;
                    lNumberOK++;
                    break;
                case ResultPartOK:
                    lChanged = true;
                    lNumberOK++;
                    lNumberNOK++;
                    break;
                case ResultError:
                    lNumberNOK++;
                    break;
            }
        }
        lPartRequest = lRequest.optJSONObject(Setting.cLightOff);
        if (lPartRequest != null) {
            lPartRequest = lRequest.getJSONObject(Setting.cLightOff);
            lResult = sProcessLightsOff(lPartRequest, lSetting);
            switch (lResult) {
                case ResultOK:
                    lChanged = true;
                    lNumberOK++;
                    break;
                case ResultPartOK:
                    lChanged = true;
                    lNumberOK++;
                    lNumberNOK++;
                    break;
                case ResultError:
                    lNumberNOK++;
                    break;
            }
        }

        if (lChanged) {
            lData.xChangeSetting(lSetting);
            lAction = new Action(Action.cActionRefresh);
            lData.xNewAction(lAction);
            lSetting = lData.xSetting();
        }
        lData.xClose();
        lSettingJ = lSetting.xSetting();

        if (lNumberOK == 0) {
            if (lNumberNOK == 0) {
                lResultS = "OK";
                lText = Message.xMessage("NoChange", mLang);
            } else {
                lResultS = "NOK";
                lText = Message.xMessage("SetError", mLang);
            }
        } else {
            if (lNumberNOK == 0) {
                lResultS = "OK";
                lText = Message.xMessage("SetChange", mLang);
            } else {
                lResultS = "XOK";
                lText = Message.xMessage("SetPartChange", mLang);
            }
        }
        lReply = new JSONObject();
        lReply.put("result", lResultS);
        lReply.put("descr", lText);
        lReply.put(Setting.cSetting, lSettingJ);
        return Response.ok(lReply.toString()).build();
    }

    private int sProcessLocation(JSONObject pLocation, Setting pSetting) {
        double lLongitude;
        double lLatitude;
        int lResult;
        int lNumberOK = 0;
        int lNumberNOK = 0;

        try {
            lLongitude = pLocation.getDouble(Setting.cLongitude);
            lResult = pSetting.xLongitude(lLongitude);
            if (lResult == Setting.cResultOK) {
                lNumberOK++;
            } else {
                lNumberNOK++;
            }
        } catch (JSONException pExc) {
        }
        try {
            lLatitude = pLocation.getDouble(Setting.cLattitude);
            lResult = pSetting.xLattitude(lLatitude);
            if (lResult == Setting.cResultOK) {
                lNumberOK++;
            } else {
                lNumberNOK++;
            }
        } catch (JSONException pExc) {
        }

        if (lNumberOK == 0) {
            if (lNumberNOK == 0) {
                lResult = ResultNoChange;
            } else {
                lResult = ResultError;
            }
        } else {
            if (lNumberNOK == 0) {
                lResult = ResultOK;
            } else {
                lResult = ResultPartOK;
            }
        }
        return lResult;
    }

    private int sProcessSensor(JSONObject pSensor, Setting pSetting) {
        int lResult;
        int lValue;
        int lNumberOK = 0;
        int lNumberNOK = 0;

        try {
            lValue = pSensor.getInt(Setting.cLimit);
            lResult = pSetting.xSensorLimit(lValue);
            if (lResult == Setting.cResultOK) {
                lNumberOK++;
            } else {
                lNumberNOK++;
            }
        } catch (JSONException pExc) {
        }
        try {
            lValue = pSensor.getInt(Setting.cInterval);
            lResult = pSetting.xPeriodSec(lValue);
            if (lResult == Setting.cResultOK) {
                lNumberOK++;
            } else {
                lNumberNOK++;
            }
        } catch (JSONException pExc) {
        }
        try {
            lValue = pSensor.getInt(Setting.cRepeat);
            lResult = pSetting.xPeriodDark(lValue);
            if (lResult == Setting.cResultOK) {
                lNumberOK++;
            } else {
                lNumberNOK++;
            }
        } catch (JSONException pExc) {
        }

        if (lNumberOK == 0) {
            if (lNumberNOK == 0) {
                lResult = ResultNoChange;
            } else {
                lResult = ResultError;
            }
        } else {
            if (lNumberNOK == 0) {
                lResult = ResultOK;
            } else {
                lResult = ResultPartOK;
            }
        }
        return lResult;
    }

    private int sProcessLightsOff(JSONObject pLightsOff, Setting pSetting) {
        int lResult;
        String lPointInTime;
        int lPeriod;
        int lNumberOK = 0;
        int lNumberNOK = 0;

        try {
            lPointInTime = pLightsOff.getString(Setting.cPointInTime);
            lResult = pSetting.xLightOff(lPointInTime);
            if (lResult == Setting.cResultOK) {
                lNumberOK++;
            } else {
                lNumberNOK++;
            }
        } catch (JSONException pExc) {
        }
        try {
            lPeriod = pLightsOff.getInt(Setting.cPeriod);
            lResult = pSetting.xLightOffPeriod(lPeriod);
            if (lResult == Setting.cResultOK) {
                lNumberOK++;
            } else {
                lNumberNOK++;
            }
        } catch (JSONException pExc) {
        }

        if (lNumberOK == 0) {
            if (lNumberNOK == 0) {
                lResult = ResultNoChange;
            } else {
                lResult = ResultError;
            }
        } else {
            if (lNumberNOK == 0) {
                lResult = ResultOK;
            } else {
                lResult = ResultPartOK;
            }
        }
        return lResult;
    }
}
