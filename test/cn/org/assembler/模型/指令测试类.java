package cn.org.assembler.模型;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import cn.org.assembler.模型.指令类;

public class 指令测试类 {

  @Test
  public void 生成二进制码() {
    assertEquals(Arrays.asList("88", "77", "66", "55", "44", "33", "22", "11"),
        指令类.生成二进制码("0x1122334455667788", 64));
    assertEquals(Arrays.asList("12", "01", "00", "00"), 指令类.生成二进制码("0x112", 32));
    assertEquals(Arrays.asList("00", "10", "00", "00"), 指令类.生成二进制码("0x1000", 32));
    assertEquals(Arrays.asList("04", "00", "00", "00"), 指令类.生成二进制码("4", 32));
    assertEquals(Arrays.asList("90", "01", "00", "00"), 指令类.生成二进制码("400", 32));
    assertEquals(Arrays.asList("80"), 指令类.生成二进制码("-128", 8));
  }

}
