/**
 * =============================================================================
 * Copyright (C) 2011-12 by ORCID
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =============================================================================
 */
package edu.colorado.orcid.impl;

import edu.colorado.orcid.*;
import edu.colorado.orcid.mvc.OrcidMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrcidServicePublicImpl implements OrcidService {

    protected Log log = LogFactory.getLog(getClass());
    private String orcidInfoURL;
    private RestTemplate orcidRestTemplate;
    private String orcidCreateURL;
    private String orcidCreateToken;
    private String orcidMessageVersion;
    private String useTestHttpProxy;
    private String testHttpProxyHost;
    private String testHttpProxyPort;

    public Document getOrcidDocument(String orcid) throws OrcidHttpException {
        String url = String.format(orcidInfoURL, orcid);

        return fetchOrcidDocument(url);
    }

    public String createOrcid(String email, String givenNames, String familyName)
            throws OrcidException, OrcidEmailExistsException, OrcidHttpException {

        String newOrcid = null;

        log.debug("Creating ORCID...");
        log.debug("email: " + email);
        log.debug("givenNames: " + givenNames);
        log.debug("familyName: " + familyName);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/xml");
        headers.set("Content-Type", "application/vdn.orcid+xml");
        headers.set("Authorization", "Bearer " + orcidCreateToken);

        OrcidMessage message = new OrcidMessage();
        message.setEmail(email);
        message.setGivenNames(givenNames);
        message.setFamilyName(familyName);
        message.setMessageVersion(orcidMessageVersion);
        //TODO Affiliation should be set based on organization once faculty from more than one organization are processed
        message.setAffiliationType(OrcidMessage.AFFILIATION_TYPE_EMPLOYMENT);
        message.setAffiliationOrganizationName(OrcidMessage.CU_BOULDER);
        message.setAffiliationOrganizationAddressCity(OrcidMessage.BOULDER);
        message.setAffiliationOrganizationAddressRegion(OrcidMessage.CO);
        message.setAffiliationOrganizationAddressCountry(OrcidMessage.US);
        message.setAffiliationOrganizationDisambiguatedId(OrcidMessage.DISAMBIGUATED_ID_CU_BOULDER);
        message.setAffiliationOrganizationDisambiguationSource(OrcidMessage.DISAMBIGUATION_SOURCE_RINGOLD);

        HttpEntity entity = new HttpEntity(message, headers);

        log.debug("Configured RestTemplate Message Converters...");
        List<HttpMessageConverter<?>> converters = orcidRestTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            log.debug("converter: " + converter);
            log.debug("supported media types: " + converter.getSupportedMediaTypes());
            log.debug("converter.canWrite(String.class, MediaType.APPLICATION_XML): " + converter.canWrite(String.class, MediaType.APPLICATION_XML));
            log.debug("converter.canWrite(Message.class, MediaType.APPLICATION_XML): " + converter.canWrite(OrcidMessage.class, MediaType.APPLICATION_XML));
        }

        log.debug("Request headers: " + headers);

        HttpStatus responseStatusCode = null;
        String responseBody = null;

        try {
            if (useTestHttpProxy.equalsIgnoreCase("TRUE")) {
                log.info("Using HTTP ***TEST*** proxy...");
                System.setProperty("http.proxyHost", testHttpProxyHost);
                System.setProperty("http.proxyPort", testHttpProxyPort);
                log.info("http.proxyHost: " + System.getProperty("http.proxyHost"));
                log.info("http.proxyPort: " + System.getProperty("http.proxyPort"));
            }
            ResponseEntity<String> responseEntity = orcidRestTemplate.postForEntity(orcidCreateURL, entity, String.class);
            responseStatusCode = responseEntity.getStatusCode();
            responseBody = responseEntity.getBody();
            HttpHeaders responseHeaders = responseEntity.getHeaders();
            URI locationURI = responseHeaders.getLocation();
            String uriString = locationURI.toString();
            newOrcid = extractOrcid(uriString);
            log.debug("HTTP response status code: " + responseStatusCode);
            log.debug("HTTP response headers:     " + responseHeaders);
            log.debug("HTTP response body:        " + responseBody);
            log.debug("HTTP response location:    " + locationURI);
            log.debug("New ORCID:                 " + newOrcid);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                log.debug(e.getStatusCode());
                log.debug(e.getResponseBodyAsString());
                log.debug(e.getMessage());
                throw new OrcidEmailExistsException(e);
            }
            OrcidHttpException ohe = new OrcidHttpException(e);
            ohe.setStatusCode(e.getStatusCode());
            throw ohe;
        }

        return newOrcid;
    }

    public void setOrcidInfoURL(String infoURL) {
        this.orcidInfoURL = infoURL;
    }

    public void setOrcidRestTemplate(RestTemplate orcidRestTemplate) {
        this.orcidRestTemplate = orcidRestTemplate;
    }

    public void setOrcidCreateToken(String orcidCreateToken) {
        this.orcidCreateToken = orcidCreateToken;
    }

    public void setOrcidCreateURL(String orcidCreateURL) {
        this.orcidCreateURL = orcidCreateURL;
    }

    public void setOrcidMessageVersion(String orcidMessageVersion) {
        this.orcidMessageVersion = orcidMessageVersion;
    }

    public void setTestHttpProxyPort(String testHttpProxyPort) {
        this.testHttpProxyPort = testHttpProxyPort;
    }

    public void setUseTestHttpProxy(String useTestHttpProxy) {
        this.useTestHttpProxy = useTestHttpProxy;
    }

    public void setTestHttpProxyHost(String testHttpProxyHost) {
        this.testHttpProxyHost = testHttpProxyHost;
    }

    protected Document fetchOrcidDocument(String url) throws OrcidHttpException {

        try {
            ResponseEntity<DOMSource> responseEntity = orcidRestTemplate
                    .getForEntity(url, DOMSource.class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            DOMSource orcidInput = responseEntity.getBody();
            Document orcidDocument = (Document) orcidInput.getNode();

            // Handle deprecated ORCID iD, which returns HTTP 301 status code
            if (statusCode == HttpStatus.MOVED_PERMANENTLY) {
                OrcidHttpException e = new OrcidHttpException("ORCID iD Deprecated");
                e.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                try {
                    e.setDocument(OrcidUtil.documentXml(orcidDocument));
                } catch (TransformerException te) {
                    String msg = "Error setting XML document: " + te.getMessage();
                    e.setDocument(msg);
                }
                String errorDesc = orcidDocument.getElementsByTagName("error-desc").item(0).getTextContent();
                e.setOrcidPrimaryRecord(extractOrcid(errorDesc)); //Set ORCID primary record value in exception
                throw e;
            }
            else if (!(statusCode == HttpStatus.OK)) {
                OrcidHttpException e = new OrcidHttpException("Error fetching URL: " + url);
                e.getStatusCode();
                try {
                    e.setDocument(OrcidUtil.documentXml(orcidDocument));
                } catch (TransformerException te) {
                    String msg = "Error setting XML document: " + te.getMessage();
                    e.setDocument(msg);
                }
                throw e;
            }

            return orcidDocument;

        } catch (HttpClientErrorException e) {
            OrcidHttpException ohe = new OrcidHttpException(e);
            ohe.setStatusCode(e.getStatusCode());
            throw ohe;
        }
    }

    private Document fetchDocument(String url) {
        DOMSource orcidInput = orcidRestTemplate.getForObject(url, DOMSource.class);
        return (Document) orcidInput.getNode();
    }

    private String extractOrcid (String stringWithOrcid) {
        String result = null;
        String regex = "[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9X]"; //Last digit can be X
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringWithOrcid);
        while (matcher.find()) {
            result = matcher.group();
        }
        return result;
    }
}
