小目标

右图是想象效果图,参考的是[HEX2ASCII.asm](https://git.oschina.net/zhishi/asm_for_all/blob/master/example/x86/win32/HEX2ASCII.asm) 代码段(左图)![](http://git.oschina.net/uploads/images/2017/0227/080602_34511d48_384016.png "效果示意图")

当前暂时设计(04/29/2017):
![暂时设计草图](https://git.oschina.net/uploads/images/2017/0430/070018_cfe80105_384016.png "在这里输入图片标题")

以这个mov指令举例:
![过程示例](https://git.oschina.net/uploads/images/2017/0430/072528_e0f290cb_384016.png "在这里输入图片标题")

使用外部资源:
- x86操作码元数据: https://github.com/Barebit/x86reference
- PE文件处理(分析/生成)库: https://github.com/kichik/pecoff4j
- NASM源码: https://github.com/cyrillos/nasm
- FASM: https://flatassembler.net/
- YASM源码: https://github.com/yasm/yasm