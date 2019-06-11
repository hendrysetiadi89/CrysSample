package com.crysp.deviceverify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 04/04/19.
 */
public class XConfirmModel {
    @SerializedName("xtid")
    @Expose
    private String xtid;
    @SerializedName("errorCode")
    @Expose
    private String errorCode;
    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("xlvr")
    @Expose
    private String xlvr;

    @SerializedName("xdvr")
    @Expose
    private String xdvr;

    @SerializedName("xdps")
    @Expose
    private String xdps;

    @SerializedName("xdes")
    @Expose
    private String xdes;

    @SerializedName("xdms")
    @Expose
    private String xdms;

    @SerializedName("xdmi")
    @Expose
    private String xdmi;

    @SerializedName("xdrs")
    @Expose
    private String xdrs;

    @SerializedName("xuvs")
    @Expose
    private String xuvs;

    @SerializedName("xpds")
    @Expose
    private String xpds;

    @SerializedName("xtvs")
    @Expose
    private String xtvs;

    @SerializedName("isDevInit")
    @Expose
    private String isDevInit;

    @SerializedName("txnTime")
    @Expose
    private String txnTime;

    @SerializedName("hmac")
    @Expose
    private String hmac;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("xdip")
    @Expose
    private XDip xdip;

    static class XDip{
        @SerializedName("osid")
        @Expose
        private String osid;

        @SerializedName("xhdp")
        @Expose
        private String xhdp;

        @SerializedName("omid")
        @Expose
        private String omid;

        @SerializedName("xosv")
        @Expose
        private String xosv;

        @SerializedName("xdpi")
        @Expose
        private String xdpi;

        public String getOsid() {
            return osid;
        }

        public String getXhdp() {
            return xhdp;
        }

        public String getOmid() {
            return omid;
        }

        public String getXosv() {
            return xosv;
        }

        public String getXdpi() {
            return xdpi;
        }
    }

    public String getXtid() {
        return xtid;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getResult() {
        return result;
    }

    public String getXlvr() {
        return xlvr;
    }

    public String getXdvr() {
        return xdvr;
    }

    public String getXdps() {
        return xdps;
    }

    public String getXdes() {
        return xdes;
    }

    public String getXdms() {
        return xdms;
    }

    public String getXdmi() {
        return xdmi;
    }

    public String getXdrs() {
        return xdrs;
    }

    public String getXuvs() {
        return xuvs;
    }

    public String getXpds() {
        return xpds;
    }

    public String getXtvs() {
        return xtvs;
    }

    public String getIsDevInit() {
        return isDevInit;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public String getHmac() {
        return hmac;
    }

    public String getLocation() {
        return location;
    }

    public XDip getXdip() {
        return xdip;
    }
}
