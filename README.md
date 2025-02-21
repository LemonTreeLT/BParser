# bilibili视频自动解析  
效果如下：

复制:  
```
https://www.bilibili.com/video/BV1EE411W7oa?vd_source=1ea648cf2e482b3e0f8610e072ad4062&p=11&spm_id_from=333.788.videopod.episodes
```

粘贴:
```
<<<<这是视频封面图片>>>>
【完结】0元搭建NAS从入门到入坑
分集: 媒体管理:DDNS设置、Video Station搭建
发布时间: 2020-03-06 12:32:43
up: 天马Pegasus
评论数: 2289
收藏数: 63387
硬币数: 52002
点赞数: 50467
https://www.bilibili.com/video/BV1EE411W7oa?p=11 
```

## 使用  
1. 复制浏览器标题的链接，或者使用分享按钮获取链接。  
2. **进行解析的判断条件**:
    - 正则表达式 `/video/(BV[0-9A-Za-z]{10})(?:[/?]|$)`


## 关于
自己瞎做的，使用Java21  
只能解析到地址栏或网页端分享按钮的BVID  
exe打包部分的maven插件配置来自 [SoNovel](https://github.com/freeok/so-novel)

## BUG
复制jetbrains的文字会报错，不影响运行  
如果在powershell使用这个命令报错，请使用cmd运行  

## 使用
下载Releases中的jar或者exe文件  
**jar:**  在jar文件所在目录下打开控制台,确保安装了java,输入`java -jar (你下载的jar名称)`  
**exe:**  确保你是windows环境，直接运行

## 闲聊
项目有问题可以提一个issue，在上面大声说出你的问题  
