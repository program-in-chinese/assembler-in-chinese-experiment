package cn.org.assembler.pe;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.boris.pecoff4j.COFFHeader;
import org.boris.pecoff4j.DOSHeader;
import org.boris.pecoff4j.DOSStub;
import org.boris.pecoff4j.ImageDataDirectory;
import org.boris.pecoff4j.OptionalHeader;
import org.boris.pecoff4j.PE;
import org.boris.pecoff4j.PESignature;
import org.boris.pecoff4j.SectionTable;
import org.boris.pecoff4j.io.PEAssembler;
import org.boris.pecoff4j.io.PEParser;
import org.junit.Test;

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
    PE pe = new PE();
    DOSHeader dh = new DOSHeader();
    int[] reserved = new int[0];
    dh.setReserved(reserved);
    dh.setReserved2(reserved);
    pe.setDosHeader(dh);

    DOSStub stub = new DOSStub();
    stub.setStub(new byte[0]);
    pe.setStub(stub);

    PESignature signature = new PESignature();
    signature.setSignature(new byte[0]);
    pe.setSignature(signature);

    COFFHeader ch = new COFFHeader();
    pe.setCoffHeader(ch);
    
    OptionalHeader oh = new OptionalHeader();
    oh.setDataDirectories(new ImageDataDirectory[0]);
    pe.setOptionalHeader(oh);
    
    pe.setSectionTable(new SectionTable());
    
    PEAssembler.write(pe, 空文件名);
    
    File 空文件 = new File(空文件名);
    assertTrue(空文件.exists() && 空文件.isFile() && 空文件.length() > 0);
    
    // 清理文件
    空文件.delete();
  }

}
