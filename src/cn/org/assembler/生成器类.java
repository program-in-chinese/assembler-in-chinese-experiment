package cn.org.assembler;

import java.io.IOException;

import org.boris.pecoff4j.PE;
import org.boris.pecoff4j.PESignature;
import org.boris.pecoff4j.io.PEAssembler;
import org.boris.pecoff4j.io.PEParser;

/**
 * 按照官方格式"Microsoft Portable Executable and Common Object File Format Specification"
 * 生成PE文件
 *
 */
public class 生成器类 {

  static PE 参照PE;
  
  static {
    try {
      参照PE = PEParser.parse("测试/PE/mov.exe");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  /**
   * @return 创建与代码无关的PE结构 
   * @throws IOException
   */
  public static PE 新建PE结构() throws IOException {

    PE pe = new PE();
    // TODO: 暂时借用参照PE文件的dosheader
    pe.setDosHeader(参照PE.getDosHeader());

    /**
     * 3.1. MS DOS Stub (Image Only)
     * The MS DOS stub is a valid application that runs under MS DOS. 
     * It is placed at the front of the EXE image. The linker places a default stub here,
     * which prints out the message “This program cannot be run in DOS mode”
     * when the image is run in MS DOS. The user can specify a different stub
     * by using the /STUB linker option.
     * At location 0x3c, the stub has the file offset to the PE signature.
     * This information enables Windows to properly execute the image file, 
     * even though it has an MS DOS stub. This file offset is placed at location 0x3c during linking.
     */
    // TODO: 暂时借用参照PE文件的stub
    pe.setStub(参照PE.getStub());

    /**
     * 3.2. Signature (Image Only) 
     * After the MS DOS stub, at the file offset specified at offset
     * 0x3c, is a 4-byte signature that identifies the file as a PE format image file. This
     * signature is “PE\0\0” (the letters “P” and “E” followed by two null bytes).
     */
    PESignature signature = new PESignature();
    
    byte[] 签名 = 转换为字节("PE\0\0");
    signature.setSignature(签名);
    pe.setSignature(signature);

    // TODO: 暂时借用参照PE文件的coff header
    pe.setCoffHeader(参照PE.getCoffHeader());

    // TODO: 暂时借用参照PE文件的option header
    pe.setOptionalHeader(参照PE.getOptionalHeader());

    // TODO: 暂时借用参照PE文件的section table
    pe.setSectionTable(参照PE.getSectionTable());
    
    return pe;
  }

  public static void 新建PE文件(PE pe, String 文件名) {
    try {
      PEAssembler.write(pe, 文件名);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private static byte[] 转换为字节(String 字符串) {
    return 字符串.getBytes();
  }

}
