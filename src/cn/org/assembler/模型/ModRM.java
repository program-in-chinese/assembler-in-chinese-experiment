package cn.org.assembler.模型;

/**
 *   7                           0
 * +---+---+---+---+---+---+---+---+
 * |  mod  |    reg    |     rm    |
 * +---+---+---+---+---+---+---+---+
 */
public class ModRM {

  public int mod;
  public int reg;
  public int rm;

  public String 生成二进制码() {
      return Integer.toHexString(mod << 6 | reg << 3 | rm);
  }

}
