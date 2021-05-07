package com.infocovid.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Sitemap {
	
	@RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET, produces = "application/xml")
	public ResponseEntity<InputStreamResource> sitemap() throws IOException {
		ClassPathResource resource = new ClassPathResource("sitemap.xml");
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");

	    return ResponseEntity
	            .ok()
	            .headers(headers)
	            .contentLength(resource.contentLength())
	            .contentType(MediaType.parseMediaType("application/octet-stream"))
	            .body(new InputStreamResource(resource.getInputStream()));
	}
//	@RequestMapping(value = "/sitemap.xml")
//	public void robots(HttpServletRequest request, HttpServletResponse response) {
//	    try {
//	    	Resource resource = new ClassPathResource("sitemap.xml");
//			Reader reader = new InputStreamReader(resource.getInputStream());
//			String xml=FileCopyUtils.copyToString(reader);
//	        response.getWriter().write(xml);
//	    } catch (IOException e) {
//	      System.out.println(e.getMessage());
//	    }
//	}
}
