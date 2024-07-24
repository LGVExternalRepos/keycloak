package org.keycloak.test.framework.admin;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.test.framework.annotations.TestAdminClient;
import org.keycloak.test.framework.config.Config;
import org.keycloak.test.framework.injection.InstanceContext;
import org.keycloak.test.framework.injection.LifeCycle;
import org.keycloak.test.framework.injection.RequestedInstance;
import org.keycloak.test.framework.injection.Supplier;
import org.keycloak.test.framework.server.KeycloakTestServer;

public class KeycloakAdminClientSupplier implements Supplier<Keycloak, TestAdminClient> {

    @Override
    public Class<TestAdminClient> getAnnotationClass() {
        return TestAdminClient.class;
    }

    @Override
    public Class<Keycloak> getValueType() {
        return Keycloak.class;
    }

    @Override
    public Keycloak getValue(InstanceContext<Keycloak, TestAdminClient> instanceContext) {
        KeycloakTestServer testServer = instanceContext.getDependency(KeycloakTestServer.class);
        return KeycloakBuilder.builder()
                .serverUrl(testServer.getBaseUrl())
                .realm("master")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(Config.getAdminClientId())
                .clientSecret(Config.getAdminClientSecret())
                .build();
    }

    @Override
    public LifeCycle getDefaultLifecycle() {
        return LifeCycle.GLOBAL;
    }

    @Override
    public boolean compatible(InstanceContext<Keycloak, TestAdminClient> a, RequestedInstance<Keycloak, TestAdminClient> b) {
        return true;
    }

    @Override
    public void close(InstanceContext<Keycloak, TestAdminClient> instanceContext) {
        instanceContext.getValue().close();
    }

}
