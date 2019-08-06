package br.rpsr.services.fs.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.rpsr.services.fs.storage.StorageService;

@RestController
//MultipartConfig(maxFileSize = 10* 1024*1024*1024, maxRequestSize = 10 * 1024*1024*1024)
public class FSController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FSController.class);

	private final StorageService storageService;

	@Autowired
	public FSController(StorageService storageService) {
		this.storageService = storageService;
	}

	@PostMapping("/")
	public Map<String, Object> handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		long id = storageService.store(file);

		HashMap<String, Object> map = new HashMap<>();

		map.put("id", Long.valueOf(id));
		return map;
	}
	
	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public void getFile(
	    @PathVariable("id") long id, 
	    HttpServletResponse response) {
	    try {
	    	
	    	storageService.load(id)
	      // get your file as InputStream
	      InputStream is = ...;
	      // copy it to response's OutputStream
	      org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
	      response.flushBuffer();
	    } catch (IOException ex) {
	      LOGGER.info("Error writing file to output stream. Filename was '{}'", id, ex);
	      throw new RuntimeException("IOError writing file to output stream");
	    }

	}	

}
