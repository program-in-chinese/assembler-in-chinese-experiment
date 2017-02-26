package cn.org.assembler;

import java.io.IOException;

import org.boris.pecoff4j.PE;
import org.boris.pecoff4j.io.PEAssembler;
import org.boris.pecoff4j.io.PEParser;

public class 生成器类 {

  public static void main(String[] args) throws IOException {

    // 仅仅用于演示之用，新老文件完全相同
    try {
      PE 格式信息 = PEParser.parse("测试/你好.exe");
      PEAssembler.write(格式信息, "测试/你也好.exe");
    } catch (Exception 异常) {
      System.out.println("请先按照‘你好.asm'注释生成源exe文件");
    }
  }

}
