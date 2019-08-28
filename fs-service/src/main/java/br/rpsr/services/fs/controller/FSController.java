package br.rpsr.services.fs.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.rpsr.services.fs.dtos.DadosDTO;
import br.rpsr.services.fs.dtos.ParamDTO;
import br.rpsr.services.fs.exceptions.FileServiceException;
import br.rpsr.services.fs.models.ArquivoDado;
import br.rpsr.services.fs.service.FSService;

@RestController
//MultipartConfig(maxFileSize = 10* 1024*1024*1024, maxRequestSize = 10 * 1024*1024*1024)
public class FSController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FSController.class);

	@Autowired
	private FSService fileService;

	@GetMapping("/union")
	public ResponseEntity<?> unionPDFs(@RequestParam("pdfs") String pdfs, @RequestParam("filename") String filename) {
		LOGGER.info("Class: FileRest Method: unionPDFs - Codigo Arquivo: " + pdfs + " Nome Arquivo: " + filename);
		try {
			ParamDTO paramTO = ParamDTO.builder().setPdf(pdfs.trim()).setFilename(filename).setUsuario("RPSR");

			Long id = fileService.unionPDFFile(paramTO);

			return ResponseEntity.ok().header("cod_arq", id.toString()).body(id.toString());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	@GetMapping("/filename/{ID}")
	public ResponseEntity<String> getNameFileById(@PathVariable("ID") String id) {
		LOGGER.info("Class: FileRest Method: getNameFileById - Codigo Arquivo: " + id);
		try {
			ArquivoDado arquivoDado = fileService.getById(id);

			Validate.notNull(arquivoDado, "id " + id + " nao encontrado!");

			return ResponseEntity.ok().header("filename", arquivoDado.getNomeOrigem())
					.body(arquivoDado.getNomeOrigem());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/download/{ID}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("ID") String id,
			@RequestParam(name = "fromEncode", required = false) String fromEncode,
			@RequestParam(name = "toEncode", required = false) String toEncode,
			@RequestParam(name = "filename", required = false) String filenameSet,
			@RequestParam(name = "compactar", required = false, defaultValue = "N") String compactar)
			throws IOException {

		LOGGER.info("Class: download Method: download - Codigo Arquivo: " + id);

		ArquivoDado arquivoDado = fileService.getById(id);

		Validate.notNull(arquivoDado, "id " + id + " nao encontrado!");

		byte[] bs = fileService.download(arquivoDado, fromEncode, toEncode);

		String nomArquivo = arquivoDado.getNomeOrigem();

		if (!StringUtils.isEmpty(filenameSet))
			nomArquivo = filenameSet;

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add("Content-Disposition", "attachment; filename=" + nomArquivo + "");

		ByteArrayResource resource = new ByteArrayResource(bs);

		return ResponseEntity.ok().headers(headers).contentLength(bs.length)
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

	}

	@PostMapping("/upload")
	public Map<String, Object> uploadFile(@RequestParam(name = "categoria", required = false, defaultValue = "") String paramCategoria,
			@RequestParam(name = "descricao", required = false) String descricao,
			@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
			@RequestHeader(value = "x-coduser",required = false,defaultValue = "") String paramCodigoUsuario)
			throws FileServiceException, IOException {

		ParamDTO paramTO = new ParamDTO();
		

		
		paramTO.setCodigoCategoria(
				StringUtils.isEmpty(paramCategoria) ? null : Integer.valueOf(paramCategoria));
		paramTO.setDescricao(descricao);
		paramTO.setUsuario(paramCodigoUsuario);

		DadosDTO dadosDTO = new DadosDTO();
		byte[] bytes = IOUtils.toByteArray(file.getInputStream());
		dadosDTO.setInputStream(bytes);
		dadosDTO.setFileName(file.getOriginalFilename());

		paramTO.setDadosTO(dadosDTO);

		long id = fileService.uploadFile(paramTO);
		;

		HashMap<String, Object> map = new HashMap<>();

		map.put("id", Long.valueOf(id));
		return map;
	}

	@GetMapping("/watermark")
	public ResponseEntity<?> watermark(@RequestParam(name = "codArq") String arquivo,
			@RequestParam(name = "texto") String texto,
			@RequestParam(name = "filename", defaultValue = "") String filename) {
		LOGGER.info("Class: FileRest Method: watermark - Codigo Arquivo: " + arquivo);
		try {
			Long codArq = Long.valueOf(arquivo);
			ParamDTO paramTO = ParamDTO.builder().setId(codArq).setTexto(texto).setFilename(filename)
					.setUsuario("rpsr");

			Long id = fileService.watermarkFile(paramTO);

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("cod_arq", id.toString());
			return ResponseEntity.ok().headers(responseHeaders).body(id.toString());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
