
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import cn.org.assembler.utils.操作码元数据处理类;
import cn.org.assembler.utils.操作码元数据类;

public class 操作码元数据处理测试类 {

  @Test
  public void 单操作码() {
    List<操作码元数据类> 操作码元数据 = 操作码元数据处理类.提取操作码信息("资源/测试单操作码元数据.xml");
    assertEquals("指令信息不应为空", 2, 操作码元数据.size());
    
    for(操作码元数据类 某操作码元数据 : 操作码元数据) {
      assertTrue(某操作码元数据.值 >= 0);
      assertTrue(某操作码元数据.值 < 255);

      assertNotNull(某操作码元数据.指令元数据.get(0).格式.get(0).助记符);
    }
  }
/*
  @Test
  public void test() {
    List<操作码元数据类> 操作码元数据 = 操作码元数据处理类.提取操作码信息();
    assertEquals("指令信息不应为空", 451, 操作码元数据.size());
    
    for(操作码元数据类 某操作码元数据 : 操作码元数据) {
      assertTrue(某操作码元数据.值 > 0);
      assertTrue(某操作码元数据.值 < 255);

      assertNotNull(某操作码元数据.指令元数据.get(0).格式.get(0).助记符);
    }
  }
*/
}
