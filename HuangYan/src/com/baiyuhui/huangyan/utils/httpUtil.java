package com.baiyuhui.huangyan.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class httpUtil {
	
    public final static int getDegress(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
     
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress); 
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }
    
    
    public final static int caculateInSampleSize(BitmapFactory.Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) return 1;
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height/ (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
     
    
    public final static Bitmap compressBitmap(String path, int rqsW, int rqsH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
     
    public final static String compressBitmap(String srcPath, int rqsW, int rqsH, boolean isDelSrc) {
        Bitmap bitmap = compressBitmap(srcPath, rqsW, rqsH);
        
        HLog.e("函数开始=="+bitmap);
        File srcFile = new File(srcPath);
        
        String desPath = "";
        if(srcPath.contains("."))
        {
        	desPath = srcPath.split(".jpg")[0]+"_temp.jpg";
        }
        int degree = getDegress(srcPath);
        
        try {
            if (degree != 0) bitmap = rotateBitmap(bitmap, degree);
            File file = new File(desPath);
            HLog.e("函数开始==11");
            FileOutputStream  fos = new FileOutputStream(file);
            //bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            HLog.e("函数开始==22");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HLog.e("函数开始==33");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//
            
            int quality=100;
            HLog.e("开始大小=="+baos.toByteArray().length/1024f);
            while (baos.toByteArray().length / 1024f>100) 
            {
            	HLog.e("大小==="+baos.toByteArray().length / 1024f);
                  quality=quality-4;// ÿ�ζ�����4
                  baos.reset();// ����baos�����baos
                  if(quality<=0)
                  {
                           break;
                  }
                  bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);                         
            }
            baos.close();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);  
            
            fos.close();
            if (isDelSrc) srcFile.deleteOnExit();
        } catch (Exception e) {
           
        }
        
        
//        try {
//            if (degree != 0) bitmap = rotateBitmap(bitmap, degree);
//            File file = new File(desPath);
//            //FileOutputStream baos = new FileOutputStream(file);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//���ǩ����png�Ļ����򲻹�quality�Ƕ��٣����������������ѹ��
//            int quality=100;
//            while (baos.toByteArray().length / 1024f>100) 
//            {
//                  quality=quality-4;// ÿ�ζ�����4
//                  baos.reset();// ����baos�����baos
//                  if(quality<=0)
//                  {
//                           break;
//                  }
//                  bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);                         
//            }
//            baos.close();
//            if (isDelSrc) 
//            	srcFile.deleteOnExit();
//        } catch (Exception e) {
//           
//        }
        return desPath;
    }
    
    
}