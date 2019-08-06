package br.rpsr.services.fs.service;

import org.springframework.stereotype.Service;

@Service
public class FileService {
	
	/**
	 * Método responsável por retornar arquivo pelo id.
	 *
	 * @param ParamDTO.
	 * @return FileDTO.
	 * @exception IOException,
	 *                FileNotFoundException
	 */
	/*
	 * public FileDTO getFileById(ParamDTO paramTO) throws IOException,
	 * FileNotFoundException {
	 * LOGGER.info("Class: FileServiceBean Method: getFileById - Codigo Arquivo: " +
	 * paramTO.getId()); FileDTO fileTO = new FileDTO(); ArquivoDado nameFile =
	 * null; LogServico logServico = null; long inicioExecucao =
	 * System.currentTimeMillis(); try {
	 * 
	 * nameFile = getArquivoDadoService().getById(paramTO.getId());
	 * 
	 * Validate.notNull(nameFile, ARQUIVO_DADO_NAO_ENCONTRADO);
	 * 
	 * PathUtil pathUtil = new PathUtil(paramTO.getId()); File file =
	 * pathUtil.getFile(false);
	 * 
	 * fileTO.setFile(FileUtil.convertToByteArray(file));
	 * fileTO.setNameFile(nameFile.getNomeOrigem());
	 * 
	 * setDecodingFile(paramTO, file, fileTO);
	 * 
	 * 
	 * } catch (FileNotFoundException e) {
	 * logServico.setSaida(FileUtilConstants.ExceptionsMessage.
	 * ARQUIVO_NAO_ENCONTRADO + e.getMessage()) .setDataExecucao(new
	 * Date()).setFlgOk(LogServico.Flags.FALHA_SERVICO); LOGGER.log(Level.SEVERE,
	 * e.getMessage(), e); throw new FileServiceException(FILENOTFOUNDEXCEPTION +
	 * e.getMessage(), e); } catch (IOException e) { logServico.setSaida(IOEXCEPTION
	 * + e.getMessage()).setFlgOk(LogServico.Flags.FALHA_SERVICO);
	 * LOGGER.log(Level.SEVERE, e.getMessage(), e); throw new
	 * FileServiceException(IOEXCEPTION + e.getMessage(), e); } catch
	 * (ServiceException e) { logServico.setSaida(STOREEXCEPTION +
	 * e.getMessage()).setFlgOk(LogServico.Flags.FALHA_SERVICO);
	 * LOGGER.log(Level.SEVERE, e.getMessage(), e); throw new
	 * FileServiceException(STOREEXCEPTION + e.getMessage(), e); } finally {
	 * getLogServicoService().save(logServico); } return fileTO; }
	 */
}
