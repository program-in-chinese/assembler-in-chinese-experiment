package cn.org.assembler.测试资源;

import static org.junit.Assert.*;

import org.junit.Test;

public class 测试资源处理测试类 {

  @Test
  public void 取所有测试源码文件名() {
    assertEquals(121, 测试资源处理类.取所有测试源码文件名().size());
  }

}
