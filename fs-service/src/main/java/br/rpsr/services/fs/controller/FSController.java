package br.rpsr.services.fs.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mapstruct.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

	@GetMapping("/download/{ID}")
	public ResponseEntity<Resource> getFileById(@Context HttpServletRequest requestContext,
			@PathVariable("ID") String id, @RequestParam(name = "fromEncode", required = false) String fromEncode,
			@RequestParam(name = "toEncode", required = false) String toEncode,
			@RequestParam(name = "filename", required = false) String filenameSet,
			@RequestParam(name = "compactar", required = false, defaultValue = "N") String compactar)
			throws IOException {

		LOGGER.info("Class: FileRest Method: getFileById - Codigo Arquivo: " + id);

		String filename = "aa.txt";
		File file2Upload = new File("C:\\Temp\\store\\a.txt");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add("Content-Disposition", "attachment; filename=" + filename + "");



		Path path = Paths.get(file2Upload.getAbsolutePath());
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

		return ResponseEntity.ok().headers(headers).contentLength(file2Upload.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

	}

	@PostMapping("/")
	public Map<String, Object> handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		long id = storageService.store(file);

		HashMap<String, Object> map = new HashMap<>();

		map.put("id", Long.valueOf(id));
		return map;
	}

	/*
	 * @RequestMapping(value = "/download/{id}", method = RequestMethod.GET) public
	 * void getFile(
	 * 
	 * @PathVariable("id") long id, HttpServletResponse response) { try {
	 * 
	 * storageService.load(id) // get your file as InputStream InputStream is = ...;
	 * // copy it to response's OutputStream org.apache.commons.io.IOUtils.copy(is,
	 * response.getOutputStream()); response.flushBuffer(); } catch (IOException ex)
	 * { LOGGER.info("Error writing file to output stream. Filename was '{}'", id,
	 * ex); throw new RuntimeException("IOError writing file to output stream"); }
	 * 
	 * }
	 */

}
