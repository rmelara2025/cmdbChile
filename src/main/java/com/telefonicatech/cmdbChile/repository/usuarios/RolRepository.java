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

    public List<UsuarioRolResponse> findRolByUserId(String idusuario) {
        // Query para obtener roles del usuario
        String sqlRoles = """
                    SELECT b.idrol, c.nombreRol
                    FROM usuario a
                    INNER JOIN usuariorol b ON a.idusuario = b.idusuario
                    INNER JOIN rol c ON c.idrol = b.idrol
                    WHERE a.idusuario = :idusuario
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("idusuario", idusuario);

        Query queryRoles = em.createNativeQuery(sqlRoles);
        params.forEach(queryRoles::setParameter);

        @SuppressWarnings("unchecked")
        List<Object[]> rolesRows = queryRoles.getResultList();

        List<UsuarioRolResponse> result = new ArrayList<>();

        // Por cada rol, obtener sus permisos
        for (Object[] r : rolesRows) {
            Integer idRol = r[0] == null ? 0 : ((Number) r[0]).intValue();
            String nombreRol = r[1] == null ? null : r[1].toString();

            // Query para obtener permisos del rol
            String sqlPermisos = """
                        SELECT p.codigopermiso
                        FROM rolpermisos rp
                        INNER JOIN permisos p ON p.idpermiso = rp.idpermiso
                        WHERE rp.idrol = :idrol
                        ORDER BY p.codigopermiso
                    """;

            Query queryPermisos = em.createNativeQuery(sqlPermisos);
            queryPermisos.setParameter("idrol", idRol);

            @SuppressWarnings("unchecked")
            List<String> permisos = queryPermisos.getResultList();

            result.add(new UsuarioRolResponse(idRol, nombreRol, permisos));
        }

        return result;
    }
}
