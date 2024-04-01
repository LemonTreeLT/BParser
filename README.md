# bilibili视频自动解析
> 喜报：本人成功了解到有一个东西叫做富文本并且成功实现图片复制  
> 中考去了，暂停维护  

效果如下：

复制:  
```
https://www.bilibili.com/video/BV1tJ4m1L7Z3
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

## 关于
自己瞎做的  
之前使用python写的,脑抽了用java写了一遍

## BUG
复制intellij的文字会报错，不影响运行  
构建后的jar中文会乱码 解决方法：使用 `java -Dfile.encoding=UTF-8 -jar BParser.jar`运行  
如果在powershell使用这个命令报错，请使用cmd运行  
在某些只支持纯文本的编辑器下(如notepad)粘贴会出现一点HTML，以后修复 

## 使用
下载Releases中的jar或者exe文件  
**jar:**  在jar文件所在目录下打开控制台,确保安装了java,输`java -jar (你下载的jar名称)`  
**exe:**  确保你是windows环境，直接运行

## ToDoList  
~~- [ ] Gui版本(可能?~~    
~~- [ ] 添加其他平台的解析(几乎不可能~~  
~~- [ ] 图片复制(10月初~~  
~~- [ ] 多线程(看心情~~  
~~- [ ] 添加config.json(会做但不定时~~  
~~- anymore idk...~~  

## 闲聊
项目有问题可以提一个issue，在上面大声说出你的问题

