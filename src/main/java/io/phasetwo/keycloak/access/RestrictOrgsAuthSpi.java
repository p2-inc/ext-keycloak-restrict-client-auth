package io.phasetwo.keycloak.access;

import com.google.auto.service.AutoService;
import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

@AutoService(Spi.class)
public final class RestrictOrgsAuthSpi implements Spi {

  private static final String SPI_NAME = "restrict-orgs-auth-access-provider";

  @Override
  public boolean isInternal() {
    return true;
  }

  @Override
  public String getName() {
    return SPI_NAME;
  }

  @Override
  public Class<? extends Provider> getProviderClass() {
    return OrgsAccessProvider.class;
  }

  @Override
  public Class<? extends ProviderFactory<OrgsAccessProvider>> getProviderFactoryClass() {
    return OrgsAccessProviderFactory.class;
  }
}
