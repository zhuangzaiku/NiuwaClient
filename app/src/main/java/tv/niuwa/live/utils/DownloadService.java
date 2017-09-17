package tv.niuwa.live.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.smart.androidutils.utils.LogUtils;
import tv.niuwa.live.intf.OnDownLoadListener;
import tv.niuwa.live.music.MusicItem;
import tv.niuwa.live.music.NetMusicFragment;

public class DownloadService extends Service {


	String path;
	HashMap<String,MusicItem>list ;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		list = new HashMap<>();
		LogUtils.i("delong===========");
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(null != intent){
			MusicItem music = (MusicItem) intent.getSerializableExtra("music");
			path = intent.getStringExtra("path");
			String index = intent.getStringExtra("index");
			music.setType(index);//充当index
			list.put(music.getHash(),music);
			downloadFile(music,path );

		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void downloadFile(final MusicItem music, final String path) {
		String[] allowedContentTypes = new String[]{"audio/mpeg"};
		DownLoadFile.downloadFile(this, music.getUrl(), allowedContentTypes, new OnDownLoadListener() {
			@Override
			public void OnSuccess(byte[] binaryData) {
				try{
					File temp = new File(path+music.getName()+".mp3");
					if(temp.exists()){
						temp.delete();
					}
					temp.createNewFile();
					OutputStream stream = new FileOutputStream(temp);
					stream.write(binaryData);
					stream.close();
					Intent intent = new Intent();
					intent.setAction(NetMusicFragment.ACTION_DOWNLOAD_SUCCESS);
					intent.putExtra("music",music);
					sendBroadcast(intent);
					list.remove(music.getHash());
					stopSelf();
				}catch (Exception e){

				}

			}

			@Override
			public void onFailure() {
				Intent intent = new Intent();
				intent.setAction(NetMusicFragment.ACTION_DOWNLOAD_FAIL);
				intent.putExtra("music",music);
				sendBroadcast(intent);
				list.remove(music.getHash());
			}

			@Override
			public void onProgress(int progress) {
				Intent intent = new Intent();
				intent.setAction(NetMusicFragment.ACTION_DOWNLOAD_PROGRESS);
				music.setProgress(progress+"");
				intent.putExtra("music",music);
				sendBroadcast(intent);
			}
		});
	}


}
