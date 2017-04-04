package cn.org.assembler.模型;

/**
 *   7                           0
 * +---+---+---+---+---+---+---+---+
 * | Scale |   Index   |    Base   |
 * +---+---+---+---+---+---+---+---+
 * 
 * 2.1.3 Vol 2
 */
public class SIB {
  public int scale;
  public int index;
  public int base;

  public String 生成二进制码() {
      String 十六进制码 = Integer.toHexString(scale << 6 | index << 3 | base);
      return 十六进制码.length() == 1 ? "0" + 十六进制码 : 十六进制码;
  }
}
