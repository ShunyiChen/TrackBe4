
package com.maxtree.trackbe4.external.tmri;

import javax.jws.WebService;

@WebService(serviceName = "Itrffwebservice", targetNamespace = "http://tempuri.org/", endpointInterface = "Tmri.Itrffweb")
public class ItrffwebserviceImpl
    implements Itrffweb
{


    public String queryObjectOut(String jkid, String QueryXmlDoc) {
        throw new UnsupportedOperationException();
    }
}
