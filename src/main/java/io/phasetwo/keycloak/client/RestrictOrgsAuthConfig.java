package io.phasetwo.keycloak.client;

import io.phasetwo.keycloak.client.access.orgs.OrgMembershipBasedAccessProviderFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.Constants;

public final class RestrictOrgsAuthConfig {

  static final String ERROR_MESSAGE = "restrictClientAuthErrorMessage";
  public static final String ACCESS_PROVIDER_ID = "accessProviderId";
  public static final String ORGANIZATION_NAME = "organizationNames";

  private final AuthenticatorConfigModel authenticatorConfigModel;

  RestrictOrgsAuthConfig(AuthenticatorConfigModel configModel) {
    this.authenticatorConfigModel = configModel;
  }

  public String getAccessProviderId() {
    return Optional.ofNullable(authenticatorConfigModel)
        .map(AuthenticatorConfigModel::getConfig)
        .map(
            config ->
                config.getOrDefault(
                    ACCESS_PROVIDER_ID, OrgMembershipBasedAccessProviderFactory.PROVIDER_ID))
        .orElse(OrgMembershipBasedAccessProviderFactory.PROVIDER_ID);
  }

  String getAuthenticatorConfigAlias() {
    return Optional.ofNullable(authenticatorConfigModel)
        .map(AuthenticatorConfigModel::getAlias)
        .orElse(null);
  }

  public Set<String> getOrgs() {
    return Optional.ofNullable(authenticatorConfigModel)
        .map(AuthenticatorConfigModel::getConfig)
        .map(config -> getAttributeMultivalued(config, ORGANIZATION_NAME))
        .orElse(Set.of());
  }

  public static Set<String> getAttributeMultivalued(Map<String, String> config, String attrKey) {
    if (config == null) {
      return new HashSet<>();
    }
    String attrValue = config.get(attrKey);
    if (attrValue == null) return new HashSet<>();
    return new HashSet<>(Arrays.asList(Constants.CFG_DELIMITER_PATTERN.split(attrValue)));
  }
}
