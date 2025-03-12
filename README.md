# android_libs_proj

all about android library(admob, game service, iap, logcat)

## 发布工具库到本地maven仓库

```
 //由于直接引用本地aar文件没有依赖传递，所以使用本地仓库加载方式
 sh gradlew publish
```

## 加载本地maven仓库中的工具库，需要在主项目仓库中配置如下配置

```
mavenLocal() {
    //地址跟本地发布地址一致
    url '/Users/niuhesong/Maven/Repo'
}
```