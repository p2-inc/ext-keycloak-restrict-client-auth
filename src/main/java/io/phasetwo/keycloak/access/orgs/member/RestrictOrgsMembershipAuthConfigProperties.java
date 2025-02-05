package io.phasetwo.keycloak.access.orgs.member;

import static io.phasetwo.keycloak.RestrictOrgsAuthConfig.ORGANIZATION_NAME;
import static io.phasetwo.keycloak.common.OrgAccessConstants.ERROR_MESSAGE;
import static io.phasetwo.keycloak.common.OrgAccessConstants.IS_REGEX;
import static org.keycloak.provider.ProviderConfigProperty.BOOLEAN_TYPE;
import static org.keycloak.provider.ProviderConfigProperty.STRING_TYPE;

import java.util.List;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.services.messages.Messages;

public class RestrictOrgsMembershipAuthConfigProperties {

  private static final ProviderConfigProperty ERROR_MESSAGE_PROPERTY =
      new ProviderConfigProperty(
          ERROR_MESSAGE,
          "Error message",
          "Error message",
          STRING_TYPE,
          Messages.ACCESS_DENIED,
          false);

  public static final ProviderConfigProperty ORGANIZATION_PROPERTY =
      new ProviderConfigProperty(
          ORGANIZATION_NAME,
          "Organizations",
          "The organizations used in the authenticator.",
          STRING_TYPE,
          null,
          false,
          true);

  public static final ProviderConfigProperty IS_REGEX_PROPERTY =
      new ProviderConfigProperty(
          IS_REGEX, "Regex", "The organization name is a regex.", BOOLEAN_TYPE, false, false, true);

  static final List<ProviderConfigProperty> CONFIG_PROPERTIES =
      ProviderConfigurationBuilder.create()
          .property(ERROR_MESSAGE_PROPERTY)
          .property(ORGANIZATION_PROPERTY)
          .property(IS_REGEX_PROPERTY)
          .build();
}
