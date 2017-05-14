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
    PE pe = PEParser.parse("测试/PE/mov.exe");
    PEAssembler.write(pe, "测试/PE/mov_gen.exe");

    // TODO: 两个文件应该完全相同
  }

  @Test
  public void 生成空PE文件() throws IOException {

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
    
    PEAssembler.write(pe, "测试/PE/empty.exe");
    
    File file = new File("测试/PE/empty.exe");
    assertTrue(file.exists() && file.isFile() && file.length() > 0);
    
    // TODO: 清理文件
    file.delete();
  }

}
