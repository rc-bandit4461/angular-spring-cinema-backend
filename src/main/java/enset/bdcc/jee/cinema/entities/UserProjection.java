package enset.bdcc.jee.cinema.entities;

import org.springframework.data.rest.core.config.Projection;
import java.util.Collection;
import java.util.List;
@Projection(name = "p4", types = {enset.bdcc.jee.cinema.entities.User.class})
public interface UserProjection {
    public Long getId();
    public String getEmail();
    public String getFirstName();
    public String getLastName();
    public Collection<Role> getRoles();
}
