package com.google.alading.client.android;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alading.ee.R;
import com.alading.ee.view.AladingAlertDialog;
import com.avantouch.base.AladingWebserviceMethod;
import com.avantouch.base.BaseAsyncTasks;
import com.liandi.InnerScannerSample;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


//GetOrderInfoByBarCode 扫码调用这个接口
        //UseIntegralByBarCode   立即使用
        //UploadDealLog  上传日志


public class EntranceActivity extends BaseActivity implements BaseAsyncTasks.CalResponse {

	private ImageView mImageView;
	private Button mButton;

	private PopupWindow mPopupWindow;
	private int mInputType;
	private RelativeLayout mScanHistory;


	private InnerScannerSample scannerSample;

	private Context mContext;

    private boolean copyToClipboard;
    private static final long VIBRATE_DURATION = 200L;


    private AladingAlertDialog updataDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImageView = (ImageView) findViewById(R.id.camera_iamge);
		mButton = (Button) findViewById(R.id.ok_button);

		mContext=this;

		scannerSample = new InnerScannerSample(this) {

			@Override
			protected void onDeviceServiceCrash() {
				// Handle in 'InnerScannerActivity'
				EntranceActivity.this.onDeviceServiceCrash();
			}

            @Override
            protected void disPlayErrorInfo(String info) {
                Toast.makeText(mContext,info,Toast.LENGTH_LONG).show();
            }

            @Override
			protected void displayDeviceInfo(String info) {
				// Handle in 'InnerScannerActivity'
//				EntranceActivity.this.displayInfo(info);


                Log.i("aaa","扫码="+info);
                playBeepSoundAndVibrate();
                Intent intent=new Intent(mContext,ScanResultActivity.class);
                intent.putExtra("ScanCode",info);
                startActivity(intent);

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setClassName(mContext,ScanResultActivity.class.getName());
//                intent.putExtra("ScanCode", info);
//                startActivity(intent);
			}
		};

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        playBeep = prefs.getBoolean(PreferencesActivity.KEY_PLAY_BEEP, true);
        if (playBeep) {
            // See if sound settings overrides this
            AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
            if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                playBeep = false;
            }
        }
        vibrate = prefs.getBoolean(PreferencesActivity.KEY_VIBRATE, false);
        copyToClipboard = prefs.getBoolean(PreferencesActivity.KEY_COPY_TO_CLIPBOARD, true);
        initBeepSound();

		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				scannerSample.start();
//				Intent intent = new Intent();
//				intent.setClass(EntranceActivity.this, CaptureActivity.class);
//				startActivity(intent);
			}
		});
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(EntranceActivity.this,
						InputManuallyActivity.class);
				intent.putExtra("input_type", mInputType);
				startActivity(intent);
			}
		});
		mScanHistory = (RelativeLayout) findViewById(R.id.r_scan_history);
		mScanHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jumpToPage(ScanHistoryActivity.class);
			}
		});

		initialize();

        checkUpgrade();
	}

    private boolean playBeep;
    private MediaPlayer mediaPlayer;
    private boolean vibrate;

    private static final float BEEP_VOLUME = 0.10f;



    private void checkUpgrade(){
        String[] method = { AladingWebserviceMethod.APP_UPDATECHECK };
        String[] paramNames = {"Ver"};
        String[] paramValues = {getVersion() };
        BaseAsyncTasks asyncTask = new BaseAsyncTasks(EntranceActivity.this, EntranceActivity.this,
                method[0], false, "");
        asyncTask.execute(method, paramNames, paramValues);
    }


    @Override
    public void setWebserviceResponse(String method, Object response) {
        List<String> list = (List<String>) response;
        Log.d("Alading", "list: " + list.toString());
        if (list.size() > 0) {
            if (method.equals(AladingWebserviceMethod.APP_UPDATECHECK)) {
                if (list.get(0).startsWith("0000")) {
//                    true|1.0.0.0|1.兑换模式升级。|http://180.153.108.78/AladingAppServiceJsonForAndroid/download/Alading.apk
//                    第一个是是否可以升级
//                    第二个：升级到的版本号
//                    第三个升级提示
//                    第四个 是地址
                      if(list.get(1).startsWith("true")){
                          String[] arr=list.get(1).split("\\|");
                          showNoticeDialog(mContext,arr[2],arr[3],"");
                      }
                } else {
//                    this.invalidOrderDialog(ERRSTR);
                }
            } else {
//                this.invalidOrderDialog(getString(R.string.backend_no_result));
            }
        }
    }

//    private boolean analyzeResponse(Object response, String rightCode,boolean tip) {
////        String content = response.toString();
////        String[] contentArray = content.split("\\|");
////        if (tip)
////            this.DisplayToast(contentArray[1]);
////        if (contentArray[0].equals(rightCode)) {
////            return true;
////        } else {
////            ERRSTR = contentArray[1];
////            Log.d("AladingEE", "ERRSTR:" + ERRSTR);
////            return false;
////        }
//    }


    protected void showNoticeDialog(Context context, String updateContent, final String updateLink,final String Upgrade) {

        final AlertDialog.Builder dialog=new AlertDialog.Builder(this) ;


        dialog.setMessage(updateContent);

        dialog.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startDownload(updateLink);
            }
        });

        dialog.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
            dialog.show();

//        .setPositiveButton("更新",new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//
//            }
//        })
//                .setPositiveText("更新")
//                .setNegativeText(getString(R.string.update_later))
//                .setContentTextGravity(Gravity.LEFT).setOnPositiveListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                //UpdateManager.getInstance(SudokuActivity.this).startUpdate(updateLink);
//                if(Upgrade!=null&&Upgrade.equals("1"))
//                {
//                    startDownload(updateLink);
//                }
//                else
//                {
//                    Intent intent=new Intent(UpdataService.ACTION);
//                    intent.putExtra("downloadurl", updateLink);
//                    intent.setPackage(mContext.getPackageName());//加这个参数是为了防止android 5.0以后版本开启服务报错
//                    startService(intent);
//                }
//            }
//        }).setOnNegativeListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        if(Upgrade!=null&&Upgrade.equals("1"))
//        {
//            dialog
//                    .hideNegative()
//                    .setOnKeyListener(new OnKeyListener() {
//
//                        @Override
//                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                            // TODO Auto-generated method stub
//                            if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
//                            {
//                                return true;
//                            }
//                            else
//                            {
//                                return false;
//                            }
//                        }
//                    });
//        }
//        dialog.show();
    }



    @Override
    public void setWebserviceResponse(String method, Object response, BaseAsyncTasks asyncTask) {

    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    private String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
//        return "1.0.0.0";
    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd( R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

	@Override
	protected void onResume() {
		super.onResume();
		bindDeviceService();
	}


	public void onDeviceServiceCrash() {
		bindDeviceService();
	}

	/**
	 * All device operation result infomation will be displayed by this method.
	 *
	 * @param info
	 */
	public void displayInfo(String info) {

		String a=info;



		Toast.makeText(mContext,info,Toast.LENGTH_LONG).show();
//		Log.i("",);

//		EditText infoEditText = (EditText) findViewById(R.id.info_text);
//		String text = infoEditText.getText().toString();
//		if (text.isEmpty()) {
//			infoEditText.setText(info);
//		} else {
//			infoEditText.setText(text + "\n" + info);
//		}
//		infoEditText.setSelection(infoEditText.length());
	}

	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();


	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerSample.stopListen();
    }

    /*
             * Function to set up initial settings: Creating the data source for
             * drop-down list, set the drop-down list.
             */
	private void initialize() {
		// data source for drop-down list
		final ArrayList<String> items = new ArrayList<String>();
		items.add(getString(R.string.product_bar_code));
		items.add(getString(R.string.prepaid_card_number));

		/*
		 * SelectBox is the TextView where the selected values will be displayed
		 * in the form of "Item 1 & 'n' more". When this selectBox is clicked it
		 * will display all the selected values and when clicked again it will
		 * display in shortened representation as before.
		 */
		final TextView tv = (TextView) findViewById(R.id.SelectBox);

		// onClickListener to initiate the dropDown list
		Button createButton = (Button) findViewById(R.id.create);
		createButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initiatePopUp(items, tv);
			}
		});
	}
	/*
	 * Function to set up the pop-up window which acts as drop-down list
	 */
	private void initiatePopUp(final ArrayList<String> items, TextView tv) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// get the pop-up window i.e. drop-down layout
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.pop_up_window,
				(ViewGroup) findViewById(R.id.PopUpView));

		// get the view to which drop-down layout is to be anchored
		RelativeLayout l = (RelativeLayout) findViewById(R.id.type_select);
		mPopupWindow = new PopupWindow(layout, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, true);

		// Pop-up window background cannot be null if we want the pop-up to
		// listen touch events outside its window
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setTouchable(true);

		// let pop-up be informed about touch events outside its window. This
		// should be done before setting the content of pop-up
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);

		// dismiss the pop-up i.e. drop-down when touched anywhere outside the
		// pop-up
		mPopupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					mPopupWindow.dismiss();
					return true;
				}
				return false;
			}
		});

		// provide the source layout for drop-down
		mPopupWindow.setContentView(layout);

		mPopupWindow.setWidth(l.getMeasuredWidth());

		int[] location = new int[2];
		l.getLocationOnScreen(location);
		LinearLayout parent = (LinearLayout) findViewById(R.id.input_area);
		mPopupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0],
				location[1] + l.getHeight() - 2);

		// populate the drop-down list


		final ListView list = (ListView) layout.findViewById(R.id.dropDownList);
		DropdownListAdapter adapter = new DropdownListAdapter(this, items, tv);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					TextView tv = (TextView) findViewById(R.id.SelectBox);
				tv.setText(items.get(arg2));
				mInputType = arg2;
				mPopupWindow.dismiss();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return false;
		}
        if(keyCode==80){
            scannerSample.start();
            return true;
        }
		return false;
	}

	protected void dialog() {
		Builder builder = 	new Builder(EntranceActivity.this);
		builder.setMessage("确定要退出程序吗？");
		builder.setTitle("提示");
		builder.setNegativeButton("确认",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				});
		builder.setPositiveButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}


    boolean isStopDownLoad=false;

    File mTempFile;
    int mPrecent;
    private void startDownload(final String url) {
        // TODO Auto-generated method stub
        updataDialog=new AladingAlertDialog(this);
        updataDialog.setTitleText("应用更新").hideButtonLyaout()
                .hideEndtag()
                .setProgress(0)
                .hideContentTv()
                .showProgressLayout();
//        ProgressBar progressBar=new ProgressBar(mContext);
//        progressBar.setProgress(20);

        Button progressBar=new Button(mContext);
        progressBar.setText("1");



        updataDialog.setOnKeyListener(new ProgressDialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode==KeyEvent.KEYCODE_BACK ){
                    int a=1;
                    dialog.dismiss();
                    isStopDownLoad=true;
                    return false;
                }
                return false;
            }
        });
//                .setProgress(0)
//                .hideContentTv()
//                .showProgressLayout();
//                .setOnKeyListener(new OnKeyListener() {
//
//                    @Override
//                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                        // TODO Auto-generated method stub
//                        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
//                        {
//                            return true;
//                        }
//                        else
//                        {
//                            return false;
//                        }
//                    }
//                });
        updataDialog.show();
        new Thread() {
            @Override
            public void run() {
                try{
                    HttpClient client = new DefaultHttpClient();
                    HttpGet get = new HttpGet(url);
                    HttpResponse response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    if (is != null) {
                        File rootFile = new File(Environment.getExternalStorageDirectory(),"/pinke");

                        if (!rootFile.exists() && !rootFile.isDirectory())
                            rootFile.mkdir();

                        mTempFile = new File(Environment.getExternalStorageDirectory(), "/pinke/" + url.substring(url.lastIndexOf("/") + 1));
                        if (mTempFile.exists()) {
                            mTempFile.delete();
                        }
                        mTempFile.createNewFile();

                        BufferedInputStream bis = new BufferedInputStream(is);
                        FileOutputStream fos = new FileOutputStream(mTempFile);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);

                        int read;
                        long count = 0;
                        int precent = 0;
                        byte[] buffer = new byte[1024];
                        while ((read = bis.read(buffer)) != -1 && !isStopDownLoad) {
                            bos.write(buffer, 0, read);
                            count += read;
                            precent = (int) (((double) count / length) * 100);
                            if (precent - mPrecent >= 1) {
                                mPrecent = precent;

                                Message m= Message.obtain(mHandler,MSG_UPDATA_PROGRESS);

                                Bundle bundle=new Bundle();
                                bundle.putLong("count",count);
                                bundle.putLong("length",length);
                                m.setData(bundle);

                                mHandler.sendMessage(m);
                            }
                        }
                        bos.flush();
                        bos.close();
                        fos.flush();
                        fos.close();
                        is.close();
                        bis.close();
                    }

                    if(!isStopDownLoad){
                        updataDialog.dismiss();
                        Message message = mHandler.obtainMessage(MSG_INSTALL_FILE, mTempFile);
                        mHandler.sendMessage(message);
                    }
                } catch (ClientProtocolException e) {
                    mHandler.sendEmptyMessage(MSG_DOWNLOAD_FAIL);
                } catch (IOException e) {
                    mHandler.sendEmptyMessage(MSG_DOWNLOAD_FAIL);
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(MSG_DOWNLOAD_FAIL);
                }

            }
        }.start();

    }

    private static final int MSG_UPDATA_PROGRESS = 3;
    private static final int MSG_DOWNLOAD_FAIL = 4;
    private static final int MSG_INSTALL_FILE = 5;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATA_PROGRESS:
                    if(updataDialog!=null){

                        Bundle b=msg.getData();

                        if(b==null)return;




                        updataDialog.setProgress(mPrecent);
                        updataDialog.setDownLoadText(com.alading.ee.util.StringUtil.FormatFileSize(b.getLong("count"))+"/"+com.alading.ee.util.StringUtil.FormatFileSize(b.getLong("length")));
                    }

                    break;
                case MSG_DOWNLOAD_FAIL:
                    if(updataDialog!=null&&updataDialog.isShowing())
                        updataDialog.dismiss();
                    showToast("下载失败，请稍后再试！");
                    break;
                case MSG_INSTALL_FILE:
                    install((File) msg.obj, mContext);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void install(File file, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
