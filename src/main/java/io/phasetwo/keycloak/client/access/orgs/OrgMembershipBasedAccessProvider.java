package io.phasetwo.keycloak.client.access.orgs;

import io.phasetwo.keycloak.client.access.OrgsAccessProvider;
import io.phasetwo.service.model.OrganizationProvider;
import java.util.Set;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public final class OrgMembershipBasedAccessProvider implements OrgsAccessProvider {

  private final OrganizationProvider organizationProvider;

  public OrgMembershipBasedAccessProvider(KeycloakSession session) {
    this.organizationProvider = session.getProvider(OrganizationProvider.class);
  }

  @Override
  public boolean isRestricted(UserModel user, RealmModel realmModel, Set<String> orgs) {
    return organizationProvider.getUserOrganizationsStream(realmModel, user).findAny().isPresent();
  }

  @Override
  public boolean isPermitted(UserModel user, RealmModel realmModel, Set<String> orgs) {
    return organizationProvider
        .getUserOrganizationsStream(realmModel, user)
        .anyMatch(organizationModel -> orgs.contains(organizationModel.getName()));
  }

  @Override
  public void close() {}
}
