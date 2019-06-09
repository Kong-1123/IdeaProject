package cn.com.dhcc.creditquery.person.queryweb.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipUtil {

	private static Logger log = LoggerFactory.getLogger(ZipUtil.class);

	/**
	 * 通过指定路径和文件名来获取文件对象，当文件不存在时自动创建
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static File getFile(String path, String fileName) throws IOException {
		// 创建文件对象
		File file;
		if (path != null && !path.equals(""))
			file = new File(path, fileName);
		else
			file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		// 返回文件
		return file;
	}

	/**
	 * 获得指定文件的输出流
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static FileOutputStream getFileStream(File file) throws FileNotFoundException {
		return new FileOutputStream(file);
	}

	/**
	 * 将多个文件压缩
	 * 
	 * @param fileList
	 *            待压缩的文件列表
	 * @param path
	 *            压缩文件路径
	 * @param zipFileName
	 *            压缩文件名
	 * @return 返回压缩好的文件
	 * @throws IOException
	 */
	public static File getZipFile(List<File> fileList, String path, String zipFileName) throws IOException {
		File zipFile = getFile(path, zipFileName);
		// 文件输出流
		FileOutputStream outputStream = getFileStream(zipFile);
		// 压缩流
		ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

		int size = fileList.size();
		// 压缩列表中的文件
		for (int i = 0; i < size; i++) {
			File file = fileList.get(i);
			zipFile(file, zipOutputStream);
		}
		// 关闭压缩流、文件流
		zipOutputStream.close();
		outputStream.close();
		return zipFile;
	}

	/**
	 * 将文件数据写入文件压缩流
	 * 
	 * @param file
	 *            带压缩文件
	 * @param zipOutputStream
	 *            压缩文件流
	 * @throws IOException
	 */
	private static void zipFile(File file, ZipOutputStream zipOutputStream) throws IOException {
		if (file.exists()) {
			if (file.isFile()) {
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);
				ZipEntry entry = new ZipEntry(file.getName());
				zipOutputStream.putNextEntry(entry);

				final int MAX_BYTE = 10 * 1024 * 1024; // 最大流为10MB
				long streamTotal = 0; // 接收流的容量
				int streamNum = 0; // 需要分开的流数目
				int leaveByte = 0; // 文件剩下的字符数
				byte[] buffer; // byte数据接受文件的数据

				streamTotal = bis.available(); // 获取流的最大字符数
				streamNum = (int) Math.floor(streamTotal / MAX_BYTE);
				leaveByte = (int) (streamTotal % MAX_BYTE);

				if (streamNum > 0) {
					for (int i = 0; i < streamNum; i++) {
						buffer = new byte[MAX_BYTE];
						bis.read(buffer, 0, MAX_BYTE);
						zipOutputStream.write(buffer, 0, MAX_BYTE);
					}
				}

				// 写入剩下的流数据
				buffer = new byte[leaveByte];
				bis.read(buffer, 0, leaveByte); // 读入流
				zipOutputStream.write(buffer, 0, leaveByte); // 写入流
				zipOutputStream.closeEntry(); // 关闭当前的zip entry

				// 关闭输入流
				bis.close();
				fis.close();
			}
		}
	}

	/**
	 * 创建ZIP文件
	 * 
	 * @param sourcePath
	 *            文件或文件夹路径
	 * @param zipPath
	 *            生成的zip文件存在路径（包括文件名）
	 */
	public static void createZip(String sourcePath, String zipPath) throws Exception{
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipPath);
			zos = new ZipOutputStream(fos);
			writeZip(new File(sourcePath), "", zos);
		} catch (FileNotFoundException e) {
			log.error("创建ZIP文件失败", e);
			throw e;
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (IOException e) {
				log.error("创建ZIP文件失败", e);
				throw e;
			}

		}
	}

	private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
		if (file.exists()) {
			if (file.isDirectory()) {// 处理文件夹
				parentPath += file.getName() + File.separator;
				File[] files = file.listFiles();
				if (files.length != 0) {
					for (File f : files) {
						writeZip(f, parentPath, zos);
					}
				} else { // 空目录则创建当前目录
					try {
						zos.putNextEntry(new ZipEntry(parentPath));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[1024];
					int len;
					while ((len = fis.read(content)) != -1) {
						zos.write(content, 0, len);
						zos.flush();
					}

				} catch (FileNotFoundException e) {
					log.error("创建ZIP文件失败", e);
				} catch (IOException e) {
					log.error("创建ZIP文件失败", e);
				} finally {
					try {
						if (fis != null) {
							fis.close();
						}
					} catch (IOException e) {
						log.error("创建ZIP文件失败", e);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		createZip("D:\\ziptest", "D:\\ziptest.zip");
		File file = new File("D:\\ziptest.zip");
		System.out.println(file.exists());
	}
}
