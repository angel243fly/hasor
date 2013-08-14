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
package org.hasor.mvc.controller.support;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hasor.context.AppContext;
/** 
 * 命名空间管理器。相同的action命名空间下的action方法，可以定义在不同的控制器下。
 * @version : 2013-4-20
 * @author 赵永春 (zyc@byshell.org)
 */
class ActionNameSpace {
    private String                        namespace        = null;
    private Map<String, ActionDefineImpl> actionInvokeMap  = new HashMap<String, ActionDefineImpl>();
    private List<ActionDefineImpl>        actionInvokeList = new ArrayList<ActionDefineImpl>();
    //
    public ActionNameSpace(String namespace) {
        this.namespace = namespace;
    }
    /**获取控制器名称。*/
    public String getNameSpace() {
        return this.namespace;
    }
    /**获取注册的所有Action*/
    public List<ActionDefineImpl> getActions() {
        return this.actionInvokeList;
    }
    /**获取控制器中定义的action方法。*/
    public ActionDefineImpl getActionByName(String actionMethod) {
        return this.actionInvokeMap.get(actionMethod);
    }
    /**初始化NameSpace*/
    public void initNameSpace(AppContext appContext) {
        for (ActionDefineImpl invoke : this.actionInvokeMap.values())
            invoke.initInvoke(appContext);
    }
    /**销毁NameSpace*/
    public void destroyNameSpace(AppContext appContext) {
        this.actionInvokeMap.clear();
    }
    /**添加Action*/
    public void putActionDefine(ActionDefineImpl define) {
        if (define == null)
            return;
        this.actionInvokeMap.put(define.getTargetMethod().getName(), define);
        this.actionInvokeList.add(define);
    }
}