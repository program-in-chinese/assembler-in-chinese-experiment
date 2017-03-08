import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import cn.org.assembler.utils.指令元数据类;
import cn.org.assembler.utils.指令格式类;
import cn.org.assembler.utils.操作码元数据处理类;
import cn.org.assembler.utils.操作码元数据类;

public class 操作码元数据处理测试类 {

  @Test
  public void 单操作码() {
    List<操作码元数据类> 操作码元数据 = 操作码元数据处理类.提取操作码信息("资源/测试单操作码元数据.xml");
    assertEquals("指令信息不应为空", 2, 操作码元数据.size());

    for (操作码元数据类 某操作码元数据 : 操作码元数据) {
      assertTrue(某操作码元数据.值 >= 0);
      assertTrue(某操作码元数据.值 <= 255);

      assertNotNull(某操作码元数据.指令元数据.get(0).格式.get(0).助记符);
    }
  }

  @Test
  public void 全部操作码() {
    List<操作码元数据类> 操作码元数据 = 操作码元数据处理类.提取操作码信息();
    assertEquals("指令信息不应为空", 451, 操作码元数据.size());

    boolean 存在操作符 = false;
    for (操作码元数据类 元数据 : 操作码元数据) {
      assertTrue("操作码值过小:" + 元数据.值, 元数据.值 >= 0);
      assertTrue("操作码值过大:" + 元数据.值, 元数据.值 <= 255);

      List<指令元数据类> 指令元数据 = 元数据.指令元数据;
      if (指令元数据.isEmpty()) {
        System.out.println("无指令的操作码: " + Integer.toHexString(元数据.值) + " " + 元数据.操作码字节数 + "字节");
      } else {
        for (指令元数据类 某指令元数据 : 指令元数据) {
          for (指令格式类 某格式 : 某指令元数据.格式) {
            assertNotNull(某格式.助记符);

            // TODO: 将所有待验证指令置于对应表中
            if (Integer.toHexString(元数据.值).equalsIgnoreCase("B8") && 某格式.助记符.equals("MOV")) {
              存在操作符 = true;
            }
          }
        }

      }
    }
    assertTrue(存在操作符);
  }

}
