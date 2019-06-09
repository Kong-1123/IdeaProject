
package service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.com.dhcc.creditquery.person.queryapi.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetCreditReport_QNAME = new QName("http://service.queryapi.person.creditquery.dhcc.com.cn/", "getCreditReport");
    private final static QName _GetCreditReportResponse_QNAME = new QName("http://service.queryapi.person.creditquery.dhcc.com.cn/", "getCreditReportResponse");
    private final static QName _GetSyncCreditReport_QNAME = new QName("http://service.queryapi.person.creditquery.dhcc.com.cn/", "getSyncCreditReport");
    private final static QName _GetSyncCreditReportResponse_QNAME = new QName("http://service.queryapi.person.creditquery.dhcc.com.cn/", "getSyncCreditReportResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.com.dhcc.creditquery.person.queryapi.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetCreditReport }
     * 
     */
    public GetCreditReport createGetCreditReport() {
        return new GetCreditReport();
    }

    /**
     * Create an instance of {@link GetCreditReportResponse }
     * 
     */
    public GetCreditReportResponse createGetCreditReportResponse() {
        return new GetCreditReportResponse();
    }

    /**
     * Create an instance of {@link GetSyncCreditReport }
     * 
     */
    public GetSyncCreditReport createGetSyncCreditReport() {
        return new GetSyncCreditReport();
    }

    /**
     * Create an instance of {@link GetSyncCreditReportResponse }
     * 
     */
    public GetSyncCreditReportResponse createGetSyncCreditReportResponse() {
        return new GetSyncCreditReportResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCreditReport }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.queryapi.person.creditquery.dhcc.com.cn/", name = "getCreditReport")
    public JAXBElement<GetCreditReport> createGetCreditReport(GetCreditReport value) {
        return new JAXBElement<GetCreditReport>(_GetCreditReport_QNAME, GetCreditReport.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCreditReportResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.queryapi.person.creditquery.dhcc.com.cn/", name = "getCreditReportResponse")
    public JAXBElement<GetCreditReportResponse> createGetCreditReportResponse(GetCreditReportResponse value) {
        return new JAXBElement<GetCreditReportResponse>(_GetCreditReportResponse_QNAME, GetCreditReportResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSyncCreditReport }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.queryapi.person.creditquery.dhcc.com.cn/", name = "getSyncCreditReport")
    public JAXBElement<GetSyncCreditReport> createGetSyncCreditReport(GetSyncCreditReport value) {
        return new JAXBElement<GetSyncCreditReport>(_GetSyncCreditReport_QNAME, GetSyncCreditReport.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSyncCreditReportResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.queryapi.person.creditquery.dhcc.com.cn/", name = "getSyncCreditReportResponse")
    public JAXBElement<GetSyncCreditReportResponse> createGetSyncCreditReportResponse(GetSyncCreditReportResponse value) {
        return new JAXBElement<GetSyncCreditReportResponse>(_GetSyncCreditReportResponse_QNAME, GetSyncCreditReportResponse.class, null, value);
    }

}
