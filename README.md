# bilibili视频自动解析  
效果如下：

复制:  
```
https://www.bilibili.com/video/BV1sUPReYEa1/?
```

粘贴:
```
<<<<这是视频封面图片>>>>
【流浪地球2】氦闪，但人类是二级文明
发布时间: 2025-02-03 16:44:59
up: 乐意Ajax
评论数: 2205
收藏数: 43665
硬币数: 22871
点赞数: 67494
https://www.bilibili.com/video/BV1sUPReYEa1 
```

## 使用  
1. 复制浏览器标题的链接，或者使用分享按钮获取链接。  
2. **进行解析的判断条件**:
    - BV号后带着 **/?**(BV1tJ4m1P7NR/?)
    - BV号为大写
    - (其实没这么麻烦，按照第一点说的做就行了)

## 关于
自己瞎做的，使用Java21  
只能解析到地址栏或网页端分享按钮的BVID  
exe打包部分的maven插件配置来自 [SoNovel](https://github.com/freeok/so-novel)

## BUG
复制intellij的文字会报错，不影响运行  
如果在powershell使用这个命令报错，请使用cmd运行  

## 使用
下载Releases中的jar或者exe文件  
**jar:**  在jar文件所在目录下打开控制台,确保安装了java,输入`java -jar (你下载的jar名称)`  
**exe:**  确保你是windows环境，直接运行

## 闲聊
项目有问题可以提一个issue，在上面大声说出你的问题  
