package io.phasetwo.keycloak.access;

import io.phasetwo.keycloak.RestrictOrgsAuthConfig;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.Provider;

public interface OrgsAccessProvider extends Provider {
  boolean isRestricted(UserModel userModel, RealmModel realmModel, RestrictOrgsAuthConfig config);

  boolean isPermitted(UserModel user, RealmModel realmModel, RestrictOrgsAuthConfig config);
}
