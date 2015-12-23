package com.xyt.ygcf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.os.Environment;

import com.xyt.ygcf.logic.my.LoginHelper;

public class FileUtils {
	/**
	 * 写文件到/data/data/包名/files 下
	 * 
	 * @param context
	 * @param fileName
	 *            文件名称
	 * @param body
	 *            文件内容
	 */
	public static void writeFile(Context context, String fileName, String body) {
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fileOutputStream.write(body.getBytes());
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取/data/data/包名/files 下的文件内容
	 * 
	 * @param context
	 * @param fileName
	 * @return 读取结果
	 */
	public static String readFile(Context context, String fileName) {
		FileInputStream fileInputStream;
		String txt = "";
		try {
			boolean isFileExist = false;
			String[] fileNameArray = context.fileList();
			for (int i = 0; i < fileNameArray.length; i++) {
				if (fileNameArray[i].equals(fileName)) {
					isFileExist = true;// 文件存在
				}
			}
			if (isFileExist) {
				fileInputStream = context.openFileInput(fileName);
				int length = fileInputStream.available();
				byte[] buffer = new byte[length];
				fileInputStream.read(buffer);
				String type = codetype(buffer);
				txt = EncodingUtils.getString(buffer, type);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return txt;
	}

	/**
	 * 读取/data/data/包名/files 下的文件内容
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String readFileData(Context context, String fileName) {
		String res = "";
		try {
			FileInputStream fin = context.openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private static String codetype(byte[] head) {
		String type = "";
		byte[] codehead = new byte[3];
		System.arraycopy(head, 0, codehead, 0, 3);
		if (codehead[0] == -1 && codehead[1] == -2) {
			type = "UTF-16";
		} else if (codehead[0] == -2 && codehead[1] == -1) {
			type = "UNICODE";
		} else if (codehead[0] == -17 && codehead[1] == -69 && codehead[2] == -65) {
			type = "UTF-8";
		} else {
			type = "GB2312";
		}
		return type;
	}

	public static boolean writeToXml(Context context, String str, String XmlFileName) {
		try {
			File newxmlfile = new File(Environment.getExternalStorageDirectory() + "/" + XmlFileName);
			if (!newxmlfile.exists())
				try {
					newxmlfile.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			OutputStream out = context.openFileOutput(XmlFileName, Context.MODE_PRIVATE);
			OutputStreamWriter outw = new OutputStreamWriter(out);

			outw.write(str);
			outw.close();
			out.close();
			return true;

		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 写文件到sd卡上
	 * 
	 * @param context
	 */
	public static void writeFileToSD(String logBody) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			return;
		}
		try {

			String pathName = "/sdcard/";
			String fileName = "miebolog.txt";
			File path = new File(pathName);
			File file = new File(pathName + fileName);
			if (!path.exists()) {
				path.mkdir();
			}
			if (!file.exists()) {
				file.createNewFile();

			}
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.seek(file.length());

			raf.write(logBody.getBytes());
			raf.close();

		} catch (Exception e) {
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param 文件绝对路径
	 *            (含文件名)
	 * @return true or false
	 */
	public static Boolean checkFileExist(String fileAbsolutePath) {
		File file = new File(fileAbsolutePath);
		return file.exists();
	}

	/**
	 * 判断文件夹是否存在
	 * 
	 * @param 文件夹绝对路径
	 * @return true or false
	 */
	public static Boolean checkFolderExist(String folderAbsolutePath) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		File file = new File(folderAbsolutePath);
		String path = file.getAbsolutePath();
		file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 如果文件夹不存在自动创建文件夹
	 * 
	 * @param 文件夹绝对路径
	 */
	public static void createDirIfNotExist(String folderAbsolutePath) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			return;
		}
		File file = new File(folderAbsolutePath);
		if (!file.exists()) {
			file.mkdirs();
		}

	}

	/**
	 * 检查Sdcard是否已挂载.
	 * 
	 * @return
	 */
	public static boolean checkSdcardExist() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) ? true : false;
	}

	/**
	 * 
	 * @return SDCard根目录
	 */
	public static File getStorageDir() {
		File sdCardDir = null;
		if (checkSdcardExist()) {
			sdCardDir = Environment.getExternalStorageDirectory();
		}
		return sdCardDir;
	}

	/**
	 * 读取指定路径文件
	 * 
	 * @param 文件绝对路径
	 * @return 字符串
	 */
	public static String readFileByDir(String fileAbsolutePath) {
		String result = "";
		File file = new File(fileAbsolutePath);
		String path = file.getAbsolutePath();
		file = new File(path);
		if (file.exists()) {
			try {
				FileInputStream fileIS = new FileInputStream(path);
				StringBuffer sb = new StringBuffer();
				BufferedReader buf = new BufferedReader(new InputStreamReader(fileIS));
				String readString = new String();
				while ((readString = buf.readLine()) != null) {
					sb.append(readString);
				}
				result = sb.toString();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}

		return result;
	}

	/* 删除指定路径的文件 */
	public static Boolean deleteFileByDir(String fileAbsolutePath) {
		Boolean result = false;
		File file = new File(fileAbsolutePath);
		String path = file.getAbsolutePath();
		file = new File(path);
		if (file.exists()) {
			file.delete();
			result = true;
		}
		return result;

	}

	/**
	 * 写入内容到文件上(该方法假设文件已经存在,否则报错)
	 * 
	 * @param str
	 * @return 成功或失败
	 */
	public static boolean write(Context context, String fullFileName, String str) {
		boolean result = true;
		File file = new File(fullFileName);
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(file, "rw");
			raf.seek(file.length());
			raf.write(str.getBytes());
			raf.close();
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * remember, modify the extend name,not ".jpg" 返回的是公用图片文件夹
	 * 
	 * @return
	 * @throws IOException
	 */
	public static File createTempImageFile(Context context) throws IOException {
		String prefix = String.valueOf(System.currentTimeMillis());
		return File.createTempFile(prefix, ".jpg", context.getExternalFilesDir(null));
	}

	public static File getExternalAvatarCropFile(Context context) {
		return new File(context.getExternalFilesDir(null), "crop.jpg");
	}

	public static File getExternalAvatarFile(Context context) {
		return new File(context.getExternalFilesDir(null), LoginHelper.user.memberNo + ".jpg");
	}
}
