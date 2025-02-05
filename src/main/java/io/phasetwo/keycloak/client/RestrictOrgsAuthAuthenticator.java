package io.phasetwo.keycloak.client;

import io.phasetwo.keycloak.client.access.OrgsAccessProvider;
import io.phasetwo.keycloak.client.access.orgs.OrgMembershipBasedAccessProviderFactory;
import jakarta.ws.rs.core.Response;
import java.util.Set;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.events.Errors;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

@JBossLog
public class RestrictOrgsAuthAuthenticator implements Authenticator {

  RestrictOrgsAuthAuthenticator() {}

  @Override
  public void authenticate(final AuthenticationFlowContext context) {
    final ClientModel client = context.getSession().getContext().getClient();
    final RestrictOrgsAuthConfig config =
        new RestrictOrgsAuthConfig(context.getAuthenticatorConfig());

    final OrgsAccessProvider access = getAccessProvider(context, config);
    final UserModel user = context.getUser();
    final Set<String> orgs = config.getOrgs();
    if (!access.isRestricted(user, context.getRealm(), orgs)) {
      context.success();
      return;
    }

    if (access.isPermitted(user, context.getRealm(), orgs)) {
      context.success();
    } else {
      context
          .getEvent()
          .realm(context.getRealm())
          .client(client)
          .user(context.getUser())
          .error(Errors.ACCESS_DENIED);
      context.failure(AuthenticationFlowError.ACCESS_DENIED, Response.accepted().build());
    }
  }

  private OrgsAccessProvider getAccessProvider(
      AuthenticationFlowContext context, RestrictOrgsAuthConfig config) {
    final String accessProviderId = config.getAccessProviderId();

    if (accessProviderId != null) {

      OrgsAccessProvider accessProvider =
          context.getSession().getProvider(OrgsAccessProvider.class, accessProviderId);

      if (accessProvider == null) {
        log.warnf(
            "Configured access provider '%s' in authenticator config '%s' does not exist.",
            accessProviderId, config.getAuthenticatorConfigAlias());
      } else {
        log.tracef(
            "Using access provider '%s' in authenticator config '%s'.",
            accessProviderId, config.getAuthenticatorConfigAlias());
        return accessProvider;
      }
    }

    final OrgsAccessProvider defaultProvider =
        context.getSession().getProvider(OrgsAccessProvider.class);
    if (defaultProvider != null) {
      return defaultProvider;
    }

    return context
        .getSession()
        .getProvider(OrgsAccessProvider.class, OrgMembershipBasedAccessProviderFactory.PROVIDER_ID);
  }

  @Override
  public void action(AuthenticationFlowContext context) {
    context.failure(AuthenticationFlowError.ACCESS_DENIED);
  }

  @Override
  public boolean requiresUser() {
    return true;
  }

  @Override
  public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
    return true;
  }

  @Override
  public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {}

  @Override
  public void close() {}
}
