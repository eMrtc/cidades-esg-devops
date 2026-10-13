package br.com.cidades.esg.repository;

import br.com.cidades.esg.model.OrientacaoEstrategica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrientacaoEstrategicaRepository extends MongoRepository<OrientacaoEstrategica, String> {
    List<OrientacaoEstrategica> findByAtivaTrue();
}
