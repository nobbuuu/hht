package com.booyue.base.app;

import android.content.Context;

/**
 * Created by Administrator on 2018/7/6.09:39
 * <p>
 * 项目初始化类
 *  ProjectInit.getInstance()
 *           //必须先调用，因为后面的链式调用可能需要用到全局context
 *           .init(this)
 *           .apiHost(UrlConstant.URL)
 *           //下面初始化路由的时候需要用到debug
 * //        .debug(LoggerUtils.isDebgFileExist(this))
 *           .debug(true)
 *           .config()
 *
 */

public class ProjectInit {
    /**
     * 在application的onCreate()中进行初始化
     * @param context 全局上下文
     * @return
     */
    public ProjectInit init(Context context) {
        getConfigurator()
                .getConfigs()
                .put(ConfigKeys.APPLICATION_CONTEXT.name(), context.getApplicationContext());
        return this;
    }

    /**
     * 使用内部类实现单例，保证线程安全
     */
    private static final class ProjectInitHolder {
        private static final ProjectInit INSTANCE = new ProjectInit();
    }

    /**
     *   ProjectInit.getInstance()
     //必须先调用，因为后面的链式调用可能需要用到全局context
     .init(this)
     .apiHost(ApiConstant.HOST_URL)
     //下面初始化路由的时候需要用到debug
     .debug(LoggerUtils.isDebgFileExist(this))
     //                .debug(true)
     .config()
     * @return
     */
    public static final ProjectInit getInstance() {
        return ProjectInitHolder.INSTANCE;
    }


    private static Configurator getConfigurator() {
        return Configurator.getInstance();
    }

    //获取配置信息
    public static synchronized <T> T getConfiguration(Object key) {
        return getConfigurator().getConfiguration(key);
    }

    //保存配置信息
    public synchronized ProjectInit setConfiguration(Object key, Object value) {
        getConfigurator().getConfigs().put(key, value);
        return this;
    }

    public ProjectInit apiHost(String apiHost) {
        getConfigurator().getConfigs().put(ConfigKeys.API_HOST.name(), apiHost);
        return this;
    }


    //是否开启调式
    public ProjectInit debug(boolean isDebug) {
        getConfigurator().getConfigs().put(ConfigKeys.DEBUG.name(), isDebug);
        return this;
    }

    //设置数据库名称
    public ProjectInit databaseName(String dbName) {
        getConfigurator().getConfigs().put(ConfigKeys.DATABASE_NAME.name(), dbName);
        return this;
    }

    public static String getDatabaseName() {
        return getConfigurator().getConfiguration(ConfigKeys.DATABASE_NAME.name());
    }

    //配置host
    public static String getApiHost() {
        return getConfiguration(ConfigKeys.API_HOST.name());
    }

    public static Context getApplicationContext() {
        return getConfiguration(ConfigKeys.APPLICATION_CONTEXT.name());
    }

    public static boolean isDebug() {
        return getConfiguration(ConfigKeys.DEBUG.name());
    }

    /**
     * 配置完成之后调用
     *
     * @return
     */
    public ProjectInit config() {
        getConfigurator().configure();
        return this;
    }
}
