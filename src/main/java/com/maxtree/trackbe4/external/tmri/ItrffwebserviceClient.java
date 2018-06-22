package com.maxtree.trackbe4.external.tmri;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

public class ItrffwebserviceClient {

  private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
  private HashMap endpoints = new HashMap();
  private Service service0;

  public ItrffwebserviceClient() {
    create0();
    Endpoint ItrffwebPortEP = service0.addEndpoint(new QName(
      "http://tempuri.org/", "ItrffwebPort"),
      new QName("http://tempuri.org/", "Itrffwebbinding"),
           "http://10.80.29.16:6000/trffweb.dll/soap/Itrffweb");
    endpoints.put(new QName("http://tempuri.org/", "ItrffwebPort"),
                  ItrffwebPortEP);
    Endpoint ItrffwebLocalEndpointEP = service0.addEndpoint(new QName(
      "http://tempuri.org/", "ItrffwebLocalEndpoint"),
      new QName("http://tempuri.org/", "ItrffwebLocalBinding"),
           "xfire.local://Itrffwebservice");
    endpoints.put(new QName("http://tempuri.org/", "ItrffwebLocalEndpoint"),
                  ItrffwebLocalEndpointEP);
  }

  public Object getEndpoint(Endpoint endpoint) {
    try {
      return proxyFactory.create( (endpoint).getBinding(), (endpoint).getUrl());
    }
    catch (MalformedURLException e) {
      throw new XFireRuntimeException("Invalid URL", e);
    }
  }

  public Object getEndpoint(QName name) {
    Endpoint endpoint = ( (Endpoint) endpoints.get( (name)));
    if ( (endpoint) == null) {
      throw new IllegalStateException("No such endpoint!");
    }
    return getEndpoint( (endpoint));
  }

  public Collection getEndpoints() {
    return endpoints.values();
  }

  private void create0() {
    TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().
                           getXFire().getTransportManager());
    HashMap props = new HashMap();
    props.put("annotations.allow.interface", true);
    AnnotationServiceFactory asf = new AnnotationServiceFactory(new
      Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
    asf.setBindingCreationEnabled(false);
    service0 = asf.create( ( com.maxtree.trackbe4.external.tmri.Itrffweb.class), props);
    {
      AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0,
        new QName("http://tempuri.org/", "ItrffwebLocalBinding"),
        "urn:xfire:transport:local");
    }
    {
      AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0,
        new QName("http://tempuri.org/", "Itrffwebbinding"),
        "http://schemas.xmlsoap.org/soap/http");
    }
  }

  public Itrffweb getItrffwebPort() {
    return ( (Itrffweb) (this).getEndpoint(new QName("http://tempuri.org/",
      "ItrffwebPort")));
  }

  public Itrffweb getItrffwebPort(String url) {
    Itrffweb var = getItrffwebPort();
    org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
    return var;
  }

  public Itrffweb getItrffwebLocalEndpoint() {
    return ( (Itrffweb) (this).getEndpoint(new QName("http://tempuri.org/",
      "ItrffwebLocalEndpoint")));
  }

  public Itrffweb getItrffwebLocalEndpoint(String url) {
    Itrffweb var = getItrffwebLocalEndpoint();
    org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
    return var;
  }

}
