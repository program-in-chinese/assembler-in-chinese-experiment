 **小目标** 

右图是想象效果图,参考的是[HEX2ASCII.asm](https://git.oschina.net/zhishi/asm_for_all/blob/master/example/x86/win32/HEX2ASCII.asm) 代码段(左图)![](http://git.oschina.net/uploads/images/2017/0227/080602_34511d48_384016.png "效果示意图")

 **04/29/2017 进度小结** 
- 暂时设计:
![暂时设计草图](https://git.oschina.net/uploads/images/2017/0430/070018_cfe80105_384016.png "在这里输入图片标题")

- 以这个mov指令举例:
![过程示例](https://git.oschina.net/uploads/images/2017/0430/072528_e0f290cb_384016.png "在这里输入图片标题")

- 当前进度
1. 这里是列表文本仅支持两个操作数的部分指令
2. 操作数可以是寄存器,立即数,或者简单的内存寻址,如[0]
3. 支持强制类型,如add ax,strict word 5


 **使用外部资源**
- x86操作码元数据: https://github.com/Barebit/x86reference
- PE文件处理(分析/生成)库: https://github.com/kichik/pecoff4j
- NASM源码: https://github.com/cyrillos/nasm
- FASM: https://flatassembler.net/
- YASM源码: https://github.com/yasm/yasm