package cn.org.assembler.模型;
import static com.github.program_in_chinese.junit4_in_chinese.断言.相等;

import java.util.Arrays;

import org.junit.Test;

import cn.org.assembler.模型.指令类;

public class 指令测试类 {

  @Test
  public void 生成二进制码() {
    // 0x1122334455667788
    相等(Arrays.asList("88", "77", "66", "55", "44", "33", "22", "11"),
        指令类.生成二进制码("1234605616436508552", 64));
    // 0x112
    相等(Arrays.asList("12", "01", "00", "00"), 指令类.生成二进制码("274", 32));
    // 0x1000
    相等(Arrays.asList("00", "10", "00", "00"), 指令类.生成二进制码("4096", 32));
    相等(Arrays.asList("04", "00", "00", "00"), 指令类.生成二进制码("4", 32));
    相等(Arrays.asList("90", "01", "00", "00"), 指令类.生成二进制码("400", 32));
    相等(Arrays.asList("80"), 指令类.生成二进制码("-128", 8));
    相等(Arrays.asList("80", "ff"), 指令类.生成二进制码("-128", 16));
    相等(Arrays.asList("00"), 指令类.生成二进制码("0", 8));
    相等(Arrays.asList("01", "00"), 指令类.生成二进制码("1", 16));
  }

}
