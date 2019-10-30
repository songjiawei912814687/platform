<?xml version="1.0" encoding="utf-8"?>
<wsdl:definitions xmlns:tm="http://microsoft.com/wsdl/mime/textMatching/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" xmlns:tns="http://tempuri.org/" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:s="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" targetNamespace="http://tempuri.org/" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">
  <wsdl:types>
    <s:schema elementFormDefault="qualified" targetNamespace="http://tempuri.org/">
      <s:element name="GetProjectAfficheList">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="tenderId" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="userToken" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetProjectAfficheListResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetProjectAfficheListResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetProjectAfficheWinBidList">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="tenderId" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="userToken" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetProjectAfficheWinBidListResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetProjectAfficheWinBidListResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetSmallProjectAfficheList">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="tenderId" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="userToken" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetSmallProjectAfficheListResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetSmallProjectAfficheListResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetSmallProjectAfficheWinBidList">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="tenderId" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="userToken" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetSmallProjectAfficheWinBidListResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetSmallProjectAfficheWinBidListResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetExposureNews">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="newsId" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="userToken" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetExposureNewsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetExposureNewsResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetPublishInfo">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="enterPriseId" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="userToken" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetPublishInfoResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetPublishInfoResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
    </s:schema>
  </wsdl:types>
  <wsdl:message name="GetProjectAfficheListSoapIn">
    <wsdl:part name="parameters" element="tns:GetProjectAfficheList" />
  </wsdl:message>
  <wsdl:message name="GetProjectAfficheListSoapOut">
    <wsdl:part name="parameters" element="tns:GetProjectAfficheListResponse" />
  </wsdl:message>
  <wsdl:message name="GetProjectAfficheWinBidListSoapIn">
    <wsdl:part name="parameters" element="tns:GetProjectAfficheWinBidList" />
  </wsdl:message>
  <wsdl:message name="GetProjectAfficheWinBidListSoapOut">
    <wsdl:part name="parameters" element="tns:GetProjectAfficheWinBidListResponse" />
  </wsdl:message>
  <wsdl:message name="GetSmallProjectAfficheListSoapIn">
    <wsdl:part name="parameters" element="tns:GetSmallProjectAfficheList" />
  </wsdl:message>
  <wsdl:message name="GetSmallProjectAfficheListSoapOut">
    <wsdl:part name="parameters" element="tns:GetSmallProjectAfficheListResponse" />
  </wsdl:message>
  <wsdl:message name="GetSmallProjectAfficheWinBidListSoapIn">
    <wsdl:part name="parameters" element="tns:GetSmallProjectAfficheWinBidList" />
  </wsdl:message>
  <wsdl:message name="GetSmallProjectAfficheWinBidListSoapOut">
    <wsdl:part name="parameters" element="tns:GetSmallProjectAfficheWinBidListResponse" />
  </wsdl:message>
  <wsdl:message name="GetExposureNewsSoapIn">
    <wsdl:part name="parameters" element="tns:GetExposureNews" />
  </wsdl:message>
  <wsdl:message name="GetExposureNewsSoapOut">
    <wsdl:part name="parameters" element="tns:GetExposureNewsResponse" />
  </wsdl:message>
  <wsdl:message name="GetPublishInfoSoapIn">
    <wsdl:part name="parameters" element="tns:GetPublishInfo" />
  </wsdl:message>
  <wsdl:message name="GetPublishInfoSoapOut">
    <wsdl:part name="parameters" element="tns:GetPublishInfoResponse" />
  </wsdl:message>
  <wsdl:portType name="Service1Soap">
    <wsdl:operation name="GetProjectAfficheList">
      <wsdl:input message="tns:GetProjectAfficheListSoapIn" />
      <wsdl:output message="tns:GetProjectAfficheListSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetProjectAfficheWinBidList">
      <wsdl:input message="tns:GetProjectAfficheWinBidListSoapIn" />
      <wsdl:output message="tns:GetProjectAfficheWinBidListSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetSmallProjectAfficheList">
      <wsdl:input message="tns:GetSmallProjectAfficheListSoapIn" />
      <wsdl:output message="tns:GetSmallProjectAfficheListSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetSmallProjectAfficheWinBidList">
      <wsdl:input message="tns:GetSmallProjectAfficheWinBidListSoapIn" />
      <wsdl:output message="tns:GetSmallProjectAfficheWinBidListSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetExposureNews">
      <wsdl:input message="tns:GetExposureNewsSoapIn" />
      <wsdl:output message="tns:GetExposureNewsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetPublishInfo">
      <wsdl:input message="tns:GetPublishInfoSoapIn" />
      <wsdl:output message="tns:GetPublishInfoSoapOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:binding name="Service1Soap" type="tns:Service1Soap">
    <soap:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="GetProjectAfficheList">
      <soap:operation soapAction="http://tempuri.org/GetProjectAfficheList" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetProjectAfficheWinBidList">
      <soap:operation soapAction="http://tempuri.org/GetProjectAfficheWinBidList" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetSmallProjectAfficheList">
      <soap:operation soapAction="http://tempuri.org/GetSmallProjectAfficheList" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetSmallProjectAfficheWinBidList">
      <soap:operation soapAction="http://tempuri.org/GetSmallProjectAfficheWinBidList" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetExposureNews">
      <soap:operation soapAction="http://tempuri.org/GetExposureNews" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetPublishInfo">
      <soap:operation soapAction="http://tempuri.org/GetPublishInfo" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="Service1Soap12" type="tns:Service1Soap">
    <soap12:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="GetProjectAfficheList">
      <soap12:operation soapAction="http://tempuri.org/GetProjectAfficheList" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetProjectAfficheWinBidList">
      <soap12:operation soapAction="http://tempuri.org/GetProjectAfficheWinBidList" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetSmallProjectAfficheList">
      <soap12:operation soapAction="http://tempuri.org/GetSmallProjectAfficheList" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetSmallProjectAfficheWinBidList">
      <soap12:operation soapAction="http://tempuri.org/GetSmallProjectAfficheWinBidList" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetExposureNews">
      <soap12:operation soapAction="http://tempuri.org/GetExposureNews" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetPublishInfo">
      <soap12:operation soapAction="http://tempuri.org/GetPublishInfo" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:service name="Service1">
    <wsdl:port name="Service1Soap" binding="tns:Service1Soap">
      <soap:address location="http://218.75.41.250:9902/LargerScreen.asmx" />
    </wsdl:port>
    <wsdl:port name="Service1Soap12" binding="tns:Service1Soap12">
      <soap12:address location="http://218.75.41.250:9902/LargerScreen.asmx" />
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>