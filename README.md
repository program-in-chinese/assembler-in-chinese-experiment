 **小目标** 

开源许可证: GPL 3.0
源码文件编码: UTF-8

右图是想象效果图,参考的是[HEX2ASCII.asm](https://git.oschina.net/zhishi/asm_for_all/blob/master/example/x86/win32/HEX2ASCII.asm) 代码段(左图)![](http://git.oschina.net/uploads/images/2017/0227/080602_34511d48_384016.png "效果示意图")

 **04/29/2017 进度小结** 
- 暂时设计:
![暂时设计草图](https://github.com/program-in-chinese/assembler-in-chinese-experiment/blob/master/文档/汇编器设计草图.png "在这里输入图片标题")

- 以这个mov指令举例:
![过程示例](https://github.com/program-in-chinese/assembler-in-chinese-experiment/blob/master/%E6%96%87%E6%A1%A3/%E6%B1%87%E7%BC%96%E5%99%A8%E8%AE%BE%E8%AE%A1%E8%8D%89%E5%9B%BE-%E7%A4%BA%E4%BE%8B.png "在这里输入图片标题")

 **04/29/2017 当前进度** 
- 仅支持两个操作数的部分指令, 第二个操作数仅支持立即数
- 第一个操作数可以是寄存器,或者简单的内存寻址,如[0]
- 支持强制类型,如add ax,strict word 5
- [缺失] 生成可执行文件(PE)

### 接口测试
- [指令分析测试](https://github.com/program-in-chinese/assembler-in-chinese-experiment/blob/master/test/cn/org/assembler/%E5%88%86%E6%9E%90%E5%99%A8%E6%B5%8B%E8%AF%95%E7%B1%BB.java)
- [生成二进制码测试](https://github.com/program-in-chinese/assembler-in-chinese-experiment/blob/master/test/cn/org/assembler/%E6%B1%87%E7%BC%96%E5%99%A8%E6%B5%8B%E8%AF%95%E7%B1%BB.java)
- [生成PE文件测试](https://github.com/program-in-chinese/assembler-in-chinese-experiment/blob/master/test/cn/org/assembler/pe/Pe%E6%96%87%E4%BB%B6%E5%A4%84%E7%90%86.java#L50)

 **使用外部资源**
- x86操作码元数据: https://github.com/Barebit/x86reference
- PE文件处理(分析/生成)库: https://github.com/kichik/pecoff4j
- NASM源码: https://github.com/cyrillos/nasm
- FASM: https://flatassembler.net/
- YASM源码: https://github.com/yasm/yasm
