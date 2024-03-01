# bilibili视频自动解析
> 学业压力，暂时停更  

效果如下：

复制:  
```
https://www.bilibili.com/video/BV1WH4y1m7qM/?spm_id_from=444.41.list.card_archive.
```

粘贴:
```
{image.getdata()} #芝士图片  
【Neuro/Saruei】联动回 英法友好交流  
发布时间: 2023-09-22 15:29:11  
up: IronMilk_铁牛奶  
评论数: 113  
收藏数: 591  
硬币数: 334  
点赞数: 2557  
https://www.bilibili.com/video/BV1WH4y1m7qM
```
> 关于复制图片的部分我没有实现，~~*在做了*~~


## 关于
自己瞎做的  
之前使用python写的,脑抽了用java写了一遍

## BUG
复制intellij的文字会报错，不影响运行  
构建后的jar中文会乱码 解决方法：使用 `java -Dfile.encoding=UTF-8 -jar BParser.jar`运行  
如果在powershell使用这个命令报错，请使用cmd运行

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
作者大脑发育不完全，小脑完全不发育，没必要跟我急  
2024-2-12目前的版本可以正常使用，被图片复制整崩溃了

