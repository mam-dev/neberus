package net.oneandone.neberus.parse;

import net.oneandone.neberus.model.SecurityIn;

import java.util.List;
import java.util.Map;

public class SecurityData {

    public SecuritySchemes securitySchemes;

    public static class SecuritySchemes {
        public String description;
        public SecurityBasic basic;
        public SecurityBearer bearer;
        public SecurityApiKey apiKey;
        public SecurityOAuth2 oAuth2;
        public SecurityOpenId openId;
        public SecurityMutualTLS mutualTLS;
        public SecurityRoles roles;
        public List<SecurityCustom> custom;

        @Override
        public String toString() {
            return "SecurityDefinition{" +
                    ", description=" + description +
                    ", basic=" + basic +
                    ", bearer=" + bearer +
                    ", apiKey=" + apiKey +
                    ", oAuth2=" + oAuth2 +
                    ", openId=" + openId +
                    ", mutualTLS=" + mutualTLS +
                    ", roles=" + roles +
                    ", custom=" + custom +
                    '}';
        }
    }

    public record SecurityBasic(
            String title,
            String description
    ) {
    }

    public record SecurityBearer(
            String title,
            String description
    ) {
    }

    public record SecurityApiKey(
            String title,
            String description,
            SecurityIn in,
            String name
    ) {
    }

    public record SecurityOAuth2(
            String title,
            String description
    ) {
    }

    public record SecurityOpenId(
            String title,
            String description,
            String openIdConnectUrl
    ) {
    }

    public record SecurityMutualTLS(
            String title,
            String description
    ) {
    }

    public record SecurityCustom(
            String type,
            SecurityIn in,
            String name,
            String scheme,
            String title,
            String description,
            Map<String, String> attributes
    ) {
    }

    public record SecurityRoles(
            List<RoleDefinition> roles,
            String title,
            String description
    ) {
    }

    public record RoleDefinition(
            String name,
            String description
    ) {
    }

}
