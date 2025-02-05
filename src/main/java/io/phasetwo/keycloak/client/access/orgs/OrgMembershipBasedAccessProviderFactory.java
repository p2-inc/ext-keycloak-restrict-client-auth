package io.phasetwo.keycloak.client.access.orgs;

import com.google.auto.service.AutoService;
import io.phasetwo.keycloak.client.access.OrgsAccessProvider;
import io.phasetwo.keycloak.client.access.OrgsAccessProviderFactory;
import io.phasetwo.keycloak.client.common.OperationalInfo;
import java.util.Map;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

@AutoService(OrgsAccessProviderFactory.class)
public final class OrgMembershipBasedAccessProviderFactory
    implements OrgsAccessProviderFactory, ServerInfoAwareProviderFactory {

  public static final String PROVIDER_ID = "org-membership";

  @Override
  public OrgsAccessProvider create(KeycloakSession session) {
    return new OrgMembershipBasedAccessProvider(session);
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
  public Map<String, String> getOperationalInfo() {
    return OperationalInfo.get();
  }
}
