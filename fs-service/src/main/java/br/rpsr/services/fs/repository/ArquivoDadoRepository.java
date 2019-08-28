package br.rpsr.services.fs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.rpsr.services.fs.models.ArquivoDado;

public interface ArquivoDadoRepository extends JpaRepository<ArquivoDado, Long> {
	

}
