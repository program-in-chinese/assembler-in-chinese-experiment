import static org.junit.Assert.*;

import org.junit.Test;

import cn.org.assembler.utils.操作码元数据处理类;

public class 操作码元数据处理测试类 {

  @Test
  public void test() {
    assertEquals("指令信息不应为空", 451, 操作码元数据处理类.提取操作码信息().size());
  }

}
