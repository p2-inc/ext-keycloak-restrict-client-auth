package io.phasetwo.keycloak.access.orgs.member;

import static org.keycloak.utils.RegexUtils.valueMatchesRegex;

import io.phasetwo.keycloak.RestrictOrgsAuthConfig;
import io.phasetwo.keycloak.access.OrgsAccessProvider;
import io.phasetwo.service.model.OrganizationProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public final class OrgMembershipBasedAccessProvider implements OrgsAccessProvider {

  private final OrganizationProvider organizationProvider;

  public OrgMembershipBasedAccessProvider(KeycloakSession session) {
    this.organizationProvider = session.getProvider(OrganizationProvider.class);
  }

  @Override
  public boolean isRestricted(
      UserModel user, RealmModel realmModel, RestrictOrgsAuthConfig config) {
    return organizationProvider.getUserOrganizationsStream(realmModel, user).findAny().isPresent();
  }

  @Override
  public boolean isPermitted(UserModel user, RealmModel realmModel, RestrictOrgsAuthConfig config) {
    boolean areValuesRegex = config.isRegex();
    return organizationProvider
        .getUserOrganizationsStream(realmModel, user)
        .anyMatch(
            organizationModel ->
                areValuesRegex
                    ? valueMatchesRegex(config.getOrganizationName(), organizationModel.getName())
                    : config.getOrganizationName().equals(organizationModel.getName()));
  }

  @Override
  public void close() {}
}
