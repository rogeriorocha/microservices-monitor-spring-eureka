package br.rpsr.services.fs.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	long store(MultipartFile file);

	Stream<Path> loadAll();

	Path load(long id);

	Resource loadAsResource(long id);

	void deleteAll();

}