/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.light.api;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jan
 */
public class Parameters {

    private final String mParam;
    private List<String> mParameters;
    private final boolean mOK;

    public Parameters(String pParam) {
        mParam = pParam;
        sSplitParam();
        mOK = sTestParams();
    }

    public boolean xOK() {
        return mOK;
    }

    public String xKey(int pIndex) {
        String lParam;
        int lIs;

        if (pIndex < mParameters.size()) {
            lParam = mParameters.get(pIndex);
            lIs = lParam.indexOf("=");
            if (lIs == -1) {
                return "";
            } else {
                return lParam.substring(0, lIs);
            }
        } else {
            return "";
        }
    }

    public String xValue(int pIndex) {
        String lParam;
        int lIs;

        if (pIndex < mParameters.size()) {
            lParam = mParameters.get(pIndex);
            lIs = lParam.indexOf("=");
            if (lIs == -1) {
                return "";
            } else {
                return lParam.substring(lIs + 1);
            }
        } else {
            return "";
        }
    }

    public int xNumberPar() {
        return mParameters.size();
    }

    private void sSplitParam() {
        String lInput;
        boolean lReady;
        int lAmp;
        String lValue;

        lInput = mParam;
        lReady = false;
        mParameters = new ArrayList<>();
        while (!lReady) {
            lAmp = lInput.indexOf("&");
            if (lAmp == -1) {
                lValue = lInput;
                lReady = true;
            } else {
                lValue = lInput.substring(0, lAmp);
                if (lInput.length() > lAmp + 1) {
                    lInput = lInput.substring(lAmp + 1);
                } else {
                    lReady = true;
                }
            }
            if (!lValue.equals("")) {
                mParameters.add(lValue);
            }
        }
    }

    private boolean sTestParams() {
        int lIndex;
        String lParam;
        int lIs;
        int lSp;

        for (lIndex = 0; lIndex < mParameters.size(); lIndex++) {
            lParam = mParameters.get(lIndex);
            lIs = lParam.indexOf("=");
            if (lIs == -1) {
                return false;
            }
            lSp = lParam.indexOf(" ");
            if (lSp != -1) {
                return false;
            }
        }
        return true;
    }
}
