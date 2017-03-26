package cn.org.assembler.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class 操作数元数据测试类 {

  @Test
  public void 取操作数类型() {
    操作数元数据类 类型 = 操作数元数据类.取操作数类型("strict dword 35");
    assertTrue(操作数元数据类.立即数32.equals(类型));
  }

}
