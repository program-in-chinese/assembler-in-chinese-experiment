package cn.org.assembler.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cn.org.assembler.模型.代码行类.操作数类型;
import cn.org.assembler.模型.操作数信息;

public class 操作数元数据测试类 {

  @Test
  public void 取操作数类型() {
    操作数元数据类 类型 = 操作数元数据类.取操作数类型("strict dword 35");
    assertTrue(操作数元数据类.立即数32.equals(类型));
    assertTrue(操作数元数据类.单字立即数.equals(操作数元数据类.取操作数类型("0x80")));
    assertTrue(操作数元数据类.寄存器64.equals(操作数元数据类.取操作数类型("rax")));
    assertTrue(操作数元数据类.单字寄存器.equals(操作数元数据类.取操作数类型("ax")));
    assertTrue(操作数元数据类.单字节寄存器.equals(操作数元数据类.取操作数类型("al")));
    assertTrue(操作数元数据类.单字节寄存器.equals(操作数元数据类.取操作数类型("byte al")));
    assertTrue(操作数元数据类.单字节内存.equals(操作数元数据类.取操作数类型("byte [0]")));
    assertTrue(操作数元数据类.立即数8_有符号.equals(操作数元数据类.取操作数类型("1h")));
  }

  @Test
  public void 匹配操作数信息() {
    assertTrue(操作数元数据类.立即数32.匹配(new 操作数信息(操作数类型.立即数, 32, "35")));
  }
}
