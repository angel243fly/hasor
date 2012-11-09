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
package org.more.webui.components.select.manytitle;
import org.more.webui.render.select.CheckManySelectInputRender;
/**
 * 继承自{@link CheckManySelectInputRender}取消输出check框。
 * <br><b>客户端模型</b>：UICheckManySelectInput（UICheckManySelectInput.js）
 * @version : 2012-5-18
 * @author 赵永春 (zyc@byshell.org)
 */
public class ManyTitleRender extends CheckManySelectInputRender<ManyTitle> {
    @Override
    protected RenderType getRenderType(ManyTitle component) {
        return RenderType.onlyTitle;
    }
}