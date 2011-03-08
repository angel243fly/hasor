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
package org.more.hypha.event;
import org.more.hypha.Event;
/**
 * 当清空Bean定义时候引发该事件。
 * @version 2010-10-11
 * @author 赵永春 (zyc@byshell.org)
 */
public class ClearDefineEvent extends Event {
    /**创建{@link ClearDefineEvent}对象。*/
    public ClearDefineEvent(Object target) {
        super(target);
    };
};