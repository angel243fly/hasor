/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
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
package net.hasor.core.context;
import java.io.File;
import java.io.IOException;
import net.hasor.Hasor;
import net.hasor.core.AppContext;
import net.hasor.core.Environment;
import net.hasor.core.environment.FileEnvironment;
import org.more.UnhandledException;
/**
 * {@link AppContext}�ӿ�Ĭ��ʵ�֡�
 * @version : 2013-4-9
 * @author ������ (zyc@hasor.net)
 */
public class FileAppContext extends SimpleAppContext {
    /***/
    public FileAppContext(String mainSettings) {
        mainSettings = Hasor.assertIsNotNull(mainSettings);
        this.mainSettings = new File(mainSettings);
    }
    /***/
    public FileAppContext(File mainSettings) {
        mainSettings = Hasor.assertIsNotNull(mainSettings);
        this.mainSettings = mainSettings;
    }
    /***/
    public FileAppContext(String mainSettings, Object context) {
        mainSettings = Hasor.assertIsNotNull(mainSettings);
        this.mainSettings = new File(mainSettings);
        this.setContext(context);
    }
    public FileAppContext(File mainSettings, Object context) {
        mainSettings = Hasor.assertIsNotNull(mainSettings);
        this.mainSettings = mainSettings;
        this.setContext(context);
    }
    //
    private File mainSettings = null;
    protected Environment createEnvironment() {
        if (!this.mainSettings.exists() || !this.mainSettings.canRead() || !this.mainSettings.isFile())
            throw new UnhandledException(new IOException("��" + this.mainSettings.getAbsolutePath() + "�� can not read or not exists or is directory."));
        return new FileEnvironment(this.mainSettings);
    }
}