package com.github.programinchinese;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * 所有方法直接调用assert*方法, 尽量避免增加调试时的信息
 */
public class 断言 {
  public static void 为真(String 反馈信息, boolean 条件) {
    assertTrue(反馈信息, 条件);
  }

  public static void 为真(boolean 条件) {
    assertTrue(null, 条件);
  }
  
  public static void 为假(String 反馈信息, boolean 条件) {
    assertFalse(反馈信息, 条件);
  }

  public static void 为假(boolean 条件) {
    assertFalse(null, 条件);
  }
  
  public static void 失败(String 反馈信息) {
    fail(反馈信息);
  }

  public static void 失败() {
    fail(null);
  }
  
  public static void 相等(String 反馈信息, Object 期望, Object 实际) {
    assertEquals(反馈信息, 期望, 实际);
  }

  public static void 相等(Object 期望, Object 实际) {
    assertEquals(null, 期望, 实际);
  }

  public static void 不等(String 反馈信息, Object 意外, Object 实际) {
    assertNotEquals(反馈信息, 意外, 实际);
  }

  public static void 不等(Object 意外, Object 实际) {
    assertNotEquals(意外, 实际);
  }
}
