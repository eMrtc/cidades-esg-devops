package br.com.cidades.esg.repository;

import br.com.cidades.esg.model.Projeto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjetoRepository extends MongoRepository<Projeto, String> {
    List<Projeto> findByIdGestor(String idGestor);
    List<Projeto> findByIdEstrategia(String idEstrategia);
}
