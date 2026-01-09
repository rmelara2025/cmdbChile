package com.telefonicatech.cmdbChile.repository.usuarios;

import com.telefonicatech.cmdbChile.dto.responseObject.UsuarioRolResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RolRepository {

    @PersistenceContext
    private EntityManager em;

    public List<UsuarioRolResponse> findRolByUserId(String idusuario){

        String sql = """
                    SELECT b.idrol, c.nombreRol FROM usuario a inner join usuariorol b on a.idusuario = b.idusuario
                            inner join rol c on c.idrol = b.idrol
                """ + " WHERE a.idusuario=:idusuario";
        Map<String, Object> params = new HashMap<>();
        params.put("idusuario", idusuario);

        Query query = em.createNativeQuery(sql);

        params.forEach(query::setParameter);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<UsuarioRolResponse> result = new ArrayList<>();

        for(Object[] r : rows){
            Integer id = r[0] == null ? 0 : ((Number) r[0]).intValue();
            String nom = r[1] == null ? null : r[1].toString();
            result.add(new UsuarioRolResponse(id,nom));
        }

        return result;
    }
}
