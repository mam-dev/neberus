package net.oneandone.neberus.parse;

import net.oneandone.neberus.Options;
import net.oneandone.neberus.annotation.ApiSecuritySchemes;
import net.oneandone.neberus.model.SecurityIn;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.oneandone.neberus.util.JavaDocUtils.extractValue;
import static net.oneandone.neberus.util.JavaDocUtils.getAnnotationDesc;

/**
 * Parses usecase related things.
 */
public class SecuritySchemesParser {

    private final Options options;


    public SecuritySchemesParser(Options options) {
        this.options = options;
    }

    public SecurityData parse(TypeElement typeElement, SecurityData securityData) {

        if (securityData.securitySchemes != null) {
            System.err.println("@ApiSecuritySchemes must not be defined multiple times!");
            if (!options.ignoreErrors) {
                throw new IllegalStateException();
            }
        }

        parseSecuritySchemes(typeElement, securityData);

        return securityData;
    }

    protected void parseSecuritySchemes(TypeElement classDoc, SecurityData SecurityData) {

        List<? extends AnnotationMirror> singleResponse = getAnnotationDesc(classDoc, ApiSecuritySchemes.class, options.environment);

        singleResponse.forEach(annotationDesc -> {


            SecurityData.SecuritySchemes securitySchemes = new SecurityData.SecuritySchemes();
            SecurityData.securitySchemes = securitySchemes;

            securitySchemes.description = extractValue(annotationDesc, "description");

            addBasicAuth(annotationDesc, securitySchemes);
            addBearerAuth(annotationDesc, securitySchemes);
            addApiKeyAuth(annotationDesc, securitySchemes);
            addMutualTLS(annotationDesc, securitySchemes);
            addOAuth2(annotationDesc, securitySchemes);
            addOpenId(annotationDesc, securitySchemes);
            addRoles(annotationDesc, securitySchemes);
            addCustomAuth(annotationDesc, securitySchemes);
        });

    }

    private static void addCustomAuth(AnnotationMirror annotationDesc, SecurityData.SecuritySchemes securitySchemes) {
        // custom
        List<AnnotationValue> custom = extractValue(annotationDesc, "custom");

        if (custom != null && !custom.isEmpty()) {
            securitySchemes.custom = custom.stream().map(customEntry -> {
                AnnotationMirror securityCustom = (AnnotationMirror) customEntry.getValue();
                String type = extractValue(securityCustom, "type");

                VariableElement rawIn = extractValue(securityCustom, "in");
                SecurityIn in = rawIn == null ? SecurityIn.UNDEFINED : SecurityIn.valueOf(rawIn.getSimpleName().toString());

                String name = extractValue(securityCustom, "name");
                String scheme = extractValue(securityCustom, "scheme");
                String title = extractValue(securityCustom, "title");
                String description = extractValue(securityCustom, "description");
                List<AnnotationValue> attributes = extractValue(securityCustom, "attributes");
                Map<String, String> parsedAttributes = new LinkedHashMap<>();

                if (attributes != null && !attributes.isEmpty()) {
                    attributes.forEach(attribute -> {
                        AnnotationMirror attributeAnnotation = (AnnotationMirror) attribute.getValue();
                        String attrName = extractValue(attributeAnnotation, "name");
                        String attrValue = extractValue(attributeAnnotation, "value");
                        parsedAttributes.put(attrName, attrValue);
                    });
                }

                return new SecurityData.SecurityCustom(type, in, name, scheme, title, description, parsedAttributes);
            }).toList();
        }
    }

    private static void addRoles(AnnotationMirror annotationDesc, SecurityData.SecuritySchemes securitySchemes) {
        // roles
        AnnotationValue roles = extractValue(annotationDesc, "roles");
        if (roles != null) {

            AnnotationMirror securityRoles = (AnnotationMirror) roles.getValue();
            String title = extractValue(securityRoles, "title");
            String description = extractValue(securityRoles, "description");
            List<AnnotationValue> roleDefinitions = extractValue(securityRoles, "roles");

            List<SecurityData.RoleDefinition> roleDefinitionList = null;
            if (roleDefinitions != null && !roleDefinitions.isEmpty()) {
                roleDefinitionList = roleDefinitions.stream().map(definition -> {
                    AnnotationMirror defAnnotation = (AnnotationMirror) definition.getValue();
                    String name = extractValue(defAnnotation, "name");
                    String roleDescription = extractValue(defAnnotation, "description");

                    return new SecurityData.RoleDefinition(name, roleDescription);
                }).toList();
            }

            securitySchemes.roles = new SecurityData.SecurityRoles(roleDefinitionList, title, description);
        }
    }

    private static void addOAuth2(AnnotationMirror annotationDesc, SecurityData.SecuritySchemes securitySchemes) {
        // oauth2
        AnnotationValue oauth2 = extractValue(annotationDesc, "oAuth2");

        if (oauth2 != null) {
            AnnotationMirror securityOAuth2 = (AnnotationMirror) oauth2.getValue();
            String title = extractValue(securityOAuth2, "title");
            String description = extractValue(securityOAuth2, "description");

            securitySchemes.oAuth2 = new SecurityData.SecurityOAuth2(title, description);
        }
    }

    private static void addOpenId(AnnotationMirror annotationDesc, SecurityData.SecuritySchemes securitySchemes) {
        // openId
        AnnotationValue openId = extractValue(annotationDesc, "openId");

        if (openId != null) {
            AnnotationMirror securityOpenId = (AnnotationMirror) openId.getValue();
            String title = extractValue(securityOpenId, "title");
            String description = extractValue(securityOpenId, "description");
            String openIdConnectUrl = extractValue(securityOpenId, "openIdConnectUrl");

            securitySchemes.openId = new SecurityData.SecurityOpenId(title, description, openIdConnectUrl);
        }
    }

    private static void addMutualTLS(AnnotationMirror annotationDesc, SecurityData.SecuritySchemes securitySchemes) {
        // mutualTLS
        AnnotationValue mutualTLS = extractValue(annotationDesc, "mutualTLS");

        if (mutualTLS != null) {
            AnnotationMirror securityMutualTLS = (AnnotationMirror) mutualTLS.getValue();
            String title = extractValue(securityMutualTLS, "title");
            String description = extractValue(securityMutualTLS, "description");

            securitySchemes.mutualTLS = new SecurityData.SecurityMutualTLS(title, description);
        }
    }

    private static void addApiKeyAuth(AnnotationMirror annotationDesc, SecurityData.SecuritySchemes securitySchemes) {
        // apiKey
        AnnotationValue apiKey = extractValue(annotationDesc, "apiKey");

        if (apiKey != null) {
            AnnotationMirror securityApiKey = (AnnotationMirror) apiKey.getValue();
            String title = extractValue(securityApiKey, "title");
            String description = extractValue(securityApiKey, "description");

            VariableElement rawIn = extractValue(securityApiKey, "in");
            SecurityIn in = rawIn == null ? SecurityIn.UNDEFINED : SecurityIn.valueOf(rawIn.getSimpleName().toString());

            String name = extractValue(securityApiKey, "name");

            securitySchemes.apiKey = new SecurityData.SecurityApiKey(title, description, in, name);
        }
    }

    private static void addBearerAuth(AnnotationMirror annotationDesc, SecurityData.SecuritySchemes securitySchemes) {
        // bearer
        AnnotationValue bearer = extractValue(annotationDesc, "bearer");

        if (bearer != null) {
            AnnotationMirror securityBearer = (AnnotationMirror) bearer.getValue();
            String title = extractValue(securityBearer, "title");
            String description = extractValue(securityBearer, "description");

            securitySchemes.bearer = new SecurityData.SecurityBearer(title, description);
        }
    }

    private static void addBasicAuth(AnnotationMirror annotationDesc, SecurityData.SecuritySchemes securitySchemes) {
        // basic
        AnnotationValue basic = extractValue(annotationDesc, "basic");

        if (basic != null) {
            AnnotationMirror securityBasic = (AnnotationMirror) basic.getValue();
            String title = extractValue(securityBasic, "title");
            String description = extractValue(securityBasic, "description");

            securitySchemes.basic = new SecurityData.SecurityBasic(title, description);
        }
    }


}
