package io.phasetwo.keycloak.client;

import static io.phasetwo.keycloak.client.RestrictOrgsAuthConfig.ACCESS_PROVIDER_ID;
import static io.phasetwo.keycloak.client.RestrictOrgsAuthConfig.ERROR_MESSAGE;
import static io.phasetwo.keycloak.client.RestrictOrgsAuthConfig.ORGANIZATION_NAME;
import static org.keycloak.provider.ProviderConfigProperty.LIST_TYPE;
import static org.keycloak.provider.ProviderConfigProperty.MULTIVALUED_STRING_TYPE;
import static org.keycloak.provider.ProviderConfigProperty.STRING_TYPE;

import io.phasetwo.keycloak.client.access.orgs.OrgMembershipBasedAccessProviderFactory;
import java.util.List;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.services.messages.Messages;

public class RestrictOrgsAuthConfigProperties {

  private static final ProviderConfigProperty ERROR_MESSAGE_PROPERTY =
      new ProviderConfigProperty(
          ERROR_MESSAGE,
          "Error message",
          "Error message",
          STRING_TYPE,
          Messages.ACCESS_DENIED,
          false);

  public static final ProviderConfigProperty ACCESS_PROVIDER_ID_PROPERTY =
      new ProviderConfigProperty(
          ACCESS_PROVIDER_ID,
          "Access Provider",
          "The access provider to be used with this authenticator.",
          LIST_TYPE,
          OrgMembershipBasedAccessProviderFactory.PROVIDER_ID,
          false);

  public static final ProviderConfigProperty ORGANIZATION_PROPERTY =
      new ProviderConfigProperty(
          ORGANIZATION_NAME,
          "Organizations",
          "The organizations used in the authenticator.",
          MULTIVALUED_STRING_TYPE,
          OrgMembershipBasedAccessProviderFactory.PROVIDER_ID,
          false);

  static final List<ProviderConfigProperty> CONFIG_PROPERTIES =
      ProviderConfigurationBuilder.create()
          .property(ERROR_MESSAGE_PROPERTY)
          .property(ACCESS_PROVIDER_ID_PROPERTY)
          .property(ORGANIZATION_PROPERTY)
          .build();
}
