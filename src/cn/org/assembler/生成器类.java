package cn.org.assembler;

import java.io.IOException;
import java.util.Arrays;

import org.boris.pecoff4j.COFFHeader;
import org.boris.pecoff4j.DOSHeader;
import org.boris.pecoff4j.ImageDataDirectory;
import org.boris.pecoff4j.OptionalHeader;
import org.boris.pecoff4j.PE;
import org.boris.pecoff4j.PESignature;
import org.boris.pecoff4j.RVAConverter;
import org.boris.pecoff4j.SectionData;
import org.boris.pecoff4j.SectionHeader;
import org.boris.pecoff4j.SectionTable;
import org.boris.pecoff4j.constant.Characteristics;
import org.boris.pecoff4j.constant.MachineType;
import org.boris.pecoff4j.constant.SectionFlag;
import org.boris.pecoff4j.constant.WindowsSubsystem;
import org.boris.pecoff4j.io.PEAssembler;
import org.boris.pecoff4j.io.PEParser;

/**
 * 按照官方格式"Microsoft Portable Executable and Common Object File Format Specification"(下面简称为"官方PE文档")
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
    // https://stackoverflow.com/questions/957057/what-is-the-mz-signature-in-a-pe-file-for. 4D 5A is "MZ"
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

    pe.setOptionalHeader(创建optionalHeader());

    COFFHeader coff头 = new COFFHeader();
    // machine类型为x64
    coff头.setMachine(MachineType.IMAGE_FILE_MACHINE_AMD64);
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
    /**
     * This table (section table) immediately follows the optional header, if any. This positioning
     * is required because the file header does not contain a direct pointer to the section table. 
     * Instead, the location of the section table is determined by calculating the location of the 
     * first byte after the headers. Make sure to use the size of the optional header as specified 
     * in the file header.
     */
    coff头.setSizeOfOptionalHeader(取optionalHeader长度(pe.getOptionalHeader()));
    
    /* characteristics 
      IMAGE_FILE_RELOCS_STRIPPED   0x0001  Image only, Windows CE, and Microsoft Windows NT® and later. This indicates that the file does not contain base relocations and must therefore be loaded at its preferred base address. If the base address is not available, the loader reports an error. The default behavior of the linker is to strip base relocations from executable (EXE) files.
      IMAGE_FILE_EXECUTABLE_IMAGE 0x0002  Image only. This indicates that the image file is valid and can be run. If this flag is not set, it indicates a linker error.
      IMAGE_FILE_LINE_NUMS_STRIPPED   0x0004  COFF line numbers have been removed. This flag is deprecated and should be zero.
      IMAGE_FILE_LOCAL_SYMS_STRIPPED  0x0008  COFF symbol table entries for local symbols have been removed. This flag is deprecated and should be zero.
      IMAGE_FILE_AGGRESSIVE_WS_TRIM   0x0010  Obsolete. Aggressively trim working set. This flag is deprecated for Windows 2000 and later and must be zero.
      IMAGE_FILE_LARGE_ADDRESS_ AWARE 0x0020  Application can handle > 2 GB addresses.
          0x0040  This flag is reserved for future use.
      IMAGE_FILE_BYTES_REVERSED_LO    0x0080  Little endian: the least significant bit (LSB) precedes the most significant bit (MSB) in memory. This flag is deprecated and should be zero.
      IMAGE_FILE_32BIT_MACHINE    0x0100  Machine is based on a 32-bit-word architecture.
      IMAGE_FILE_DEBUG_STRIPPED   0x0200  Debugging information is removed from the image file.
      IMAGE_FILE_REMOVABLE_RUN_ FROM_SWAP 0x0400  If the image is on removable media, fully load it and copy it to the swap file.
      IMAGE_FILE_NET_RUN_FROM_SWAP    0x0800  If the image is on network media, fully load it and copy it to the swap file.
      IMAGE_FILE_SYSTEM   0x1000  The image file is a system file, not a user program.
      IMAGE_FILE_DLL  0x2000  The image file is a dynamic-link library (DLL). Such files are considered executable files for almost all purposes, although they cannot be directly run.
      IMAGE_FILE_UP_SYSTEM_ONLY   0x4000  The file should be run only on a uniprocessor machine.
      IMAGE_FILE_BYTES_REVERSED_HI    0x8000  Big endian: the MSB precedes the LSB in memory. This flag is deprecated and should be zero.
     */
    int characteristics = Characteristics.IMAGE_FILE_RELOCS_STRIPPED + Characteristics.IMAGE_FILE_EXECUTABLE_IMAGE + Characteristics.IMAGE_FILE_LARGE_ADDRESS_AWARE;
    coff头.setCharacteristics(characteristics);
    pe.setCoffHeader(coff头);


    // 总生成64位文件
    pe.set64(true);
    return pe;
  }

  public static void set数据区(PE pe, String 数据) {
    pe.setSectionTable(创建SectionTable(数据));
  }
  
  private static OptionalHeader 创建optionalHeader() {
    // 此部分是必需的,由此取名
    OptionalHeader 必需头 = new OptionalHeader();
    
    // PE32+可执行文件
    必需头.setMagic(OptionalHeader.MAGIC_PE32plus);
    
    // TODO: 没有注释的部分需要挨个推敲,并添加注释
    必需头.setMajorLinkerVersion(1);
    必需头.setMinorLinkerVersion(71);
    必需头.setSizeOfCode(512);
    必需头.setSizeOfInitializedData(512);
    必需头.setSizeOfUninitializedData(0);
    必需头.setAddressOfEntryPoint(4096);
    必需头.setBaseOfCode(4096);
    
    // PE32+不需BaseOfData
    必需头.setBaseOfData(0);
    
    // Windows NT/2000/xp/95/98/Me
    必需头.setImageBase(0x400000);
    
    必需头.setSectionAlignment(4096);
    必需头.setFileAlignment(512);
    必需头.setMajorOperatingSystemVersion(1);
    必需头.setMinorOperatingSystemVersion(0);
    必需头.setMajorImageVersion(0);
    必需头.setMinorImageVersion(0);
    必需头.setMajorSubsystemVersion(5);
    必需头.setMinorSubsystemVersion(0);
    必需头.setWin32VersionValue(0);
    必需头.setSizeOfImage(8192);
    必需头.setSizeOfHeaders(512);
    必需头.setCheckSum(15189);
    
    /* Windows Subsystem
      IMAGE_SUBSYSTEM_UNKNOWN    0 An unknown subsystem
      IMAGE_SUBSYSTEM_NATIVE    1 Device drivers and native Windows processes
      IMAGE_SUBSYSTEM_WINDOWS_GUI   2 The Windows graphical user interface (GUI) subsystem
      IMAGE_SUBSYSTEM_WINDOWS_CUI   3 The Windows character subsystem
      IMAGE_SUBSYSTEM_OS2_CUI   5 The OS/2 character subsystem
      IMAGE_SUBSYSTEM_POSIX_CUI     7 The Posix character subsystem
      IMAGE_SUBSYSTEM_NATIVE_WINDOWS    8 Native Win9x driver
      IMAGE_SUBSYSTEM_WINDOWS_CE_GUI    9 Windows CE
      IMAGE_SUBSYSTEM_EFI_APPLICATION 10  An Extensible Firmware Interface (EFI) application
      IMAGE_SUBSYSTEM_EFI_BOOT_ SERVICE_DRIVER    11  An EFI driver with boot services
      IMAGE_SUBSYSTEM_EFI_RUNTIME_ DRIVER 12  An EFI driver with run-time services
      IMAGE_SUBSYSTEM_EFI_ROM 13  An EFI ROM image
      IMAGE_SUBSYSTEM_XBOX    14  XBOX
      IMAGE_SUBSYSTEM_WINDOWS_BOOT_APPLICATION    16  Windows boot application.
     */
    必需头.setSubsystem(WindowsSubsystem.IMAGE_SUBSYSTEM_WINDOWS_CUI);
    必需头.setDllCharacteristics(0);
    必需头.setSizeOfStackReserve(4096);
    必需头.setSizeOfStackCommit(4096);
    必需头.setSizeOfHeapReserve(65536);
    必需头.setSizeOfHeapCommit(0);
    
    // 保留,必须为0
    必需头.setLoaderFlags(0);
    
    必需头.setNumberOfRvaAndSizes(16);
    
    // TODO: 暂时置零
    ImageDataDirectory[] 数据映像 = new ImageDataDirectory[16];
    for (int i = 0; i < 数据映像.length; i++) {
      ImageDataDirectory 映像 = new ImageDataDirectory();
      映像.setSize(0);
      映像.setVirtualAddress(0);
      数据映像[i] = 映像;
    }
    必需头.setDataDirectories(数据映像);
    
    return 必需头;
  }

  public static SectionTable 创建SectionTable(String 数据) {
    
    SectionTable 段落表 = new SectionTable();
    SectionHeader 段落头 = new SectionHeader();
    段落头.setName("random");

    // 根据数据长度赋值
    段落头.setVirtualSize(数据.length()/2);

    // TODO: 提取所有绝对数. 另外, 为何是512?
    段落头.setVirtualAddress(4096);
    段落头.setSizeOfRawData(512);
    段落头.setPointerToRawData(512);
    段落头.setPointerToRelocations(0);
    段落头.setPointerToLineNumbers(0);
    段落头.setNumberOfRelocations(0);
    段落头.setNumberOfLineNumbers(0);

    // Section Flag由高位到低位
    段落头.setCharacteristics(SectionFlag.IMAGE_SCN_MEM_WRITE + SectionFlag.IMAGE_SCN_MEM_READ
        + SectionFlag.IMAGE_SCN_MEM_EXECUTE + SectionFlag.IMAGE_SCN_CNT_INITIALIZED_DATA
        + SectionFlag.IMAGE_SCN_CNT_CODE);
    段落表.add(段落头);

    RVAConverter 转换器 = new RVAConverter(new int[] {4096}, new int[] {512});
    段落表.setRvaConverter(转换器);

    SectionData 段落数据 = new SectionData();
    // TODO: 填入真实数据
    段落数据.setData(Arrays.copyOf(hexStringToByteArray(数据), 512));

    段落表.put(0, 段落数据);
    return 段落表;
  }

  public static int 取optionalHeader长度(OptionalHeader oh) {
    return
    // image data directory 每个8字节(两个DWORD). 个数是NumberOfRvaAndSizes(见官方PE文档)
    oh.getNumberOfRvaAndSizes() * 8
        // ImageBase/SizeOfStackReserve/SizeOfStackCommit/SizeOfHeapReserve/SizeOfHeapCommit
        // 在64位时, 是8字节, 并且没有BaseOfData.虽然暂时只支持生成64位文件,还是保留了32位的逻辑.
        + (oh.isPE32plus() ? 112 : 96);
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

  private static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}
}
