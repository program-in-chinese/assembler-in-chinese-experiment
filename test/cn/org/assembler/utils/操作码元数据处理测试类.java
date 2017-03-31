package cn.org.assembler.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Objects;

import org.junit.Test;

import cn.org.assembler.模型.指令类;

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
    List<操作码元数据类> 操作码元数据 = 操作码元数据处理类.操作码元数据表;
    assertEquals("指令信息不应为空", 451, 操作码元数据.size());

    int 正确操作符计数 = 0;
    for (操作码元数据类 元数据 : 操作码元数据) {
      assertTrue("操作码值过小:" + 元数据.值, 元数据.值 >= 0);
      assertTrue("操作码值过大:" + 元数据.值, 元数据.值 <= 255);

      List<指令元数据类> 指令元数据 = 元数据.指令元数据;

      // TODO: 确认是否有用
      if (指令元数据.isEmpty()) {
        // 调试输出("无指令的操作码: ", 元数据);
      } else {
        for (指令元数据类 某指令元数据 : 指令元数据) {
          for (指令格式类 某格式 : 某指令元数据.格式) {
            assertNotNull(某格式.助记符);

            // TODO: 将所有待验证指令置于对应表中
            if (某指令元数据.扩展码 == -1 && 指令类.生成操作码(元数据.值).equals("b8") && 某格式.助记符.equals("MOV")) {
              List<操作数元数据类> 操作数 = 某格式.操作数;
              assertEquals(2, 操作数.size());

              assertEquals(new 操作数元数据类(false, 操作数元数据类.类型16_32_64, 操作数元数据类.寻址方式_寄存器, null),
                  操作数.get(0));
              assertEquals(new 操作数元数据类(true, 操作数元数据类.类型16_32_64, 操作数元数据类.寻址方式_立即数, null),
                  操作数.get(1));

              正确操作符计数++;
            }
            if (某指令元数据.扩展码 == -1 && 指令类.生成操作码(元数据.值).equals("05") && 某格式.助记符.equals("ADD")) {

              List<操作数元数据类> 操作数 = 某格式.操作数;
              assertEquals(2, 操作数.size());

              assertEquals(new 操作数元数据类(false, 操作数元数据类.类型16_32_64, "rAX", null), 操作数.get(0));
              assertEquals(new 操作数元数据类(true, 操作数元数据类.类型16_32_可扩展到64, 操作数元数据类.寻址方式_立即数, null), 操作数.get(1));

              正确操作符计数++;
            }
            if (某指令元数据.扩展码 == -1 && 指令类.生成操作码(元数据.值).equals("25") && 某格式.助记符.equals("AND")) {

              List<操作数元数据类> 操作数 = 某格式.操作数;
              assertEquals(2, 操作数.size());

              assertEquals(new 操作数元数据类(false, 操作数元数据类.类型16_32_64, "rAX", null), 操作数.get(0));
              assertEquals(new 操作数元数据类(true, 操作数元数据类.类型16_32_可扩展到64, 操作数元数据类.寻址方式_立即数, null), 操作数.get(1));

              正确操作符计数++;
            }
            if (某指令元数据.扩展码 == 4 && 指令类.生成操作码(元数据.值).equals("81") && 某格式.助记符.equals("AND")) {

              List<操作数元数据类> 操作数 = 某格式.操作数;
              assertEquals(2, 操作数.size());

              assertEquals(new 操作数元数据类(false, 操作数元数据类.类型16_32_64, 操作数元数据类.寻址方式_寄存器_ModRM, null),
                  操作数.get(0));
              assertEquals(new 操作数元数据类(true, 操作数元数据类.类型16_32_可扩展到64, 操作数元数据类.寻址方式_立即数, null), 操作数.get(1));

              正确操作符计数++;
            }
            if (某指令元数据.扩展码 == 0 && 指令类.生成操作码(元数据.值).equals("81") && 某格式.助记符.equals("ADD")) {

              List<操作数元数据类> 操作数 = 某格式.操作数;
              assertEquals(2, 操作数.size());

              assertEquals(new 操作数元数据类(false, 操作数元数据类.类型16_32_64, 操作数元数据类.寻址方式_寄存器_ModRM, null),
                  操作数.get(0));
              assertEquals(new 操作数元数据类(true, 操作数元数据类.类型16_32_可扩展到64, 操作数元数据类.寻址方式_立即数, null), 操作数.get(1));

              正确操作符计数++;
            }
            if (某指令元数据.扩展码 == -1
                && 指令类.生成操作码(元数据.值).equals("02")
                && 某格式.助记符.equals("LAR")
                && 某格式.操作数.size() == 2
                && Objects.equals(new 操作数元数据类(false, 操作数元数据类.类型16_32_64,
                    操作数元数据类.寻址方式_寄存器_ModRM_reg_通用寄存器, null), 某格式.操作数.get(0))
                && Objects.equals(new 操作数元数据类(true, 操作数元数据类.类型16_32,
                    操作数元数据类.寻址方式_寄存器_ModRM_mod_通用寄存器, null), 某格式.操作数.get(1))) {
              正确操作符计数++;
            }
            // TODO: 确认是否有用
            if (某格式.操作数.size() == 0) {
              // 调试输出("无目标操作数的操作码: ", 元数据);
            }
          }
        }

      }
    }
    assertEquals(6, 正确操作符计数);
  }

  private void 调试输出(String 前缀, 操作码元数据类 元数据) {
    System.out.println(前缀 + Integer.toHexString(元数据.值) + " " + 元数据.操作码字节数 + "字节");
  }
}
