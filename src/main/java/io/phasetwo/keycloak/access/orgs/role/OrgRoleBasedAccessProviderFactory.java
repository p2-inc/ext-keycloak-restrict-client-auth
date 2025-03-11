package io.phasetwo.keycloak.access.orgs.role;

import com.google.auto.service.AutoService;
import io.phasetwo.keycloak.access.OrgsAccessProvider;
import io.phasetwo.keycloak.access.OrgsAccessProviderFactory;
import io.phasetwo.keycloak.common.CommunityProfiles;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.EnvironmentDependentProviderFactory;

@AutoService(OrgsAccessProviderFactory.class)
public final class OrgRoleBasedAccessProviderFactory
    implements OrgsAccessProviderFactory, EnvironmentDependentProviderFactory {

  public static final String PROVIDER_ID = "ext-org-role";

  @Override
  public OrgsAccessProvider create(KeycloakSession session) {
    return new OrgRoleBasedAccessProvider(session);
  }

  @Override
  public void init(Config.Scope config) {}

  @Override
  public void postInit(KeycloakSessionFactory factory) {}

  @Override
  public void close() {}

  @Override
  public String getId() {
    return PROVIDER_ID;
  }

  @Override
  public boolean isSupported(Config.Scope config) {
    return CommunityProfiles.isRestrictOrgAuthenticators();
  }
}
