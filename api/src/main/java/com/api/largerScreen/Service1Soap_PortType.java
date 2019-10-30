/**
 * Service1Soap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.api.largerScreen;

public interface Service1Soap_PortType extends java.rmi.Remote {
    public String getProjectAfficheList(String tenderId, String userToken) throws java.rmi.RemoteException;
    public String getProjectAfficheWinBidList(String tenderId, String userToken) throws java.rmi.RemoteException;
    public String getSmallProjectAfficheList(String tenderId, String userToken) throws java.rmi.RemoteException;
    public String getSmallProjectAfficheWinBidList(String tenderId, String userToken) throws java.rmi.RemoteException;
    public String getExposureNews(String newsId, String userToken) throws java.rmi.RemoteException;
    public String getPublishInfo(String enterPriseId, String userToken) throws java.rmi.RemoteException;
}
