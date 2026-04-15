package app.user.domain;

import core.framework.api.json.Property;
import core.framework.db.Column;
import core.framework.db.DBEnumValue;
import core.framework.db.PrimaryKey;
import core.framework.db.Table;

@Table(name = "users")
public class User {
    @PrimaryKey(autoIncrement = true)
    @Column(name = "id")
    public Long id;

    @Column(name = "username")
    public String username;

    @Column(name = "password")
    public String password;

    @Column(name = "company_id")
    public Long companyId;

    @Column(name = "status")
    public UserStatus status;

    @Column(name = "role")
    public UserRole role;
}