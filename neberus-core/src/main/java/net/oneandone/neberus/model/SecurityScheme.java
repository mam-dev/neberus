package net.oneandone.neberus.model;

public enum SecurityScheme {

    BASIC("BasicAuth"),
    BEARER("BearerAuth"),
    API_KEY("ApiKeyAuth"),
    OPEN_ID("OpenID"),
    OAUTH2("OAuth2"),
    MUTUAL_TLS("MutualTLS"),
    ROLES("Roles"),
    CUSTOM("Custom");

    public final String typeName;

    SecurityScheme(String typeName) {
        this.typeName = typeName;
    }
}
