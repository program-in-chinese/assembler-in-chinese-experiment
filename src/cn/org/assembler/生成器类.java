package cn.org.assembler;

import java.io.IOException;

import org.boris.pecoff4j.COFFHeader;
import org.boris.pecoff4j.DOSHeader;
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
    DOSHeader dh = new DOSHeader();
    dh.setAddressOfNewExeHeader(128);
    /*dh.setAddressOfRelocationTable(64);
    dh.setFileSizeInPages(1);
    dh.setHeaderSizeInParagraphs(4);
    dh.setInitialSP(320);*/
    dh.setMagic(23117);
    //dh.setMaxExtraParagraphs(65535);
    //dh.setMinExtraParagraphs(16);
    dh.setReserved(new int[4]);
    dh.setReserved2(new int[10]);
    //dh.setStubSize(64);
    //dh.setUsedBytesInLastPage(128);
    pe.setDosHeader(dh);

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
    // 暂时借用参照PE文件的stub. 猜想该stub是不变的, 因而新建价值不大.
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

    // TODO: 暂时借用参照PE文件的option header
    pe.setOptionalHeader(参照PE.getOptionalHeader());

    COFFHeader coff头 = new COFFHeader();
    // machine类型为x64
    coff头.setMachine(0x8664);
    // section数暂为1
    coff头.setNumberOfSections(1);
    // timestamp为文件生成的时间戳(秒),尽可能在此方法最后取时间,以接近文件产生时间. 如1494046181
    coff头.setTimeDateStamp((int) (System.currentTimeMillis() / 1000));
    // PointerToSymbolTable - The file offset of the COFF symbol table, or zero if no COFF symbol
    // table is present. This value should be zero for an image because COFF debugging information
    // is deprecated.
    coff头.setPointerToSymbolTable(0);
    // NumberOfSymbols - The number of entries in the symbol table. This value should be zero for an
    // image because COFF debugging information is deprecated.
    coff头.setNumberOfSymbols(0);
    // SizeOfOptionalHeader - The size of the optional header, which is required for executable
    // files but not for object files. This value should be zero for an object file. For a
    // description of the header format, see section 3.4, “Optional Header (Image Only).”
    // TODO: 获取optional header长度
    coff头.setSizeOfOptionalHeader(0);
    /*
     * characteristics IMAGE_FILE_RELOCS_STRIPPED 0x0001 Image only, Windows CE, and Microsoft
     * Windows NT® and later. This indicates that the file does not contain base relocations and
     * must therefore be loaded at its preferred base address. If the base address is not available,
     * the loader reports an error. The default behavior of the linker is to strip base relocations
     * from executable (EXE) files. IMAGE_FILE_EXECUTABLE_IMAGE 0x0002 Image only. This indicates
     * that the image file is valid and can be run. If this flag is not set, it indicates a linker
     * error. IMAGE_FILE_LINE_NUMS_STRIPPED 0x0004 COFF line numbers have been removed. This flag is
     * deprecated and should be zero. IMAGE_FILE_LOCAL_SYMS_STRIPPED 0x0008 COFF symbol table
     * entries for local symbols have been removed. This flag is deprecated and should be zero.
     * IMAGE_FILE_AGGRESSIVE_WS_TRIM 0x0010 Obsolete. Aggressively trim working set. This flag is
     * deprecated for Windows 2000 and later and must be zero. IMAGE_FILE_LARGE_ADDRESS_ AWARE
     * 0x0020 Application can handle > 2 GB addresses. 0x0040 This flag is reserved for future use.
     * IMAGE_FILE_BYTES_REVERSED_LO 0x0080 Little endian: the least significant bit (LSB) precedes
     * the most significant bit (MSB) in memory. This flag is deprecated and should be zero.
     * IMAGE_FILE_32BIT_MACHINE 0x0100 Machine is based on a 32-bit-word architecture.
     * IMAGE_FILE_DEBUG_STRIPPED 0x0200 Debugging information is removed from the image file.
     * IMAGE_FILE_REMOVABLE_RUN_ FROM_SWAP 0x0400 If the image is on removable media, fully load it
     * and copy it to the swap file. IMAGE_FILE_NET_RUN_FROM_SWAP 0x0800 If the image is on network
     * media, fully load it and copy it to the swap file. IMAGE_FILE_SYSTEM 0x1000 The image file is
     * a system file, not a user program. IMAGE_FILE_DLL 0x2000 The image file is a dynamic-link
     * library (DLL). Such files are considered executable files for almost all purposes, although
     * they cannot be directly run. IMAGE_FILE_UP_SYSTEM_ONLY 0x4000 The file should be run only on
     * a uniprocessor machine. IMAGE_FILE_BYTES_REVERSED_HI 0x8000 Big endian: the MSB precedes the
     * LSB in memory. This flag is deprecated and should be zero.
     */
    int characteristics = 0x1 + 0x2 + 0x20;
    coff头.setCharacteristics(characteristics);
    pe.setCoffHeader(coff头);

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
