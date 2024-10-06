# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Users\Administrator\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
#指定代码的压缩级别
    -optimizationpasses 5

    #包明不混合大小写
    -dontusemixedcaseclassnames

    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses

     #优化  不优化输入的类文件
    -dontoptimize

     #预校验
    -dontpreverify

     #混淆时是否记录日志
    -verbose

     # 混淆时所采用的算法
   # -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    #保护注解
    -keepattributes *Annotation*

    # 保持哪些类不被混淆
    -keep public class * extends android.app.Fragment
    -dontwarn android.support.v4.**
    -keep public class * extends android.support.v4.**{ *; }
    -dontwarn android.support.v7.**
    -keep public class * extends android.support.v7.**{ *; }
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService

   #####    Gson
   -keep class com.google.gson.** {*;}
   -keep class sun.misc.Unsafe { *; }
   -keep class com.google.gson.stream.** { *; }
   -keep class com.google.gson.examples.android.model.** { *; }
   -keep class com.google.** {
       <fields>;
       <methods>;
   }
   -keepclassmembers class * implements java.io.Serializable {
       static final long serialVersionUID;
       private static final java.io.ObjectStreamField[] serialPersistentFields;
       private void writeObject(java.io.ObjectOutputStream);
       private void readObject(java.io.ObjectInputStream);
       java.lang.Object writeReplace();
       java.lang.Object readResolve();
   }


    ###########  不混淆R类
   -keep public class **.R$*{
       public static final int *;
   }
   ###########  不混淆枚举
   -keepclassmembers enum * {
       public static **[] values();
       public static ** valueOf(java.lang.String);
   }

   -keepclasseswithmembers class com.booyue.poetry.bean.** {
        <fields>;
        <methods>;
   }
    -keepclasseswithmembers class com.booyue.base.bean.** {
        <fields>;
        <methods>;
   }
  -keepclasseswithmembers class com.booyue.database.greendao.bean.** {
         <fields>;
        <methods>;
   }

 -keepclasseswithmembers class com.booyue.database.greendao.gen.** {
         <fields>;
        <methods>;
   }


      #####    Vitamio
       -dontwarn mp3.**
       -keep class mp3.** { *; }
       -dontwarn mpg.**
       -keep class mpg.** { *; }
       -dontwarn org.tecunhuman**
       -keep class org.tecunhuman.** { *; }



    #####    Vitamio
    -dontwarn io.vov.vitamio.**
    -keep class io.vov.vitamio.** { *; }


       #####    bluetoothcontrol
    -dontwarn com.jieli.bluetoothcontrol.**
    -keep class com.jieli.bluetoothcontrol.** { *; }


    ####    viewpagerIndicator
    -dontwarn com.viewpagerindicator.**
    -keep class com.viewpagerindicator.** { *; }

   ####    utilslibrary
    -dontwarn com.lidroid.xutils.**
    -keep class com.lidroid.xutils.** { *; }

    #####    bluetoothLibrary
    -dontwarn com.chipsguide.lib.bluetooth.**
    -keep class com.chipsguide.lib.bluetooth.** { *; }
    -dontwarn com.ifeegoo.lib.toolbox.**
    -keep class com.ifeegoo.lib.toolbox.** { *; }

    ###commons_codec###
    -dontwarn org.apache.commons.codec.**
    -keep class org.apache.commons.codec.**{ *; }


    ####mibar-core####
    -dontwarn com.csr.gaia.android.library.gaia.**
    -keep class com.csr.gaia.android.library.gaia.** { *; }
    -dontwarn com.xiaomi.mitv.**
    -keep class com.xiaomi.mitv.** { *; }



    ###ibluz###
    -dontwarn com.actions.ibluz.**
    -keep class com.actions.ibluz.**{ *; }

    ###jsoup###
    -dontwarn org.jsoup.**
    -keep class org.jsoup.**{ *; }

   ###okhttp3###
    -dontwarn okhttp3.**
    -keep class okhttp3.**{ *; }
    ###okio###
    -dontwarn okio.**
    -keep class okio.**{ *; }


    ###okhttp3###
    -dontwarn com.squareup.okhttp.**
    -keep class com.squareup.okhttp.**{ *; }

    -dontwarn javax.net.ssl.**
    -keep class javax.net.ssl.**{ *; }


    ####   ormlite
    -dontwarn com.j256.ormlite.**
    -keep class com.j256.ormlite.** { *; }


    ##pinyin###
    -dontwarn com.hp.hpl.sparta.**
    -keep class com.hp.hpl.sparta.** { *; }
    -dontwarn net.sourceforge.pinyin4j.**
    -keep class net.sourceforge.pinyin4j.** { *; }
    -dontwarn pinyindb.**
    -keep class pinyindb.** { *; }


    ###soundtouch###
    -dontwarn com.example.soundtouchdemo.**
    -keep class com.example.soundtouchdemo.** { *; }

   ########   permission-lib
   -keep class com.zhy.m.permission.** {*;}
   -dontwarn com.zhy.m.permission.**


   ####viewinjected
    -dontwarn com.dbjtech.inject.**
    -keep class com.dbjtech.inject.** { *; }


    ####volley
    -dontwarn com.android.volley.**
    -keep class com.android.volley.** { *; }
      ####volley
    -dontwarn org.apache.http.**
    -keep class org.apache.http.** { *; }


  ####videocache
    -dontwarn com.danikula.videocache.**
    -keep class com.danikula.videocache.** { *; }

  ####shuwei
    -dontwarn com.shuwei.logcollection.**
    -keep class com.shuwei.logcollection.** { *; }
    -dontwarn com.szshuwei.x.**
    -keep class com.szshuwei.x.** { *; }



    ###  忽略警告
    -ignorewarning


    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt

    #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }
    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }
    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable
    #避免混淆泛型 如果混淆报错建议关掉
    -keepattributes Signature
    # 实体类不参与混淆。
    -keep class com.booyue.poetry.bean.** { *; }

    # 自定义控件不参与混淆
    -keep class com.booyue.poetry.view.** { *; }
    #自定义注解不参与混淆
    -keep class * extends java.lang.annotation.Annotation { *; }
    -keep interface * extends java.lang.annotation.Annotation { *; }
    ######          友盟分享
     -dontshrink
     -dontoptimize
     -dontwarn com.google.android.maps.**
     -dontwarn android.webkit.WebView
     -dontwarn com.umeng.**
     -dontwarn com.tencent.weibo.sdk.**
     -dontwarn com.facebook.**
     -keep public class javax.**
     -keep public class android.webkit.**
     -dontwarn android.support.v4.**
     -keep enum com.facebook.**
     -keepattributes Exceptions,InnerClasses,Signature
     -keepattributes *Annotation*
     -keepattributes SourceFile,LineNumberTable

     -keep public interface com.facebook.**
     -keep public interface com.tencent.**
     -keep public interface com.umeng.socialize.**
     -keep public interface com.umeng.socialize.sensor.**
     -keep public interface com.umeng.scrshot.**

     -keep public class com.umeng.socialize.* {*;}


     -keep class com.facebook.**
     -keep class com.facebook.** { *; }
     -keep class com.umeng.scrshot.**
     -keep public class com.tencent.** {*;}
     -keep class com.umeng.socialize.sensor.**
     -keep class com.umeng.socialize.handler.**
     -keep class com.umeng.socialize.handler.*
     -keep class com.umeng.weixin.handler.**
     -keep class com.umeng.weixin.handler.*
     -keep class com.umeng.qq.handler.**
     -keep class com.umeng.qq.handler.*
     -keep class UMMoreHandler{*;}
     -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
     -keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
     -keep class im.yixin.sdk.api.YXMessage {*;}
     -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
     -keep class com.tencent.mm.sdk.** {
        *;
     }
     -keep class com.tencent.mm.opensdk.** {
        *;
     }
     -keep class com.tencent.wxop.** {
        *;
     }
     -keep class com.tencent.mm.sdk.** {
        *;
     }

     -keep class com.twitter.** { *; }

     -keep class com.tencent.** {*;}
     -dontwarn com.tencent.**
     -keep class com.kakao.** {*;}
     -dontwarn com.kakao.**
     -keep public class com.umeng.com.umeng.soexample.R$*{
         public static final int *;
     }
     -keep public class com.linkedin.android.mobilesdk.R$*{
         public static final int *;
     }
     -keepclassmembers enum * {
         public static **[] values();
         public static ** valueOf(java.lang.String);
     }

     -keep class com.tencent.open.TDialog$*
     -keep class com.tencent.open.TDialog$* {*;}
     -keep class com.tencent.open.PKDialog
     -keep class com.tencent.open.PKDialog {*;}
     -keep class com.tencent.open.PKDialog$*
     -keep class com.tencent.open.PKDialog$* {*;}
     -keep class com.umeng.socialize.impl.ImageImpl {*;}
     -keep class com.sina.** {*;}
     -dontwarn com.sina.**
     -keep class  com.alipay.share.sdk.** {
        *;
     }

     -keepnames class * implements android.os.Parcelable {
         public static final ** CREATOR;
     }

     -keep class com.linkedin.** { *; }
     -keep class com.android.dingtalk.share.ddsharemodule.** { *; }
     -keepattributes Signature

     ############友盟统计混淆
     -keepclassmembers class * {
        public <init> (org.json.JSONObject);
     }

    #5.0以上sdk需要添加
     -keepclassmembers enum * {
         public static **[] values();
         public static ** valueOf(java.lang.String);
     }

     ###########极光推送##################
     -dontoptimize
     -dontpreverify
     -dontwarn cn.jpush.**
     -keep class cn.jpush.** { *; }
     -keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
     -dontwarn cn.jiguang.**
     -keep class cn.jiguang.** { *; }
     -dontwarn com.google.**
     -keep class com.google.** { *; }


     ##############极光推送####################
     ### bugly
     -dontwarn com.tencent.bugly.**
     -keep public class com.tencent.bugly.**{*;}

     ### multiDex
     -dontwarn com.android.support.**
     -keep class com.android.support.** { *; }
     ### securityenvsdk
      -dontwarn com.wireless.**
     -keep class com.wireless.** { *; }

     #greendao3.2.0,此是针对3.2.0，如果是之前的，可能需要更换下包名
     -keep class org.greenrobot.greendao.**{*;}
     -dontwarn org.greenrobot.greendao.**
     -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
     public static java.lang.String TABLENAME;
     }
     -keep class **$Properties

       #取消java.lang.invoke(lambda表达式) 的警告, 会影响build
        -dontwarn java.lang.invoke.**
        #Umeng push
        -dontwarn org.**
        -keep class org.**{ *;
        }
        -dontwarn com.umeng.**
        -keep class com.umeng.**{ *;
        }
        -dontwarn anet.**
        -keep class anet.**{ *;
        }
        -dontwarn anetwork.**
        -keep class anetwork.**{ *;
        }

        -dontwarn com.alibaba.**
        -keep class com.alibaba.**{ *;
        }
        -dontwarn com.taobao.**
        -keep class com.taobao.**{ *;
        }

        -dontwarn com.ta.**
        -keep class com.ta.**{ *;
        }

        -dontwarn com.ut.**
        -keep class com.ut.**{ *;
        }
        -dontwarn net.sourceforge.zba.**
        -keep class net.sourceforge.zbar.** { *; }

  -dontwarn com.umeng.**
        -keep class com.umeng.** { *; }


        # OkHttp
        -dontwarn okhttp3.**
        -dontwarn okio.**
        -dontwarn com.squareup.okhttp.**
        -keep class okio.**{*;}
        -keep class com.squareup.okhttp.** { *; }
        -keep interface com.squareup.okhttp.** { *; }



        -dontwarn java.nio.file.*
        -dontwarn javax.annotation.**
        -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement


        -dontwarn com.booyue.poetry.bean.**
        -keep class com.booyue.poetry.bean.**{ *;
        }
        ### 一键登录混淆
        -dontwarn com.mobile.auth.**
        -keep class com.mobile.auth.**{ *;}
         -dontwarn cn.com.chinatelecom.gateway.lib.**
         -keep class cn.com.chinatelecom.gateway.lib.**{ *; }
         -dontwarn com.cmic.sso.sdk.**
          -keep class com.cmic.sso.sdk.**{ *; }
           -dontwarn com.unicom.xiaowo.login.**
            -keep class com.unicom.xiaowo.login**{ *; }
        ### 视频播放器混淆
         -dontwarn tv.danmaku.ijk.media.**
        -keep class tv.danmaku.ijk.media.**{ *;
        }












