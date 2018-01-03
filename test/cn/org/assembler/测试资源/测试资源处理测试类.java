package cn.org.assembler.测试资源;

import static com.github.program_in_chinese.junit4_in_chinese.断言.相等;

import org.junit.Test;

public class 测试资源处理测试类 {

  @Test
  public void 取所有测试源码文件名() {
    相等(121, 测试资源处理类.取所有测试源码文件名().size());
  }

}
