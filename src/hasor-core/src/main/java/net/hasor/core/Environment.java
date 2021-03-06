/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
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
package net.hasor.core;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Set;
/**
 * 环境支持
 * @version : 2013-6-19
 * @author 赵永春 (zyc@hasor.net)
 */
public interface Environment {
    /**获取系统启动时间*/
    public long getStartTime();
    /**获取配置文件URI*/
    public URI getSettingURI();
    /**获取应用程序配置。*/
    public Settings getSettings();
    /**获取事件操作接口。*/
    public EventManager getEventManager();
    /**在框架扫描包的范围内查找具有特征类集合。（特征可以是继承的类、标记的注解）*/
    public Set<Class<?>> getClassSet(Class<?> featureType);
    /**释放环境所占用的资源*/
    public void release();
    /**判断是否为调试模式。*/
    public boolean isDebug();
    //
    //
    //
    /**添加配置文件改变事件监听器。*/
    public void addSettingsListener(SettingsListener listener);
    /**删除配置文件改变事件监听器。*/
    public void removeSettingsListener(SettingsListener listener);
    /**获得所有配置文件改变事件监听器。*/
    public SettingsListener[] getSettingListeners();
    //
    //
    //
    /**获取工作目录，工作路径的配置可以在config.xml的“<b>environmentVar.HASOR_WORK_HOME</b>”节点上配置。*/
    public static final String Work_Home          = "HASOR_WORK_HOME";
    /**获取临时文件存放目录，工作路径的配置可以在config.xml的“<b>environmentVar.HASOR_TEMP_PATH</b>”节点上配置。*/
    public static final String TempPath           = "HASOR_TEMP_PATH";
    /**获取工作空间中专门用于存放日志的目录空间，配置可以在config.xml的“<b>environmentVar.HASOR_LOG_PATH</b>”节点上配置。*/
    public static final String LogPath            = "HASOR_LOG_PATH";
    /**获取工作空间中专门用于存放模块配置信息的目录空间，配置可以在config.xml的“<b>environmentVar.HASOR_PLUGIN_PATH</b>”节点上配置。*/
    public static final String PluginPath         = "HASOR_PLUGIN_PATH";
    /**获取工作空间中专门用于存放模块配置信息的目录空间，配置可以在config.xml的“<b>environmentVar.HASOR_PLUGIN_SETTINGS</b>”节点上配置。*/
    public static final String PluginSettingsPath = "HASOR_PLUGIN_SETTINGS";
    //
    /**计算字符串，将字符串中定义的环境变量替换为环境变量值。环境变量名不区分大小写。<br/>
     * <font color="ff0000"><b>注意</b></font>：只有被百分号包裹起来的部分才被解析成为环境变量名，
     * 如果无法解析某个环境变量平台会抛出一条警告，并且将环境变量名连同百分号作为环境变量值一起返回。<br/>
     * <font color="00aa00"><b>例如</b></font>：环境中定义了变量Hasor_Home=C:/hasor、Java_Home=c:/app/java，下面的解析结果为
     * <div>%hasor_home%/tempDir/uploadfile.tmp&nbsp;&nbsp;--&gt;&nbsp;&nbsp;C:/hasor/tempDir/uploadfile.tmp</div>
     * <div>%JAVA_HOME%/bin/javac.exe&nbsp;&nbsp;--&gt;&nbsp;&nbsp;c:/app/java/bin/javac.exe</div>
     * <div>%work_home%/data/range.png&nbsp;&nbsp;--&gt;&nbsp;&nbsp;%work_home%/data/range.png；并伴随一条警告</div>
     * */
    public String evalString(String eval);
    /**计算指定名称的环境变量值.*/
    public String evalEnvVar(String varName);
    /**根据环境变量名称获取环境变量的值，如果不存在该环境变量的定义则返回null.*/
    public String getEnvVar(String varName);
    /**添加环境变量，添加的环境变量并不会影响到系统环境变量，它会使用内部Map保存环境变量从而避免影响JVM正常运行。*/
    public void addEnvVar(String varName, String value);
    /**删除环境变量，该方法从内部Map删除所保存的环境变量，这样做的目的是为了避免影响JVM正常运行。*/
    public void remoteEnvVar(String varName);
    /**获取定义的环境变量集合，Map形式返回。*/
    public Map<String, String> getEnv();
    /**在缓存目录内创建一个不重名的临时文件名。*/
    public File uniqueTempFile() throws IOException;
}