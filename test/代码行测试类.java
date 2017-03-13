import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import cn.org.assembler.utils.文件功能类;
import cn.org.assembler.模型.代码行类;

public class 代码行测试类 {

  private static final String 测试文件路径 = "测试/指令/";
  @Test
  public void test() {
    List<String> 行 = 文件功能类.读取行(测试文件路径 + "imm64.asm");
    assertTrue("测试数据不应为空", 行.size() > 0);
    
    for(String 单行 : 行) {
      代码行类 代码行 = 代码行类.分析(单行);
      System.out.println(代码行);
    }
  }

}
