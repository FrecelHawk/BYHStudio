package com.baiyuhui.huangyan.utils;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class UILManager {
	private static ImageLoaderConfiguration mImageLoaderConfiguration;

	public static ImageLoaderRequest with(Context context) {
		
		if (mImageLoaderConfiguration == null) {

			DisplayMetrics dm = context.getResources().getDisplayMetrics();

			File cacheDir = StorageUtils.getCacheDirectory(context, false);
			mImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(
					context)
					.memoryCacheExtraOptions(dm.widthPixels, dm.heightPixels)
					.diskCacheExtraOptions(dm.widthPixels, dm.heightPixels, null)
					.threadPoolSize(3)
					.threadPriority(Thread.NORM_PRIORITY - 2)
					.memoryCache(
							new UsingFreqLimitedMemoryCache(5 * 1024 * 1024))
					.memoryCacheSize(5 * 1024 * 1024)
					.diskCacheSize(100 * 1024 * 1024)
					.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
					.tasksProcessingOrder(QueueProcessingType.FIFO)
					.diskCacheFileCount(100)
					.diskCache(new UnlimitedDiscCache(cacheDir))
					.defaultDisplayImageOptions(
							DisplayImageOptions.createSimple())
					.imageDownloader(
							new BaseImageDownloader(context, 5 * 1000,
									30 * 1000))
					.denyCacheImageMultipleSizesInMemory()				
					.writeDebugLogs()
					.build();

			ImageLoader.getInstance().init(mImageLoaderConfiguration);
		}

		return new ImageLoaderRequest();
	}

	public static class ImageLoaderRequest {

		private String mUri;
		private int mErrorResId = 0;
		private int mLoadingResId = 0;
		private ImageScaleType mImageScaleType = ImageScaleType.IN_SAMPLE_INT;
		private ImageSize mImageSize;
		
		public ImageLoaderRequest(){ 
			mImageSize = new ImageSize(180, 120);
		}
		
		public ImageLoaderRequest error(int errorResId) {
			mErrorResId = errorResId;
			return this;
		}

		public ImageLoaderRequest placeholder(int placeholderResId) {
			mLoadingResId = placeholderResId;
			return this;
		}

		public ImageLoaderRequest resize(int targetWidth, int targetHeight) {

			mImageSize = new ImageSize(targetWidth, targetHeight);
			return this;
		}

		public ImageLoaderRequest load(String uri) {
			mUri = uri;
			return this;
		}

		public ImageLoaderRequest centerCrop() {
			 mImageScaleType = ImageScaleType.EXACTLY;
			return this;
		}

		public ImageLoaderRequest load(File file) {
			mUri = "file://" + file.getAbsolutePath();
			return this;
		}

		public void into(ImageView imageView) {
			DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();

			builder.showImageOnLoading(mLoadingResId) // resource or drawable
					.showImageForEmptyUri(mErrorResId) // resource or drawable
					.showImageOnFail(mErrorResId) // resource or drawable
					.cacheInMemory(true)
					.cacheOnDisk(true)  
					.considerExifParams(true)
					.imageScaleType(mImageScaleType)
					.resetViewBeforeLoading(true)
					.bitmapConfig(Bitmap.Config.RGB_565);

			DisplayImageOptions options = builder.build();

			ImageLoader.getInstance().displayImage(mUri, imageView, options);

		}
		
		public void listen(ImageView imageView, ImageLoadingListener listener){
			if (listener!=null){
				
				DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();

				builder.showImageOnLoading(mLoadingResId) // resource or drawable
						.showImageForEmptyUri(mErrorResId) // resource or drawable
						.showImageOnFail(mErrorResId) // resource or drawable
						.cacheInMemory(true)
						.cacheOnDisk(true)  
						.considerExifParams(true)
						.imageScaleType(mImageScaleType)
						.resetViewBeforeLoading(true)
						.bitmapConfig(Bitmap.Config.RGB_565);

				DisplayImageOptions options = builder.build();

				ImageLoader.getInstance().displayImage(mUri, imageView, options, listener);

			}
		}
	
		public Bitmap cached(){
			Bitmap bitmap=null;
			
			if (mUri!=null){				
				String uri = mUri;
				
				List<Bitmap> bitmaps = MemoryCacheUtils.findCachedBitmapsForImageUri(mUri, ImageLoader.getInstance().getMemoryCache());
				
				if (bitmaps!=null && bitmaps.size()>0){
					bitmap = bitmaps.get(0);
				}
				else{
					File bitmapFile = DiskCacheUtils.findInCache(mUri, ImageLoader.getInstance().getDiskCache());
					if (bitmapFile!=null){
						bitmap = BitmapFactory.decodeFile(bitmapFile
								.getAbsolutePath());
					}
					
				}
//				
//				
//				String memoryCacheKey = MemoryCacheUtils.generateKey(uri,
//						mImageSize);
//
//				if (memoryCacheKey != null) {
//					bitmap = ImageLoader.getInstance().getMemoryCache().get(uri);
//
//					if (bitmap == null || bitmap.isRecycled()) {
//						bitmap = null;
//						File file = ImageLoader.getInstance().getDiskCache()
//								.get(uri);
//
//						if (file != null) {
//							bitmap = BitmapFactory.decodeFile(file
//									.getAbsolutePath());
//
//						}
//
//					}
//				}
			}
			
			return bitmap;

		}
		
		
	}
	
	
	
	
	

}
