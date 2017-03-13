import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import cn.org.assembler.分析器类;
import cn.org.assembler.utils.操作码元数据类;
import cn.org.assembler.模型.代码行类;


public class 分析器测试类 {

  @Test
  public void 查找操作码() {
    验证操作码("b8", 代码行类.分析("mov rax, 0x1122334455667788"));
    验证操作码("c7", 代码行类.分析("mov rax, 0x1000"));
  }

  private void 验证操作码(String 操作码值, 代码行类 代码行) {
    List<操作码元数据类> 操作码元数据 = 分析器类.查找操作码(代码行);
    assertEquals(1, 操作码元数据.size());
    assertEquals(操作码值, Integer.toHexString(操作码元数据.get(0).值));
  }
}
