package org.carl.jwt;

import static org.carl.generated.Tables.ROLES;
import static org.carl.generated.Tables.USERS;
import static org.carl.generated.Tables.USER_PERMISSIONS;

import io.smallrye.jwt.build.Jwt;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import jakarta.inject.Inject;
import org.carl.auth.utils.JwtUtils;
import org.carl.engine.DB;
import org.carl.user.model.Role;
import org.carl.user.model.User;
import org.eclipse.microprofile.jwt.Claims;

public class TestJwt {
    @Inject
    DB DB;

    // @Test
    public void testGenToken() {

        User user =
            DB.get(dsl -> dsl.selectFrom(USERS).where(USERS.ID.eq(1)).fetchOneInto(User.class));
        List<Role> roles = DB.get(dsl -> dsl.select(ROLES.NAME).from(USER_PERMISSIONS).join(ROLES)
            .on(ROLES.ID.eq(USER_PERMISSIONS.ROLE_ID))
            .where(USER_PERMISSIONS.USER_ID.eq(user.getId())).fetch().into(Role.class));
        user.setRoles(roles);

        String s = JwtUtils.generateAccentToken(user);

        System.out.println(s);
    }

    String genToken() {
        return Jwt.issuer("https://org.carl/issuer").upn("jdoe@quarkus.io")
            .groups(new HashSet<>(Arrays.asList("USER", "ADMIN")))
            .claim(Claims.birthdate.name(), "2001-07-13").jws().keyId("/privatekey.pem").sign();
    }
}
