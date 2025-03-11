package io.phasetwo.keycloak.common;

public class CommunityProfiles {
  private static final String ENV_RESTRICT_ORG_AUTHENTICATORS_ENABLED =
      "KC_RESTRICT_ORG_AUTHENTICATORS_ENABLED";
  private static final String PROP_RESTRICT_ORG_AUTHENTICATORS_ENABLED =
      "kc.community.restrict.org.authenticator.enabled";

  private static final boolean isRestrictOrgAuthenticatorsEnabled;

  static {
    isRestrictOrgAuthenticatorsEnabled =
        Boolean.parseBoolean(System.getenv(ENV_RESTRICT_ORG_AUTHENTICATORS_ENABLED))
            || Boolean.parseBoolean(System.getProperty(PROP_RESTRICT_ORG_AUTHENTICATORS_ENABLED));
  }

  public static boolean isRestrictOrgAuthenticators() {
    return isRestrictOrgAuthenticatorsEnabled;
  }
}
