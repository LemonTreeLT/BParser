# bilibili视频自动解析  
效果如下：

复制:  
```
https://www.bilibili.com/video/BV1tJ4m1L7Z3/?
```

粘贴:
```
[图像]
用了个开源软件，电脑就被黑了？这手法有点妙！
发布时间: 2024-03-29 18:30:00
up: 码农高天
评论数: 236
收藏数: 579
硬币数: 448
点赞数: 2010
https://www.bilibili.com/video/BV1tJ4m1L7Z3
```

## 使用  
1. 复制浏览器标题的链接，或者使用分享按钮获取链接。  
2. **进行解析的判断条件**:
    - BV号后带着 **/?**(BV1tJ4m1P7NR/?)
    - BV号为大写
    - (其实没这么麻烦，按照第一点说的做就行了)

## 关于
自己瞎做的  
之前使用python写的,脑抽了用java写了一遍  
只能解析到地址栏或网页端分享按钮的BVID

## BUG
复制intellij的文字会报错，不影响运行  
构建后的jar中文会乱码 解决方法：使用 `java -Dfile.encoding=UTF-8 -jar com.lemontree.BParser.jar`运行  
如果在powershell使用这个命令报错，请使用cmd运行  

## 使用
下载Releases中的jar或者exe文件  
**jar:**  在jar文件所在目录下打开控制台,确保安装了java,输`java -jar (你下载的jar名称)`  
**exe:**  确保你是windows环境，直接运行

## 闲聊
项目有问题可以提一个issue，在上面大声说出你的问题

