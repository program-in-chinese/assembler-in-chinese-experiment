package cn.org.assembler.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cn.org.assembler.模型.代码行类.操作数类型;
import cn.org.assembler.模型.操作数信息类;

public class 操作数元数据测试类 {

  @Test
  public void 匹配操作数信息() {
    assertTrue(new 操作数元数据类(false, "vqs", "rAX", null).操作数类型匹配(new 操作数信息类(操作数类型.寄存器, 64, "RAX")));
    assertTrue(new 操作数元数据类(false, "vqp", "rAX", null).操作数类型匹配(new 操作数信息类(操作数类型.寄存器, 16, "AX")));
  }

  @Test
  public void 匹配() {
    assertTrue(操作数元数据类.立即数32.操作数类型匹配(new 操作数信息类(操作数类型.立即数, 32, "35")));
  }
}
