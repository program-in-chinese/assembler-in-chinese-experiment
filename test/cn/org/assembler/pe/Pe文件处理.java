package cn.org.assembler.pe;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.boris.pecoff4j.PE;
import org.boris.pecoff4j.io.PEAssembler;
import org.boris.pecoff4j.io.PEParser;
import org.junit.Test;

import cn.org.assembler.生成器类;

public class Pe文件处理 {

  @Test
  public void 解析后生成() throws IOException {
    String 源文件名 = "测试/PE/mov.exe";
    String 目标文件名 = "测试/PE/mov_gen.exe";
    PE pe = PEParser.parse(源文件名);
    PEAssembler.write(pe, 目标文件名);

    File 目标文件 = new File(目标文件名);
    
    // TODO: 两个文件应该完全相同
    assertTrue(目标文件.exists() && (new File(源文件名)).length() == 目标文件.length());
    
    目标文件.delete();
  }

  @Test
  public void 解析后生成你好() {
    String 源文件名 = "测试/PE/你好.exe";
    String 目标文件名 = "测试/PE/你也好.exe";
    try {
      PE 格式信息 = PEParser.parse(源文件名);
      PEAssembler.write(格式信息, 目标文件名);

      File 目标文件 = new File(目标文件名);
      目标文件.delete();
    } catch (Exception 异常) {
      System.out.println("请先按照‘你好.asm'注释生成源exe文件");
    }
  }
  
  @Test
  public void 生成空PE文件() throws IOException {

    String 空文件名 = "测试/PE/empty.exe";
    PE pe = 生成器类.新建PE结构();
    
    生成器类.新建PE文件(pe, 空文件名);
    PE 小pe = PEParser.parse(空文件名);
    
    // TODO: 添加更多检验.
    assertEquals(1, 小pe.getCoffHeader().getNumberOfSections());
    assertEquals(0x8664, 小pe.getCoffHeader().getMachine());
    assertEquals(0x20b, 小pe.getOptionalHeader().getMagic());
    
    assertEquals(1, 小pe.getSectionTable().getNumberOfSections());
    assertTrue(小pe.getSectionTable().getSection(0).getData().length > 0);
    
    File 空文件 = new File(空文件名);
    assertTrue(空文件.exists() && 空文件.isFile() && 空文件.length() > 0);
    
    // TODO: 清理文件. 空文件.delete()无用
  }

  @Test
  public void 比较PE文件() throws IOException {
    String 文件名1 = "测试/PE/mov.exe";
    String 文件名2 = "测试/PE/你好.exe";
    PE 小pe = PEParser.parse(文件名1);
    PE 大pe = PEParser.parse(文件名2);
    
    // TODO: 比较每个变量
    assertArrayEquals(小pe.getDosHeader().getReserved(), 大pe.getDosHeader().getReserved());
  }
  
  @Test
  public void 取optionalHeader长度() throws IOException {
    String 文件名 = "测试/PE/mov.exe";
    PE pe = PEParser.parse(文件名);
    assertEquals(240, 生成器类.取optionalHeader长度(pe.getOptionalHeader()));
  }
}
