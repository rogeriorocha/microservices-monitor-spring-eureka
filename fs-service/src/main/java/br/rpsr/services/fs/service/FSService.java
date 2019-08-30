package br.rpsr.services.fs.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import br.gov.mg.bdmg.commons.utils.StringUtil;
import br.gov.mg.bdmg.commons.utils.file.constants.FileUtilConstants.Util;
import br.gov.mg.bdmg.commons.utils.file.util.FilePDFUtil;
import br.gov.mg.bdmg.commons.utils.file.util.FileUtil;
import br.gov.mg.bdmg.commons.utils.file.util.PathUtil;
import br.rpsr.services.fs.dtos.ParamDTO;
import br.rpsr.services.fs.exceptions.FileServiceException;
import br.rpsr.services.fs.models.ArquivoDado;
import br.rpsr.services.fs.repository.ArquivoDadoRepository;

@Service
public class FSService {
	private Logger LOGGER = Logger.getLogger(FSService.class.getName());

	private static final String IOEXCEPTION = "[IOEXCEPTION] ";
	private static final String EXCEPTION = "[EXCEPTION] ";
	private static final String STOREEXCEPTION = "[STOREEXCEPTION] ";
	private static final String FILENOTFOUNDEXCEPTION = "[FILENOTFOUNDEXCEPTION] ";
	private static final String FALHA_AO_SETAR_ENTRADA_WRITE = "Falha ao escrever entrada arquivo.";
	private static final String UNKNOW = "unknow";
	private static final String ARQUIVO_DADO_NAO_ENCONTRADO = "Arquivo Dado nao encontrado";
	private static final String ERRO_ALTERAR_STATUS = "Falha ao Alterar Status do Arquivo";
	private static final Integer CODIGO_DEFAULT = 1;
	private static final Integer CODIGO_CATEGORIA_UNION = 26;
	private static final Integer CODIGO_CATEGORIA_WATERMARK = 27;
	private static final String QUERY_MAGICA_JFS_EXPURGO = "JFS_Expurgo";

	@Autowired
	ArquivoDadoRepository arquivoDadoService;

	public Long uploadFile(ParamDTO paramTO) throws FileServiceException, IOException {
		LOGGER.info("Class: FileServiceBean Method: uploadFile");

		ArquivoDado arquivoDadoTemp = null;
		ArquivoDado arquivoDado = null;
		Long id = null;

		try {
			Long codigoUsuarioIncl = null;
			if (!paramTO.getUsuario().isEmpty())
				codigoUsuarioIncl = Long.valueOf(paramTO.getUsuario());

			arquivoDadoTemp = ArquivoDado.builder().setFlagMigr(ArquivoDado.Flags.MIGR)
					.setAtivo(ArquivoDado.Flags.ATIVO).setCodigoUsuarioIncl(codigoUsuarioIncl).setCodigoCategoria(
							paramTO.getCodigoCategoria() == null ? CODIGO_DEFAULT : paramTO.getCodigoCategoria());

			arquivoDado = arquivoDadoService.save(arquivoDadoTemp);

			id = arquivoDado.getId();

			PathUtil file = new PathUtil(id);

			File fileToSave = file.getFile(true);

			String repHash = PathUtil.saveFile(paramTO.getDadosTO().getInputStream(), fileToSave);

			arquivoDado.setTamanhoArquivo(fileToSave.length()).setHash(repHash).setDataIncl(new Date())
					.setNomeOrigem(paramTO.getDadosTO().getFileName())
					// .setCodigoUsuarioIncl(getNameUserFile(paramTO.getDadosFileTO().getUsuario()))
					.setDescricaoArquivo(paramTO.getDescricao()).setId(id).setEnderecoArquivo(fileToSave.getPath());

			arquivoDadoService.save(arquivoDado);

		} catch (IOException e) {

			if (arquivoDado != null) {
				arquivoDadoService.delete(arquivoDado);
			}
			throw new FileServiceException(IOEXCEPTION + e.getMessage(), e);
		} catch (Throwable e) {
			if (arquivoDado != null) {
				arquivoDadoService.delete(arquivoDado);
			}
			throw new FileServiceException(STOREEXCEPTION + e.getMessage(), e);
		}
		return id;
	}

	/**
	 * Método responsável colocar marca d'agua em pdf e retornar id do documento.
	 *
	 * @param ParamDTO.
	 * @return Long.
	 * @exception IOException, FileNotFoundException
	 * @throws DocumentException
	 */
	public Long watermarkFile(ParamDTO paramTO) throws FileServiceException, IOException, DocumentException {
		LOGGER.info("Class: FileServiceBean Method: watermarkFile");

		ArquivoDado arquivoDado = null;
		Long newID = null;
		try {

			PathUtil pathUtil = new PathUtil(paramTO.getId());
			File fileOld = pathUtil.getFile(false);

			FileInputStream fis = new FileInputStream(fileOld);

			arquivoDado = ArquivoDado.builder().setFlagMigr(ArquivoDado.Flags.MIGR).setAtivo(ArquivoDado.Flags.ATIVO)
					.setCodigoCategoria(CODIGO_CATEGORIA_WATERMARK);

			arquivoDado = arquivoDadoService.save(arquivoDado);

			newID = arquivoDado.getId();

			File fileTmp = File.createTempFile(Util.WATERMARK_ + newID, Util.EXTENSAO_PDF);

			FileOutputStream fos = new FileOutputStream(fileTmp);

			FilePDFUtil.insertWaterMark(fis, fos, paramTO.getTexto());

			PathUtil newPath = new PathUtil(newID);
			File newFile = newPath.getFile(true);

			byte[] file = FileUtil.convertToByteArray(fileTmp);

			String repHash = PathUtil.saveFile(file, newFile);

			arquivoDado.setTamanhoArquivo(newFile.length()).setHash(repHash).setDataIncl(new Date())
					.setNomeOrigem(getFileName(paramTO.getFilename(), newID))
					.setDescricaoArquivo(Util.WATERMARK + paramTO.getId()).setEnderecoArquivo(newFile.getPath());

			arquivoDado = arquivoDadoService.save(arquivoDado);

			fileTmp.delete();

		} catch (FileNotFoundException e) {

			LOGGER.log(Level.SEVERE, e.getMessage(), e);

			throw new FileServiceException(FILENOTFOUNDEXCEPTION + e.getMessage(), e);
		} catch (IOException e) {

			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new FileServiceException(IOEXCEPTION + e.getMessage(), e);
		} catch (ServiceException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new FileServiceException(STOREEXCEPTION + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new FileServiceException(EXCEPTION + e.getMessage(), e);
		}

		return newID;
	}

	private String getFileName(String paramName, Long codigo) {
		if (paramName != null && !paramName.isEmpty()) {
			return paramName;
		}
		return codigo.toString() + Util.EXTENSAO_PDF;
	}

	public ArquivoDado getById(String id) {

		Optional<ArquivoDado> adOptional = arquivoDadoService.findById(Long.valueOf(id));
		if (!adOptional.isPresent())
			return null;
		else
			return adOptional.get();
	}

	/**
	 * Método responsável por fazer união de pdf e retornar id do novo arquivo
	 * gerado.
	 *
	 * @param ParamDTO.
	 * @return Long.
	 * @exception IOException, FileNotFoundException
	 */
	public Long unionPDFFile(ParamDTO paramTO) throws FileServiceException, IOException {
		LOGGER.info("Class: FileServiceBean Method: unionPDFFile");

		ArquivoDado arquivoDado = null;

		Long codArqNew = null;
		try {

			String dscArq = Util.UNION
					+ (paramTO.getPdf().length() > 200 ? paramTO.getPdf().substring(0, 200) : paramTO.getPdf());

			arquivoDado = ArquivoDado.builder().setFlagMigr(ArquivoDado.Flags.MIGR).setAtivo(ArquivoDado.Flags.ATIVO)
					.setCodigoCategoria(CODIGO_CATEGORIA_UNION).setDescricaoArquivo(dscArq);

			List<String> localFiles = getLocalFiles(paramTO.getPdf());

			arquivoDado = arquivoDadoService.save(arquivoDado);

			codArqNew = arquivoDado.getId();

			File unionFileTmp = File.createTempFile(Util.UNION_ + codArqNew, Util.EXTENSAO_PDF);

			FilePDFUtil.unionPDFs(unionFileTmp.getAbsolutePath(), localFiles);

			PathUtil pathUtil = new PathUtil(codArqNew);
			File novoArquivo = pathUtil.getFile(true);

			byte[] file = FileUtil.convertToByteArray(unionFileTmp);

			String repHash = PathUtil.saveFile(file, novoArquivo);

			arquivoDado.setTamanhoArquivo(novoArquivo.length()).setHash(repHash).setDataIncl(new Date())
					.setNomeOrigem(getFileName(paramTO.getFilename(), codArqNew))
					.setEnderecoArquivo(novoArquivo.getPath());

			arquivoDado = arquivoDadoService.save(arquivoDado);

			unionFileTmp.delete();

		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new FileServiceException(FILENOTFOUNDEXCEPTION + e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new FileServiceException(IOEXCEPTION + e.getMessage(), e);
		} catch (ServiceException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new FileServiceException(STOREEXCEPTION + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new FileServiceException(EXCEPTION + e.getMessage(), e);
		}

		return codArqNew;
	}

	private List<String> getLocalFiles(String pdf) throws IOException {
		List<String> localFiles = new ArrayList<String>();
		String[] files = pdf.split(",");
		Long id = null;

		try {
			for (String codigoArquivo : files) {
				id = Long.valueOf(codigoArquivo);
				PathUtil pathUtil = new PathUtil(id);
				File file = pathUtil.getFile(false);
				localFiles.add(file.getAbsolutePath());
			}
		} catch (IOException e) {
			throw new IOException("Codigo Arquivo não encontrado: " + id);
		}
		return localFiles;
	}

	public byte[] encodeFile(String fromEncode, String toEncode, File file) throws FileNotFoundException, IOException {
		if (StringUtil.nonEmpty(fromEncode)) {
			InputStream inputStream = new FileInputStream(file);

			byte[] byteFile = IOUtils.toByteArray(inputStream);
			String decoded = FileUtil.decodedFile(fromEncode, byteFile);
			if (StringUtil.nonEmpty(toEncode)) {
				byte[] bsEncoded = FileUtil.encodedFile(toEncode, decoded);
				return bsEncoded;
			} else {
				return decoded.getBytes();
			}
		} else {
			InputStream inputStream = new FileInputStream(file);

			byte[] byteFile = IOUtils.toByteArray(inputStream);
			return byteFile;
		}

	}

	public byte[] download(ArquivoDado arquivoDado, String fromEncode, String toEncode) throws IOException {
		PathUtil pUtil = new PathUtil(arquivoDado.getId());
		File file2Upload = pUtil.getFile(false);
		return encodeFile(fromEncode, toEncode, file2Upload);
	}

	public long expurgar() {

		
		List<ArquivoDado> lst = arquivoDadoService.findTop10ByAtivoAndDataExpurgoLessThanEqualOrderByDataInclAsc(ArquivoDado.Flags.ATIVO, new Date());

		for (Iterator<ArquivoDado> it = lst.iterator(); it.hasNext();) {
			ArquivoDado arquivoDado = it.next();
			
			arquivoDado.setAtivo(ArquivoDado.Flags.INATIVO);
			arquivoDadoService.save(arquivoDado);
			
			System.out.println("DELETE " + arquivoDado.getId());
		}

		return lst.size();
	}

}
