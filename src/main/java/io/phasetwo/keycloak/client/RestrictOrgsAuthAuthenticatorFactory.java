package io.phasetwo.keycloak.client;

import static org.keycloak.models.AuthenticationExecutionModel.Requirement.DISABLED;
import static org.keycloak.models.AuthenticationExecutionModel.Requirement.REQUIRED;

import com.google.auto.service.AutoService;
import io.phasetwo.keycloak.client.access.OrgsAccessProvider;
import io.phasetwo.keycloak.client.common.OperationalInfo;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

@JBossLog
@AutoService(AuthenticatorFactory.class)
public class RestrictOrgsAuthAuthenticatorFactory
    implements AuthenticatorFactory, ServerInfoAwareProviderFactory {

  private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES =
      new AuthenticationExecutionModel.Requirement[] {REQUIRED, DISABLED};

  private static final String PROVIDER_ID = "restrict-orgs-auth-authenticator";

  private Config.Scope config;

  @Override
  public String getDisplayType() {
    return "Restrict user authentication on orgs";
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
    return "Restricts user authentication on orgs based on an access provider";
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return RestrictOrgsAuthConfigProperties.CONFIG_PROPERTIES;
  }

  @Override
  public Authenticator create(KeycloakSession session) {
    return new RestrictOrgsAuthAuthenticator();
  }

  @Override
  public void init(Config.Scope config) {
    this.config = config;
  }

  @Override
  public void postInit(KeycloakSessionFactory factory) {
    RestrictOrgsAuthConfigProperties.ACCESS_PROVIDER_ID_PROPERTY.setOptions(
        factory
            .getProviderFactoriesStream(OrgsAccessProvider.class)
            .map(ProviderFactory::getId)
            .collect(Collectors.toList()));
  }

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
