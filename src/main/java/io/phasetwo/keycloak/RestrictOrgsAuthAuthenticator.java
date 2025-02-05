package io.phasetwo.keycloak;

import io.phasetwo.keycloak.access.OrgsAccessProvider;
import io.phasetwo.keycloak.access.orgs.member.OrgMembershipBasedAccessProviderFactory;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.events.Errors;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.OAuth2ErrorRepresentation;
import org.keycloak.services.messages.Messages;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.utils.MediaTypeMatcher;

@JBossLog
public class RestrictOrgsAuthAuthenticator implements Authenticator {

  private final String providerId;

  public RestrictOrgsAuthAuthenticator(String providerId) {
    this.providerId = providerId;
  }

  @Override
  public void authenticate(final AuthenticationFlowContext context) {
    final ClientModel client = context.getSession().getContext().getClient();
    final RestrictOrgsAuthConfig config =
        new RestrictOrgsAuthConfig(context.getAuthenticatorConfig());

    final OrgsAccessProvider access = getAccessProvider(context, providerId);
    final UserModel user = context.getUser();
    if (!access.isRestricted(user, context.getRealm(), config)) {
      context.success();
      return;
    }

    if (access.isPermitted(user, context.getRealm(), config)) {
      context.success();
    } else {
      context
          .getEvent()
          .realm(context.getRealm())
          .client(client)
          .user(context.getUser())
          .error(Errors.ACCESS_DENIED);
      context.failure(AuthenticationFlowError.ACCESS_DENIED, errorResponse(context, config));
    }
  }

  private OrgsAccessProvider getAccessProvider(
      AuthenticationFlowContext context, String providerId) {

    if (providerId != null) {
      OrgsAccessProvider accessProvider =
          context.getSession().getProvider(OrgsAccessProvider.class, providerId);

      if (accessProvider == null) {
        log.warnf("Configured access provider '%s' in authenticator not found.", providerId);
      } else {
        log.tracef("Using access provider '%s'.", providerId);
        return accessProvider;
      }
    } else {
      log.error("Null OrgAccessProvider for authenticator.");
    }

    log.warnf(
        "Defaulting to access provider '%s'.", OrgMembershipBasedAccessProviderFactory.PROVIDER_ID);
    return context
        .getSession()
        .getProvider(OrgsAccessProvider.class, OrgMembershipBasedAccessProviderFactory.PROVIDER_ID);
  }

  private Response errorResponse(AuthenticationFlowContext context, RestrictOrgsAuthConfig config) {
    Response response;
    if (MediaTypeMatcher.isHtmlRequest(context.getHttpRequest().getHttpHeaders())) {
      response = htmlErrorResponse(context, config);
    } else {
      response = oAuth2ErrorResponse();
    }
    return response;
  }

  private Response htmlErrorResponse(
      AuthenticationFlowContext context, RestrictOrgsAuthConfig config) {
    AuthenticationSessionModel authSession = context.getAuthenticationSession();
    return context
        .form()
        .setError(
            config.getErrorMessage(),
            authSession.getAuthenticatedUser().getUsername(),
            authSession.getClient().getClientId())
        .createErrorPage(Response.Status.FORBIDDEN);
  }

  private static Response oAuth2ErrorResponse() {
    return Response.status(Response.Status.UNAUTHORIZED.getStatusCode())
        .entity(
            new OAuth2ErrorRepresentation(Messages.ACCESS_DENIED, "Access to client is denied."))
        .type(MediaType.APPLICATION_JSON_TYPE)
        .build();
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
