package com.xyt.ygcf.daying;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class FileUtils {
	public static String LOGPATH;
	public static String BASEPATH;
	public static String DCIMPATH;
	public static String LOGOPATH;
	public static String FILEPATH;
	public static String SNAPPATH;
	public static String PHOTOPATH;
	public static String VIDEOPATH;
	public static String CLOUDPATH;

	public static final String SNAPEXT = ".jpg";
	public static final String VIDEOEXT = ".mp4";
	private static final String TAG = "FileUtils";

	public static String copyLastPhoto(String cameraIn, boolean isSnap) {
		File fileDir = new File(DCIMPATH);
		FileFilter filter = new CustomFileFilter("", SNAPEXT);

		File[] fileArray = fileDir.listFiles(filter);
		if (fileArray.length < 1)
			return null;

		List<File> fileList = new ArrayList<File>();
		for (File f : fileArray) {
			fileList.add(f);
		}
		Collections.sort(fileList, new Comparator<File>() {
			@Override
			public int compare(File lhs, File rhs) {
				if (lhs.lastModified() < rhs.lastModified()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		String sourFile = fileList.get(0).getAbsolutePath();
		String desFile = null;
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.CHINA);

		if (isSnap) {
			desFile = SNAPPATH + "/" + cameraIn + SNAPEXT;
		} else {
			desFile = PHOTOPATH + "/" + cameraIn + "_" + format.format(now)
					+ SNAPEXT;
		}

		File desF = new File(desFile);
		if (desF.exists())
			desF.delete();

		Log.d(TAG, "copyfile " + fileList.get(0) + " " + desFile);
		copyFile(sourFile, desFile);
		fileList.get(0).delete();
		return desFile;
	}

	public static class CustomFileFilter implements FileFilter {
		private String fileName;
		private String ext;

		public CustomFileFilter(String fileName, String ext) {
			this.fileName = fileName.toLowerCase(Locale.CHINA);
			this.ext = ext.toLowerCase(Locale.CHINA);
		}

		@Override
		public boolean accept(File pathname) {
			String pathName = pathname.getName().toLowerCase(Locale.CHINA);
			if (pathName.contains(fileName)
					&& (TextUtils.isEmpty(ext) || pathName.endsWith(ext)))
				return true;
			return false;
		}

	}

	public static String extractDate(File file) {
		String[] parts = file.getName().split("_");
		if (parts.length > 1) {
			return new StringBuilder().append(parts[1].substring(0, 4))
					.append("-").append(parts[1].substring(4, 6)).append("-")
					.append(parts[1].substring(6, 8)).toString();
		} else
			return null;
	}

	public static Map<String, List<String>> getPhotoFilesSortDate(
			String filePath, String cameraIn) {
		String ext = null;
		if (filePath.equals(PHOTOPATH))
			ext = SNAPEXT;
		else
			ext = VIDEOEXT;

		File fileDir = new File(filePath);
		FileFilter filter = new CustomFileFilter(cameraIn, ext);

		File[] fileArray = fileDir.listFiles(filter);

		Map<String, List<String>> fileMap = new HashMap<String, List<String>>();

		if (fileArray != null) {
			for (File f : fileArray) {
				String date = extractDate(f);
				if (date != null) {
					List<String> list = fileMap.get(date);
					if (list == null) {
						list = new ArrayList<String>();
						fileMap.put(date, list);
					}
					list.add(f.getAbsolutePath());
				}
			}
		}

		return fileMap;
	}

	public static List<String> getPhotoFiles(String filePath, String cameraIn) {
		String ext = null;
		if (filePath.equals(PHOTOPATH))
			ext = SNAPEXT;
		else
			ext = VIDEOEXT;

		File fileDir = new File(filePath);
		FileFilter filter = new CustomFileFilter(cameraIn, ext);

		File[] fileArray = fileDir.listFiles(filter);
		List<String> fileList = new ArrayList<String>();
		for (File f : fileArray) {
			fileList.add(f.getAbsolutePath());
		}

		return fileList;
	}

	public static String getSnapFilePath(String filename) {
		return new StringBuilder().append(SNAPPATH).append("/")
				.append(filename).append(SNAPEXT).toString();
	}

	public static String reFixLocalFilepath(String filename) {
		return filename.substring(7);
	}

	public static String fixLocalFilePath(String filename) {
		return new StringBuilder().append("file://").append(filename)
				.toString();
	}

	public static void init(Context context) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			FILEPATH = Environment.getExternalStorageDirectory() + "/homca";
		} else {
			FILEPATH = context.getFilesDir().getParent() + "/homca";
		}

		// /获取应用程序数据路径
		BASEPATH = context.getFilesDir().getParent() + "/";
		LOGPATH = BASEPATH + "log/";
		LOGOPATH = BASEPATH + "logo/";
		DCIMPATH = Environment.getExternalStorageDirectory() + "/DCIM";

		SNAPPATH = FILEPATH + "/snap";
		PHOTOPATH = FILEPATH + "/photo";
		VIDEOPATH = FILEPATH + "/video";
		CLOUDPATH = FILEPATH + "/cloud";

		if (!isFileExist(LOGPATH))
			creatDir(LOGPATH);
		if (!isFileExist(LOGOPATH))
			creatDir(LOGOPATH);
		if (!isFileExist(FILEPATH))
			creatDir(FILEPATH);
		if (!isFileExist(DCIMPATH))
			creatDir(DCIMPATH);
		if (!isFileExist(SNAPPATH))
			creatDir(SNAPPATH);
		if (!isFileExist(PHOTOPATH))
			creatDir(PHOTOPATH);
		if (!isFileExist(VIDEOPATH))
			creatDir(VIDEOPATH);
		if (!isFileExist(CLOUDPATH))
			creatDir(CLOUDPATH);
	}

	/**
	 * 创建文件
	 * 
	 * @throws IOException
	 */
	public static File creatFile(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists())
			file.createNewFile();
		return file;
	}

	/**
	 * 获取文件流
	 * 
	 * @throws IOException
	 */
	public static BufferedWriter getWriter(String fileName) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
		return bw;
	}

	/**
	 * 获取文件流
	 * 
	 * @throws IOException
	 */
	public static DataOutputStream getDataOutputStream(String fileName)
			throws IOException {
		DataOutputStream bw = new DataOutputStream(new FileOutputStream(
				BASEPATH + fileName, true));
		return bw;
	}

	/**
	 * 创建目录
	 * 
	 * @param dirName
	 */
	public static File creatDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断的文件夹是否存在
	 */
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 删除文件
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists())
			return file.delete();
		return true;
	}

	/**
	 * 将一个InputStream里面的数据写入到中
	 */
	public static File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			file = creatFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 文件拷贝
	 * 
	 * @param f1
	 * @param f2
	 * @return
	 * @throws Exception
	 */
	public static boolean copyFile(String f1, String f2) {
		try {
			int length = 1024;
			FileInputStream in = new FileInputStream(f1);
			FileOutputStream out = new FileOutputStream(f2);
			byte[] buffer = new byte[length];
			int ins = 0;
			while ((ins = in.read(buffer)) > 0) {
				out.write(buffer, 0, ins);
			}
			in.close();
			out.close();
			Log.d(TAG, "copyfile finished " + f1 + " " + f2);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static InputStream openSteam(String fileName) {
		try {
			return new FileInputStream(fileName);
		} catch (Exception ex) {
			return null;
		}

	}

	public static boolean downFile(String localPath, String url, String fileName) {
		try {
			java.net.URL u = new java.net.URL(url);
			URLConnection c = u.openConnection();
			InputStream is = c.getInputStream();
			OutputStream os = new FileOutputStream(localPath + fileName);
			byte[] bs = new byte[1024];
			while (true) {
				int count = is.read(bs);
				if (count > 0)
					os.write(bs, 0, count);
				else
					break;
			}
			os.close();
			return true;

		} catch (Exception ex) {
			// LogUtil.e("FileUtils->downFile", ex);
			return false;
		}

	}

	public static void delLogo() {
		File file = new File(LOGOPATH);
		if (file == null || file.exists() == false)
			return;
		for (File f : file.listFiles()) {
			f.delete();
		}
	}

	public static int getImageType(int w, int h) {
		/**
		 * 240*320 宽*高 1 480*320 2 480*800 3 480*854 4 720*1280 5 800*1280 6
		 * 1080*1920 7
		 */
		if (h == 320 && w == 240)
			return 1;
		else if (h == 480 && w == 320)
			return 2;
		else if (h == 800 && w == 480)
			return 3;
		else if (h == 854 && w == 480)
			return 4;
		else if (h == 896 && w == 540)
			return 4;
		else if (h == 940 && w == 560) {
			return 4;
		} else if (h == 960 && w == 540) {
			return 4;
		} else if (h == 1280 && w == 720)
			return 5;
		/** 使用等同于1280×720分辨率的logo */
		else if (h == 1134 && w == 720) {
			return 5;
		} else if (h == 1184 && w == 720) {
			return 5;
		} else if (h == 1280 && w == 800)
			return 6;
		else if (h == 1920 && w == 1080)
			return 7;

		return 0;
	}

}