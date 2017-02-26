import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import cn.org.assembler.分析器类;
import cn.org.assembler.指令类;
import cn.org.assembler.utils.文件功能类;

public class 分析器测试类 {

  private static final String 测试文件路径 = "测试/指令/";
  @Test
  public void test() {
    List<String> 行 = 文件功能类.读取行(测试文件路径 + "imm64.asm");
    assertTrue("测试数据为空", 行.size() > 0);
    
    for(String 单行 : 行) {
      指令类 指令 = 分析器类.分析代码行(单行);
      System.out.println(指令);
    }
  }

}
