package io.phasetwo.keycloak;

import static io.phasetwo.keycloak.common.OrgAccessConstants.IS_REGEX;

import java.util.Optional;
import org.keycloak.models.AuthenticatorConfigModel;

public final class RestrictOrgsAuthConfig {

  public static final String ORGANIZATION_NAME = "organizationNames";
  public static final String ROLE_NAME = "roleName";

  private final AuthenticatorConfigModel authenticatorConfigModel;

  RestrictOrgsAuthConfig(AuthenticatorConfigModel configModel) {
    this.authenticatorConfigModel = configModel;
  }

  public String getOrganizationName() {
    return Optional.ofNullable(authenticatorConfigModel)
        .map(AuthenticatorConfigModel::getConfig)
        .map(config -> config.get(ORGANIZATION_NAME))
        .orElseThrow();
  }

  public String getRoleName() {
    return Optional.ofNullable(authenticatorConfigModel)
        .map(AuthenticatorConfigModel::getConfig)
        .map(config -> config.get(ROLE_NAME))
        .orElseThrow();
  }

  public boolean isRegex() {
    return Optional.ofNullable(authenticatorConfigModel)
        .map(AuthenticatorConfigModel::getConfig)
        .map(config -> config.get(IS_REGEX))
        .map(Boolean::parseBoolean)
        .orElse(false);
  }
}
