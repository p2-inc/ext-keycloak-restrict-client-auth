package io.phasetwo.keycloak.access.orgs.role;

import static org.keycloak.models.AuthenticationExecutionModel.Requirement.DISABLED;
import static org.keycloak.models.AuthenticationExecutionModel.Requirement.REQUIRED;

import com.google.auto.service.AutoService;
import io.phasetwo.keycloak.RestrictOrgsAuthAuthenticator;
import io.phasetwo.keycloak.common.CommunityProfiles;
import java.util.List;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.EnvironmentDependentProviderFactory;
import org.keycloak.provider.ProviderConfigProperty;

@JBossLog
@AutoService(AuthenticatorFactory.class)
public class RestrictOrgRoleAuthAuthenticatorFactory
    implements AuthenticatorFactory, EnvironmentDependentProviderFactory {

  private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES =
      new AuthenticationExecutionModel.Requirement[] {REQUIRED, DISABLED};

  private static final String PROVIDER_ID = "ext-restrict-org-role-authenticator";

  @Override
  public String getDisplayType() {
    return "Restrict user access based on organization role.";
  }

  @Override
  public String getReferenceCategory() {
    return "Authorization";
  }

  @Override
  public boolean isConfigurable() {
    return true;
  }

  @Override
  public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
    return REQUIREMENT_CHOICES;
  }

  @Override
  public boolean isUserSetupAllowed() {
    return false;
  }

  @Override
  public String getHelpText() {
    return "Restrict user access based on organization role.";
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return RestrictOrgsRoleAuthConfigProperties.CONFIG_PROPERTIES;
  }

  @Override
  public Authenticator create(KeycloakSession session) {
    return new RestrictOrgsAuthAuthenticator(OrgRoleBasedAccessProviderFactory.PROVIDER_ID);
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
