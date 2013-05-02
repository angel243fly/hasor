/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.platform.security;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import org.platform.context.AppContext;
import org.platform.security.DefaultSecurityQuery.CheckPermission;
/**
 * ��ȫ��֤ϵͳ��������ֻ��Ҫ���SessionData�洢�Ϳ����ˡ�
 * @version : 2013-4-9
 * @author ������ (zyc@byshell.org)
 */
public abstract class SecurityContext {
    /*��ʾ����Ӧ����ʱ���ͻ���cookie��д*/
    public static final String               WriteAuthSession         = "SecurityContext.WriteAuthSession";
    //
    private AppContext                       appContext               = null;
    private InternalDispatcherManager        dispatcherManager        = null;
    private InternalUriPatternMatcherManager uriPatternMatcherManager = null;
    private InternalCodeDigestManager        codeDigestManager        = null;
    private UriPatternMatcher                defaultRules             = null;
    private ThreadLocal<List<AuthSession>>   currentAuthSessionList   = null;
    private ManagedSecurityAuthManager       securityAuthManager      = null;
    private ManagedSecurityAccessManager     securityAccessManager    = null;
    private SecuritySettings                 settings                 = null;
    //
    //
    //
    /**ʹ��SessionData�����ݴ���AuthSession��*/
    protected abstract void removeSessionData(String sessionDataID);
    /**����ˢ�»������ݵĹ���ʱ��*/
    protected abstract void updateSessionData(String sessionID);
    /**����SessionData*/
    protected abstract void updateSessionData(String sessionDataID, SessionData newSessionData);
    /**ʹ��SessionData�����ݴ���AuthSession��*/
    protected abstract SessionData getSessionData(String sessionDataID);
    //
    /**�½�AuthSession*/
    protected AuthSession newAuthSession(String authSessionID) throws SecurityException {
        AuthSession newAuthSession = new AuthSession(authSessionID, this) {};
        return newAuthSession;
    };
    //
    //
    /**��ʼ������*/
    public synchronized void initSecurity(AppContext appContext) {
        this.appContext = appContext;
        //
        this.dispatcherManager = new InternalDispatcherManager();
        this.uriPatternMatcherManager = new InternalUriPatternMatcherManager();
        this.codeDigestManager = new InternalCodeDigestManager();
        this.securityAuthManager = new ManagedSecurityAuthManager();
        this.securityAccessManager = new ManagedSecurityAccessManager();
        //
        this.settings = appContext.getBean(SecuritySettings.class);
        this.defaultRules = settings.getRulesDefault();
        //
        this.dispatcherManager.initManager(appContext);
        this.uriPatternMatcherManager.initManager(appContext);
        this.codeDigestManager.initManager(appContext);
        this.securityAuthManager.initManager(appContext);
        this.securityAccessManager.initManager(appContext);
        //
        this.currentAuthSessionList = new ThreadLocal<List<AuthSession>>();
    }
    /**���ٷ���*/
    public synchronized void destroySecurity(AppContext appContext) {
        this.dispatcherManager.destroyManager(appContext);
        this.uriPatternMatcherManager.destroyManager(appContext);
        this.codeDigestManager.destroyManager(appContext);
        this.securityAuthManager.destroyManager(appContext);
        this.securityAccessManager.destroyManager(appContext);
        //
        this.currentAuthSessionList = new ThreadLocal<List<AuthSession>>();
    }
    /**����һ��Ȩ�޻Ự������Ϊʹ�õ���Ȩϵͳ*/
    public synchronized AuthSession createAuthSession() throws SecurityException {
        AuthSession newAuthSession = this.newAuthSession(AppContext.genIDBy36());
        newAuthSession.loadSessionData(new SessionData());
        this.activateAuthSession(newAuthSession);
        return newAuthSession;
    };
    /**ͨ��AuthSessionID��ȡȨ�޻Ự�������ڷ��ؿա�*/
    public AuthSession getAuthSession(String authSessionID) throws SecurityException {
        SessionData sessionData = this.getSessionData(authSessionID);
        if (sessionData != null) {
            AuthSession newAuthSession = this.newAuthSession(authSessionID);
            newAuthSession.loadSessionData(sessionData);
            return newAuthSession;
        }
        return null;
    };
    /**��������ʾ�ĻỰ�����ǰ�̡߳�*/
    public synchronized boolean activateAuthSession(String authSessionID) throws SecurityException {
        AuthSession authSession = this.getAuthSession(authSessionID);/*�÷�����������סauthSessionID�Ķ���*/
        if (authSession != null)
            return this.activateAuthSession(authSession);
        return false;
    };
    /**��������ʾ�ĻỰ�����ǰ�̡߳�*/
    public synchronized boolean activateAuthSession(AuthSession activateAuthSession) throws SecurityException {
        if (activateAuthSession == null)
            return false;
        //
        List<AuthSession> curSessionList = this.currentAuthSessionList.get();
        if (curSessionList == null) {
            curSessionList = new ArrayList<AuthSession>();
            this.currentAuthSessionList.set(curSessionList);
        }
        //
        String activateAuthSessionID = activateAuthSession.getSessionID();
        for (AuthSession authSession : curSessionList) {
            if (authSession.getSessionID().equals(activateAuthSessionID) == true)
                return false;
        }
        // 
        curSessionList.add(activateAuthSession);
        //
        return true;
    }
    /**�ӵ�ǰ�߳��л�ĻỰ��ȥ��ĳ���Ự��*/
    public synchronized boolean inactivationAuthSession(String sessionID) {
        List<AuthSession> curSessionList = this.currentAuthSessionList.get();
        if (curSessionList == null)
            return false;
        //
        AuthSession removeAuthSession = null;
        for (AuthSession authSession : curSessionList) {
            if (authSession.getSessionID().equals(sessionID) == true)
                removeAuthSession = authSession;
        }
        //
        if (removeAuthSession != null) {
            curSessionList.remove(removeAuthSession);
            if (curSessionList.size() == 0)
                this.currentAuthSessionList.remove();
            return true;
        }
        //
        return false;
    };
    /**�ӵ�ǰ�߳��л�ĻỰ��ȥ��ĳ���Ự��*/
    public synchronized boolean inactivationAuthSession(AuthSession authSession) {
        return this.inactivationAuthSession(authSession.getSessionID());
    }
    /**��ȡ��ǰ�̰߳󶨵�Ȩ�޻Ự���ϡ�����ֵ������Ϊ�ա�*/
    public AuthSession[] getCurrentAuthSession() {
        List<AuthSession> curSessionMap = this.currentAuthSessionList.get();
        if (curSessionMap == null || curSessionMap.size() == 0)
            return new AuthSession[0];
        else {
            return curSessionMap.toArray(new AuthSession[curSessionMap.size()]);
        }
    };
    public AuthSession getCurrentBlankAuthSession() {
        AuthSession[] authList = getCurrentAuthSession();
        if (authList != null)
            for (AuthSession auth : authList)
                if (auth.isBlank() == true)
                    return auth;
        return null;
    }
    public AuthSession getCurrentGuestAuthSession() {
        AuthSession[] authList = getCurrentAuthSession();
        if (authList != null)
            for (AuthSession auth : authList)
                if (auth.isGuest() == true)
                    return auth;
        return null;
    }
    /**��ȡ���빤��*/
    public Digest getCodeDigest(String name) throws SecurityException {
        Digest digest = this.codeDigestManager.getCodeDigest(name);
        if (digest == null)
            throw new SecurityException("CodeDigest :" + name + " is Undefined.");
        return digest;
    };
    /**����uri��ȡ�����ж�Ȩ�޵Ĺ��ܽӿڡ�*/
    public UriPatternMatcher getUriMatcher(String requestPath) {
        UriPatternMatcher matcher = this.uriPatternMatcherManager.getUriMatcher(requestPath);
        return (matcher == null) ? this.defaultRules : matcher;
    }
    /**����uri��ȡ��������ת�����ࡣ*/
    public SecurityDispatcher getDispatcher(String requestPath) throws ServletException {
        return this.dispatcherManager.getDispatcher(requestPath);
    };
    /**��ȡ{@link ISecurityAuth}�ӿڶ�����������ڷ���null��*/
    protected ISecurityAuth getSecurityAuth(String authName) throws SecurityException {
        return securityAuthManager.getSecurityAuth(authName, this.appContext);
    }
    /**��ȡ{@link ISecurityAccess}�ӿڶ�����������ڷ���null��*/
    protected ISecurityAccess getSecurityAccess(String authName) {
        return securityAccessManager.getSecurityAccess(authName, this.appContext);
    }
    /**��Permissionע��ת��ΪSecurityNode��*/
    public SecurityNode getSecurityCondition(Permission permission) {
        return new CheckPermission(permission);
    }
    /**��Stringע��ת��ΪSecurityNode��*/
    public SecurityNode getSecurityCondition(String permissionCode) {
        return new CheckPermission(new Permission(permissionCode));
    }
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery() {
        return this.appContext.getGuice().getInstance(SecurityQuery.class);
    };
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(Permission permission) {
        return this.newSecurityQuery().and(permission);
    }
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(String permissionCode) {
        return this.newSecurityQuery().and(permissionCode);
    }
    /**����{@link SecurityQuery} �࣬����������������û���Ȩ�ޡ�*/
    public SecurityQuery newSecurityQuery(SecurityNode testNode) {
        return this.newSecurityQuery().andCustomer(testNode);
    }
}