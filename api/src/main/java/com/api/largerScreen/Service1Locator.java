/**
 * Service1Locator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.api.largerScreen;

public class Service1Locator extends org.apache.axis.client.Service implements com.api.largerScreen.Service1 {

    public Service1Locator() {
    }


    public Service1Locator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Service1Locator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Service1Soap12
//    private String Service1Soap12_address = "http://218.75.41.250:9902/LargerScreen.asmx";
    private String Service1Soap12_address = "http://218.108.176.10:9080/LargerScreen.asmx";

    public String getService1Soap12Address() {
        return Service1Soap12_address;
    }

    // The WSDD service name defaults to the port name.
    private String Service1Soap12WSDDServiceName = "Service1Soap12";

    public String getService1Soap12WSDDServiceName() {
        return Service1Soap12WSDDServiceName;
    }

    public void setService1Soap12WSDDServiceName(String name) {
        Service1Soap12WSDDServiceName = name;
    }

    public com.api.largerScreen.Service1Soap_PortType getService1Soap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Service1Soap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getService1Soap12(endpoint);
    }

    public com.api.largerScreen.Service1Soap_PortType getService1Soap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.api.largerScreen.Service1Soap12Stub _stub = new com.api.largerScreen.Service1Soap12Stub(portAddress, this);
            _stub.setPortName(getService1Soap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setService1Soap12EndpointAddress(String address) {
        Service1Soap12_address = address;
    }


    // Use to get a proxy class for Service1Soap
//    private String Service1Soap_address = "http://218.75.41.250:9902/LargerScreen.asmx";
    private String Service1Soap_address = "http://218.108.176.10:9080/LargerScreen.asmx";

    public String getService1SoapAddress() {
        return Service1Soap_address;
    }

    // The WSDD service name defaults to the port name.
    private String Service1SoapWSDDServiceName = "Service1Soap";

    public String getService1SoapWSDDServiceName() {
        return Service1SoapWSDDServiceName;
    }

    public void setService1SoapWSDDServiceName(String name) {
        Service1SoapWSDDServiceName = name;
    }

    public com.api.largerScreen.Service1Soap_PortType getService1Soap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Service1Soap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getService1Soap(endpoint);
    }

    public com.api.largerScreen.Service1Soap_PortType getService1Soap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.api.largerScreen.Service1Soap_BindingStub _stub = new com.api.largerScreen.Service1Soap_BindingStub(portAddress, this);
            _stub.setPortName(getService1SoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setService1SoapEndpointAddress(String address) {
        Service1Soap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.api.largerScreen.Service1Soap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.api.largerScreen.Service1Soap12Stub _stub = new com.api.largerScreen.Service1Soap12Stub(new java.net.URL(Service1Soap12_address), this);
                _stub.setPortName(getService1Soap12WSDDServiceName());
                return _stub;
            }
            if (com.api.largerScreen.Service1Soap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.api.largerScreen.Service1Soap_BindingStub _stub = new com.api.largerScreen.Service1Soap_BindingStub(new java.net.URL(Service1Soap_address), this);
                _stub.setPortName(getService1SoapWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("Service1Soap12".equals(inputPortName)) {
            return getService1Soap12();
        }
        else if ("Service1Soap".equals(inputPortName)) {
            return getService1Soap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "Service1");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "Service1Soap12"));
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "Service1Soap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

if ("Service1Soap12".equals(portName)) {
            setService1Soap12EndpointAddress(address);
        }
        else
if ("Service1Soap".equals(portName)) {
            setService1SoapEndpointAddress(address);
        }
        else
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
