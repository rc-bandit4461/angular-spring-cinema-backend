package enset.bdcc.jee.cinema.dao;

import enset.bdcc.jee.cinema.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin("*")
public interface RoleRepository extends JpaRepository<Role,Long> {
}
