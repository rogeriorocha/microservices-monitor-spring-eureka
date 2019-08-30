package br.rpsr.services.fs.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.rpsr.services.fs.models.ArquivoDado;

public interface ArquivoDadoRepository extends JpaRepository<ArquivoDado, Long> {
	
	
	List<ArquivoDado> findTop10ByAtivoAndDataExpurgoLessThanEqualOrderByDataInclAsc(String ativo, Date dataExpurgo);
}
