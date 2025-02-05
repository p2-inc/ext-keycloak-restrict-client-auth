package io.phasetwo.keycloak.client.access;

import java.util.Set;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.Provider;

public interface OrgsAccessProvider extends Provider {
  boolean isRestricted(UserModel userModel, RealmModel realmModel, Set<String> orgs);

  boolean isPermitted(UserModel user, RealmModel realmModel, Set<String> orgs);
}
