package io.phasetwo.keycloak.access.orgs.member;

import static io.phasetwo.keycloak.RestrictOrgsAuthConfig.ORGANIZATION_NAME;
import static io.phasetwo.keycloak.common.OrgAccessConstants.IS_REGEX;
import static org.keycloak.provider.ProviderConfigProperty.BOOLEAN_TYPE;
import static org.keycloak.provider.ProviderConfigProperty.STRING_TYPE;

import java.util.List;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

public class RestrictOrgsMembershipAuthConfigProperties {

  public static final ProviderConfigProperty ORGANIZATION_PROPERTY =
      new ProviderConfigProperty(
          ORGANIZATION_NAME,
          "Organizations",
          "If a organization name is provided the flow will restrict access based on that specific organization. If a regex is used the flow will restrict access to all organization which match the regex.",
          STRING_TYPE,
          null,
          false,
          true);

  public static final ProviderConfigProperty IS_REGEX_PROPERTY =
      new ProviderConfigProperty(
          IS_REGEX, "Regex", "The organization name is a regex.", BOOLEAN_TYPE, false, false, true);

  static final List<ProviderConfigProperty> CONFIG_PROPERTIES =
      ProviderConfigurationBuilder.create()
          .property(ORGANIZATION_PROPERTY)
          .property(IS_REGEX_PROPERTY)
          .build();
}
