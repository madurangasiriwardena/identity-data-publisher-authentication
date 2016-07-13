/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.data.publisher.application.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.AuthenticationDataPublisher;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.context.SessionContext;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.core.bean.context.MessageContext;
import org.wso2.carbon.identity.core.handler.AbstractIdentityMessageHandler;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.data.publisher.application.authentication.model.AuthenticationData;
import org.wso2.carbon.identity.data.publisher.application.authentication.model.SessionData;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractAuthenticationDataPublisher extends AbstractIdentityMessageHandler implements
        AuthenticationDataPublisher {

    private static final Log log = LogFactory.getLog(AbstractAuthenticationDataPublisher.class);

    /**
     * Publish authentication success
     *
     * @param request Request which comes to the framework for authentication
     * @param context Authentication context
     * @param params  Other parameters which are need to be passed
     */
    public void publishAuthenticationStepSuccess(HttpServletRequest request, AuthenticationContext context,
                                                 Map<String, Object> params) {

        if (log.isDebugEnabled()) {
            log.debug("Publishing authentication step success");
        }
        AuthenticationData authenticationData = new AuthenticationData();
        int step = context.getCurrentStep();
        if (context.getExternalIdP() == null) {
            authenticationData.setIdentityProvider(FrameworkConstants.LOCAL_IDP_NAME);
        } else {
            authenticationData.setIdentityProvider(context.getExternalIdP().getIdPName());
        }
        Object userObj = params.get(FrameworkConstants.AnalyticsAttributes.USER);
        if (userObj != null && userObj instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) userObj;
            authenticationData.setTenantDomain(user.getTenantDomain());
            authenticationData.setUserStoreDomain(user.getUserStoreDomain());
            authenticationData.setUsername(user.getUserName());
        }
        Object isFederatedObj = params.get(FrameworkConstants.AnalyticsAttributes.IS_FEDERATED);
        if (isFederatedObj != null) {
            boolean isFederated = (Boolean) isFederatedObj;
            if (isFederated) {
                authenticationData.setIdentityProviderType(FrameworkConstants.FEDERATED_IDP_NAME);
            } else {
                authenticationData.setIdentityProviderType(FrameworkConstants.LOCAL_IDP_NAME);
            }
        }
        authenticationData.setContextId(context.getContextIdentifier());
        authenticationData.setEventId(UUID.randomUUID().toString());
        authenticationData.setAuthnSuccess(false);
        authenticationData.setRemoteIp(IdentityUtil.getClientIpAddress(request));
        authenticationData.setServiceProvider(context.getServiceProviderName());
        authenticationData.setInboundProtocol(context.getRequestType());
        authenticationData.setRememberMe(context.isRememberMe());
        authenticationData.setForcedAuthn(context.isForceAuthenticate());
        authenticationData.setPassive(context.isPassiveAuthenticate());
        authenticationData.setInitialLogin(true);
        authenticationData.setAuthenticator(context.getCurrentAuthenticator());
        authenticationData.setSuccess(true);
        authenticationData.setStepNo(step);
        doPublishAuthenticationStepSuccess(authenticationData);
    }

    /**
     * Published authentication step failure
     *
     * @param request Incoming Http request to framework for authentication
     * @param context Authentication Context
     * @param params  Other relevant parameters which needs to be published
     */
    public void publishAuthenticationStepFailure(HttpServletRequest request, AuthenticationContext context,
                                                 Map<String, Object> params) {

        if (log.isDebugEnabled()) {
            log.debug("Publishing authentication step failure");
        }
        AuthenticationData authenticationData = new AuthenticationData();
        int step = context.getCurrentStep();
        if (context.getExternalIdP() == null) {
            authenticationData.setIdentityProvider(FrameworkConstants.LOCAL_IDP_NAME);
        } else {
            authenticationData.setIdentityProvider(context.getExternalIdP().getIdPName());
        }
        Object userObj = params.get(FrameworkConstants.AnalyticsAttributes.USER);
        if (userObj != null && userObj instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) userObj;
            authenticationData.setTenantDomain(user.getTenantDomain());
            authenticationData.setUserStoreDomain(user.getUserStoreDomain());
            authenticationData.setUsername(user.getUserName());
        }
        Object isFederatedObj = params.get(FrameworkConstants.AnalyticsAttributes.IS_FEDERATED);
        if (isFederatedObj != null) {
            boolean isFederated = (Boolean) isFederatedObj;
            if (isFederated) {
                authenticationData.setIdentityProviderType(FrameworkConstants.FEDERATED_IDP_NAME);
            } else {
                authenticationData.setIdentityProviderType(FrameworkConstants.LOCAL_IDP_NAME);
            }
        }
        authenticationData.setContextId(context.getContextIdentifier());
        authenticationData.setEventId(UUID.randomUUID().toString());
        authenticationData.setAuthnSuccess(false);
        authenticationData.setRemoteIp(IdentityUtil.getClientIpAddress(request));
        authenticationData.setServiceProvider(context.getServiceProviderName());
        authenticationData.setInboundProtocol(context.getRequestType());
        authenticationData.setRememberMe(context.isRememberMe());
        authenticationData.setForcedAuthn(context.isForceAuthenticate());
        authenticationData.setPassive(context.isPassiveAuthenticate());
        authenticationData.setInitialLogin(true);
        authenticationData.setAuthenticator(context.getCurrentAuthenticator());
        authenticationData.setSuccess(false);
        authenticationData.setStepNo(step);
        doPublishAuthenticationStepFailure(authenticationData);
    }

    /**
     * Publishes authentication success
     *
     * @param request Incoming request for authentication
     * @param context Authentication context
     * @param params  Other relevant parameters which needs to be published
     */
    public void publishAuthenticationSuccess(HttpServletRequest request, AuthenticationContext context,
                                             Map<String, Object> params) {

        if (log.isDebugEnabled()) {
            log.debug("Publishing authentication success");
        }
        AuthenticationData authenticationData = new AuthenticationData();
        Object userObj = params.get(FrameworkConstants.AnalyticsAttributes.USER);
        if (userObj != null && userObj instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) userObj;
            authenticationData.setTenantDomain(user.getTenantDomain());
            authenticationData.setUserStoreDomain(user.getUserStoreDomain());
            authenticationData.setUsername(user.getUserName());
        }

        Object hasFederatedStepObj = context.getProperty(FrameworkConstants.AnalyticsAttributes.HAS_FEDERATED_STEP);
        Object hasLocalStepObj = context.getProperty(FrameworkConstants.AnalyticsAttributes.HAS_LOCAL_STEP);
        boolean hasFederated = false;
        boolean hasLocal = false;
        if (hasFederatedStepObj != null) {
            hasFederated = (Boolean) hasFederatedStepObj;
        }
        if (hasLocalStepObj != null) {
            hasLocal = (Boolean) hasLocalStepObj;
        }
        if (hasFederated && hasLocal) {
            authenticationData.setIdentityProviderType(FrameworkConstants.FEDERATED_IDP_NAME + "," +
                    FrameworkConstants.LOCAL_IDP_NAME);
        } else if (hasLocal) {
            authenticationData.setIdentityProviderType(FrameworkConstants.LOCAL_IDP_NAME);
        } else if (hasFederated) {
            authenticationData.setIdentityProviderType(FrameworkConstants.FEDERATED_IDP_NAME);
        }
        authenticationData.setSuccess(true);
        authenticationData.setContextId(context.getContextIdentifier());
        authenticationData.setEventId(UUID.randomUUID().toString());
        authenticationData.setAuthnSuccess(true);
        authenticationData.setRemoteIp(IdentityUtil.getClientIpAddress(request));
        authenticationData.setServiceProvider(context.getServiceProviderName());
        authenticationData.setInboundProtocol(context.getRequestType());
        authenticationData.setRememberMe(context.isRememberMe());
        authenticationData.setForcedAuthn(context.isForceAuthenticate());
        authenticationData.setPassive(context.isPassiveAuthenticate());
        authenticationData.setInitialLogin(true);
        doPublishAuthenticationSuccess(authenticationData);
    }

    /**
     * Publishes authentication failure
     *
     * @param request Incoming authentication request
     * @param context Authentication context
     * @param params  Other relevant parameters which needs to be published
     */
    public void publishAuthenticationFailure(HttpServletRequest request, AuthenticationContext context,
                                             Map<String, Object> params) {

        if (log.isDebugEnabled()) {
            log.debug("Publishing authentication failure");
        }
        AuthenticationData authenticationData = new AuthenticationData();
        Object userObj = params.get(FrameworkConstants.AnalyticsAttributes.USER);
        if (userObj != null && userObj instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) userObj;
            authenticationData.setTenantDomain(user.getTenantDomain());
            authenticationData.setUserStoreDomain(user.getUserStoreDomain());
            authenticationData.setUsername(user.getUserName());
        }

        authenticationData.setContextId(context.getContextIdentifier());
        authenticationData.setEventId(UUID.randomUUID().toString());
        authenticationData.setAuthnSuccess(false);
        authenticationData.setRemoteIp(IdentityUtil.getClientIpAddress(request));
        authenticationData.setServiceProvider(context.getServiceProviderName());
        authenticationData.setInboundProtocol(context.getRequestType());
        authenticationData.setRememberMe(context.isRememberMe());
        authenticationData.setForcedAuthn(context.isForceAuthenticate());
        authenticationData.setPassive(context.isPassiveAuthenticate());
        authenticationData.setInitialLogin(true);
        doPublishAuthenticationFailure(authenticationData);
    }

    /**
     * Publishes session creation information
     *
     * @param request        Incoming request for authentication
     * @param context        Authentication Context
     * @param sessionContext Session context
     * @param params         Other relevant parameters which needs to be published
     */
    public void publishSessionCreation(HttpServletRequest request, AuthenticationContext context, SessionContext
            sessionContext, Map<String, Object> params) {

        if (log.isDebugEnabled()) {
            log.debug("Publishing session creation");
        }
        SessionData sessionData = new SessionData();
        Object userObj = params.get(FrameworkConstants.AnalyticsAttributes.USER);
        String sessionId = (String) params.get(FrameworkConstants.AnalyticsAttributes.SESSION_ID);
        String userName = null;
        String userStoreDomain = null;
        String tenantDomain = null;
        Long terminationTime = null;
        Long createdTime = null;
        if (userObj != null && userObj instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) userObj;
            userName = user.getUserName();
            userStoreDomain = user.getUserStoreDomain();
            tenantDomain = user.getTenantDomain();
        }
        if (sessionContext != null) {
            Object createdTimeObj = sessionContext.getProperty(FrameworkConstants.CREATED_TIMESTAMP);
            createdTime = (Long) createdTimeObj;
            terminationTime = AuthnDataPublisherUtils.getSessionExpirationTime(createdTime, createdTime,
                    tenantDomain, sessionContext.isRememberMe());
            sessionData.setIsRememberMe(sessionContext.isRememberMe());
        }
        sessionData.setUser(userName);
        sessionData.setUserStoreDomain(userStoreDomain);
        sessionData.setTenantDomain(tenantDomain);
        sessionData.setSessionId(sessionId);
        sessionData.setCreatedTimestamp(createdTime);
        sessionData.setUpdatedTimestamp(createdTime);
        sessionData.setTerminationTimestamp(terminationTime);
        sessionData.setRemoteIP(IdentityUtil.getClientIpAddress(request));
        sessionData.setUserAgent(request.getHeader(AuthPublisherConstants.USER_AGENT));

        doPublishSessionCreation(sessionData);
    }

    /**
     * Publishes session update
     *
     * @param request        Incoming request for authentication
     * @param context        Authentication context
     * @param sessionContext Session context
     * @param params         Other relevant parameters which needs to be published
     */
    public void publishSessionUpdate(HttpServletRequest request, AuthenticationContext context, SessionContext
            sessionContext, Map<String, Object> params) {

        if (log.isDebugEnabled()) {
            log.debug("Publishing session update");
        }

        SessionData sessionData = new SessionData();
        Object userObj = params.get(FrameworkConstants.AnalyticsAttributes.USER);
        String sessionId = (String) params.get(FrameworkConstants.AnalyticsAttributes.SESSION_ID);
        String userName = null;
        String userStoreDomain = null;
        String tenantDomain = null;
        Long terminationTime = null;
        Long createdTime = null;
        Long currentTime = System.currentTimeMillis();

        if (userObj != null && userObj instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) userObj;
            userName = user.getUserName();
            userStoreDomain = user.getUserStoreDomain();
            tenantDomain = user.getTenantDomain();
        }

        if (sessionContext != null) {
            Object createdTimeObj = sessionContext.getProperty(FrameworkConstants.CREATED_TIMESTAMP);
            createdTime = (Long) createdTimeObj;
            terminationTime = AuthnDataPublisherUtils.getSessionExpirationTime(createdTime, currentTime,
                    tenantDomain, sessionContext.isRememberMe());
            sessionData.setIsRememberMe(sessionContext.isRememberMe());
        }

        sessionData.setUser(userName);
        sessionData.setUserStoreDomain(userStoreDomain);
        sessionData.setTenantDomain(tenantDomain);
        sessionData.setSessionId(sessionId);
        sessionData.setCreatedTimestamp(createdTime);
        sessionData.setUpdatedTimestamp(currentTime);
        sessionData.setTerminationTimestamp(terminationTime);
        sessionData.setRemoteIP(IdentityUtil.getClientIpAddress(request));

        doPublishSessionUpdate(sessionData);
    }

    /**
     * Publishes session termination
     *
     * @param request        Incoming request for authentication
     * @param context        Authentication context
     * @param sessionContext Session context
     * @param params         Other relevant parameters which needs to be published
     */
    public void publishSessionTermination(HttpServletRequest request, AuthenticationContext context,
                                          SessionContext sessionContext, Map<String, Object> params) {

        if (log.isDebugEnabled()) {
            log.debug("Publishing session termination");
        }
        SessionData sessionData = new SessionData();
        Object userObj = params.get(FrameworkConstants.AnalyticsAttributes.USER);
        String sessionId = (String) params.get(FrameworkConstants.AnalyticsAttributes.SESSION_ID);
        String userName = null;
        String userStoreDomain = null;
        String tenantDomain = null;
        Long createdTime = null;
        Long currentTime = System.currentTimeMillis();
        if (userObj != null && userObj instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) userObj;
            userName = user.getUserName();
            userStoreDomain = user.getUserStoreDomain();
            tenantDomain = user.getTenantDomain();
        }

        if (sessionContext != null) {
            Object createdTimeObj = sessionContext.getProperty(FrameworkConstants.CREATED_TIMESTAMP);
            createdTime = (Long) createdTimeObj;
            sessionData.setIsRememberMe(sessionContext.isRememberMe());
        }

        sessionData.setUser(userName);
        sessionData.setUserStoreDomain(userStoreDomain);
        sessionData.setTenantDomain(tenantDomain);
        sessionData.setSessionId(sessionId);
        sessionData.setCreatedTimestamp(createdTime);
        sessionData.setUpdatedTimestamp(currentTime);
        sessionData.setTerminationTimestamp(currentTime);
        sessionData.setRemoteIP(IdentityUtil.getClientIpAddress(request));
        doPublishSessionTermination(sessionData);
    }

    /**
     * Does the publishing part of authentication step success
     *
     * @param authenticationData Bean with authentication information
     */
    public abstract void doPublishAuthenticationStepSuccess(AuthenticationData authenticationData);

    /**
     * Does the publishing part of authentication step failure
     *
     * @param authenticationData Bean with authentication information
     */
    public abstract void doPublishAuthenticationStepFailure(AuthenticationData authenticationData);

    /**
     * Does the publishing part of authentication success
     *
     * @param authenticationData Bean with authentication information
     */
    public abstract void doPublishAuthenticationSuccess(AuthenticationData authenticationData);

    /**
     * Does the publishing part of authentication step failure
     *
     * @param authenticationData Bean with authentication information
     */
    public abstract void doPublishAuthenticationFailure(AuthenticationData authenticationData);

    /**
     * Does the publishing part of session creation
     *
     * @param sessionData Bean with session information
     */
    public abstract void doPublishSessionCreation(SessionData sessionData);

    /**
     * Does the publishing part of session update
     *
     * @param sessionData Bean with session information
     */
    public abstract void doPublishSessionUpdate(SessionData sessionData);

    /**
     * Does the publishing part of session termination
     *
     * @param sessionData Bean with session information
     */
    public abstract void doPublishSessionTermination(SessionData sessionData);

    @Override
    public boolean canHandle(MessageContext messageContext) {
        return true;
    }
}