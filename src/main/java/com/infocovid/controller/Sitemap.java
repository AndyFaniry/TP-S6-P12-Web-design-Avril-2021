package com.infocovid.controller;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Sitemap {
	
	@RequestMapping(value = "sitemap.xml", method = RequestMethod.GET, produces = "application/xml")
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
}
