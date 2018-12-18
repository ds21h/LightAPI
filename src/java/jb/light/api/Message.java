/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jb.light.api;

/**
 *
 * @author Jan
 */
class Message {
    private static final String[] mLanguage = {"en", "nl"};
    
    private static final String[][] mMessage = {
        {"ActUnsupp", "Unsupported action", "Onbekende aktie"},
        {"InconsistentID", "Inconsistent ID", "Inconsistente Naam"},
        {"NoID", "No ID supplied", "Geen ID gegeven"},
        {"NoChange", "Nothing changed", "Niets gewijzigd"},
        {"NoSw", "Switch doesn't exixt", "Schakelaar bestaat niet"},
        {"SetChange", "Settings modified", "Instellingen gewijzigd"},
        {"SetError", "Setting has errors", "Instellingen fout"},
        {"SetPartChange", "Settings partially modified", "Instellingen gedeeltelijk gewijzigd"},
        {"SwChanged", "Switch modified", "Schakelaar gewijzigd"},
        {"SwCreated", "Switch created", "Schakelaar gemaakt"},
        {"SwDel", "Switch deleted", "Schakelaar verwijderd"},
        {"SwExist", "Switch already exists", "Schakelaar bestaat al"},
        {"SwIncorr", "Switch incorrect", "Schakelaar fout"},
        {"ZZZZ-Laatste", "Undefined message", "Onbekende boodschap"}
    };
    
    static String xMessage(String pName, String pLang){
        int lCount;
        int lLangIndex;
        String lMessage;
        
        lLangIndex = 1;
        for (lCount = 0; lCount < mLanguage.length; lCount++){
            if (mLanguage[lCount].equals(pLang)){
                lLangIndex = lCount + 1;
                break;
            }
        }
        for (lCount = 0; lCount < mMessage.length - 1; lCount++){
            if (mMessage[lCount][0].equals(pName)){
                break;
            }
        }
        lMessage = mMessage[lCount][lLangIndex];
        return lMessage;
    }
}
