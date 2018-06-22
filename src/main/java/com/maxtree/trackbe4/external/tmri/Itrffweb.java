package com.maxtree.trackbe4.external.tmri;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "Itrffweb", targetNamespace = "http://tempuri.org/")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Itrffweb {


    @WebMethod(operationName = "queryObjectOut", action = "urn:trffwebIntf-Itrffweb#queryObjectOut")
    @WebResult(name = "return", targetNamespace = "http://tempuri.org/")
    public String queryObjectOut(
        @WebParam(name = "jkid", targetNamespace = "http://tempuri.org/")
        String jkid,
        @WebParam(name = "QueryXmlDoc", targetNamespace = "http://tempuri.org/")
        String QueryXmlDoc);

}
