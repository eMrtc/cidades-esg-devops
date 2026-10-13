package br.com.cidades.esg.repository;

import br.com.cidades.esg.model.Ideia;
import br.com.cidades.esg.model.IdeiaStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeiaRepository extends MongoRepository<Ideia, String> {
    List<Ideia> findByIdOperador(String idOperador);
    List<Ideia> findByStatus(IdeiaStatus status);
    List<Ideia> findByIdEstrategia(String idEstrategia);
}
